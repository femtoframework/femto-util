package org.femtoframework.util.timer;

/**
 * Crontab ParseException
 *
 * @author fengyun
 * @version 1.00 Jun 9, 2003 2:49:35 PM
 */
public class CronParseException
    extends IllegalArgumentException
{
    public CronParseException(String msg)
    {
        super(msg);
    }

    public CronParseException()
    {
        super();
    }
}
