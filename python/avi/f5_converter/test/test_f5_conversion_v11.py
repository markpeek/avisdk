import copy
import logging
import os
import unittest
import csv

import avi.f5_converter.f5_config_converter as f5_config_converter
import avi.f5_converter.f5_parser as f5_parser
import avi.f5_converter.f5_converter as f5_converter

gSAMPLE_CONFIG = None
log = logging.getLogger(__name__)


def setUpModule():
    dir_path = os.path.abspath(os.path.dirname(__file__))
    cfg_file = open(dir_path+os.path.sep+'bigip_v11.conf', 'r')
    cfg = cfg_file.read()
    global gSAMPLE_CONFIG
    gSAMPLE_CONFIG = cfg
    log.debug(' read cofig %s', gSAMPLE_CONFIG)


class Test(unittest.TestCase):

    LOG = logging.getLogger("converter-log")
    LOG.setLevel(logging.DEBUG)
    dir_path = os.path.abspath(os.path.dirname(__file__))
    dir_path = dir_path.rsplit(os.path.sep, 1)[0]
    dir_path = dir_path + os.path.sep + "output"
    if not os.path.exists(dir_path):
        os.makedirs(dir_path)
    fh = logging.FileHandler(dir_path + os.path.sep + "converter.log",
                              mode='a', encoding=None, delay=False)
    fh.setLevel(logging.DEBUG)
    formatter = logging.Formatter(
        '%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    fh.setFormatter(formatter)
    LOG.addHandler(fh)

    def test_config_conversion(self):
        dir_path = os.path.abspath(os.path.dirname(__file__))
        dir_path = dir_path.rsplit(os.path.sep, 1)[0]
        f5_config_dict = f5_parser.parse_config(gSAMPLE_CONFIG, 11)

        defaults_file = open(dir_path+os.path.sep+"f5_v11_defaults.conf", "r")
        f5_defaults_dict = f5_parser.parse_config(defaults_file.read(), 11)
        f5_converter.dict_merge(f5_defaults_dict, f5_config_dict)
        f5_config_dict = f5_defaults_dict

        assert f5_config_dict.get("virtual", None)
        assert f5_config_dict.get("monitor", None)
        assert f5_config_dict.get("pool", None)
        assert f5_config_dict.get("profile", None)
        assert f5_config_dict.get("node", None)
        f5_config_test = copy.deepcopy(f5_config_dict)
        avi_config_dict = f5_config_converter.convert(
            f5_config_dict, dir_path+os.path.sep+"output", "disable",
            "certs", "api-upload", 11)
        with open('%s%soutput%sConversionStatus.csv' %
                          (dir_path, os.path.sep, os.path.sep)) as csvfile:
            reader = csv.DictReader(csvfile)
            for row in reader:
                assert row['Status'] != 'error'


if __name__ == "__main__":
    unittest.main()
