import sys
import requests
import json
import threading
import os
from common import *
import setup
from setup import CONCERTS

api_url = "http://127.0.0.1:8080"
url = api_url + "/concert"
headers = {"Content-Type":"application/json"}
concerts = CONCERTS

def simpleGetRequestResponse200Test():
    response = send_get( url , check_http_status_ok= True )
    assert isinstance(response['data'], list)
    assert response['appStatus']['code'] == 'success', response['appStatus']['code']


def artistGetRequestResonseOneRecordTest():

    url = api_url + "/concert"
    data = {
        'artist': concerts[1]['artist'],
        'place': concerts[1]['place'],
        'concertDate': concerts[1]['concertDate']
    }
    print( 'Sendig \n' + str( data ) )
    response = send_get( url , data, check_http_status_ok= True )
    print( 'Response \n' + str( response ) )
    
    assert isinstance(response['data'], list)
    assert response['appStatus']['code'] == 'success', response['appStatus']['code']
       
    r = response['data'][0]
    print( r )
    assert r['artist'] == concerts[1]['artist'], r['artist']
    assert r['place'] == concerts[1]['place'], r['place']
    assert r['concertDate'] == concerts[1]['concertDate'], r['place']
    
    
def unknownArtistGetRequestResponseEmptyTest():

    # we know that the dbtest has only one record with this artitst
    url = api_url + "/concert"
    data = {
        'artist': 'UNKNOWN_ARTITST'
    }
    response = send_get( url , data, check_http_status_ok= True )
    assert len( response['data'] ) == 0, response['data']
    

def multiplesGetRequestTest():

    workers = []
    for i in range(50):
        w = threading.Thread(target=simpleGetRequestResponse200Test, args=())
        workers += [w]
    for w in workers:
        w.start()
    for w in workers: 
        w.join()


def paramGetRequestResponseOneTwoThreeRecTest():

    url = api_url + "/concert?rec_num=0&offset=1"
    response = send_get(url)
    response = assert_reponse( response )
  
    assert len( response['data'] ) == 1, response['data']
    
    url = api_url + "/concert?rec_num=0&offset=3"
    response = send_get(url)
    response = assert_reponse( response )
  
    assert len( response['data'] ) == 3, len( response['data'] )

    url = api_url + "/concert?rec_num=1&offset=2"
    response = send_get(url)
    response = assert_reponse( response )
  
    assert len( response['data'] ) == 2, len( response['data'] )


def combineInputFieldsGetReturnsSuccess():

    input_fields = [
        { 'artist': 'ARTIST_1' },
        { 'place': 'PLACE_1' },
        { 'concertDate': '2025-01-01' },
        { 'artist': 'ARTIST_1', 'place': 'PLACE_1' },
        { 'artist': 'ARTIST_1', 'concertDate': '2025-01-01' },
        { 'place': 'PLACE_1', 'concertDate': '2025-01-01' },
        { 'artist': 'ARTIST_1', 'place': 'PLACE_1', 'concertDate': '2025-01-01' },
    ]

    for input_field in input_fields:
        url = api_url + "/concert"
        send_get(url, input_field)      


def paramGetRequestResponseOneRecTest():

    url = api_url + "/concert?rec_num=0&offset=1"
    response = send_get( url )
    
    assert len( response['data'] ) == 1, response['data']
    
    url = api_url + "/concert?rec_num=0&offset=3"
    response = send_get( url )
    
    assert len( response['data'] ) == 3, len( response['data'] )

    url = api_url + "/concert?rec_num=1&offset=2"
    response = send_get( url )
    
    assert len( response['data'] ) == 2, len( response['data'] )


def assertResponseFields( response ):

    assert 'data' in response.keys()

    data_fields = ['artist', 'place', 'concertDate', 'concertTime', 'sectors']
    concertSector_fields = ['name', 'price', 'roomSpace', 'occupiedSpace', 'hasSeat', 'seats']

    record = response['data'][0]
    for k in data_fields:
        assert k in list( record.keys() ), "Missing field %s in 'data'\n%s" % ( k, response)

    if len( record['sectors'] ) > 0:
        sector = record['sectors'][0]
        for k in concertSector_fields:
            assert k in list( sector.keys() ), "Missing field %s in 'data'->'sectors'" % k
    

def checksResponseFields():
    url = api_url + "/concert"
    response = send_get(url)
    response = assert_reponse( response )
    assertResponseFields( response )


def setupTest():


    setup.deleteAll()
    setup.createArtist()
    setup.createPlace()


def createConcertRespons200():
    
    for c in concerts:
        payload = {
            'artist': c['artist'],
            'place': c['place'],
            'concertDate': c['concertDate'],
            'time': c['time']
        }
        print(payload)
        send_post(url, payload, check_http_status_ok=True)


if __name__ == '__main__': 

    setupTest()    
    
    tests = [
        test_runner(createConcertRespons200),
        test_runner(simpleGetRequestResponse200Test),
        test_runner(artistGetRequestResonseOneRecordTest),
        test_runner(unknownArtistGetRequestResponseEmptyTest),
        test_runner(multiplesGetRequestTest),
        test_runner(combineInputFieldsGetReturnsSuccess),
        test_runner(paramGetRequestResponseOneRecTest),
        test_runner(paramGetRequestResponseOneTwoThreeRecTest),
        test_runner(checksResponseFields)
    ]

    print("Running " + os.path.basename(__file__) + "\n" )

    for t in tests:
        t()

    print( "\n" + os.path.basename(__file__) + " end\n" )


    
