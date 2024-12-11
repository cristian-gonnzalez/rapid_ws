import sys
import requests
import json
import threading
import os
from common import test_runner, assert_reponse


api_url = "http://127.0.0.1:8080"
url = api_url + "/place"
headers = {"Content-Type":"application/json"}

def createPlaceReturns200Test():

    url = api_url + "/place"
    data = {
        'name': 'Place1'
    }
    response = requests.post(url, data=json.dumps(data), headers=headers)
    response = assert_reponse( response )
    
    response = requests.get(url, data=json.dumps(data), headers=headers)
    response = assert_reponse( response )
    print(response)
    
    assert response['data'][0]['name'] == 'Place1', response
    

def deletePlaceReturns200Test():

    # deletes all places
    response = requests.get(url, data=json.dumps({}), headers=headers)
    response = assert_reponse( response )
     
    for d in response['data']:
        data = {
                'name': d['name']
        }
        requests.delete(url, data=json.dumps(data), headers=headers)
        

    # creates artits    
    for i in range(5):
        name = 'Place%d' % i
        print("adding " + name )
        data = {
            'name': name
        }
        response = requests.post(url, data=json.dumps(data), headers=headers)
        response = assert_reponse( response )
        
    
    # gets artits
    response = requests.get(url, data=json.dumps({}), headers=headers)
    response = assert_reponse( response )
     
    for i, d in enumerate(response['data']):
        name = 'Place%d' % i
        assert d['name'] == name, "%s != %s" % (d['name'], name)
    
    
    # deletes Place3
    data = {
            'name': 'Place3'
    }
    response = requests.delete(url, data=json.dumps(data), headers=headers)
    response = assert_reponse( response )
    
    
    # gets Places
    response = requests.get(url, data=json.dumps({}), headers=headers)
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

    tests = [
        test_runner(createPlaceReturns200Test),
        test_runner(deletePlaceReturns200Test)
    ]

    print("Running " + os.path.basename(__file__) + "\n" )

    for t in tests:
        t()

    print( "\n" + os.path.basename(__file__) + " end\n" )


    
