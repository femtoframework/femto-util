package org.femtoframework.util.status;

/**
 * Status
 *
 * code + message
 *
 * @author fengyun
 * @version 1.00 Mar 6, 2002 2:43:37 PM
 */
public interface Status
{
    /**
     * Status code
     *
     * @return Code
     */
    int getCode();

    /**
     * Status message
     *
     * @return message
     */
    String getMessage();
}

