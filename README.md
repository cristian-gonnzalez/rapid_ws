# RapidWS

## Requirements:
    - MySQL
    - Java 17
    - Python 3

## Running the app
  
       1 - Clone the repository
       2 - Run the sql script tools\create_db.sql to create the db with data.
       3 - Run the app
          - Windows:
              ./mvnw.cmd spring-boot:run
          - Linux:
             ./mvnw spring-boot:run
          
   Open the browser and go to [http://localhost:8080/concert](http://localhost:8080/concert) to check that the app is running.

## Running the tests
   You will find tests in tests\integration directory.
   
   Example:

        tests\integration> python .\concertTests.py
        Running concertTests.py
        
        simpleGetRequestResponse200Test starting ...
        simpleGetRequestResponse200Test end.
        artistGetRequestResonseOneRecordTest starting ...
        artistGetRequestResonseOneRecordTest end.
        unknownArtistGetRequestResponseEmptyTest starting ...
        unknownArtistGetRequestResponseEmptyTest end.
        multiplesGetRequestTest starting ...
        multiplesGetRequestTest end.
        combineInputFieldsGetReturnsSuccess starting ...
        combineInputFieldsGetReturnsSuccess end.
        paramGetRequestResponseOneRecTest starting ...
        paramGetRequestResponseOneRecTest end.
        paramGetRequestResponseOneTwoThreeRecTest starting ...
        paramGetRequestResponseOneTwoThreeRecTest end.
        checksResponseFields starting ...
        checksResponseFields end.
        
        concertTests.py end


## REST protocol:

- GET /concert
  
    Get the list of concerts.

    You can combine the fields in the input to get an specific list.
        
        GET /concert
        Host: localhost:8080
        Content-type: application/json
        { 
          'artist': 'ARTIST_1', 
          'place': 'PLACE_1', 
          'concertDate': '2025-01-01' 
        }

 
- GET /concert/range
  
    Get the list of concerts by the range given.
  
    You can combine the fields in the input to get an specific list.
        
        GET /concert/range
        Host: localhost:8080
        Content-type: application/json
        {
          'artist':'ARTIST_1',
          'place': 'PLACE_1', 
           'fromDate': '2020-01-01', 'untilDate':'2026-01-01', 
           'fromPrice': 10, 'untilPrice': 10000, 
           'dateASC': False, 
           'priceASC': False 
        }

 ### Request parameters:

 You can send request parameters in the GET requests to control the number of records in the output. The parameter fields are:
 
     rec_num: Record number in which starting the response
     offset: Number of records to return

 Example: return 30 records and start after finding the 10th record 
    
    GET /concert?rec_num=10&offset=30
 

- POST /concert/reserve
  
    Create a reserve:
  
       POST /concert/reserve
            Host: localhost:8080
            Content-type: application/json
        {
            "artist": "ARTIST_1",
            "place": "PLACE_1",
            "concertDate": "2025-01-01",
            "sector": "A1P1S2", 
            "seats": [3,4], 
            "surname": "Gonzalez",
            "name": "Cristian",
            "dni": "12123123"
        }

    All fields are mandatories.
  
    If you want to create a reserve in a sector with no seat, change the field 'seats': [] by 'qty': integer 


- DELETE /concert/reserve
  
    Delete a reserve:
  
       DELETE /concert/reserve
            Host: localhost:8080
            Content-type: application/json
        {
            'reserveId': 1,
            "artist": "ARTIST_1",
            "place": "PLACE_1",
            "concertDate": "2025-01-01",
            "sector": "A1P1S2", 
            "seats": [3,4]
        }



- GET /concert/reserve
  
    Get a list of reserves
  
       GET /concert/reserve
            Host: localhost:8080
            Content-type: application/json
        {
            "reserveId": 10,
            "artist": "ARTIST_1",
            "place": "PLACE_1",
            "concertDate": "2025-01-01",
            "sector": "A1P1S2", 
            "surname": "Gonzalez",
            "name": "Cristian",
            "dni": "12123123"
        }

    You can combine fields in the input. 

   ### Response

  - GET response:
  
          {
              "data": [
                {
                  "artist": "ARTIST_1",
                  "place": "PLACE_1",
                  "concertDate": "2025-01-01",
                  "concertTime": "09:00:00",
                  "concertSector": [
                    {
                      "name": "A1P1S2",
                      "price": 500000,
                      "roomSpace": 5,
                      "occupiedSpace": 4,
                      "hasSeat": true,
                      "seats": [
                        3,
                        4
                      ],
                    },
                    {
                      "name": "A1P1S3",
                      "price": 200000,
                      "roomSpace": 5,
                      "occupiedSpace": 0,
                      "hasSeat": false,
                      "seats": []
                    }
                  ],
              "appStatus": {
                "message": "Success",
                "code": "success"
              }
            }

    As you can see, all commands output has this format:

        {
            "data":[],"
            "response_code":{
                "msg":"Success",
                "code":"success"
            }
        }

## Database

### Tables

- Artist: Save the artist. For ex: metallica, korn, eminem, etc
- Place: Save the place. For ex: River Plate Stadium, La Plata Stadium, etc.
- Concert: Save the concert. The concert is the show that is defined by an artist, place and a date. For ex: the show of metallica in River Plate Stadium at 2025/01/30.
- ConcertSector: saves the sector or sections that the place has in a concert. For ex: campo, platea alta, platea baja, etc
- Seat: Save the seat number related to those sector that has seats in a concert. This table has a FK to the ConcertSector. 
- Reserve: Saves the reserve

## Directory structure

- common: Contains the classes that are shared in the app.
- db: Contains the classes related with the database comunication
- req_ctx: Contains the request context. A context containts the input (payload or request body), request parameters and output (response)
- ws: Contains the web server:
  
  -- controller: Exposes the endpoints for managing concerts and reserves.
  
  -- models: Entity for holding a record from the table
  
  -- repositories: Allows us to comunicate with the database
  
  -- services: Contains business interface for managing concerts and reseves
