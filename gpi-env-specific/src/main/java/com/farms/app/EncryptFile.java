/*--------------------------------------------------------------------------
     Copyright (c) 2005-2019, Jnr DevOps Farms LLC
     @url    : <a href="http://www.devopsfarms.com/">DevOps Farms</a>
---------------------------------------------------------------------------*/
package com.farms.app;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import com.farms.enc.EncryptionUtil;
import com.farms.env.InjectProperties;

public class EncryptFile implements EncryptInt {

    /**
     * @param args
     */
    public static void main(String[] args) {
        EncryptInt encr = new EncryptFile();
        encr.exec();
    }
    public void exec() {
        //Encrypting a file:
        StringBuffer result = new StringBuffer();
        String envName = System.getProperty("env");
        String secKey = System.getProperty("sec_key");
        result.append("seckey="+secKey+"\n");
        InjectProperties prop = new InjectProperties(envName);
        Map<String, String> properties = prop.getMapProperties();
        properties.put("seckey", secKey);
        //
        for(Map.Entry<String,String> property : properties.entrySet()){
           String key = property.getKey();
           //System.out.println("Key is: "+key);
           String value = property.getValue();
           if(!"seckey".equalsIgnoreCase(key)){
                String encKey = EncryptionUtil.encryptString(key, secKey);
                String encValue = EncryptionUtil.encryptString(value, secKey);
                result.append(encKey+"="+encValue+"\n");
            }
       }
       EncryptionUtil.writeFile(result.toString(), "ENC-"+System.getProperty("path_str"));
    }
}
