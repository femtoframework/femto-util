package org.femtoframework.bean.info;

/**
 * Bean Info Syntax Exception
 *
 * @author Sheldon Shao
 * @version 1.0.0
 */
public class BeanInfoSyntaxException extends RuntimeException {

    public BeanInfoSyntaxException(String message)
    {
        super(message);
    }

    public BeanInfoSyntaxException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
