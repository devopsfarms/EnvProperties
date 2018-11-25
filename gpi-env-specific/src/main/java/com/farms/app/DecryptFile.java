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
public class DecryptFile implements EncryptInt {

    /**
     * @param args
     */
    public static void main(String[] args) {
        EncryptInt encr = new DecryptFile();
        encr.exec();
    }
    public void exec() {
        //decrypting section:
        StringBuffer result = new StringBuffer();
        String envName = System.getProperty("env");
        String secKey = System.getProperty("sec_key");
        InjectProperties prop = new InjectProperties(envName);
        Map<String,String> properties = prop.getMapProperties();
        if(System.getProperty("sec_key") != null){
             secKey = System.getProperty("sec_key");
        }
        result.append("seckey="+secKey+"\n");
        System.out.println("seckey="+secKey);
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            //System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
            String encKey = (String) entry.getKey();
            String encValue = entry.getValue();
            if(!"seckey".equalsIgnoreCase(encKey)){
                String key =  EncryptionUtil.decryptString(encKey, secKey);
                String value =  EncryptionUtil.decryptString(encValue, secKey);
                //System.out.println("Key : " + key + " Value : " + value);
                result.append(key+"="+value+"\n");
            }
        }
        //e.g. ENC-dev-IBNGDN1-IBS_CSP_IS.properties
        //e.g. DEC-dev-IBNGDN1-IBS_CSP_IS.properties
        String new_path = System.getProperty("path_str").toString();
        new_path = "DEC-"+new_path.substring(4, new_path.length());
        EncryptionUtil.writeFile(result.toString(), new_path);
    }
}