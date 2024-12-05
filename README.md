# rapidws
Rapid Web Server


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
