import sys
import requests
import json
import threading
import os
from common import test_runner, assert_reponse
import setup

api_url = "http://127.0.0.1:8080"
url = api_url + "/concert/sector"
headers = {"Content-Type":"application/json"}



def createSectorsResponds200Test():
    pass


if __name__ == '__main__': 

    setup.initWithNoSectors()
    
    tests = [
        test_runner(createSectorsResponds200Test)
    ]

    print("Running " + os.path.basename(__file__) + "\n" )

    for t in tests:
        t()

    print( "\n" + os.path.basename(__file__) + " end\n" )


    
