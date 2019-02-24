package org.femtoframework.io;

import org.femtoframework.annotation.ImplementedBy;

/**
 * ObjectCodec工厂
 *
 * @author fengyun
 * @version 1.00 2005-2-11 13:38:04
 */
public interface ObjectCodecFactory
{
    /**
     * 根据对象Codec名称返回ObjectCodec
     *
     * @param codecName 名称
     * @return
     */
    public ObjectCodec getObjectCodec(String codecName);

    /**
     * 返回默认的对象Codec
     *
     * @return
     */
    public ObjectCodec getDefaultObjectCodec();

}
