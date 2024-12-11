import sys
import requests
import json
import threading
import os
from common import *
import setup

from setup import place_url


def createPlaceReturns200Test():

    data = {
        'name': 'Place1'
    }
    response = send_post(place_url, data)
    response = assert_reponse( response )
    
    response = send_get(place_url, data)
    response = assert_reponse( response )
    
    assert response['data'][0]['name'] == 'Place1', response
    

def deletePlaceReturns200Test():

    # deletes all places
    response = send_get(place_url)
    response = assert_reponse( response )
     
    for d in response['data']:
        data = {
                'name': d['name']
        }
        send_delete(place_url, data)
        

    # creates artits    
    for i in range(5):
        name = 'Place%d' % i
        data = {
            'name': name
        }
        response = send_post(place_url, data)
        response = assert_reponse( response )
        
    
    # gets artits
    response = send_get(place_url)
    response = assert_reponse( response )
     
    for i, d in enumerate(response['data']):
        name = 'Place%d' % i
        assert d['name'] == name, "%s != %s" % (d['name'], name)
    
    
    # deletes Place3
    data = {
            'name': 'Place3'
    }
    response = send_delete(place_url, data)
    response = assert_reponse( response )
    
    
    # gets Places
    response = send_get(place_url, data)
    response = assert_reponse( response )
    
    # checks that Place 3 does not exits
    i = 0
    for d in response['data']:
        if i == 3:
            i+=1
            
        name = 'Place%d' % i
        assert d['name'] == name, "%s != %s" % (d['name'], name)
        i+=1
        
if __name__ == '__main__': 
    
    setup.deleteAll()

    tests = [
        test_runner(createPlaceReturns200Test),
        test_runner(deletePlaceReturns200Test)
    ]

    print("Running " + os.path.basename(__file__) + "\n" )

    for t in tests:
        t()

    print( "\n" + os.path.basename(__file__) + " end\n" )


    
