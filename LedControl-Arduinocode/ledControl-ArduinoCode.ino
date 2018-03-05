/*
   Copyright [2018] [Adish Mallik]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/


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
