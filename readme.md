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
Creates a car with specified parameters. Returns created car: 
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
Model/color value should be contained in the corresponding list (see above).  
Manufacture year should be between 1885 and (current year + 5 year). 

#### Read
```http
GET /api/cars
Content-Type: application/json
```
Body example (all parameters are **optional**, but json must be valid):  
```json
{
	"model": ["Lada", "Audi"],
	"color": ["Red", "Black"],
	"number": "A777AA 77",
	"manufactureYearMin": 1850,
	"manufactureYearMax": 2020,
	"sortedBy": "createdAt",
	"sortedAsc": true
}
```
Returns a list of cars that match the criteria:
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
  
Optional parameters description: 

| Parameter | Type | Description |
| :--- | :--- | :--- |
|**Filters**|
| `model` | `array` | List of required car models |
| `color` | `array` | List of required car colors |
| `number` | `string` | Exact car number |
| `manufactureYearMin` | `integer` | Minimal manufacture year |
| `manufactureYearMax` | `integer` | Maximal manufacture year |
|**Sorting**|
| `sortedBy` | `string` | Sorting parameter (model, color, number, manufactureYear or createdAt) |
| `sortedAsc` | `boolean` | Ascending(true) or Descending(false) sort.<br>**Default: true** |

#### Delete
```http
DELETE /api/cars/[car_id]
```
Deletes a car with specific id.   
Returns deleted car id:
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
