import sys
import requests
import json
import threading
import os
from common import *
from itertools import combinations
from concertTests import assertResponseFields
import setup
from setup import CONCERTS

api_url = "http://127.0.0.1:8080"
headers = {"Content-Type":"application/json"}

def simpleGetRequestResponse200Test():

    url = api_url + "/concert/range"
    response = send_get(url)
    response = assert_reponse( response )
    
    assert isinstance(response['data'], list)
    assert response['appStatus']['code'] == 'success', response['appStatus']['code']


def rSubset(arr):

    # return list of all subsets of length r
    # to deal with duplicate subsets use
    # set(list(combinations(arr, r)))
    fields_combinatios = []

    for i in range( len(arr) ):
        fields_combinatios = fields_combinatios + list( set( list(combinations(arr, i)) ) )

    return fields_combinatios


def combinateInputFieldsResponse200Test():
    input_cmd = { 
      'artist': CONCERTS[0]['artist'],
      'place': CONCERTS[0]['place'], 
       'fromDate': '2020-01-01', 'untilDate':'2026-01-01', 
       'fromPrice': 10, 'untilPrice': 10000, 
       'dateASC': False, 
       'priceASC': False 
    }

    input_fnames = [ 'artist', 'place', 'fromDate', 'untilDate', 'fromPrice', 'untilPrice', 'dateASC', 'priceASC']
    input_fnames = rSubset(input_fnames)

    url = api_url + "/concert/range"

    for fields in list( input_fnames ):
        data = input_cmd.copy()
        for key in fields:
            data.pop(key, None)
        print( data )
        response = send_get(url, data)
        response = assert_reponse( response )
    
        assert isinstance(response['data'], list)
        assert response['appStatus']['code'] == 'success', response['appStatus']['code']
        assertResponseFields( response )



if __name__ == '__main__': 

    setup.init()

    tests = [
        test_runner(combinateInputFieldsResponse200Test),
    ]

    print("Running " + os.path.basename(__file__) + "\n" )

    for t in tests:
        t()

    print( "\n" + os.path.basename(__file__) + " end\n" )


    