package org.femtoframework.util.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocaleUtil {
    private LocaleUtil()
    {
    }

    private static Map<String, Locale> locales = new HashMap<String, Locale>();

    //语言到默认的Locale，当按照某一个Locale找不到相应的资源的时候，找同语言默认Locale资源文件 Language --> Locale
    private static Map<String, Locale> language2Locales = new HashMap<String, Locale>();


    public static final Locale EN_US = Locale.US;
    //Great Britain (UK)
    public static final Locale EN_GB = Locale.UK;
    public static final Locale EN_UK = EN_GB;

    public static final Locale ZH_CN = Locale.CHINA;

    public static final Locale ZH_TW = Locale.TAIWAN;

    public static final Locale HONGKONG = new Locale("zh", "HK");
    public static final Locale ZH_HK = HONGKONG;

    public static final Locale SINGAPORE = new Locale("zh", "SG");
    public static final Locale ZH_SG = SINGAPORE;

    public static final Locale JA_JP = Locale.JAPAN;
    public static final Locale JAPAN = JA_JP;
    public static final Locale KO_KR = Locale.KOREA;
    public static final Locale KOREA = KO_KR;


    static {
        locales.put("en_us", EN_US);
        locales.put("en_gb", EN_GB);
        locales.put("en_uk", EN_UK);
        locales.put("zh_cn", ZH_CN);
        locales.put("zh_tw", ZH_TW);
        locales.put("zh_sg", ZH_SG);
        locales.put("zh_hk", ZH_HK);
        locales.put("ja_jp", JAPAN);
        locales.put("ko_kr", KOREA);

        language2Locales.put("en", EN_US);
        language2Locales.put("zh", ZH_CN);
        language2Locales.put("ja", JA_JP);
        language2Locales.put("ko", KO_KR);
        language2Locales.put("fr", Locale.FRANCE);
    }

    /**
     * 根据语言，返回默认的Locale
     *
     * @param language 语言
     * @return Locale
     */
    public static Locale getLocaleByLanguage(String language)
    {
        return language2Locales.get(language);
    }
}
