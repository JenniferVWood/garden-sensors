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

char sensorId[] = "e331c2b4-a932-11eb-9184-ffb10fae0872";
char sensorName[] = "farm - hoop";
char type[] = "temp";
char unit[] = "deg F";

CooperativeMultitasking tasks;
WiFiClient wificlient;
MQTTClient mqttclient(&tasks, &wificlient, host, 1883, clientid, NULL, NULL);
MQTTTopic topic(&mqttclient, topicname);

int ThermistorPin = A1;
int Vo;
float R1 = 9960;
float logR2, R2, T;

//https://forum.arduino.cc/t/coefficients-correct-according-to-docs-but-thermistor-temp-10-degrees-off/590149/7
float c1 = 0.0020959970520719553, c2 = 0.00005193374541559819, c3 = 8.742988520719741e-7;


//http://cdn.sparkfun.com/datasheets/Sensors/Temp/ntcle100.pdf model 103*B0
// calculator: https://rusefi.com/Steinhart-Hart.html


void setup() {
  Serial.begin(9600);
  WiFi.begin(ssid, pass);
  delay(10000);
}

void loop() {

  T = readTemp();
  Serial.print("Temperature: ");
  Serial.print(T);
  Serial.println(" F");
  sendData(T);
//  delay(1000);
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

float readSensor(int numSamples) {
  float sum = 0.0;
  for (int i = 0; i < numSamples; i++) {
    sum += analogRead(ThermistorPin);
    delay(10);
  }
  return sum / numSamples;
}


float readTemp() {
  Vo = readSensor(100);

//  Serial.print("Vo: ");
//  Serial.println(Vo);

  R2 = R1 * (1023.0 / (float)Vo - 1.0);
  logR2 = log(R2);
  T = (1.0 / (c1 + c2*logR2 + c3*logR2*logR2*logR2));
  T = T - 273.15;
  T = (T * 9.0)/ 5.0 + 32.0;
}

//float readTemp(int numReadings) {
//  float sum = 0.0;
//  for (int i = 0; i < numReadings; i++) {
//    sum += readTemp();
//  }
//  return sum / numReadings;
//}