# FitBox
<img src="http://www.dis.uniroma1.it/sites/default/files/marchio%20logo%20eng%20jpg.jpg" width="75%" height="75%">
<img src="https://github.com/Galli1598333/smart_locker/blob/master/fitbox-banner.png" width="50%" height="50%">

## Description

This is an Android app for the IoT 2018/2019 course final project and it is based on [Firebase](https://firebase.google.com) and [Nucleo-64 STM32F401 board](https://www.st.com/content/st_com/en/products/evaluation-tools/product-evaluation-tools/mcu-mpu-eval-tools/stm32-mcu-mpu-eval-tools/stm32-nucleo-boards/nucleo-f401re.html) + [X-NUCLEO-IDW01M1 expansion board](https://www.st.com/en/ecosystems/x-nucleo-idw01m1.html). The idea is to make it easier to find a public and secure place where you can leave your belongings in order to go running in total freedom and also to find other people near you with your same interests.

## Installation

In order to install FitBox on your android smartphone you can simply install the .apk file placed in our git repository. During the installation you would be asked from the Google Play Protect service if you want to continue with the installation (this is because our application is not published on the Play Store).

## Architecture

The architecture of our system is the following:

<img src="https://github.com/Galli1598333/smart_locker/blob/master/images/diagram.png" width="100%" height="100%">

## Nucleo Schema

The schema is composed only by the Nucleo board + the Wi-Fi expansion board ontop and a simple servo:

<img src="https://github.com/Galli1598333/smart_locker/blob/master/mockup/archScheme.png" width="75%" height="75%">

## Usage

### Splash Screen

In order to access our application,you can use your Facebook profile or you can create a new profile that will be stored in our database on Firebase: 

<img src="https://github.com/Galli1598333/smart_locker/blob/master/mockup/splash.jpg" width="35%" height="35%">

### Signup and Login

If you want to create a new account on our application, you can click on the *SIGNUP* button and you will be redirected to the following page:
 
<img src="https://github.com/Galli1598333/smart_locker/blob/master/mockup/signup.jpg" width="35%" height="35%">

If you already have an account you can log in by pressing on the *LOGIN* button in the splash screen; in this case you will be redirected to the following page:

<img src="https://github.com/Galli1598333/smart_locker/blob/master/mockup/login.jpg" width="35%" height="35%">

### Home page

If you don't have bookings your home screen will be like the following:

<img src="https://github.com/Galli1598333/smart_locker/blob/master/mockup/homeNoBookings.jpg" width="35%" height="35%">

otherwise it will be like the following:

<img src="https://github.com/Galli1598333/smart_locker/blob/master/mockup/homeBookings.jpg" width="35%" height="35%">

### Book a locker

In order to book a locker you can click on the third icon of the bottom navigation bar and you will be redirected to the following page:

<img src="https://github.com/Galli1598333/smart_locker/blob/master/mockup/selPark.jpg" width="35%" height="35%">

and now you need to select a park where you want to go. Once selected the park (clicking on the name) you will be shown a page containing a map with a marker on the park entrance. To select the day and time of the running you can press the button *SELECT HOUR*. You will be redirected to the following page:

<img src="https://github.com/Galli1598333/smart_locker/blob/master/mockup/nearYou.jpg" width="35%" height="35%">

Now you have two possibilities:
- Select a friend from those shown in the list (in this case your running will be the same day and at the same time as your friend's)
- Click on the *BOOK* button an select a new day and a new hour
In the second case you will be shown the following page:

<img src="https://github.com/Galli1598333/smart_locker/blob/master/mockup/selDate.jpg" width="35%" height="35%">

### Select a locker

Whatever your previous choice was, in order to book a locker you will be redirected to the following page:

<img src="https://github.com/Galli1598333/smart_locker/blob/master/mockup/selLocker.jpg" width="35%" height="35%">

Now you can select a locker (obviously red lockers are the ones that are not available). Once selected the locker you will be redirected to the home page and your new booking will be displayed.

### Manage the locker

In order to manage your locker (so open/close and leave it) in the home page you can click on the booking you are interested in managing. You will be redirected to the following page:

<img src="https://github.com/Galli1598333/smart_locker/blob/master/mockup/manageB.jpg" width="35%" height="35%">

Now if you want to open/close the locker you must authenticate you with your fingerprint. If you want to leave your locker you must click on the *LEAVE LOCKER* button. If you leave your locker, your booking will become disabled. If you click on the *DELETE BOOKING* button your booking will be deleted. Once you leave the locker the corresponding booking will be displayed in the profile page.

### See details about past bookings

If you click on the second icon (*Account*) of the navigation bar, the following page will be displayed:

<img src="https://github.com/Galli1598333/smart_locker/blob/master/mockup/profile.jpg" width="35%" height="35%">

If you click on one of the displayed bookings you will see the following details:

<img src="https://github.com/Galli1598333/smart_locker/blob/master/mockup/bookingDetails.jpg" width="35%" height="35%">

## ToDo

- Implement a wearOS application in order to lock and unlock the locker with the smartwatch: in this way you will be able to leave also your smartphone into the locker
- Add a backup battery management system, a NFC reader and implement a push notification system that can inform the users about events that could occur to the locker (for example an electricity failure)

## CREDITS

- Gianluca Capozzi [<img src="https://github.com/Galli1598333/smart_locker/blob/master/images/gitIcon.png" height="20" width="20" >](https://github.com/GianlucaCapozzi)
					[<img src="https://github.com/Galli1598333/smart_locker/blob/master/images/inIcon.png" height="20" width="20" >](https://www.linkedin.com/in/gianluca-capozzi-b9a75a16b/)
- Gianmarco Cariggi [<img src="https://github.com/Galli1598333/smart_locker/blob/master/images/gitIcon.png" height="20" width="20" >](https://github.com/giacar)
					[<img src="https://github.com/Galli1598333/smart_locker/blob/master/images/inIcon.png" height="20" width="20" >](https://www.linkedin.com/in/gianmarco-cariggi/)
- Gianluca Galli  [<img src="https://github.com/Galli1598333/smart_locker/blob/master/images/gitIcon.png" height="20" width="20" >](https://github.com/Galli1598333)
					[<img src="https://github.com/Galli1598333/smart_locker/blob/master/images/inIcon.png" height="20" width="20" >](https://www.linkedin.com/in/galli-gianluca/)

## LICENSE

Code is under MIT License
