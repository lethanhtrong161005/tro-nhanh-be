package com.tronhanh.helper;

import java.lang.reflect.Field;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

/**
 * Utility helper for applying common sanitization rules specifically to search payloads. Escapes HTML
 * entities and wildcard characters to prevent XSS and DB wildcard injection attacks.
 */
@Component
public class SearchHelper {

  private static final Logger logger = LoggerFactory.getLogger(SearchHelper.class);

  /**
   * Introspects the given payload and sanitizes all String fields by escaping HTML characters and SQL
   * wildcard characters (% and _).
   *
   * @param searchDto The search criteria object to be sanitized in place.
   * @param <T> The payload type.
   */
  public <T> void sanitizeSearchPayload(T searchDto) {
    if (Objects.isNull(searchDto)) {
      return;
    }

    Field[] fields = searchDto.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (String.class.equals(field.getType())) {
        field.setAccessible(true);
        try {
          String value = (String) field.get(searchDto);
          if (Objects.nonNull(value)) {
            // 1. Prevent XSS by escaping HTML entities (< to &lt;, © to &copy;, etc)
            String escapedHtml = HtmlUtils.htmlEscape(value);
            // 2. Prevent SQL Wildcard Injection by escaping LIKE wildcards
            String safeValue =
                escapedHtml.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");

            field.set(searchDto, safeValue);
          }
        } catch (IllegalAccessException e) {
          logger.warn("Failed to sanitize search field: {}", field.getName(), e);
        }
      }
    }
  }
}
