package com.tronhanh.util;

import java.util.Locale;
import java.util.Objects;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Utility component resolving messages from Spring {@link MessageSource}.
 */
@Component
public class MessageUtils
{

  private static MessageSource messageSource;

  /**
   * MessageUtils constructor injecting Spring MessageSource bean.
   *
   * @param messageSource Spring MessageSource instance
   */
  public MessageUtils(MessageSource messageSource) {
    MessageUtils.messageSource = messageSource;
  }

  /**
   * Resolves a message code for English locale with optional substitution parameters.
   *
   * @param code key in message bundle
   * @param args message parameters for %s / {0} substitution
   * @return resolved English message string
   */
  public static String getMessage(String code, Object... args) {
    return resolveMessage(code, Locale.ENGLISH, args);
  }

  /**
   * Resolves message text for English locale with optional substitution parameters.
   *
   * @param code key in message bundle
   * @param args message parameters for %s / {0} substitution
   * @return resolved English message string
   */
  public static String getLocalizedText(String code, Object... args) {
    return resolveMessage(code, Locale.ENGLISH, args);
  }

  private static String resolveMessage(String code, Locale locale, Object... args) {
    if (Objects.isNull(code) || code.isBlank()) {
      return "";
    }
    try {
      return messageSource.getMessage(code, args, locale);
    } catch (Exception e) {
      return code;
    }
  }
}
