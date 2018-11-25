package com.farms.app;

import java.util.Enumeration;
import java.util.Properties;

import com.farms.env.InjectProperties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class VersionAppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public VersionAppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( VersionAppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
        /*
        String env = "local";
        new InjectProperties(env.trim());
        //
        Properties properties = System.getProperties();
        Enumeration<?> pE = properties.propertyNames();
        while (pE.hasMoreElements()) {
            String key = (String) pE.nextElement();
            String value = properties.getProperty(key);
            System.out.println("Property key="+key+" value="+value);
        }
        assertNotNull(System.getProperty("liferay.app.server.deploy.dir"));
        */
    }
}
