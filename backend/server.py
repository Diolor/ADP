from json import dumps
import logging

import os
from file_manager import FileManager
from flask import Flask, request, abort, make_response
import models
from utils import handler, TokenAuth
from flask_sandboy import BadRequestException


app = Flask(__name__)
app.config.from_pyfile('config.cfg')
file_manager = FileManager(app)
auth = TokenAuth(app)


# Defaults to stdout
logging.basicConfig(level=logging.INFO)


# get the logger for the current Python module
log = logging.getLogger(__name__)





@app.route('/', methods=['GET'])
def home():
    return 'Android Distribution Platform up and running!'


@app.route('/upload', methods=['PUT'])
@auth.requires_auth
def upload():
    try:
        _file = models.FileFactory(request, request.form).get_file()

        log.info('Start reading database')
        file_manager.index_file(_file)
        file_manager.s3_upload(_file)

        return 'Successfully uploaded: %s %s \n\n' % (_file.version, _file.commit)

    except BadRequestException, e:
        abort(400, e.message)


@app.route('/list', methods=['GET'])
@auth.requires_auth
def list_apks():
    files = file_manager.get_files()
    return dumps({"files" : files}, default=handler)


@app.route('/download/<file_id>', methods=['GET'])
@auth.requires_auth
def get_by_id(file_id):
    try:
        file_manager.get_file_from_s3(file_id)
        temp_file = os.path.join(os.path.dirname(__file__), "temp.apk")

        with open(temp_file, 'rb') as f:
            response = make_response(f.read())
            response.headers['Content-Type'] = "application/vnd.android.package-archive"
            response.headers['Content-Disposition'] = 'attachment; filename="distributed_app.apk"'

            return response

    except BadRequestException, e:
        abort(400, e.message)


@app.errorhandler(400)
@app.errorhandler(403)
@app.errorhandler(500)
@app.errorhandler(503)
def page_not_found(error):
    return dumps({"error": error.description}), error.code
