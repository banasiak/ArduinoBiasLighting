#define NUM_INPUT_PINS 3
#define NUM_OUTPUT_PINS 3

// RGB color definitions
int black[3] = {0, 0, 0};
int blue[3] = {0, 0, 255};
int green[3] = {0, 255, 0};
int cyan[3] = {0, 255, 255};
int red[3] = {255, 0, 0};
int magenta[3] = {255, 0, 255};
int yellow[3] = {255, 255, 0};
int white[3] = {255, 255, 255};

// other colors
int purple[3] = {128, 0, 255};
int pink[3] = {175, 75, 148};
int orange[3] = {237, 120, 6};

// control input pins
int powerControlPin = 2;
int modeControlPin = 12;

// variables to store control input values
int powerControlValue;
int modeControlValue;

// input pins
int regionOneInputPins[NUM_INPUT_PINS] = {A0, A1, A2};
int regionTwoInputPins[NUM_INPUT_PINS] = {A3, A4, A5};

// arrays to store input values
int regionOneValues[NUM_INPUT_PINS] = {0, 0, 0};
int regionTwoValues[NUM_INPUT_PINS] = {0, 0, 0};

// output pins {red, green, blue}
int regionOneOutputPins[NUM_OUTPUT_PINS] = {3, 5, 6};
int regionTwoOutputPins[NUM_OUTPUT_PINS] = {9, 10, 11};


void setup()
{
  // set control pins to INPUT
  pinMode(powerControlPin, INPUT);
  pinMode(modeControlPin, INPUT);
  
  // set region 1 & 2 input pins to INPUT
  for (int i=0; i<NUM_INPUT_PINS; i++)
  {
    pinMode(regionOneInputPins[i], INPUT);
    pinMode(regionTwoInputPins[i], INPUT);
  }
  
  // set region 1 & 2 output pins to OUTPUT
  for (int i=0; i<NUM_OUTPUT_PINS; i++)
  {
    pinMode(regionOneOutputPins[i], OUTPUT);
    pinMode(regionTwoOutputPins[i], OUTPUT);
  }
  
  // open the serial connection 
  Serial.begin(9600);
}


void loop()
{
  // read the values of the control pins
  powerControlValue = digitalRead(powerControlPin);
  modeControlValue = digitalRead(modeControlPin);
  
  if(powerControlValue == 0)
  {
    // disable the LEDs
    illuminateLED(regionOneOutputPins, black);
    illuminateLED(regionTwoOutputPins, black);
  }
  else
  {
    if(modeControlValue == 0)
    {      
      // control colors using DIP switches
      readInputValues(regionOneInputPins, regionOneValues);
      setRegionColor(regionOneOutputPins, regionOneValues);
      
      readInputValues(regionTwoInputPins, regionTwoValues);
      setRegionColor(regionTwoOutputPins, regionTwoValues);
    }
    else
    { 
      // control the colors using the serial port
      if(Serial.available() >= 7)
      {
        if(Serial.read() == 0xff)
        {
          regionOneValues[0] = Serial.read();
          regionOneValues[1] = Serial.read();
          regionOneValues[2] = Serial.read();
          regionTwoValues[0] = Serial.read();
          regionTwoValues[1] = Serial.read();
          regionTwoValues[2] = Serial.read();          
        }
      }
      illuminateLED(regionOneOutputPins, regionOneValues);
      illuminateLED(regionTwoOutputPins, regionTwoValues);
    } 
  } 
}

  
void readInputValues(int inputPins[], int inputValues[])
{
  for (int i=0; i<NUM_INPUT_PINS; i++)
  {
    inputValues[i] = digitalRead(inputPins[i]);
  }
}


void printValues(String arrayName, int array[], int arraySize)
{
  for (int i=0; i<arraySize; i++)
  {
    Serial.print(arrayName);
    Serial.print("[");
    Serial.print(i);
    Serial.print("] = ");
    Serial.println(array[i]);
  }
  
  Serial.println("");
}


void setRegionColor(int regionOutputPins[], int regionValues[])
{
  // treat these 3 pins as binary input, do some clever math to see what the decimal value is
  int regionPinTotal = (4 * regionValues[0]) + (2 * regionValues[1]) + (regionValues[2]);
  
  // set the color according to the total binary value of the 3 pins
  switch(regionPinTotal)
    {
      case 0:
        //red
        illuminateLED(regionOutputPins, blue);
        break;
      case 1:
        //green
        illuminateLED(regionOutputPins, green);
        break;
      case 2:
        //blue
        illuminateLED(regionOutputPins, cyan);
        break;
      case 3:
        //yellow
        illuminateLED(regionOutputPins, red);
        break;
      case 4:
        //purple
        illuminateLED(regionOutputPins, magenta);
        break;
      case 5:
        //pink
        illuminateLED(regionOutputPins, yellow);
        break;
      case 6:
        //orange
        illuminateLED(regionOutputPins, orange);
        break;
      case 7:
        //white
        illuminateLED(regionOutputPins, white);
        break;  
    }
}


void illuminateLED(int outputPins[], int color[])
{
  for(int i=0; i<NUM_OUTPUT_PINS; i++)
  {
    analogWrite(outputPins[i], color[i]);
  }
}

