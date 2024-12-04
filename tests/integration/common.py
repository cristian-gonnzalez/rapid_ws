import sys


def test_runner( test_func ):

        def wrapper():
            print("%s starting ..." % test_func.__name__)
            test_func()
            print("%s end." % test_func.__name__);
        return wrapper



def assert_reponse(response):
    try:
        response = response.json()
    except TypeError:
        pass
    assert response['appStatus']['code'] == 'success', response['appStatus']

    return response
