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
int serialControlPin = 12;

int powerControlValue;
int serialControlValue;

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
  pinMode(serialControlPin, INPUT);
  
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
  powerControlValue = digitalRead(powerControlPin);
  serialControlValue = digitalRead(serialControlPin);
  
  if(powerControlValue == 0)
  {
    illuminateLED(regionOneOutputPins, black);
    illuminateLED(regionTwoOutputPins, black);
  }
  else
  {
    if(serialControlValue == 0)
    {
      // control colors using DIP switches
      readInputValues(regionOneInputPins, regionOneValues);
      setRegionOneLED();
      
      readInputValues(regionTwoInputPins, regionTwoValues);
      setRegionTwoLED(); 
    }
    else
    {
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
      delay(10);
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

void setRegionOneLED()
{
    int regionOnePinTotal = (4 * regionOneValues[0]) + (2 * regionOneValues[1]) + (regionOneValues[2]);
    switch(regionOnePinTotal)
    {
      case 0:
        //red
        illuminateLED(regionOneOutputPins, blue);
        break;
      case 1:
        //green
        illuminateLED(regionOneOutputPins, green);
        break;
      case 2:
        //blue
        illuminateLED(regionOneOutputPins, cyan);
        break;
      case 3:
        //yellow
        illuminateLED(regionOneOutputPins, red);
        break;
      case 4:
        //purple
        illuminateLED(regionOneOutputPins, magenta);
        break;
      case 5:
        //pink
        illuminateLED(regionOneOutputPins, yellow);
        break;
      case 6:
        //orange
        illuminateLED(regionOneOutputPins, orange);
        break;
      case 7:
        //white
        illuminateLED(regionOneOutputPins, white);
        break;  
    }
}

void setRegionTwoLED()
{
    int regionTwoPinTotal = (4 * regionTwoValues[0]) + (2 * regionTwoValues[1]) + (regionTwoValues[2]);
    switch(regionTwoPinTotal)
    {
      case 0:
        //red
        illuminateLED(regionTwoOutputPins, blue);
        break;
      case 1:
        //green
        illuminateLED(regionTwoOutputPins, green);
        break;
      case 2:
        //blue
        illuminateLED(regionTwoOutputPins, cyan);
        break;
      case 3:
        //yellow
        illuminateLED(regionTwoOutputPins, red);
        break;
      case 4:
        //purple
        illuminateLED(regionTwoOutputPins, magenta);
        break;
      case 5:
        //pink
        illuminateLED(regionTwoOutputPins, yellow);
        break;
      case 6:
        //orange
        illuminateLED(regionTwoOutputPins, orange);
        break;
      case 7:
        //white
        illuminateLED(regionTwoOutputPins, white);
        break;  
    } 
}

void illuminateLED(int outputPins[3], int color[3])
{
  for(int i=0; i<NUM_OUTPUT_PINS; i++)
  {
    analogWrite(outputPins[i], color[i]);
  }
}

