int red1 = 255;
int green1 = 255;
int blue1 = 255;

int red2 = 255;
int green2 = 255;
int blue2 = 255;

int redPin1 = 3;
int greenPin1 = 5;
int bluePin1 = 6;

int redPin2 = 9;
int greenPin2 = 10;
int bluePin2 = 11;

void setup()
{
  Serial.begin(9600);
}

void loop()
{
  if (Serial.available() >= 7)
  {
    if (Serial.read() == 0xff)
    {
      red1 = Serial.read();
      green1 = Serial.read();
      blue1 = Serial.read();
      
      red2 = Serial.read();
      green2 = Serial.read();
      blue2 = Serial.read();
    }
  }
  
  analogWrite(redPin1, red1);
  analogWrite(greenPin1, green1);
  analogWrite(bluePin1, blue1);
  
  analogWrite(redPin2, red2);
  analogWrite(greenPin2, green2);
  analogWrite(bluePin2, blue2);
  
  delay(10);
  
}
