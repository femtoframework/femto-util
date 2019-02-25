package org.femtoframework.util.i18n;

import org.femtoframework.util.i18n.resources.PropertiesResources;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * @author fengyun
 * @version 1.00 2004-11-10 15:46:19
 */

public class PropertiesResourcesTest
{
    @Test
    public void testGetResource() throws Exception
    {
        String resourceKey = "org.femtoframework.util.i18n.resources";
        PropertiesResources resources1 = new PropertiesResources(resourceKey, Locale.CHINA);

        assertEquals(resources1.getString("integer"), "整数测试: {1} + {0} = {2}");

        PropertiesResources resources2 = new PropertiesResources(resourceKey, Locale.US);

        assertEquals(resources2.getString("integer"), "The number is: {1} + {0} = {2}");
    }
}