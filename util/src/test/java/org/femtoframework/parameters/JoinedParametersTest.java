package org.femtoframework.parameters;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Joined Parameters
 *
 * @author Sheldon Shao xshao@ebay.com on 12/13/16.
 * @version 1.0
 */
public class JoinedParametersTest {

    private static Parameters parameters;

    @BeforeClass
    public static void init() {
        Properties properties = new Properties();
        properties.put("my_property", "value1");
        properties.put("my_property_remove", "value1");
        parameters = new JoinedParameters(
                new ParametersMap(properties),
                new ParametersMap(System.getProperties())
        );

        System.getProperties().put("_test_in_system_properties", "my value");
        System.getProperties().put("_test_in_system_properties_remove", "my value");
    }

    @Test
    public void get() throws Exception {
        assertEquals(parameters.getString("my_property"), "value1");
        assertEquals(parameters.getString("_test_in_system_properties"), "my value");
    }

    @Test
    public void put() throws Exception {
        parameters.put("test", "new one");
        assertEquals(parameters.getString("test"), "new one");
        assertTrue(parameters.containsKey("test"));
        assertFalse(System.getProperties().containsKey("test"));
    }

    @Test
    public void keySet() throws Exception {
        Set<String> set = parameters.keySet();
        assertTrue(set.contains("my_property"));
        assertTrue(set.contains("_test_in_system_properties"));
    }

    @Test
    public void values() throws Exception {
        Collection<String> set = parameters.values();
        assertTrue(set.contains("my value"));
        assertTrue(set.contains("value1"));
    }

    @Test
    public void entrySet() throws Exception {
        Set<String> set = parameters.entrySet();
        assertNotNull(set);
    }

    @Test
    public void remove() throws Exception {
        assertEquals(parameters.getString("my_property_remove"), "value1");
        assertEquals(parameters.getString("_test_in_system_properties_remove"), "my value");
        parameters.remove("my_property_remove");
        assertFalse(parameters.containsKey("my_property_remove"));
        parameters.remove("_test_in_system_properties_remove");
        assertFalse(parameters.containsKey("_test_in_system_properties_remove"));
    }
}