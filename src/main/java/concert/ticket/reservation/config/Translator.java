package concert.ticket.reservation.config;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
@Component
public class Translator {
    private static final long serialVersionUID = -1327752301830478795L;
    private static ResourceBundleMessageSource messageSource;

    public Translator(ResourceBundleMessageSource messageSource) {
        Translator.messageSource = messageSource;
    }

    public static String get(String code) {
        return get(code);
    }

    public static String get(String code, String... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return get(code, locale, args);
    }

    public static String get(String code, Locale locale, String... args) {
        return messageSource.getMessage(code, args, locale);
    }
}
