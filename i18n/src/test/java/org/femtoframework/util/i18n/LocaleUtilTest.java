package org.femtoframework.util.i18n;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilTest {

    @Test
    public void getLocale() {
        Locale locale = LocaleUtil.getLocale("zh_cn");
        assertEquals("zh_CN", locale.toString());
        locale = LocaleUtil.getLocale("en_us");
        assertEquals("en_US", locale.toString());
        locale = LocaleUtil.getLocale("zh");
        assertEquals("zh_CN", locale.toString());

        locale = LocaleUtil.getLocale("en-SG");
        assertEquals("en_SG", locale.toString());

        locale = LocaleUtil.getLocale("en_SG");
        assertEquals("en_SG", locale.toString());
    }

    @Test
    public void getLocaleByLanguage() {
        Locale locale = LocaleUtil.getLocaleByLanguage("zh");
        assertEquals("zh_CN", locale.toString());

        locale = LocaleUtil.getLocaleByLanguage("en");
        assertEquals("en_US", locale.toString());
    }
}