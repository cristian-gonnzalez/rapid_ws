import sys
import requests
import json
import threading
import os
from common import test_runner, assert_reponse


api_url = "http://127.0.0.1:8080"
headers = {"Content-Type":"application/json"}

def simpleGetRequestResponse200Test():

    url = api_url + "/concert"
    data = {
    }
    response = requests.get(url, data=json.dumps(data), headers=headers)
    assert response.status_code == 200, response.json()
    response = assert_reponse( response )
    
    assert isinstance(response['data'], list)
    assert response['appStatus']['code'] == 'success', response['appStatus']['code']


def artistGetRequestResonseOneRecordTest():

    url = api_url + "/concert"
    data = {
        'artist': 'ARTIST_2'
    }
    response = requests.get(url, data=json.dumps(data), headers=headers)
    response = assert_reponse( response )
    
    assert isinstance(response['data'], list)
    assert response['appStatus']['code'] == 'success', response['appStatus']['code']
    
    assert len( response['data'] ) == 5, len( response['data'] ) 
    
    r = response['data'][0]
    assert r['artist'] == 'ARTIST_2', r['artist']
    assert r['place'] == 'PLACE_1', r['place']
    assert len(r['sectors']) == 1, len(r['sectors'])
    
    

def unknownArtistGetRequestResponseEmptyTest():

    # we know that the dbtest has only one record with this artitst
    url = api_url + "/concert"
    data = {
        'artist': 'UNKNOWN_ARTITST'
    }
    response = requests.get(url, data=json.dumps(data), headers=headers)
    response = assert_reponse( response )
  
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
    data = {
    }
    response = requests.get(url, data=json.dumps(data), headers=headers)
    response = assert_reponse( response )
  
    assert len( response['data'] ) == 1, response['data']
    
    url = api_url + "/concert?rec_num=0&offset=3"
    response = requests.get(url, data=json.dumps(data), headers=headers)
    response = assert_reponse( response )
  
    assert len( response['data'] ) == 3, len( response['data'] )

    url = api_url + "/concert?rec_num=1&offset=2"
    response = requests.get(url, data=json.dumps(data), headers=headers)
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
        response = requests.get(url, data=json.dumps(input_field), headers=headers)
        response = assert_reponse( response )
      


def paramGetRequestResponseOneRecTest():

    url = api_url + "/concert?rec_num=0&offset=1"
    data = {
    }
    response = requests.get(url, data=json.dumps(data), headers=headers)
    response = assert_reponse( response )
  
    assert len( response['data'] ) == 1, response['data']
    
    url = api_url + "/concert?rec_num=0&offset=3"
    response = requests.get(url, data=json.dumps(data), headers=headers)
    response = assert_reponse( response )
  
    assert len( response['data'] ) == 3, len( response['data'] )

    url = api_url + "/concert?rec_num=1&offset=2"
    response = requests.get(url, data=json.dumps(data), headers=headers)
    response = assert_reponse( response )
  
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
    input_fields = {
    }
    response = requests.get(url, data=json.dumps(input_fields), headers=headers)
    response = assert_reponse( response )
    assertResponseFields( response )

if __name__ == '__main__': 

    tests = [
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


    
