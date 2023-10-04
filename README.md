# Service Manager

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

![cover](https://github.com/deepakrkris/samplespring/blob/main/docs/ServiceManagerUsecase.png?raw=true)

Service Manager

## Table of Content

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

Step 1: Start the Inventory Service Harness

```bash
cd InventoryServiceHarness

mvn package

java -jar target/InventoryApi-0.0.1-SNAPSHOT.jar
```

Step 2: Start the Service Manager

```bash
cd ServiceManager

mvn package

java -jar target/ServiceManager-0.0.1-SNAPSHOT.jar
```

Step 3: Test with the given postman collection

Import the collection and test the given APIs

![PostmanTest](https://github.com/deepakrkris/samplespring/blob/main/docs/Postman_Test.png?raw=true)

## Design

![Design](https://github.com/deepakrkris/UI_workout/blob/main/day02/05_connect4/Connect4_components.excalidraw.png?raw=true)

## Database

The harness uses H2 as a local demo database

## Licence

[MIT](/LICENCE)
This repo is licenced under the MIT Licence.
