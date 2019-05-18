package org.femtoframework.text;

import java.nio.charset.Charset;

public interface ChineseCharsets {

    Charset GBK = Charset.forName("GBK");

    Charset GB2312 = Charset.forName("GB2312");

    Charset GB18030 = Charset.forName("GB18030");

    Charset BIG5 = Charset.forName("Big5");

    Charset HKSCS = Charset.forName("Big5-HKSCS");
}
