package com.open.appbuilder.test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.open.appbuilder.Spec;
import com.open.appbuilder.SpecParser;

public class SpecReaderTest {

    @Test
    public void testReadSpec1() throws IOException {
        SpecParser parser = new SpecParser();
        Spec spec = parser.parse(new InputStreamReader(getClass().getResourceAsStream("/app1.spec"), "UTF-8"));
        Assert.assertNotNull(spec);
        assertSpecEquals(spec, "/app1.json");
    }

    @Test
    public void testReadSpec2() throws IOException {
        SpecParser parser = new SpecParser();
        Spec spec = parser.parse(new InputStreamReader(getClass().getResourceAsStream("/app2.spec"), "UTF-8"));
        Assert.assertNotNull(spec);
        assertSpecEquals(spec, "/app2.json");
    }

    private static void assertSpecEquals(Spec spec, String jsonFileName) throws IOException {
        String json = new Gson().toJson(spec);
        StringWriter writer = new StringWriter();
        IOUtils.copy(new InputStreamReader(SpecReaderTest.class.getResourceAsStream(jsonFileName), "UTF-8"), writer);
        Assert.assertEquals(writer.toString(), json);
    }
}
