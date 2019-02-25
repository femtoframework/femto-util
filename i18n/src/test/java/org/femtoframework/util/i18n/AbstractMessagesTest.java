package org.femtoframework.util.i18n;

import org.junit.Assert;

import java.util.Locale;

/**
 * @author fengyun
 * @version 1.00 2004-8-14 2:01:17
 */

public class AbstractMessagesTest
{
    public static final String RESOURCES_KEY = "org.femtoframework.util.i18n.resources";

    public void testGetMessage() throws Exception
    {
        Messages resources1 = MessagesUtil.getMessages(RESOURCES_KEY, Locale.CHINA);

        Assert.assertEquals(resources1.getMessage("integer", new Integer(2),
            "1", "3"), "整数测试: 1 + 2 = 3");

        Messages resources2 = MessagesUtil.getMessages(RESOURCES_KEY, Locale.US);

        Assert.assertEquals(resources2.getMessage("403", "fengyun"), "Service not found:fengyun");
        Assert.assertEquals(resources2.getMessage("404", "test"), "Module not found:test");
        Assert.assertEquals(resources2.getMessage("405", "test"), "Invalid request");
        Assert.assertEquals(resources2.getMessage("405"), "Invalid request");
        Assert.assertEquals(resources2.getMessage("integer", new Integer(2), new Integer(1), "3"),
            "The number is: 1 + 2 = 3");
    }
}