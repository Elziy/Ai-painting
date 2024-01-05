package com.zsheep.ai.config.i18n;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AcceptHeaderLocaleResolver implements LocaleResolver {
    private static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(Locale.CHINA, Locale.US);
    private static final Locale DEFAULT_LOCALE = Locale.CHINA;
    
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String acceptLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        try {
            if (StringUtils.hasText(acceptLanguage)) {
                List<Locale.LanguageRange> list = Locale.LanguageRange.parse(acceptLanguage);
                Locale locale = Locale.lookup(list, SUPPORTED_LOCALES);
                if (locale != null) {
                    return locale;
                }
            }
        } catch (Exception e) {
            return DEFAULT_LOCALE;
        }
        return DEFAULT_LOCALE;
    }
    
    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

