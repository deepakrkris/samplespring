# Service Manager Design and Implementation

Service Manager Design and Implementation

- [Requirements](#Requirements)
- [Installation](#installation)
- [Design](#Design)
- [Licence](#Licence)
- [Database](#Database)

## Requirements

This project requires local installations of the following

- [Java](version 11)
- [Maven](https://maven.apache.org/install.html)
- [Postman](https://www.postman.com/home)

## Build and Start

`Step 1: Start the Inventory Service Harness`

- The inventory service starts in port 8080

```bash
cd InventoryServiceHarness

mvn package

java -jar target/InventoryApi-0.0.1-SNAPSHOT.jar
```

`Step 2: Start the Service Manager`

- The service manager starts in port 9090, inventory service must be started for the service manager to start successfully

```bash
cd ServiceManager

mvn package

java -jar target/ServiceManager-0.0.1-SNAPSHOT.jar
```

`Step 3: Test with the given postman collection`

Import the collection and test the given APIs

![PostmanTest](https://github.com/deepakrkris/samplespring/blob/main/docs/Postman_Test.png?raw=true)

## Implementation Details

This implementation looks the core of the solution to identify services from params as a Set Intersection problem.

There are various set intersection discussed like List , BitMap, etc in the README.md of the ServiceManager project

![Design](https://github.com/deepakrkris/samplespring/blob/main/docs/ServiceManager_BitMap_Intersection.png?raw=true)

## Database

The harness uses H2 as a local demo database

