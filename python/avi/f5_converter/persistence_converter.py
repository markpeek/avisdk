import logging

import avi.f5_converter.conversion_util as conv_utils
import avi.f5_converter.converter_constants as final
from avi.f5_converter.profile_converter import ProfileConfigConv

LOG = logging.getLogger(__name__)


class PersistenceConfigConv(object):
    @classmethod
    def get_instance(cls, version):
        if version == '10':
            return PersistenceConfigConvV10()
        if version == '11':
            return PersistenceConfigConvV11()

    supported_types = None
    ignore_for_defaults = None

    def convert_cookie_persistence(self, name, profile):
        pass

    def convert_cookie(self, name, profile, skipped, tenant):
        pass

    def convert_ssl(self, name, profile, skipped, indirect_mappings, tenant):
        pass

    def convert_source_addr(self, name, profile, skipped, tenant):
        pass

    def convert(self, f5_config, avi_config, user_ignore):
        avi_config['hash_algorithm'] = []
        avi_config["ApplicationPersistenceProfile"] = []
        f5_persistence_dict = f5_config.get('persistence')
        user_ignore = user_ignore.get('persistence', {})
        for key in f5_persistence_dict.keys():
            persist_mode = None
            name = None
            skipped = []
            try:
                persist_mode, name = key.split(" ")
                LOG.debug("Converting persistence profile: %s" % name)
                profile = f5_persistence_dict[key]
                prof_conv = ProfileConfigConv()
                profile = prof_conv.update_with_default_profile(
                    persist_mode, profile, f5_persistence_dict, name)
                indirect = ["hash-length", "hash-offset", "mirror", "method",
                            "cookie-encryption", 'override-connection-limit']
                tenant, name = conv_utils.get_tenant_ref(name)
                if persist_mode == "cookie":
                    persist_profile = self.convert_cookie(name, profile,
                                                          skipped, tenant)
                    u_ignore = user_ignore.get('cookie', [])
                elif persist_mode == "ssl":
                    persist_profile = self.convert_ssl(
                        name, profile, skipped, indirect, tenant)
                    u_ignore = user_ignore.get('ssl', [])
                elif persist_mode == "source-addr":
                    persist_profile = self.convert_source_addr(
                        name, profile, skipped, tenant)
                    u_ignore = user_ignore.get('source-addr', [])
                elif persist_mode == "hash":
                    avi_config['hash_algorithm'].append(name)
                    conv_utils.add_status_row(
                        'profile', "hash-persistence", name, 'indirect-mapping')
                    continue
                else:
                    LOG.error(
                        'persist mode not supported skipping conversion: %s' %
                        name)
                    conv_utils.add_status_row("persistence", persist_mode, name,
                                              "skipped")
                    continue
                if not persist_profile:
                    continue
                avi_config["ApplicationPersistenceProfile"].append(
                    persist_profile)

                ignore_for_defaults = {"app-service": "none", "mask": "none"}
                conv_status = conv_utils.get_conv_status(
                    skipped, indirect, ignore_for_defaults, profile, u_ignore)
                conv_utils.add_conv_status('persistence', persist_mode, name,
                                           conv_status, persist_profile)
                LOG.debug("Conversion successful for persistence profile: %s" %
                          name)
            except:
                LOG.error("Failed to convert persistance profile : %s" % key,
                          exc_info=True)
                if name:
                    conv_utils.add_status_row("persistence", persist_mode, name,
                                              "error")
                else:
                    conv_utils.add_status_row("persistence", key, key, "error")
        count = len(avi_config["ApplicationPersistenceProfile"])
        LOG.debug("Converted %s persistence profiles" % count)
        f5_config.pop('persistence')

    def convert_timeout(self, timeout):
        if ':' in str(timeout):
            expiration = timeout.split(':')
            expiration.reverse()
            timeout = 0
            i = 0
            for val in expiration:
                val = int(val)
                if i == 0:
                    timeout = int(val/final.SEC_IN_MIN)
                elif i == 1:
                    timeout += val
                elif i == 2:
                    timeout += (val*final.MIN_IN_HR)
                elif i == 3:
                    timeout += (val*final.MIN_IN_HR*final.HR_IN_DAY)
                i += 1
        else:
            timeout = 1 if int(timeout) == 0 else timeout
        return timeout


class PersistenceConfigConvV11(PersistenceConfigConv):

    def convert_cookie(self, name, profile, skipped, tenant):
        supported_attr = ["cookie-name", "defaults-from", "expiration"]
        ignore_lst = ['always-send']
        parent_obj = super(PersistenceConfigConvV11, self)
        supported_attr += ignore_lst
        skipped += [attr for attr in profile.keys()
                    if attr not in supported_attr]
        cookie_name = profile.get("cookie-name", None)
        timeout = profile.get("expiration", '1')
        timeout = parent_obj.convert_timeout(timeout)
        persist_profile = {
            "name": name,
            "app_cookie_persistence_profile": {
                "prst_hdr_name": cookie_name,
                "timeout": timeout
            },
            "server_hm_down_recovery": "HM_DOWN_PICK_NEW_SERVER",
            "persistence_type": "PERSISTENCE_TYPE_APP_COOKIE",
        }
        if tenant:
            persist_profile['tenant_ref'] = tenant

        return persist_profile

    def convert_ssl(self, name, profile, skipped, indirect_mappings, tenant):
        supported_attr = ["defaults-from"]
        skipped += [attr for attr in profile.keys()
                    if attr not in supported_attr]
        indirect_mappings.append("timeout")
        persist_profile = {
            "server_hm_down_recovery": "HM_DOWN_PICK_NEW_SERVER",
            "persistence_type": "PERSISTENCE_TYPE_TLS",
            "name": name
        }
        if tenant:
            persist_profile['tenant_ref'] = tenant
        return persist_profile

    def convert_source_addr(self, name, profile, skipped, tenant):
        supported_attr = ["timeout", "defaults-from"]
        ignore_lst = ['map-proxies']
        supported_attr += ignore_lst
        skipped += [attr for attr in profile.keys()
                    if attr not in supported_attr]
        timeout = profile.get("timeout", final.SOURCE_ADDR_TIMEOUT)
        if timeout > 0:
            timeout = int(timeout)/final.SEC_IN_MIN
        persist_profile = {
            "server_hm_down_recovery": "HM_DOWN_PICK_NEW_SERVER",
            "persistence_type": "PERSISTENCE_TYPE_CLIENT_IP_ADDRESS",
            "ip_persistence_profile": {
                "ip_persistent_timeout": timeout
            },
            "name": name
        }
        if tenant:
            persist_profile['tenant_ref'] = tenant
        return persist_profile


class PersistenceConfigConvV10(PersistenceConfigConv):

    def convert_cookie(self, name, profile, skipped, tenant):
        supported_attr = ["cookie name", "mode", "defaults from", "mirror",
                          "cookie hash offset", "cookie hash length"]
        skipped = [attr for attr in profile.keys()
                   if attr not in supported_attr]
        cookie_name = profile.get("cookie name", None)
        if not cookie_name:
            LOG.error("Missing Required field cookie name in: %s", name)
            conv_utils.add_status_row('profile', 'persist-cookie', name,
                                      'skipped')
            return None
        timeout = profile.get("expiration", '1')
        parent_obj = super(PersistenceConfigConvV10, self)
        timeout = parent_obj.convert_timeout(timeout)
        persist_profile = {
            "name": name,
            "app_cookie_persistence_profile": {
                "prst_hdr_name": cookie_name,
                "timeout": timeout
            },
            "server_hm_down_recovery": "HM_DOWN_PICK_NEW_SERVER",
            "persistence_type": "PERSISTENCE_TYPE_APP_COOKIE",
        }
        if tenant:
            persist_profile['tenant_ref'] = tenant
        return persist_profile, skipped

    def convert_ssl(self, name, profile, skipped, indirect, tenant):
        supported_attr = ["mode", "defaults from", "mirror"]
        skipped += [attr for attr in profile.keys()
                    if attr not in supported_attr]
        indirect.append("timeout")
        persist_profile = {
            "server_hm_down_recovery": "HM_DOWN_PICK_NEW_SERVER",
            "persistence_type": "PERSISTENCE_TYPE_TLS",
            "name": name
        }
        if tenant:
            persist_profile['tenant_ref'] = tenant
        return persist_profile

    def convert_source_addr(self, name, profile, skipped, tenant):
        supported_attr = ["timeout", "mode", "defaults from"]
        skipped += [attr for attr in profile.keys()
                    if attr not in supported_attr]
        timeout = profile.get("timeout", final.SOURCE_ADDR_TIMEOUT)
        if timeout > 0:
            timeout = int(timeout)/final.SEC_IN_MIN
        persist_profile = {
          "server_hm_down_recovery": "HM_DOWN_PICK_NEW_SERVER",
          "persistence_type": "PERSISTENCE_TYPE_CLIENT_IP_ADDRESS",
          "ip_persistence_profile": {
            "ip_persistent_timeout": timeout
          },
          "name": name
        }
        if tenant:
            persist_profile['tenant_ref'] = tenant
        return persist_profile
