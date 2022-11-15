package com.ab.worldcup.web.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.WebRequest;

@Log4j2
@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
    Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
    List<Map<String, String>> messages = new ArrayList<>();

    // for debug purposes only
    String mapEntries = errorAttributes.keySet().stream()
        .map(key -> key + "=" + errorAttributes.get(key))
        .collect(Collectors.joining(", ", "{", "}"));

    Throwable throwable = super.getError(webRequest);

    log.info(errorAttributes);
    log.info(mapEntries);

    Integer status = (Integer) errorAttributes.get("status");
    String severity = "success"; //
    if (status > 200 && status < 400) {
      severity = "info";
    } else if (status >= 400 && status < 500) {
      severity = "warning";
    } else if (status >= 500) {
      severity = "error";
    }

    Object object = errorAttributes.get("errors");
    if (object != null) {
      messages.addAll(extractErrorsAsMessage((List<FieldError>) object));
    } else {
      String message;

      if (throwable != null) {
        message = throwable.getMessage();
      } else {
        message = Optional.ofNullable(errorAttributes.get("default message"))
            .map(Object::toString)
            .orElse(Optional.ofNullable(errorAttributes.get("message"))
                .map(Object::toString)
                .orElse("No message available"));
      }
      String error =
          Optional.ofNullable(errorAttributes.get("error"))
              .map(Object::toString)
              .orElse(StringUtils.capitalize(severity));

      messages.add(Map.of("text", message, "severity", severity, "title", error));
    }
    errorAttributes.put("messages", messages);
    return errorAttributes;
  }

  private List<Map<String, String>> extractErrorsAsMessage(List<FieldError> errors) {
    List<Map<String, String>> messages = new ArrayList<>();
    errors.forEach(
        e ->
            messages.add(
                Map.of(
                    "text",
                    StringUtils.defaultString(e.getDefaultMessage(), "No message available"),
                    "severity",
                    "warning",
                    "title",
                    e.getField())));
    return messages;
  }
}
