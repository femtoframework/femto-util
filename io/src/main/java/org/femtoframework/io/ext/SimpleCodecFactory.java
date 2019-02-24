package org.femtoframework.io.ext;

import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.io.ObjectCodec;
import org.femtoframework.io.ObjectCodecFactory;

/**
 * SimpleCodecFactory实现
 *
 * @author fengyun
 * @version 1.00 2005-2-11 13:40:40
 */
public class SimpleCodecFactory implements ObjectCodecFactory {
    private static final String DEFAULT_CODEC = "jrmp";

    private ObjectCodec defaultCodec = null;

    /**
     * 根据对象Codec名称返回ObjectCodec
     *
     * @param codecName 名称
     * @return
     */
    public ObjectCodec getObjectCodec(String codecName) {
        if (codecName == null) {
            return getDefaultObjectCodec();
        }
        return ImplementUtil.getInstance(codecName, ObjectCodec.class);
    }

    /**
     * 返回默认的对象Codec
     *
     * @return
     */
    public ObjectCodec getDefaultObjectCodec() {
        if (defaultCodec == null) {
            defaultCodec = ImplementUtil.getInstance(DEFAULT_CODEC, ObjectCodec.class);
        }
        return defaultCodec;
    }
}

