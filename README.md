# Service Manager

![cover](./docs/ServiceManagerUsecase.png?raw=true)

This project requires local installations of the following

- [Java](version > 11)
- [Maven](https://maven.apache.org/install.html)
- [Postman](https://www.postman.com/home)

## Table of Content

- [About](#About)
- [Test Harness](#TestHarness)
- [Service Manager](#ServiceManager)
- [Postman Collection](#PostmanCollection)

## About

This prototype implementation of a service manager has the following parts

- 1. A test harness
- 2. The Service Manager framework
- 3. A postman collection

## Test Harness

This test harness is an inventory api which provides service endpoints with data loaded from json files for stocks, sales and reviews.

For example testing the service manager with,
    - http://localhost:9090/invoke?brand=iphone gives the stocks, sales, reviews for iphone
    - http://localhost:9090/invoke?brand=samsung&date=__fromDate:01-10-2023 gives the stocks from the first of october
    - http://localhost:9090/invoke/aggregateByModel?brand=iphone, gives the aggregated sales and stock data for iphone

All the above test urls are configured in the given postman collection with visualization scripts.

Implementation details of the test harness are inside the `InventoryServiceHarness` folder

`Build and start the Inventory Service Harness`

- The inventory service starts in port 8080

```bash
cd InventoryServiceHarness

mvn package

java -jar target/InventoryApi-0.0.1-SNAPSHOT.jar
```

## Service Manager

The Service Manager is implemented as a Spring Boot application, more details on the requirements and design are in the `ServiceManager` folder.
This design looks at the solution to identify services from params as a `set intersection` problem. There are various set intersection algorithms discussed like List, BitMap in the README.md of the ServiceManager project

`Build and start the Service Manager`

- The service manager starts in port 9090 and is preconfigured with endpoints of the inventory harness. The inventory service must be started for the service manager to start successfully

```bash
cd ServiceManager

mvn package

java -jar target/ServiceManager-0.0.1-SNAPSHOT.jar
```

## Postman Collection

The postman collection has test scripts with params for category, model, brand, zip, sales, sales amount, available stock.
It can test the service manager and visualize from the results, it also has a load test suite

`Test with the given postman collection`

Import the collection into postman and test the given APIs

![PostmanTest](https://github.com/deepakrkris/samplespring/blob/main/docs/Postman_Test.png?raw=true)
