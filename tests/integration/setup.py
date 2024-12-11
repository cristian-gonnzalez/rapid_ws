import sys
import requests
import json
import threading
import os
from common import *


api_url = "http://127.0.0.1:8080"

artist_url = api_url + '/artist'
place_url = api_url + '/place'
concert_url = api_url + '/concert'
sector_url = api_url + '/concert/sector'
reserve_url = api_url + '/concert/reserve'

ARTISTS = ['Artist%d' % i for i in range(5)]
PLACES = ['Place%d' % i for i in range(5)]
CONCERTS = [ 
    { 
        'artist': 'Artist1',
        'concertDate': '2024-03-11',
        'time': '09:00:00',
        'place': 'Place1',
        'sectors': [
            { 'name': 'PLATEA_ALTA', 'roomSpace': 10, 'occupiedSpace': 0, 'hasSeat': False, 'price': 300 },
            { 'name': 'PLATEA_BAJA', 'roomSpace': 10, 'occupiedSpace': 0, 'hasSeat': False, 'price': 200 },
            { 'name': 'PLATEA_VIP', 'roomSpace': 10, 'occupiedSpace': 0, 'hasSeat': True, 'price': 500 },
            { 'name': 'CAMPO', 'roomSpace': 10, 'occupiedSpace': 0, 'hasSeat': False, 'price': 100 }
        ]
    },
    {   
        'artist': 'Artist1',
        'concertDate': '2024-03-12',
        'time': '09:00:00',
        'place': 'Place1',
        'sectors': [
            { 'name': 'PLATEA', 'roomSpace': 10, 'occupiedSpace': 0, 'hasSeat': True, 'price': 500 },
            { 'name': 'CAMPO', 'roomSpace': 10, 'occupiedSpace': 0, 'hasSeat': False, 'price': 100 }
        ]
    },
    {   
        'artist': 'Artist2',
        'concertDate': '2024-03-18',
        'time': '09:00:00',
        'place': 'Place2',
         'sectors': [
            { 'name': 'CAMPO', 'roomSpace': 10, 'occupiedSpace': 0, 'hasSeat': False, 'price': 100 }
        ]
    },
    {   
        'artist': 'Artist3',
        'concertDate': '2024-05-01',
        'time': '09:00:00',
        'place': 'Place3',
        'sectors': [
            { 'name': 'VIP', 'roomSpace': 10, 'occupiedSpace': 0, 'hasSeat': True, 'price': 300 },
            { 'name': 'SECTOR1', 'roomSpace': 10, 'occupiedSpace': 0, 'hasSeat': True, 'price': 200 },
            { 'name': 'SECTOR2', 'roomSpace': 10, 'occupiedSpace': 0, 'hasSeat': True, 'price': 500 }
        ]
    },
    {   
        'artist': 'Artist3',
        'concertDate': '2024-05-02',
        'time': '09:00:00',
        'place': 'Place3',
         'sectors': [
            { 'name': 'SALA', 'roomSpace': 10, 'occupiedSpace': 0, 'hasSeat': True, 'price': 100 }
        ]
    }
]


def deleteAll():
    print("deleteAll")

    response = send_get(reserve_url)
    for r in response['data']:
        payload = {
            'reserveId': r['reserveId'], 
            'artist': r['concertInfo']['artist'], 
            'place': r['concertInfo']['place'], 
            'concertDate': r['concertInfo']['concertDate'], 
            'sector': r['concertInfo']['sector']['name']
        }
        send_delete( reserve_url, payload )
    
    response = send_get(concert_url )
    for d in response['data']:
        data = {
                'artist': d['artist'],
                'concertDate': d['concertDate'],
                'place': d['place']
        }
        send_delete(concert_url, data)

    response = send_get(artist_url )
    for d in response['data']:
        data = {
                'name': d['name']
        }
        send_delete(artist_url, data)
   
    response = send_get(place_url )
    for d in response['data']:
        data = {
                'name': d['name']
        }
        send_delete(place_url, data)
   
def createArtist():
    print("createArtist")
    for name in ARTISTS:
        payload = {
                'name': name
        }
        send_post(artist_url, payload)

def createPlace():
    print("createPlace")
    for name in PLACES:
        payload = {
            'name': name
        }
        send_post(place_url, payload)

def createConcertWNoSectors():
    print("createConcertWNoSectors")
    for concert in CONCERTS:
        send_post(concert_url, concert)



def createSectors():
    print("createSectors")
    for concert in CONCERTS:
        for sector in concert['sectors']:
            payload = {
                'artist': concert['artist'],
                'place': concert['place'],
                'concertDate': concert['concertDate'],
                'sector': sector
            }
            send_post(sector_url, payload)

def init():
    initWithNoSectors()
    createSectors()


def initWithNoSectors():
    deleteAll()
    
    # order sequence is important
    createArtist()
    createPlace()
    createConcertWNoSectors()    

if __name__ == '__main__': 
    init()