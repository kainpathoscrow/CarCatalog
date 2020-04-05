# Car Catalog
Simple car catalog api. Allows you to create, search and delete cars from catalog.  
Written using Scala, Play Framework, PostgreSQL+Slick and Caffeine caching.  

## Deployment
1. Configure conf/application.conf for your database connection (url, user, password)
2. `sbt run` will run the app on **localhost:9000**
3. Open localhost:9000 and apply migrations

## Api Reference
### Colors
```http
GET /api/colors
```
Returns a json array of available car colors:
```json
[
    "Black",
    "White",
    "Red",
    "Silver",
    "Blue",
    "Yellow",
    "Orange",
    "Gold",
    "Grey",
    "Green"
]
```

### Models
```http
GET /api/models
```
Returns a json array of available car models:
```json
[
    "Audi",
    "BMW",
    "Ford",
    "Honda",
    "Lexus",
    "Nissan",
    "Opel",
    "Toyota",
    "Volkswagen",
    "LADA",
    "Mitsubishi"
]
```

### Cars
  
#### Create
```http
POST /api/cars
Content-Type: application/json
```
Body example (all parameters are **required**):
```json
{
    "model": "Lada",
    "number": "A777AA 77",
    "color": "Red",
    "manufactureYear": 2000
}
```
Creates a car with specified parameters. Returns the created car: 
```json
{
    "id": 27,
    "model": "LADA",
    "color": "Red",
    "number": "A777AA 77",
    "manufactureYear": 2000,
    "createdAt": 1586061362023
}
```
Model/color value should be one of the values in the models/colors list (see above).  
Manufacture year should be between 1885 and (current year + 5 year). 

#### Read
```http
GET /api/cars?model=Lada&model=Audi&color=Red&color=Black&number=A777AA%2077&manufactureYearMin=1850&manufactureYearMax=2020&sortedBy=model&sortingDirection=-1
```
Returns a list of cars that match the query string criteria (all parameters are **optional**):
```json
[
    {
        "id": 27,
        "model": "LADA",
        "color": "Red",
        "number": "A777AA 77",
        "manufactureYear": 2000,
        "createdAt": 1586061362023
    }
]
```
  
Query parameters description: 

| Parameter | Description |
| :--- | :--- |
|**Filters**|
| `model` |  Car model <br> _Allowed multiple choice, like ?model=Lada&model=Audi_|
| `color` |  Car color <br> _Allowed multiple choice, like ?color=Red&color=Black_ |
| `number` | Exact car number |
| `manufactureYearMin` | Minimal manufacture year |
| `manufactureYearMax` | Maximal manufacture year |
|**Sorting**|
| `sortedBy` | Sorting parameter (model, color, number, manufactureYear or createdAt) |
| `sortingDirection` | -1 for descending sorting, any other value for ascending<br>**Default: true** |

#### Delete
```http
DELETE /api/cars/[car_id]
```
Deletes a car with a specific id.   
Returns the deleted car's id:
```json
{
    "deletedId": 26
}
```

#### Statistics
```http request
GET /api/cars/statistics
```
Returns a catalog statistics:
```json
{
    "totalCars": 12,
    "firstCarCreationUtc": 1585981929536,
    "lastCarCreationUtc": 1586059671802
}
```

### Exceptions
All requests may return an error with 4xx or 5xx status code.
Error format:
```json
{
    "error": "[ERROR_DESCRIPTION]"
}
```
