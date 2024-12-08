import sys
import requests
import json
import os
from common import test_runner, assert_reponse


api_url = "http://127.0.0.1:8080"
headers = {"Content-Type":"application/json"}


reserve_id = None


def assertFieldsResponse( response ):
    ticket_reserve = ['datetime', 'reserveId', 'totalInfo', 'userInfo', 'concertInfo']
    concert_info = ['artist', 'place', 'concertDate', 'sector']
    user = ['name', 'surname', 'dni']
    total_info = ['price', 'total', 'quantity']
    
    assert 'data' in response.keys()
    for k in ticket_reserve:
        assert k in response['data'].keys(), response['data'].keys()
    for k in total_info:
        assert k in response['data']['totalInfo'].keys(), response['data']['totalInfo'].keys() 
    for k in concert_info:
        assert k in response['data']['concertInfo'].keys(), "%s != %s " % (k, response['data']['concertInfo'].keys() )
    for k in user:
        assert k in response['data']['userInfo'].keys(), "%s != %s " % (k, response['data']['userInfo'].keys() )


def reserveResponse200Test():

    url = api_url + "/concert/reserve"
    data = {
        "artist": "ARTIST_1",
        "place": "PLACE_1",
        "concertDate": "2025-01-01",
        "sector": "A1P1S2", 
        "seats": [3,4], 
        "surname": "Gonzalez",
        "name": "Cristian",
        "dni": "12123123"
    }
    response = requests.post(url, data=json.dumps(data), headers=headers)
    print(response.json())
    assert response.status_code == 200, response.status_code
    response = assert_reponse( response )
    
    assert isinstance(response['data'], dict)
    assert response['appStatus']['code'] == 'success', response['appStatus']['code']
    assertFieldsResponse( response )
    global reserve_id
    reserve_id = response['data']['reserveId']


def deleteReserveResponse200Test():
    global reserve_id
    
    url = api_url + "/concert/reserve"
    data = {
        'reserveId': 1,
        "artist": "ARTIST_1",
        "place": "PLACE_1",
        "concertDate": "2025-01-01",
        "sector": "A1P1S2", 
        "seats": [3,4]
    }

    response = requests.delete(url, data=json.dumps(data), headers=headers)
    assert response.status_code == 200, response.status_code
    response = assert_reponse( response )
    print(response)
    
    #assert isinstance(response['data'], dict)
    assert response['appStatus']['code'] == 'success', response['appStatus']['code']
    

def reserveDuplicatedResponseFailedTest():

    url = api_url + "/concert/reserve"
    data = {
        "artist": "ARTIST_1",
        "place": "PLACE_1",
        "concertDate": "2025-01-01",
        "sector": "A1P1S2", 
        "seats": [3,4], 
        "surname": "Gonzalez",
        "name": "Cristian",
        "dni": "12123123"
    }
    response = requests.post(url, data=json.dumps(data), headers=headers)
    print( response.json() )
    response = response.json()
    #assert response.status_code == 400, response.status_code
    #response = assert_reponse( response )
    
    #assert not isinstance(response['data'], dict)
    assert response['appStatus']['code'] == 'noRoomAvailable', response['appStatus']['code']


if __name__ == '__main__': 

    tests = [
        test_runner(reserveResponse200Test),
        test_runner(reserveDuplicatedResponseFailedTest),
        test_runner(deleteReserveResponse200Test)
    ]

    print("Running " + os.path.basename(__file__) + "\n" )

    for t in tests:
        t()

    print( "\n" + os.path.basename(__file__) + " end\n" )
    


    


    