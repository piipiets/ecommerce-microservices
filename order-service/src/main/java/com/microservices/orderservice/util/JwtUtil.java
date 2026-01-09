package com.microservices.orderservice.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {

    public static Map<String, Object> getAllValueFromToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return new HashMap<>();
        }

        JSONParser jsonParser = new JSONParser();
        String[] chunks = token.split("\\.");
        String payload = chunks.length > 1 ? chunks[1] : chunks[0];

        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new String(Base64.getDecoder().decode(payload)));

            Map<String, Object> result = new HashMap<>();
            for (Object key : obj.keySet()) {
                result.put(String.valueOf(key), obj.get(key));
            }

            return result;
        } catch (ParseException e) {
            log.error("Failed to extract value from token!", e);
        } catch (IllegalArgumentException e) {
            log.error("Invalid Base64 token payload!", e);
        }
        return new HashMap<>();
    }
}

