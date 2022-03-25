# Build and run instructions

The service written using Java SpringBoot, and can be built/executed using the maven plugin.

## Build

- Clone the project
- On a terminal, enter the project directory
- Run the mvn package command.

```sh
mvn clean package
```

Executing the above command, builds a .jar file that can be executed.



## Run
We can run the jar using the following command:

```sh
java -jar target/demosmsapi-1.0-SNAPSHOT.jar
```
Basic Authentication is required, you can find authentication credentials in the PostGre Database or script.

Once the application is running, it exposes two endpoints:
```sh
http://localhost:8081/inbound/sms
http://localhost:8081/outbound/sms
```

The endpoints expect POST request with the following sample structure

```json
{
    "from": "4924195509029",
    "to" : "4924195509196",
    "text" : "Hello"
}
```

   
