#include <DHT.h>
#include <ESP8266WiFi.h>
#include <xxtea.h>
#include <rBase64.h>
#include <MD5Builder.h>
#include <PubSubClient.h>
#include <WiFiClient.h>

#define DHTPIN 13 //Chân data của DHT
#define DHTTYPE DHT22 // DHT 11 or 22

char* KEY = "2q7dFwrS9R4TLlYW";  // nhan Key tu he thong web

const char* WIFI_NAME = "tranvietanh";// wifi name
const char* WIFI_PASS = "12344321";//pass wifi
       
const char* HOST          = "192.168.43.209"; //MQTT Server
const int   PORT          = 1883;
const char* MQTT_USERNAME = "tranvietanh190196";
const char* MQTT_PASSWORD = "vietanhh12";
    

long time_lable = 0;
//============================================================
DHT dht(DHTPIN, DHTTYPE);
WiFiClient espClient;
PubSubClient client(espClient);
char macAddr[25];


void setup() {
  byte mac[6];
  
  
  Serial.begin(115200);
  dht.begin();
  
  WiFi.macAddress(mac);
  sprintf(macAddr, "%2X:%2X:%2X:%2X:%2X:%2X", mac[0], mac[1], mac[2], mac[3], mac[4], mac[5]);
  Serial.print("Connecting to "); Serial.println(WIFI_NAME);
  WiFi.begin(WIFI_NAME, WIFI_PASS);//Kết nối đến wifi.
  while (WiFi.status() != WL_CONNECTED) {  //Đợi kết nối thành công
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWifi Connected!");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  client.setServer(HOST, PORT);
  client.setCallback(callback);
  while (!client.connected()) {
    Serial.println("Connecting to MQTT...");
    if(client.connect("ESP8266Client", MQTT_USERNAME, MQTT_PASSWORD )) {
     Serial.println("Connected");
    }else{
      Serial.print("failed state ");
      Serial.print(client.state());
      delay(2000);
    }
  }
  client.subscribe("mqtt-cbnd-encrypto", 0);
  client.subscribe("test", 0);
 
}

String getData(){
  String temperature = (String) dht.readTemperature();
  String humidity    = (String) dht.readHumidity();
  String data        =  temperature + ";" + humidity + ";" + time_lable;
  return data;
}


void publishServer(String data){
    char buff[data.length()+1];
    data.toCharArray(buff, data.length() + 1);
    client.publish("mqtt-cbnd-encrypto", buff);
}

void callback(char* topic, byte* payload, unsigned int length)
{
  Serial.print("Messageved in topic: ");
  Serial.println(topic);
  Serial.print("Message:");
  String data;
  for(int i = 0; i < length; i++) {
   data += (char) payload[i];
  }
  Serial.println(data);
}

void loop() {
  time_lable++;
  client.loop();
  if (WiFi.status() != WL_CONNECTED) {
     Serial.println("Error in WiFi connection");
  }
  String data =  getData();
  String message = (String) getCipherStringBase64(data);
  
  String messageAndKey = message + (String) KEY;
  String MAC           = toMd5(messageAndKey);
  String dataSend      = (String) macAddr + ";" + MAC + ";"  + message;
  
  publishServer(dataSend);
  Serial.println(data);
  Serial.println(message);
  Serial.println(MAC); 

  delay(3000);  //Gửi lại request sau mỗi 3s, sau này chỉnh lại thành 5p 1 lần
 
}

char *getCipherStringBase64(String data){
 size_t len;
 char dataArray[data.length() + 1];
 data.toCharArray(dataArray, data.length() + 1);
 
 char *cipher = (char*)xxtea_encrypt(dataArray, strlen(dataArray), KEY, &len);
 rbase64.encode(cipher);
 char *cipherStringBase64 = rbase64.result();
 return cipherStringBase64;
}
String toMd5(String str) {
  MD5Builder md5;
  md5.begin();
  md5.add(String(str));
  md5.calculate();
  return md5.toString();
}
