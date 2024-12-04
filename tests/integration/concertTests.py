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
    assert response.status_code == 200, response.status_code
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
    assert len(r['concertSector']) == 1, len(r['concertSector'])
    
    

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


def rangeGetRequestResponseOneRecTest():

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

if __name__ == '__main__': 

    tests = [
        test_runner(simpleGetRequestResponse200Test),
        test_runner(artistGetRequestResonseOneRecordTest),
        test_runner(unknownArtistGetRequestResponseEmptyTest),
        test_runner(multiplesGetRequestTest),
        test_runner(paramGetRequestResponseOneTwoThreeRecTest)
    ]

    print("Running " + os.path.basename(__file__) + "\n" )

    for t in tests:
        t()

    print( "\n" + os.path.basename(__file__) + " end\n" )


    