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
import java.util.Properties;

import org.dom4j.Document;

import com.farms.enc.EncryptionUtil;
import com.farms.env.InjectProperties;

public class CreateBrokerFile implements EncryptInt {

    /**
     * @param args
     */
    public static void main(String[] args) {
        EncryptInt encr = new CreateBrokerFile();
        encr.exec();
    }
    public void exec() {
        //Broker file needs: environment name, and integration node name
        //command = command name, passed on.
        //env = environment+int node name (e.g. dev.IBNGDN1.broker)
        //path_str = properties file
        //
        String filePath = System.getProperty("path_str");
        String envName = System.getProperty("env");
        Document doc = EncryptionUtil.createBrokerXml(envName, filePath);
        System.out.println(doc.asXML());
    }
}
