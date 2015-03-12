import logging
import shelve

from boto.exception import S3ResponseError
import boto
from flask.ext.sandboy import BadRequestException
import os.path


class FileManager(object):
    def __init__(self, app):
        self.app = app

        # Name of the lazy db on the s3 bucket
        self.lazy_db = "lazy"

        # Connect to S3
        if app.config['AWS_REGION']:
            conn = boto.s3.connect_to_region(app.config['AWS_REGION'],
                                     aws_access_key_id=app.config['AWS_KEY'],
                                     aws_secret_access_key=app.config['AWS_SECRET'])
        else:
            conn = boto.connect_s3(app.config['AWS_KEY'], app.config['AWS_SECRET'])

        self.boto = conn.get_bucket(app.config['AWS_BUCKET'], validate=False)

    def s3_upload(self, _file, acl='public-read'):
        # Upload the File
        sml = self.boto.new_key(_file.filepath)
        sml.set_contents_from_string(_file.source_file.read())

        # Set the file's permissions.
        # sml.set_acl(acl)

    def update_lazy_from_s3(self):
        k = self.boto.new_key("/%s" % self.lazy_db)
        try:
            k.get_contents_to_filename(self.lazy_db)
        except S3ResponseError:
            pass

        return k

    def index_file(self, f):
        key = self.update_lazy_from_s3()

        local_db = os.path.join(os.path.dirname(__file__), self.lazy_db)
        d = shelve.open(local_db)

        try:
            files = d["files"]
        except KeyError:
            files = []

        # check if duplicate commit / version already in the db
        p = next((
                 files.index(x) for x in files
                 if x['commit'] == f.commit and x['version'] == f.version and x['build_variant'] == f.build_variant
        ), None)

        if p is not None:
            files[p] = f.details()
        else:
            files.append(f.details())

        d['files'] = files
        d.close()

        logging.debug(files)

        key.set_contents_from_filename(local_db)

    def get_files(self):
        self.update_lazy_from_s3()

        local_db = os.path.join(os.path.dirname(__file__), self.lazy_db)
        d = shelve.open(local_db)

        try:
            return d["files"]
        except KeyError:
            return []

    def get_file_from_s3(self, id):
        temp_file = self.boto.new_key("/".join(["files", id + ".apk"]))

        try:
            temp_file.get_contents_to_filename("temp.apk")
        except S3ResponseError:
            raise BadRequestException("File not found")