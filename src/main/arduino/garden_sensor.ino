// see https://lefthandbrain.com/iot-water-metering-part-1-arduino-mqtt/

#include <Client.h>
#include <WiFiNINA.h>
#include <CooperativeMultitasking.h>
#include <MQTTClient.h>

char ssid[] = "hello";
char pass[] = "world";
char host[] = "192.168.0.8";
char clientid[] = " ";
char topicname[] = "sensors";

char sensorId[] = "42f1d91f-b118-4b63-ab57-36ad39ffda2c";
char sensorName[] = "home - patio hoop";
char type[] = "temp";
char unit[] = "deg F";

CooperativeMultitasking tasks;
WiFiClient wificlient;
MQTTClient mqttclient(&tasks, &wificlient, host, 1883, clientid, NULL, NULL);
MQTTTopic topic(&mqttclient, topicname);

int ThermistorPin = A0;
int Vo;
float R1 = 9930;
float logR2, R2, T;
float c1 = 1.009249522e-03, c2 = 2.378405444e-04, c3 = 2.019202697e-07;

void setup() {
  Serial.begin(9600);
  WiFi.begin(ssid, pass);
  delay(10000);
}

void loop() {
  
  T = readTemp(100);
  Serial.print("Temperature: "); 
  Serial.print(T);
  Serial.println(" F"); 
  sendData(T);
  delay(1000 * 60 * 15); // 15 minutes
}


void sendData(float T) {
  if (mqttclient.connect()) {
    String temp = String(T);
    String json = "{";
    json.concat("\"sensor\": { \"id\": \"" );
    json.concat(sensorId);
    json.concat("\", \"name\":\"");
    json.concat(sensorName);
    json.concat("\"}, \"type\":");
    json.concat("\"");
    json.concat(type);
    json.concat("\", \"value\":");
    json.concat(temp);
    json.concat(", \"unit\":\"");
    json.concat(unit);
    json.concat("\"}");
    
    int strLen = json.length() +1;
    char buff[strLen];
    json.toCharArray(buff, strLen);
    topic.publish(buff);
 
    while (tasks.available()) {
      tasks.run();
    }
    //
    mqttclient.disconnect();
  }
  //
  switch (WiFi.status()) {
    case WL_CONNECT_FAILED:
    case WL_CONNECTION_LOST:
    case WL_DISCONNECTED: WiFi.begin(ssid, pass);
  }
  //
  delay(3000);

}



float readTemp() {
  Vo = analogRead(ThermistorPin);
//  Serial.print("Vo: ");
//  Serial.println(Vo);
  
  R2 = R1 * (1023.0 / (float)Vo - 1.0);
  logR2 = log(R2);
  T = (1.0 / (c1 + c2*logR2 + c3*logR2*logR2*logR2));
  T = T - 273.15;
  T = (T * 9.0)/ 5.0 + 32.0;   
}

float readTemp(int numReadings) {
  float sum = 0.0;
  for (int i = 0; i < numReadings; i++) {
    sum += readTemp();
  }
  return sum / numReadings;
}
