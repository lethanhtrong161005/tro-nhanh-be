package com.tronhanh.util;

import com.tronhanh.dto.response.common.LocalizedMessageDto;
import com.tronhanh.validation.I18nField;
import java.util.Locale;
import java.util.Objects;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility component resolving internationalized (i18n) messages from Spring {@link MessageSource}.
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
   * Resolves a message code for both Vietnamese (vi) and English (en) locales.
   *
   * @param code key in message bundle
   * @param args message parameters for %s substitution
   * @return LocalizedMessageDto containing vi and en strings
   */
  public static LocalizedMessageDto getMessage(String code, Object... args) {
    String viMessage = resolveMessage(code, Locale.of("vi"), args);
    String enMessage = resolveMessage(code, Locale.ENGLISH, args);

    return LocalizedMessageDto.builder()
        .vi(viMessage)
        .en(enMessage)
        .build();
  }

  /**
   * Resolves a message code with localized i18n field labels for VI and EN.
   *
   * @param code key in message bundle (e.g. MSG_CODE_200)
   * @param i18n I18nField annotation instance
   * @return LocalizedMessageDto containing vi and en strings
   */
  public static LocalizedMessageDto getMessageWithI18n(String code, I18nField i18n) {
    String viLabel = Objects.nonNull(i18n) ? i18n.vi() : "";
    String enLabel = Objects.nonNull(i18n) ? i18n.en() : "";

    String viMessage = resolveMessage(code, Locale.of("vi"), viLabel);
    String enMessage = resolveMessage(code, Locale.ENGLISH, enLabel);

    return LocalizedMessageDto.builder()
        .vi(viMessage)
        .en(enMessage)
        .build();
  }

  /**
   * Resolves message text based on the request's active LocaleContextHolder.
   *
   * @param code key in message bundle
   * @param args message parameters for %s substitution
   * @return localized message string
   */
  public static String getLocalizedText(String code, Object... args) {
    Locale currentLocale = LocaleContextHolder.getLocale();
    return resolveMessage(code, currentLocale, args);
  }

  /**
   * Resolves message text based on active locale using an I18nField annotation.
   *
   * @param code key in message bundle
   * @param i18n I18nField meta-annotation
   * @return localized message string
   */
  public static String getLocalizedTextWithI18n(String code, I18nField i18n) {
    Locale currentLocale = LocaleContextHolder.getLocale();
    String label = Locale.of("vi").getLanguage().equals(currentLocale.getLanguage())
        ? (Objects.nonNull(i18n) ? i18n.vi() : "")
        : (Objects.nonNull(i18n) ? i18n.en() : "");
    return resolveMessage(code, currentLocale, label);
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
