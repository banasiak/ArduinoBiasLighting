ArduinoBiasLighting
===================

A pure Java implementation of software to drive an Arduino-based "Ambilight" LED system.  Inspired by Rajarshi Roy's version using the Open Processing IDE.


####How It Works
The software constantly samples the image on the screen and divides it into two regions (left/right).  It averages the pixel colors for each region and sends a serial command to the Arduino instructing it to change the color of two RGB LED strips attached to the back of the monitor (one on the left and another on the right).

####Demo Videos
* Test 1 - Static Colors:  http://www.youtube.com/watch?v=i2vxLTBGXME
* Test 2 - Contrasting Channels:  http://www.youtube.com/watch?v=OUaGR0aM8Ps
* Test 3 - Fast Action: http://www.youtube.com/watch?v=g6H-SKYLm7g

####Suggested Hardware
* Arduino Uno Kit: http://www.amazon.com/gp/product/B0051QHPJM
* RGB LED Strip (5 m): http://www.amazon.com/gp/product/B0085IXEYS
* Diagnostic RGB LEDs (50 pcs): http://www.amazon.com/gp/product/B005VMDROS
* Fancy Wire (10 m): http://www.amazon.com/gp/product/B009VD3EW8
* Darlington Transistor Array: http://www.digikey.com/product-detail/en/ULN2003A/497-2344-5-ND/599603


####References
1. http://en.wikipedia.org/wiki/Ambilight
2. http://siliconrepublic.blogspot.com/2011/02/arduino-based-pc-ambient-lighting.html
