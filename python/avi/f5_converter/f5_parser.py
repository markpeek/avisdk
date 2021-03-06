from pyparsing import *
import logging

LOG = logging.getLogger(__name__)


def generate_grammar_v11():
    # define data types that might be in the values
    unquoted_string = Word(alphanums+"!#$%&'()*+,-./:;<=>?@[\]^_`|~")
    quoted_string = quotedString.setParseAction(removeQuotes)
    ltm = Keyword("ltm")
    apm = Keyword("apm")
    auth = Keyword("auth")
    net = Keyword("net")
    sys = Keyword("sys")
    and_kw = Keyword("and")
    monitor_kw = Keyword("monitor")
    empty_object = Keyword("{ }")

    common = Suppress("/Common/")
    comment = Suppress("#") + Suppress(restOfLine)
    BS, LBRACE, RBRACE = map(Suppress, " {}")
    SOL = LineStart().suppress()
    EOL = LineEnd().suppress()
    reserved_words = (ltm | apm | auth | net | sys).suppress()

    ignore = (common | comment)

    entity_type = SOL.suppress()+Optional(reserved_words).\
        suppress() + unquoted_string
    data = (unquoted_string | quoted_string)

    # define structures
    value = Forward()
    value_object = Forward()

    property_name = data
    monitor_kv = monitor_kw+restOfLine
    dict_kv = property_name+(~EOL) + Optional(value, default=None)
    dict_sv = property_name+EOL + Empty()
    f5_property = Dict(ZeroOrMore(Group(monitor_kv | dict_kv | dict_sv)))
    entity_details = (originalTextFor(ZeroOrMore(unquoted_string)))
    entity = Group(entity_type+Group(entity_details +
                                     LBRACE + (f5_property | BS) + RBRACE))
    entities = OneOrMore(entity)

    value_object << ((LBRACE + f5_property + RBRACE) | empty_object)
    value << (value_object | originalTextFor(data + OneOrMore(and_kw+data)) |
              data)

    data_set = entities.ignore(ignore)
    return data_set


def generate_grammar_v10():
    # define data types that might be in the values
    unquoted_string = Word(alphanums+"!#$%&'()*+,-./:;<=>?@[\]^_`|~")
    quoted_string = quotedString.setParseAction(removeQuotes)
    ltm = Keyword("ltm")
    apm = Keyword("apm")
    auth = Keyword("auth")
    net = Keyword("net")
    sys = Keyword("sys")
    opt_kw = Keyword("options")
    monitor_kw = Keyword("monitor")
    profiles_kw = Keyword("profiles")
    session_kw = Keyword("session")
    mode_kw = Keyword("mode")
    lb_method_kw = Keyword("lb method")
    v_addr_kw = Keyword("virtual address")
    ip_forward_kw = Keyword("ip forward")
    l2_forward_kw = Keyword("l2 forward")
    ct_include_kw = Keyword("compress content type include")
    ct_exclude_kw = Keyword("compress content type exclude")
    empty_object = Keyword("{ }")

    common = Suppress("/Common/")
    comment = Suppress("#") + Suppress(restOfLine)
    BS, LBRACE, RBRACE = map(Suppress, " {}")
    LBRACE_KW = Keyword("{")
    EOL = LineEnd().suppress()
    SOL = LineStart().suppress()
    reserved_words = (ltm | apm | auth | net | sys).suppress()

    ignore = (common | comment)

    entity_type = SOL.suppress()+Optional(reserved_words).\
        suppress() + (v_addr_kw | unquoted_string)
    data = (unquoted_string | quoted_string)

    key_exceptions = (opt_kw | profiles_kw | monitor_kw | session_kw | mode_kw |
                      lb_method_kw | ip_forward_kw | l2_forward_kw |
                      ct_include_kw | ct_exclude_kw)

    # define structures
    value = Forward()
    value_object = Forward()
    multi_word_key = originalTextFor(OneOrMore((~key_exceptions)+data+(~EOL)))
    property_name = (data+EOL | key_exceptions | multi_word_key)
    f5_property = dictOf(property_name, Optional(value, default=None))
    properties = Dict(f5_property)
    entity_details = (originalTextFor(ZeroOrMore(unquoted_string)))
    entity = Group(entity_type+Group(
        entity_details + LBRACE + (f5_property | BS) + RBRACE))
    entities = OneOrMore(entity)

    value_object << ((LBRACE + properties + RBRACE) | empty_object)
    value << (value_object | originalTextFor(data + restOfLine + (~LBRACE_KW)) |
              data)

    data_set = entities.ignore(ignore)
    return data_set


def parse_config(source_str, version=11):
    grammar = get_grammar_by_version(version)
    result = []
    skipped_list = []
    last_end = 0
    source_str = source_str.replace("\t", "    ")
    source_str = source_str.replace("user-defined ", "user-defined_")
    for tokens, start, end in grammar.scanString(source_str):
        result = result+tokens.asList()
        if last_end != 0:
            if start - 3 > last_end:
                skipped_info = {"start": last_end, "end": start,
                                "str_start":
                                    source_str[last_end:last_end+10]+"...",
                                "str_end": "..."+source_str[start-10:start]}
                skipped_list.append(skipped_info)
        last_end = end
    for skipped in skipped_list:
        LOG.warn("Skipped for parse unmatched from offset:%s to offset:%s" %
                 (skipped["start"], skipped["end"]))
    result_dict = convert_to_dict(result)
    LOG.debug("Parsing complete...")
    return result_dict


def get_grammar_by_version(version):
    grammar = None
    if int(version) == 10:
        grammar = generate_grammar_v10()
    elif int(version) == 11:
        grammar = generate_grammar_v11()
    return grammar


def convert_to_dict(result):
    result_dict = {}
    for item in result:
        # determine the key and value to be inserted into the dict
        key = None
        dict_val = None
        if isinstance(item, list):
            try:
                key = item[0].replace("/Common/", "")
                if isinstance(item[1], list):
                    dict_val = convert_to_dict(item)
                    if isinstance(result_dict.get(key, ""), dict):
                        result_dict[key].update(dict_val)
                    else:
                        result_dict[key] = dict_val
                else:
                    if isinstance(item[1], str):
                        result_dict[key] = item[1].replace("/Common/", "")
                    else:
                        result_dict[key] = item[1]
            except IndexError:
                # determine whether to insert the value into the key or to
                # merge the value with existing values at this key
                if key:
                    if key in result_dict:
                        if isinstance(result_dict[key], list):
                            result_dict[key].append(dict_val)
                        else:
                            old = result_dict[key]
                            new = [old]
                            dict_val = dict_val.replace("/Common/", "") \
                                if dict_val else None
                            new.append(dict_val)
                            result_dict[key] = new
                    else:
                        result_dict[key] = dict_val
    return result_dict
