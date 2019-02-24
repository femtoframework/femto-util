package org.femtoframework.io;

import org.femtoframework.implement.ImplementUtil;

/**
 * 对象编码器工具类
 *
 * @author fengyun
 */
public class ObjectCodecUtil
{
    private static ObjectCodecFactory factory = ImplementUtil.getInstance(ObjectCodecFactory.class);

    /**
     * 根据对象Codec名称返回ObjectCodec
     *
     * @param codecName 名称
     * @return
     */
    public static ObjectCodec getObjectCodec(String codecName)
    {
        return factory.getObjectCodec(codecName);
    }

    /**
     * 返回默认的对象Codec
     *
     * @return
     */
    public static ObjectCodec getDefaultObjectCodec()
    {
        return factory.getDefaultObjectCodec();
    }
}
