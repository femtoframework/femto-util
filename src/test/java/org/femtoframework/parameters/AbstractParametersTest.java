package org.femtoframework.parameters;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by xshao on 9/16/16.
 */
public class AbstractParametersTest {

    ParametersMap parameters = new ParametersMap();

    @Test
    public void getString() throws Exception {
        assertEquals(parameters.getString("test"), Parameters.DEFAULT_STRING);
        assertEquals(parameters.getString("test", "abc"), "abc");
    }

    @Test
    public void getStrings() throws Exception {
        assertArrayEquals(parameters.getStrings("test"), Parameters.DEFAULT_STRING_ARRAY);
        assertArrayEquals(parameters.getStrings("test", null), null);
    }

    @Test
    public void getBoolean() throws Exception {
        assertEquals(parameters.getBoolean("test"), false);
        assertEquals(parameters.getBoolean("test", true), true);
    }


    @Test
    public void getInt() throws Exception {
        assertEquals(parameters.getInt("test"), 0);
        assertEquals(parameters.getInt("test", 1), 1);
    }

    @Test
    public void getInts() throws Exception {
        assertArrayEquals(parameters.getInts("test"), Parameters.DEFAULT_INT_ARRAY);
        assertArrayEquals(parameters.getInts("test", null), null);
    }

    @Test
    public void getDouble() throws Exception {
        assertEquals(parameters.getDouble("test"), 0, 0.1);
        assertEquals(parameters.getDouble("test", 1), 1, 0.1);
    }


    @Test
    public void getLong() throws Exception {
        assertEquals(parameters.getLong("test"), 0);
        assertEquals(parameters.getLong("test", 1), 1);
    }

    @Test
    public void get() throws Exception {
        assertEquals(parameters.get("test"), null);
        assertEquals(parameters.get("test", 1), 1);
    }

    @Test
    public void getArray() throws Exception {
        assertArrayEquals(parameters.getObjects("test"), null);
        assertArrayEquals(parameters.getObjects("test", new String[] { "test" }), new String[] { "test" });

        ParametersMap map = new ParametersMap();
        map.put("test1", new Object[] {"abc", "bcd"});
        map.put("test2", "abc");

        assertArrayEquals(map.getObjects("test1", new String[] { "test" }), new Object[] {"abc", "bcd"});
        assertArrayEquals(map.getObjects("test2", new String[] { "test" }), new String[] { "abc" });
    }

    @Test
    public void getList() throws Exception {
        assertNull(parameters.getStringList("test"));

        ParametersMap map = new ParametersMap();
        map.put("test1", new Object[] {"abc", "bcd"});
        map.put("test2", "abc");

        assertArrayEquals(map.getStringList("test1").toArray(), new String[] {"abc", "bcd"});
        assertArrayEquals(map.getStringList("test2").toArray(), new String[] { "abc" });
    }

    @Test
    public void testParameters() {
        AbstractParameters.toParameters(new HashMap());
        AbstractParameters.toParameters(new ParametersMap());
    }

    @Test
    public void getParameters() {
        Parameters parametersMap = new ParametersMap<>();
        parametersMap.put("map", new HashMap<>());
        parametersMap.put("parameters", new ParametersMap<>());
        parametersMap.put("string", "test");
        assertNotNull(parametersMap.getParameters("map"));
        assertNotNull(parametersMap.getParameters("parameters"));
        assertNull(parametersMap.getParameters("string"));
    }
}