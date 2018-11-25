/*--------------------------------------------------------------------------
     Copyright (c) 2005-2019, Jnr DevOps Farms LLC
     @url    : <a href="http://www.devopsfarms.com/">DevOps Farms</a>
---------------------------------------------------------------------------*/
package com.farms.app;

import java.util.Enumeration;
import java.util.Properties;

import com.farms.enc.EncryptionUtil;
import com.farms.env.InjectProperties;

public class App
{
    public static void main( String[] args )
    {
        //System.out.println( "Hello World!" );
        String env = "local";
        new InjectProperties(env.trim(), true);
        //
        Properties properties = System.getProperties();
        System.out.println("This value: "+properties.getProperty("liferay.app.server.deploy.dir"));
        System.out.println("This new value: "+System.getProperties().getProperty("liferay.user.pw"));
        Enumeration<?> pE = properties.propertyNames();
        while (pE.hasMoreElements()) {
            String key = (String) pE.nextElement();
            String value = properties.getProperty(key);
            System.out.println("Property key="+key+" value="+value);
        }
        String pwd = System.getProperties().getProperty("liferay.user.pw");
        System.out.println("This encrypted value: "+pwd);
        String str = EncryptionUtil.encryptString("jesus", pwd);
        System.out.println("This encrypted value: "+str);
        System.out.println("This decrypted value: "+EncryptionUtil.decryptString(str, pwd));
    }

}
