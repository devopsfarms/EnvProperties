/*--------------------------------------------------------------------------
     Copyright (c) 2005-2019, Jnr DevOps Farms LLC
     @url    : <a href="http://www.devopsfarms.com/">DevOps Farms</a>
---------------------------------------------------------------------------*/
package com.farms.app;

import java.util.Enumeration;
import java.util.Properties;

import com.farms.enc.EncryptionUtil;
import com.farms.env.InjectProperties;
/**
 * Hello world!
 *
 */
public class VersionApp
{
    public static void main( String[] args )
    {
        //Encrypting a file:
        String env = "qa";
        String version = null;

        InjectProperties prop = new InjectProperties(env.trim(), true);
        //
        Properties properties = prop.getInjProperties();
        version = properties.getProperty("seckey");
        System.out.println("Main():Property key=seckey file value="+version);
        Enumeration<?> pE = properties.propertyNames();
        //
        while (pE.hasMoreElements()) {
            String key = (String) pE.nextElement();
            String value = properties.getProperty(key);
            if(!"seckey".equalsIgnoreCase(key)){
                System.out.println("Main():Property key="+key+" ENCRYPTED value="+value);
            }
        }

        //decrypting section:
        env = "local";
        prop = new InjectProperties(env.trim(), false);
        //
        properties = prop.getEncProperties();
        version = properties.getProperty("seckey");
        System.out.println("Main(2):Property key=seckey file value="+version);
        pE = properties.propertyNames();
        //
        while (pE.hasMoreElements()) {
            String key = (String) pE.nextElement();
            String value = properties.getProperty(key);
            if(!"seckey".equalsIgnoreCase(key)){
                System.out.println("Main(2):Property key="+key+" NON-ENCRYPTED value="+value);
            }
        }
    }
}
