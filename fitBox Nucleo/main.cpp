#include "mbed.h"
#include <stdio.h>
#include "SpwfSAInterface.h"
#include "SPWFSAxx.h"
#include "TLSSocket.h"

#include "mbed_trace.h"
#include "https_request.h"
#include "http_request.h"

#include <string.h>

//Program to 'sweep' test a 'standard RC type servo
//Define some parameters using compiler directive '#define'
//Check Servo DATA if 0.75ms to 2.25ms then use min=750 and max=2250
//NB be values in microseconds (Following are generic values)
#define MID         1500
#define MIN         1000
#define MAX         2000
#define STEP         500
//Time delay between steps in milliseconds
#define TIME         100

//Google Trusted CA certificate in PEM format
static const char SSL_CA_PEM[]="-----BEGIN CERTIFICATE-----\n" 
            "MIIDujCCAqKgAwIBAgILBAAAAAABD4Ym5g0wDQYJKoZIhvcNAQEFBQAwTDEgMB4G" 
            "A1UECxMXR2xvYmFsU2lnbiBSb290IENBIC0gUjIxEzARBgNVBAoTCkdsb2JhbFNp" 
            "Z24xEzARBgNVBAMTCkdsb2JhbFNpZ24wHhcNMDYxMjE1MDgwMDAwWhcNMjExMjE1" 
            "MDgwMDAwWjBMMSAwHgYDVQQLExdHbG9iYWxTaWduIFJvb3QgQ0EgLSBSMjETMBEG" 
            "A1UEChMKR2xvYmFsU2lnbjETMBEGA1UEAxMKR2xvYmFsU2lnbjCCASIwDQYJKoZI" 
            "hvcNAQEBBQADggEPADCCAQoCggEBAKbPJA6+Lm8omUVCxKs+IVSbC9N/hHD6ErPL" 
            "v4dfxn+G07IwXNb9rfF73OX4YJYJkhD10FPe+3t+c4isUoh7SqbKSaZeqKeMWhG8" 
            "eoLrvozps6yWJQeXSpkqBy+0Hne/ig+1AnwblrjFuTosvNYSuetZfeLQBoZfXklq" 
            "tTleiDTsvHgMCJiEbKjNS7SgfQx5TfC4LcshytVsW33hoCmEofnTlEnLJGKRILzd" 
            "C9XZzPnqJworc5HGnRusyMvo4KD0L5CLTfuwNhv2GXqF4G3yYROIXJ/gkwpRl4pa" 
            "zq+r1feqCapgvdzZX99yqWATXgAByUr6P6TqBwMhAo6CygPCm48CAwEAAaOBnDCB" 
            "mTAOBgNVHQ8BAf8EBAMCAQYwDwYDVR0TAQH/BAUwAwEB/zAdBgNVHQ4EFgQUm+IH" 
            "V2ccHsBqBt5ZtJot39wZhi4wNgYDVR0fBC8wLTAroCmgJ4YlaHR0cDovL2NybC5n" 
            "bG9iYWxzaWduLm5ldC9yb290LXIyLmNybDAfBgNVHSMEGDAWgBSb4gdXZxwewGoG" 
            "3lm0mi3f3BmGLjANBgkqhkiG9w0BAQUFAAOCAQEAmYFThxxol4aR7OBKuEQLq4Gs" 
            "J0/WwbgcQ3izDJr86iw8bmEbTUsp9Z8FHSbBuOmDAGJFtqkIk7mpM0sYmsL4h4hO" 
            "291xNBrBVNpGP+DTKqttVCL1OmLNIG+6KYnX3ZHu01yiPqFbQfXf5WRDLenVOavS" 
            "ot+3i9DAgBkcRcAtjOj4LaR0VknFBbVPFd5uRHg5h6h+u/N5GJG79G+dwfCMNYxd" 
            "AfvDbbnvRG15RjF+Cv6pgsH/76tuIMRQyV+dTZsXjAzlAcmgQWpzU/qlULRuJQ/7" 
            "TBj0/VLZjmmx6BEP3ojY+x1J96relc8geMJgEtslQIxq/H5COEBkEveegeGTLg==" 
            "-----END CERTIFICATE-----";


DigitalOut myLed(LED1);
InterruptIn  myButton2(USER_BUTTON);
InterruptIn  myButton(D10);

char jsonSource[2048];

PwmOut myServo(PA_7);

bool open_door = false;
bool last_state;
bool restart;

WiFiInterface* wifi;

void pressedUser() {
    restart = true;    
}

void pressed(){
    printf("Open the door\n");
    myServo.period_ms(20);
    myServo.pulsewidth_us(MID); //NB in microseconds
    if(open_door == false){
        for (int i=MIN;i<=MAX;i+=STEP){
            myServo.pulsewidth_us(i);
            wait_ms(TIME);
        }
    }
    else if(open_door == true){
        for (int i=MAX;i>=MIN;i-=STEP){
            myServo.pulsewidth_us(i);
            wait_ms(TIME);
        }
    }
    open_door = last_state;
    myLed = !myLed;
    wait(0.2);
    myLed = !myLed;
}

int getLockerState(){
    
    char* valueOpen = NULL;
    
    valueOpen = strstr(jsonSource, "open");
    if (valueOpen == NULL) {
        printf("Field open not exist, problem with JSON string\n\r");
        return -1;
    }
    else {
        valueOpen += 31;    // move pointer to open value position
        
        if (!strncmp(valueOpen, "true", 4)) {
            printf("Open value of locker is true!\n\r");
            last_state = true;
        }
        else if (!strncmp(valueOpen, "false", 5)) {
            printf("Open value of locker is false!\n\r");
            last_state = false;
        }
        else {
            printf("Value not recognized\n\r");
            return -1;    
        }
    }
    
    return 0;
}

int connectWifi() {
    int ret;
    
    // get wifi interface
    printf("\n\rGetting WiFi interface...\r\n"); 
    wifi = WiFiInterface::get_default_instance();
    if (!wifi) {
        printf("No WiFi Interface\n\r");
        return -1;
    }

    // connect to the wifi network
    printf("\n\rConnecting to AP %s\n\r", MBED_CONF_APP_WIFI_SSID);
    ret = wifi->connect(MBED_CONF_APP_WIFI_SSID, MBED_CONF_APP_WIFI_PASSWORD, NSAPI_SECURITY_WPA2);
    if (ret!=0) {
        printf("Wifi connection error: %d\n\r", ret);
        return -1;
    }
    
    printf("IP address: %s\n\r", wifi->get_ip_address());
    
    return 0;
}

int poolRequest(){
    int ret; 
    
    printf("\n\rHTTPClient mbed Application\r\n");

    //GET data
    printf("\n\rTrying to fetch page...\n\r");    
    NetworkInterface* wifimodule = (NetworkInterface*) wifi;
    HttpsRequest* request = new HttpsRequest(wifimodule, SSL_CA_PEM, HTTP_GET, "https://firestore.googleapis.com/v1beta1/projects/smartlocker-e57ce/databases/(default)/documents/parks/1549029155/lockers/87473014");
    HttpResponse* response;
    response = request->send(NULL, 0);    // execute the request
    
    // analysis of the response
    if (response!=NULL && response->get_status_code()==200) {
        // getting the body
        char* body_p = (char *) (response->get_body_as_string()).c_str();
        int body_length = strlen(body_p);
        
        // body too large
        if (body_length >= 2048) {
            printf("Body too large (>= 2048), ignoring the response\n\r");
            ret = -1;
        }
        // compute the body
        else {
            strncpy(jsonSource, body_p, body_length);   // copy the body to variable jsonSource
            
            printf("Page fetched successfully - read %d characters\n\r", strlen(jsonSource));
            
            ret = getLockerState();     // get "open" value of the locker from JSON file
            
            if (ret!=0) 
                printf("Can't get locker state from JSON file, ignoring it\n\r");
            else {
                if (open_door != last_state) {
                    printf("Move the servo!\n\r");
                    pressed();  // move the servo 
                }
                else 
                    printf("Locker state doesn't change\n\r");
            }
        }
    }
    // status code != (200 OK)
    /*else if (request != NULL && response->get_status_code()!=200) {
        printf("HttpRequest failed (HTTP Status Code=%d)\n\r", response->get_status_code());
        printf("Can't get data from DB\n\r");
        ret = -1;        
    }*/
    // response NULL
    else {
        printf("HttpRequest failed (Error Code=%d)\n\r", request->get_error());
        printf("Can't get data from DB\n\r");
        ret = -1;
    }
    
    delete request;
    
    return ret;
}
 
int main() {
    int ret;
        
    //mbed_trace_init();

    while (true) {
        restart = false;
        
    #ifdef MBED_MAJOR_VERSION
        printf("Mbed OS version %d.%d.%d\n\n", MBED_MAJOR_VERSION, MBED_MINOR_VERSION, MBED_PATCH_VERSION);
    #endif
        
        // init wifi interface and connect to internet
        do {
            ret = connectWifi();
            
            if (!ret) printf("\n\rWiFi connection successful\n\r");
            else printf("\n\rWiFi conncection failed\n\r");
        
        } while (ret!=0);
        
        while(true){
            myButton2.fall(&pressedUser);  // restart code if pressed
            
            if (restart) {
                printf("Restart=%s\n\r", restart ? "true" : "false");
                break;             // I want to restart code
            }
            
            ret = poolRequest();
            if (!ret) printf("Pool data successful\n\r");
            else printf("Some problem with pool function (see above)\n\r");
            
            wait(1); // Sleep for 1 second
        }
        
        wifi->disconnect();
        printf ("WIFI disconnected, exiting ...\n\r");
    }  
}