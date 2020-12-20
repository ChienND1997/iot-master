#include <DHT.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <xxtea.h>
#include <rBase64.h>
#include <MD5Builder.h>

#define DHTPIN 13 //Chân data của DHT
#define DHTTYPE DHT22 // DHT 11 or 22

const char* WIFI_NAME = "NguyenLong";// wifi name
const char* WIFI_PASS = "taokhongbiet";//pass wifi
const char* URL       = "http://10.0.2.10:8080/api/cbnd/handler-encrypto"; //ip+path server
const char* KEY       = "Iip2u4U6rgGivv82";// nhan Key tu he thong web


byte mac[6];
char macAddr[25];
DHT dht(DHTPIN, DHTTYPE);
MD5Builder md5;

void setup() {
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
}

String getTemperatureHumidity(){
  String temperature = (String) dht.readTemperature();
  String humidity    = (String) dht.readHumidity();
  String macId       = (String) macAddr;
  String jsonData        = "{" + "\"macAddress\":" + macId + "\"temperatureValue\":" + temperature + "\"humidityValue\":" + humidity + "}";
  return jsonData;
}

char *getCipherStringBase64(char* plainData){
 size_t len;
 char *cipher = (char*)xxtea_encrypt(plainData, strlen(plainData), KEY, &len);
 rbase64.encode(cipher);
 char *cipherStringBase64 = rbase64.result();
 return cipherStringBase64;
}

void toSendHttp(String data){
    HTTPClient http; 
    http.begin(URL);      
    http.addHeader("Content-Type", "plain/text"); 
    int httpCode = http.POST(data);   //Gửi data lên web server
    if(httpCode != 200)
    {
      Serial.println("Error!" + httpCode); 
      return;
    }
    Serial.println("Send to server done!");  
    http.end();  
}



String macAddrToMd5(String str) {
  md5.begin();
  md5.add(String(str));
  md5.calculate();
  return md5.toString();
}

void loop() {
  if (WiFi.status() != WL_CONNECTED) {
     Serial.println("Error in WiFi connection");
  }
  
  String data =  getTemperatureHumidity();
  char dataArray[data.length() + 1];
  data.toCharArray(dataArray,data.length() + 1);
  String cipher = (String) getCipherStringBase64(dataArray);
  Serial.println("Cipher:" + cipher);
  String hashMac = macAddrToMd5((String) macAddr);
  Serial.println("MAC ID:" + (String) macAddr);
  Serial.println("Hash MAC:" + hashMac);

  String dataToSendSerVer = hashMac + cipher;
  toSendHttp(dataToSendSerVer);
  delay(3000);  //Gửi lại request sau mỗi 3s, sau này chỉnh lại thành 5p 1 lần
 
}
