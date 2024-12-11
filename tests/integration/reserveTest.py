import sys
import requests
import json
import os
from common import *
import setup
from setup import CONCERTS

logger = Logger(True)

api_url = "http://127.0.0.1:8080"
url = api_url + "/concert/reserve"
    

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
    data = {
        "artist": CONCERTS[3]['artist'],
        "place": CONCERTS[3]['place'],
        "concertDate": CONCERTS[3]['concertDate'],
        "sector": CONCERTS[3]['sectors'][0]['name'], 
        "seats": [3,4], 
        "surname": "Gonzalez",
        "name": "Cristian",
        "dni": "12123123"
    }

    logger.log( "Sending " + str( data ) )
    response = send_post( url, data )
    logger.log( "Response " + str( response ) )

    assert isinstance(response['data'], dict)
    assert response['appStatus']['code'] == 'success', response['appStatus']['code']
    assertFieldsResponse( response )
    global reserve_id
    reserve_id = response['data']['reserveId']


def deleteReserveResponse200Test():
    global reserve_id
    
    data = {
        'reserveId': reserve_id,
        "artist": CONCERTS[3]['artist'],
        "place": CONCERTS[3]['place'],
        "concertDate": CONCERTS[3]['concertDate'],
        "sector": CONCERTS[3]['sectors'][0]['name']
    }

    response = send_delete(url, data)
    response = assert_reponse( response )
    

def reserveDuplicatedResponseFailedTest():

    data = {
        "artist": CONCERTS[3]['artist'],
        "place": CONCERTS[3]['place'],
        "concertDate": CONCERTS[3]['concertDate'],
        "sector": CONCERTS[3]['sectors'][0]['name'], 
        "seats": [3,4], 
        "surname": "Gonzalez",
        "name": "Cristian",
        "dni": "12123123"
    }
    response = send_post(url, data, check_http_status_ok=False)
    assert_error( response, 'alreadyRserved' )

def reservesBoundariesResponseErrorTest():

    for concert in CONCERTS:
        data = {
            "artist": concert['artist'],
            "place": concert['place'],
            "concertDate": concert['concertDate'],
            "surname": "Gonzalez",
            "name": "Cristian",
            "dni": "12123123"
        }

        for sector in concert['sectors']:
            payload = {'sector': sector['name']}
            payload.update( data )
            if sector['hasSeat']:
                 seats = [ i+1 for i in range(sector['roomSpace']) ]
                 payload.update( {'seats' : seats } )
            else:
                 payload.update( {'quantity' : sector['roomSpace'] } )
            
            response = send_post( url, payload )
            assert_reponse(response)
            

    for concert in CONCERTS:
        data = {
            "artist": concert['artist'],
            "place": concert['place'],
            "concertDate": concert['concertDate'],
            "surname": "Gonzalez",
            "name": "Cristian",
            "dni": "12123123"
        }
        
        for sector in concert['sectors']:
            
            payload = {'sector': sector['name']}
            payload.update( data )
            if sector['hasSeat']:
                 payload.update( {'seats' : [1] } )
            else:
                 payload.update( {'quantity' : 1 } )
            
            logger.log( str( payload ) )
            response = send_post( url, payload, check_http_status_ok=False )
            logger.log( "resp: " + str( response ) )
            assert_error(response)

if __name__ == '__main__':

    setup.init()

    tests = [
        test_runner(reserveResponse200Test),
        test_runner(reserveDuplicatedResponseFailedTest),
        test_runner(deleteReserveResponse200Test),
        test_runner(reservesBoundariesResponseErrorTest)
    ]

    print("Running " + os.path.basename(__file__) + "\n" )

    for t in tests:
        t()

    print( "\n" + os.path.basename(__file__) + " end\n" )
    


    


    