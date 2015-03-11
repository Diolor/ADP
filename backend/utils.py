from datetime import datetime
from functools import wraps
from flask import request, abort

__author__ = 'dionysis_lorentzos'

# Simple json ISO handler
def handler(obj):
    if hasattr(obj, 'isoformat'):
        return obj.isoformat()
    else:
        raise TypeError, "Object of type %s with value of %s is not JSON serializable" % (type(obj), repr(obj))


def unix_time(dt):
    epoch = datetime.utcfromtimestamp(0)
    delta = dt - epoch
    return delta.total_seconds()


class TokenAuth(object):
    def __init__(self, app):
        self.app = app

    def requires_auth(self, f):
        @wraps(f)
        def decorated(*args, **kwargs):
            if not self.app.config["API_TOKEN"] == request.headers.get("Api-Token"):
                abort(403, "Bad token credentials")

            return f(*args, **kwargs)

        return decorated