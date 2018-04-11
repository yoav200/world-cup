package com.ab.worldcup.web.components;

import com.google.common.collect.ImmutableMap;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);

        List<Map<String, String>> messages = new ArrayList<>();

        Integer status = (Integer) errorAttributes.get("status");
        String severity = "success"; //
        if (status > 200 && status < 400) {
            severity = "info";
        } else if (status >= 400 && status < 500) {
            severity = "warning";
        } else if (status >= 500) {
            severity = "error";
        }
        String message = (String) errorAttributes.get("message");
        String error = (String) errorAttributes.get("error");

        messages.add(ImmutableMap.of("text", message, "severity", severity, "title", error));
        errorAttributes.put("messages", messages);

        return errorAttributes;
    }
}
