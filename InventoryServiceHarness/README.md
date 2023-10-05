# Test Harness

- [Starting the application](#StartingTheApplication)
- [Database](#Database)

## Starting The Application

This test harness is an inventory api which provides service endpoints with data loaded from json files for stocks, sales and reviews

Implementation details of the test harness are inside the `InventoryServiceHarness` folder

`Build and start the Inventory Service Harness`

- The inventory service starts in port 8080

```bash
cd InventoryServiceHarness

mvn package

java -jar target/InventoryApi-0.0.1-SNAPSHOT.jar
```

## Database
This test harness uses a H2 database to act as a replacement for an actual database. Use http://localhost:8080/h2-console for logging in and 
running queries against the stock, sales and reviews data loaded. Default password is `pass`

![Database](../docs/TestHarnessH2DB.png?raw=true)

![Database Queries](../docs/H2DB_Queries.png?raw=true)

## Test Data

Test data was generated from https://json-generator.com/ , generated files are placed under resource and are loaded into the H2 database during startup

Sales :

```js
[
  '{{repeat(30)}}',
  {
    guid: '{{guid()}}',
    category: "smartphones",
    brand: '{{random("iphone", "samsung", "sony")}}',
    model: function (rand) {
      var model_map = {
        iphone: ['plus', 'se'],
        samsung: ['galaxy', 'notes'],
        sony: ['M', 'K']
      };
      return this.brand + ' ' + model_map[this.brand][rand.integer(0, 1)] + ' ' + rand.integer(1, 2);
    },
    salesAmount: '{{integer(20000, 60000)}}',
    sales: '{{integer(45, 120)}}',
    date: '{{ date(new Date(2023, 09, 1), new Date(2023, 09, 4), "dd-MM-YYYY")}}'
  }
]
```

Stock :

```js
[
  '{{repeat(30)}}',
  {
    guid: '{{guid()}}',
    category: "smartphones",
    brand: '{{random("iphone", "samsung", "sony")}}',
    model: function (rand) {
      var model_map = {
        iphone: ['plus', 'se'],
        samsung: ['galaxy', 'notes'],
        sony: ['M', 'K']
      };
      return this.brand + ' ' + model_map[this.brand][rand.integer(0, 1)] + ' ' + rand.integer(1, 2);
    },
    availableStock: '{{integer(20000, 60000)}}',
    releaseDate: '{{ date(new Date(2023, 09, 1), new Date(2023, 09, 4), "dd-MM-YYYY")}}'
  }
]
```

Reviews :

```js
[
  '{{repeat(30)}}',
  {
    guid: '{{guid()}}',
    category: "smartphones",
    brand: '{{random("iphone", "samsung", "sony")}}',
    model: function (rand) {
      var model_map = {
        iphone: ['plus', 'se'],
        samsung: ['galaxy', 'notes'],
        sony: ['M', 'K']
      };
      return this.brand + ' ' + model_map[this.brand][rand.integer(0, 1)] + ' ' + rand.integer(1, 2);
    },
    rating: '{{floating(5, 10, 1)}}',
    comment: '{{random("manageable", "good", "ok")}}'
  }
]
```
