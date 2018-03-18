from __future__ import print_function

import sys
import os
import tempfile
import json
import subprocess

from ingredient_phrase_tagger.training import utils


_, tmpFile = tempfile.mkstemp()

with open(tmpFile, 'w') as outfile:
    outfile.write(utils.export_data(['1 cup of white wine', '3 cups of vinegar']))

tmpFilePath = "tmp/model_file"
modelFilename = os.path.join(os.path.dirname(__file__), tmpFilePath)
command = ("crf_test -v 1 -m %s %s" % (modelFilename, tmpFile)).split(" ")

proc = subprocess.Popen(command, stdout=subprocess.PIPE)
output = proc.stdout.read()
ingredients = json.dumps(utils.import_data(output.split('\n')))

os.system("rm %s" % tmpFile)
return ingredients