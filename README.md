# RapidWS

## Requirements:
    - MySQL
    - Java 17
    - Python 3

## Running the app
  
   - 1 - Clone the repository
   - 2 - Run the sql script tools\create_db.sql to create the db with data.
   - 3 - Run the app
      - Windows:
          ./mvnw.cmd spring-boot:run to start the app
      - Linux:
         ./mvnw spring-boot:run to start the app
      
   Open the browser and go to [http://local](http://localhost:8080/concert) to check that the app is running.

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

- POST /concert
    Gets the list of concerts.
    You can send combinatios of input fields o none depends on the search.
        
        POST /concert
        Host: localhost:8080
        Content-type: application/json
        { 
          'artist': 'ARTIST_1', 
          'place': 'PLACE_1', 
          'concertDate': '2025-01-01' 
        }

    Example of response:
  
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
