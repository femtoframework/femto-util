package org.femtoframework.parameters;

import org.femtoframework.util.DataBindUtil;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author smathew
 * 
 */
public class DataBindUtilTest {

    public static String oldEscapeMessage(String val) {
        if (val == null) {
            return val;
        }
        return val.replaceAll("\"", "'").replaceAll("\n", " ")
                .replaceAll("\r", " ").replaceAll("\t", " ");
    }

    public static String oldEscape(String val) {
        if (val == null) {
            return val;
        }

        return val.replaceAll("\"", "'").replaceAll("\n", "%x6E")
                .replaceAll("\r", "%x72").replaceAll("\t", "%x74");
    }

    private static final String testStr = "\t\tAssert.assertEquals(1, DataBindUtil.safeGetInt(\"a\", json));\n" +
            "\t\tAssert.assertEquals(3, DataBindUtil.safeGetInt(\"c\", json));\n" +
            "\t\tAssert.assertEquals(0, DataBindUtil.safeGetInt(\"e\", json));\n" +
            "\t\tAssert.assertEquals(0, DataBindUtil.safeGetInt(\"nothing\", json));\n" +
            "\t\tAssert.assertEquals(0, DataBindUtil.safeGetInt(\"\", json));\n" +
            "\t\tAssert.assertEquals(0, DataBindUtil.safeGetInt(null, json));";

//    @Test
//    public void testEscapeMessage() throws Exception {
//        new DataBindUtil();
//        assertEquals("'", DataBindUtil.escapeMessage("\""));
//        assertEquals(" ", DataBindUtil.escapeMessage("\r"));
//        assertEquals(" ", DataBindUtil.escapeMessage("\n"));
//        assertEquals(" ", DataBindUtil.escapeMessage("\t"));
//
//        int times = 10000;
//        assertEquals(oldEscapeMessage(testStr), DataBindUtil.escapeMessage(testStr));
//
//        long start = System.currentTimeMillis();
//        for(int i = 0; i < times; i ++) {
//            oldEscape(testStr);
//        }
//        System.out.println("Old:" + (System.currentTimeMillis()-start));
//        start = System.currentTimeMillis();
//        for(int i = 0; i < times; i ++) {
//            DataBindUtil.escapeMessage(testStr);
//        }
//        System.out.println("New:" + (System.currentTimeMillis()-start));
//    }

//    @Test
//    public void testEscape() throws Exception {
//        new DataBindUtil();
//
//        assertEquals("'", DataBindUtil.escape("\""));
//        assertEquals("%x72", DataBindUtil.escape("\r"));
//        assertEquals("%x6E", DataBindUtil.escape("\n"));
//        assertEquals("%x74", DataBindUtil.escape("\t"));
//
//        int times = 10000;
//        assertEquals(oldEscape(testStr), DataBindUtil.escape(testStr));
//
//        long start = System.currentTimeMillis();
//        for(int i = 0; i < times; i ++) {
//            oldEscape(testStr);
//        }
//        System.out.println("Old:" + (System.currentTimeMillis()-start));
//        start = System.currentTimeMillis();
//        for(int i = 0; i < times; i ++) {
//            DataBindUtil.escape(testStr);
//        }
//        System.out.println("New:" + (System.currentTimeMillis()-start));
//    }

    @Test
    public void jsonToMap() throws Exception {
        Map map = DataBindUtil.jsonToMap("{}");
        assertEquals(map.size(), 0);
    }

    @Test
    public void jsonToParameters() throws Exception {
        Parameters map = DataBindUtil.jsonToParameters("{}");
        assertEquals(map.size(), 0);
        Parameters map2 = DataBindUtil.jsonToParameters("{\"name\":\"value\"}");
        assertEquals(map2.size(), 1);
        assertEquals(map2.getString("name"), "value");
    }

    @Test
    public void mapToJson() throws Exception {
        Parameters parameters = new ParametersMap();
        parameters.put("name", "value");

        assertEquals("{\"name\":\"value\"}", DataBindUtil.writeValueAsString(parameters));
    }

    @Test
    public void jsonToParameters1() throws Exception {
        String str = "{\"parameters\":{\"name\":\"value\",\"parameters\":{\"parameters\":{\"name\":\"value\"},\"test\":2}},\"test\":2}";

        Parameters map = DataBindUtil.jsonToParameters(str);
        assertNotNull(map);
        assertNotNull(map.getParameters("parameters"));
        assertNotNull(map.getParameters("parameters").getParameters("parameters"));
        assertNotNull(map.getParameters("parameters").getParameters("parameters").getParameters("parameters"));
    }

    /*
    parameters:
      name: value
      parameters:
        parameters:
          name: value
        test: 2
      test: 2
     */

    @Test
    public void yamlToParameters1() throws Exception {
        String str = "parameters:\n" +
                "  name: value\n" +
                "  parameters:\n" +
                "    parameters:\n" +
                "      name: value\n" +
                "    test: 2\n" +
                "  test: 2";

        Parameters map = DataBindUtil.yamlToParameters(str);
        assertNotNull(map);
        assertNotNull(map.getParameters("parameters"));
        assertNotNull(map.getParameters("parameters").getParameters("parameters"));
        assertNotNull(map.getParameters("parameters").getParameters("parameters").getParameters("parameters"));


        String str2 = "parameters:\n" +
                "  name: value1\n" +
                "  parameters:\n" +
                "    parameters:\n" +
                "      name: value2\n" +
                "    test: 3\n" +
                "  test: 4";


        DataBindUtil.applyYaml(map, str2);

        assertEquals(map.getParameters("parameters").getString("name"), "value1");
        assertEquals(map.getParameters("parameters").getParameters("parameters").getInt("test"), 3);
        assertEquals(map.getParameters("parameters").getParameters("parameters").getParameters("parameters").getString("name"), "value2");
    }
}
