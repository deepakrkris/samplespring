# Service Manager Design and Implementation

- [Design](#Design)
- [ServiceRegistry](#ServiceRegistry)
- [List Intersection](#ListIntersection)
- [BitMap Intersection](#BitMapIntersection)
- [Aggregation](#Aggregation)
- [Endpoint Call Limit](#EndpointCallLimit)
- [Concurrency](#Concurrency)

## Design
![Design](https://github.com/deepakrkris/samplespring/blob/main/docs/ServiceManager_Internals.png?raw=true)

## Service Registry
Service Registry registers all the services and initializes maps that connect the supported params to the services, which are later used for 
set intersection.

- Parameters to List of ServiceIds map
- Parameters to Services Bitmap

This implementation looks the core of the solution to identify services from params as a Set Intersection problem.
There are various set intersection discussed like List , BitMap

## List Intersection
When a list of params are sent for querying, the java collections framework is utilized to find the intersection of all serviceIds

## BitMap Intersection
When a list of params are sent for querying, a BitMap utility is used to find the intersection of all serviceIds

![Bitmap](https://github.com/deepakrkris/samplespring/blob/main/docs/ServiceManager_BitMap_Intersection.png?raw=true)

## Aggregation
A custom aggregation endpoint is included in the ServiceManager to demo on data aggregation. It is not a generic implementation
and is very demo specific.

![Aggregation](https://github.com/deepakrkris/samplespring/blob/main/docs/ServiceDataAggregation_Example.png?raw=true)

## Endpoint call limit
Every endpoint has a concurrency limit, this implementation uses one local semaphore for every registered endpoint.
But in a distributed scenario where multiple service managers are expected global counters should also be used.

## Concurrency
The endpoints are to be invoked concurrently for managing call latency. A thread pool is used to assign executors and wait for turns using futures
