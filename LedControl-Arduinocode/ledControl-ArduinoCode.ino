char command;
String string;
boolean ledon = false;
#define led 5
void setup()
{
Serial.begin(9600);
pinMode(led, OUTPUT);
}
void loop()
{
if (Serial.available() > 0)
{string = "";}
while(Serial.available() > 0)
{command = ((byte)Serial.read());
  Serial.println(command); 
if(command == ':')
{
break;
}
else
{
string += command;
}
delay(1);
}
if(string == "on")
{
ledOn();
ledon = true;
}
if(string =="off")
{
ledOff();
ledon = false;
}
if ((string.toInt()>=0)&&(string.toInt()<=255))
{
if (ledon==true)
{
analogWrite(led, string.toInt());
delay(10);
}
}
}
void ledOn()
{
analogWrite(led, 255);
delay(10);
}
void ledOff()
{
analogWrite(led, 0);
delay(10);
}
