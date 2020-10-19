 vb-api

How to start the vb-api application
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/vb-api-1.0-SNAPSHOT.jar server`
3. To check that your application is running enter url `http://localhost:8080`

RestAPI:
---
1. Get Country Name from provided Country Codes CSV string:  
   - 2 parameters should be provided:
     - `path_parameter`: Country Codes parameter: Ex: "<code1>, <code2>,<code3>,..."  
      Note: could be provided with ot without spaces.
     - `query_parameter`: refresh=[true|false]: Should be local file refreshed from remote API or not.  
    
2. Get Country Code from provided Country Names CSV string:  
   - 2 parameters should be provided:
     - `path_parameter`: Country Names parameter: Ex: "<name1>, <name2> , <name3>,..."  
      Note: could be provided with ot without spaces.
     - `query_parameter`: refresh=[true|false]: Should be local file refreshed from remote API or not.  
    
Appendix:
---
1. Get Country Name curl example and return output:
   ```
   Country Codes parameter: "US, CA,12"
   ``` 
   curl command:
   ```
   curl -X GET --header 'Accept: application/json' 'http://localhost:8080/api/v1/convert/US%2C%20CA%2C12?refresh=true'
   ```
   Returns:
   ```
   [
       {
           "countryCode": "12",
           "countryName": "Unknown Country"
       },
       {
           "countryCode": "CA",
           "countryName": "Canada"
       },
       {
           "countryCode": "US",
           "countryName": "United States"
       }
   ]
   ```

2. Get Country Code curl example and return output:  
   ```
   Country Names parameter: "Canada, Anguilla,XXXXX,Antigua and Barbuda"
   ```
   curl command:
   ```
   curl -X GET --header 'Accept: application/json' 'http://localhost:8080/api/v1/convertreverse/Canada%2C%20Anguilla%2CXXXXX%2CAntigua%20and%20Barbuda?refresh=true'
   ```
   Returns:
   ```
   [
       {
           "countryName": "Anguilla",
           "countryCode": "AI"
       },
       {
           "countryName": "Antigua and Barbuda",
           "countryCode": "AG"
       },
       {
           "countryName": "Canada",
           "countryCode": "CA"
       },
       {
           "countryName": "UnknownCountry_XXXXX",
           "countryCode": "XXXXX"
       }
   ]
   ```

ToDo:
---
1. Complete Health and Diag interfaces
2. Fix `refresh` parameter default behavior
3. Create proper exceptions handling.