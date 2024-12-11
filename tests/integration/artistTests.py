import sys
import requests
import json
import threading
import os
from common import *
import setup
from setup import artist_url


def createArtistReturns200Test():

    data = {
        'name': 'Artist1'
    }
    response = send_post( artist_url, data )
    response = assert_reponse( response )
    
    response = send_get( artist_url, data )
    response = assert_reponse( response )
    
    assert response['data'][0]['name'] == 'Artist1', response
    

def deleteArtistReturns200Test():

    # deletes all artists
    response = send_get( artist_url)
    response = assert_reponse( response )
     
    for d in response['data']:
        data = {
                'name': d['name']
        }
        send_delete(artist_url, data)
        

    # creates artits    
    for i in range(5):
        name = 'Artist%d' % i
        data = {
            'name': name
        }
        response = send_post( artist_url, data )
        response = assert_reponse( response )
    
    
    # gets artits
    response = send_get( artist_url)
    response = assert_reponse( response )
     
    for i, d in enumerate(response['data']):
        name = 'Artist%d' % i
        assert d['name'] == name, "%s != %s" % (d['name'], name)
    
    
    # deletes artist3
    data = {
            'name': 'Artist3'
    }
    response = send_delete(artist_url, data)
    response = assert_reponse( response )
    
    
    # gets artits
    response = send_get( artist_url)
    response = assert_reponse( response )
    
    # checks that artist 3 does not exits
    i = 0
    for d in response['data']:
        if i == 3:
            i+=1
            
        name = 'Artist%d' % i
        assert d['name'] == name, "%s != %s" % (d['name'], name)
        i+=1
        
if __name__ == '__main__': 
    
    setup.deleteAll()

    tests = [
        test_runner(createArtistReturns200Test),
        test_runner(deleteArtistReturns200Test)
    ]

    print("Running " + os.path.basename(__file__) + "\n" )

    for t in tests:
        t()

    print( "\n" + os.path.basename(__file__) + " end\n" )


    
