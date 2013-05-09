ArduinoBiasLighting
===================

A pure Java implementation of software to drive an Arduino-based "Ambilight" LED system.  Inspired by Rajarshi Roy's version using the Open Processing IDE.


####How It Works
The software constantly samples the image on the screen and divides it into two regions (left/right).  It averages the pixel colors for each region and sends a serial command to the Arduino instructing it to change the color of two RGB LED strips attached to the back of the monitor (one on the left and another on the right).


####References
1. http://en.wikipedia.org/wiki/Ambilight
2. http://siliconrepublic.blogspot.com/2011/02/arduino-based-pc-ambient-lighting.html
