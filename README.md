# RapidWS
Rapid Web Server

- Requirements:
    - MySQL
    - Java 17
    - Python 3

- Running the app
  
   - 1 - Clone the repository
   - 2 - Run the sql script tools\create_db.sql to create the db with data.
   - 3 - Run the app
      - Windows:
          ./mvnw.cmd spring-boot:run to start the app
      - Linux:
         ./mvnw spring-boot:run to start the app
      
   Open the browser and go to [http://local](http://localhost:8080/concert) to check that the app is running.

- Running the tests
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


- REST protocol:

POST /concert
    Gets the list of concerts
    If no input is specified, returns all the concerts with its caracteristics

POST /concert
Host: localhost:8080
Content-type: application/json

{}


    The command accepts the following fields individually or combination of these

    - artist: Specified the artist name
    - place: Specified the Place
    - concertDate: Specified the concertDate


POST /concert
Host: localhost:8080
Content-type: application/json

{
    'artist': 'Metallica'
    'place': 'La Plata Stadium'
    'concertData': '2025-01-01'
}


Command response:

    All command output has the following representation:

HTTP/1.1 200 OK
Content-Type: application/json
{
    "data":[],"
    "response_code":{
        "msg":"Success",
        "code":"success"
    }
}
