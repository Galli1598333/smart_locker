# FitBox
<img src="http://www.dis.uniroma1.it/sites/default/files/marchio%20logo%20eng%20jpg.jpg">

## Description

This is an Android app for the IoT 2018/2019 course final project and it is based on [Firebase](https://firebase.google.com) and [Nucleo-64 STM32F401 board](https://www.st.com/content/st_com/en/products/evaluation-tools/product-evaluation-tools/mcu-mpu-eval-tools/stm32-mcu-mpu-eval-tools/stm32-nucleo-boards/nucleo-f401re.html). The idea is to make it easier to find a public and secure place where you can leave your belongings in order to go running in total freedom and also to find other people near you with your same interests.

## Installation

In order to install FitBox on your android smartphone you can simply install the .apk file placed in our git repository. During the installation you would be asked from the Google Play Protect service if you want to continue with the installation (this is because our application is not published on the Play Store).

## Usage

### Login

In order to access our application, you must be logged in on facebook, you can do that by clicking on the facebook button:

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

Now if you want to open/close the locker you must authenticate you with your fingerprint. If you want to leave your locker you must click on the *LEAVE LOCKER* button. If you leave your locker, your booking will be deleted both from the db and the home page.
