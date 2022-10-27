package com.muedsa.mock.wechat.mch.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizationUtil {

    private static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("^"
            + "WECHATPAY2-SHA256-RSA2048 "
            + "(\\w+)=\"(.*?)\"" + ","
            + "(\\w+)=\"(.*?)\"" + ","
            + "(\\w+)=\"(.*?)\"" + ","
            + "(\\w+)=\"(.*?)\"" + ","
            + "(\\w+)=\"(.*?)\""
            + "$");
    private static final int MATCH_GROUP_LENGTH = 5 * 2;

    public static Map<String, String> resolveAuthorization(String authorization, String successFlag) {
        Matcher matcher = AUTHORIZATION_PATTERN.matcher(authorization);
        Map<String, String> map = new HashMap<>();
        if(matcher.matches()){
            map.put(successFlag, successFlag);
            int count = matcher.groupCount();
            if(count == MATCH_GROUP_LENGTH) {
                for (int i = 1; i < count + 1; i +=2) {
                    String name = matcher.group(i);
                    String value = matcher.group(i + 1);
                    if(Objects.nonNull(name) && Objects.nonNull(value)) {
                        map.put(name, value);
                    }
                }
            }
        }
        return map;
    }
}
