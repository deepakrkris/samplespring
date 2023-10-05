# Service Manager Design and Implementation

- [Design](#Design)
- [ServiceRegistry](#ServiceRegistry)
- [List Intersection](#ListIntersection)
- [BitMap Intersection](#BitMapIntersection)
- [Aggregation](#Aggregation)
- [Endpoint Call Limit](#EndpointCallLimit)
- [Concurrency](#Concurrency)

## Design
![Design](./docs/ServiceManager_Internals.png?raw=true)

- The service manager starts in port 9090 and is preconfigured with endpoints of the inventory harness. The inventory service must be started for the service manager to start successfully

```bash
cd ServiceManager

mvn package

java -jar target/ServiceManager-0.0.1-SNAPSHOT.jar
```

## Service Registry
Service Registry registers all the services and initializes maps that connect the supported params to the services, which are later used for 
set intersection.

- Parameters to List of ServiceIds map
- Parameters to Services Bitmap

This implementation looks the core of the solution to identify services from params as a Set Intersection problem.

## List Intersection
When a list of params are sent for querying, the java collections framework is utilized to find the intersection of all serviceIds

## BitMap Intersection
When a list of params are sent for querying, a BitMap utility is used to find the intersection of all serviceIds

![Bitmap](./docs/ServiceManager_BitMap_Intersection.png?raw=true)

## Aggregation
A custom aggregation endpoint is included in the ServiceManager to demo on data aggregation. It is not a generic implementation
and is very demo specific.

![Aggregation](./docs/ServiceDataAggregation_Example.png?raw=true)

## Endpoint call limit
Every endpoint has a concurrency limit, this implementation uses one local semaphore for every registered endpoint.
But in a distributed scenario where multiple service managers are expected global counters should also be used.

## Concurrency
The endpoints are to be invoked concurrently for managing call latency. A thread pool is used to assign executors and wait for turns using futures.
