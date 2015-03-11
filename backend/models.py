from calendar import timegm
from datetime import datetime, time, timedelta
from flask.ext.sandboy import BadRequestException

import os
from utils import unix_time
from werkzeug.utils import secure_filename


class FileFactory:
    def __init__(self, request, form):
        self.request = request
        self.form = form

    def get_file(self):
        # File
        try:
            f = self.request.files['f']
        except KeyError:
            raise BadRequestException('No file provided.\n' \
                                      'E.g: curl -X ' + self.request.method + ' ' + self.request.url + \
                                      ' -F \'f=@path_to/file.apk\' -F \'v=1.02.3\' -F \'c=a83na8d\' -F \'b=fullRelease\'\n\n')

        # Version, Commit & Build variant
        try:
            v = self.request.form['v']
            c = self.request.form['c']
            b = self.request.form['b']
        except KeyError:
            raise BadRequestException('Please provide version, commit and build variant parameters.\n' \
                                      'E.g: curl -X ' + self.request.method + ' ' + self.request.url + \
                                      ' -F \'f=@path_to/file.apk\' -F \'v=1.02.3\' -F \'c=a83na8d\' -F \'b=fullRelease\'\n\n')

        # Notes
        if 'n' in self.request.form:
            n = self.request.form['n']
        else:
            n = None

        return File(f, c, v, b, n)


class File(object):
    def __init__(self, source_file, commit, version, build_variant, notes):
        # From curl
        self.source_file = source_file
        self.commit = commit
        self.version = version
        self.build_variant = build_variant
        self.notes = notes

        # Class made
        self.basename = ""
        self.filename = self.make_filename()
        self.filepath = self.make_filepath()

    def make_filename(self):
        source_filename = secure_filename(self.source_file.filename)
        source_extension = os.path.splitext(source_filename)[1]

        self.basename = '%s_%s_%s' % (self.commit, self.version, self.build_variant)
        return self.basename + source_extension

    def make_filepath(self):
        if self.filename is not None:
            self.filename = self.make_filename()

        print "/".join(["files", self.filename])

        return "/".join(["files", self.filename])

    def details(self):
        return {
            'commit': self.commit,
            'version': self.version,
            'build_variant': self.build_variant,
            'notes': self.notes,
            'path': self.filepath,
            'date': datetime.utcnow(),
            'id': self.basename
        }
