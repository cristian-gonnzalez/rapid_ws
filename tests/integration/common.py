import sys
import requests
import json


class Logger:

    def __init__(self, debug ):
        self.debug = debug

    def log( self, msg ):
        if self.debug:
            print( msg )


def send_request(method, url, payload, check_http_status_ok = True):
    headers = {"Content-Type":"application/json"}
    s = method + " " + url + " " + str(payload) 
    if method == 'post':
        response = requests.post(url, data=json.dumps(payload), headers=headers)
    if method == 'get':
        response = requests.get(url, data=json.dumps(payload), headers=headers)
    if method == 'delete':
        response = requests.delete(url, data=json.dumps(payload), headers=headers)
    if check_http_status_ok:
        assert response.status_code == 200, response.json()
    
    return response.json()


def send_post( url, payload = {} , check_http_status_ok = True):
    return send_request('post', url, payload, check_http_status_ok)

def send_get( url, payload = {} , check_http_status_ok = True):
    return send_request('get', url, payload, check_http_status_ok)

def send_delete( url, payload = {}, check_http_status_ok = True ):
    return send_request('delete', url, payload, check_http_status_ok)


def test_runner( test_func ):

        def wrapper():
            print("%s starting ..." % test_func.__name__)
            test_func()
            print("%s end." % test_func.__name__);
        return wrapper



def assert_reponse(response):
    assert response['appStatus']['code'] == 'success', response['appStatus']
    return response


def assert_error(response, error_code=None):
    assert response['appStatus']['code'] != 'success', response['appStatus']
    if error_code:
        assert response['appStatus']['code'] != 'error_code', response['appStatus']
    


