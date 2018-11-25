/*--------------------------------------------------------------------------
     Copyright (c) 2005-2019, Jnr DevOps Farms LLC
     @url    : <a href="http://www.devopsfarms.com/">DevOps Farms</a>
---------------------------------------------------------------------------*/
package com.farms.env;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import com.farms.enc.EncryptionUtil;

/**
 * @author jesus.n.rodriguez
 * April 4, 2012
 */
public class InjectProperties {
    private static final String STRING_OUTPUT_TYPE_HEXADECIMAL = "hexadecimal";
    Properties injProperties;
    Properties encProperties;
    Map<String, String> mapProperties;
    String encKey;
    @Resource
    String environment;
    ClassLoader loader = getClass().getClassLoader();
    /**
     * Constructors set to load properties when Spring loads the context.
     */
    public InjectProperties() {
        //loadFileProperties(loader, geEnvironment());
    }
    public InjectProperties(String env) {
        loadFileProperties(loader, env);
    }
    public InjectProperties(String env, boolean enc) {
        if(enc){
            loadFileProperties(loader, env);
        }else{
            loadProperties(loader, env);
        }
    }
    /**
     *
     * @param loader
     * @param env
     */
    private void loadFileProperties(ClassLoader loader, final String env) {
     Map<String, String> properties =  new HashMap<String, String>();
     try {
         String filePath = null;
         if(System.getProperty("path_str") == null){
             InputStream inStream = loader.getResourceAsStream("com/gpi/env/prop/"+env.trim()+".properties");
             properties = load(inStream);

         }else{
             System.out.println("Given file: "+System.getProperty("path_str"));
             filePath = System.getProperty("path_str");
             InputStream inStream = new FileInputStream(filePath);
             properties = load(inStream);
         }
            mapProperties=properties;
        } catch (IOException e) {
            e.printStackTrace(System.err);
            mapProperties=null;
        }
   }
    /**
     * This method load an environment.properties files with encrypted or not encrypted values with jasypt
     * @param loader ClassLoader
     * @param encryptor PBE
     * @param env String
     */
    private void loadProperties(ClassLoader loader, final String env) {
        loadAndDecProperties(env);
    }
    private void loadProperties2(ClassLoader loader, final String env) {
         Properties properties =  new Properties();
         try {
             String filePath = null;
             if(System.getProperty("path_str") == null){
                 properties.load(loader.getResourceAsStream("com/gpi/env/prop/"+env.trim()+".properties"));
             }else{
                 System.out.println("Given file: "+System.getProperty("path_str"));
                 filePath = System.getProperty("path_str");
                 InputStream inStream = new FileInputStream(filePath);
                 properties.load(inStream);
             }
             Enumeration<?> pE = properties.propertyNames();
             while (pE.hasMoreElements()) {
                String key = (String) pE.nextElement();
                String value = properties.getProperty(key);
                System.out.println("InjectProperties.loadPropertis(): "+key+" = "+value);
                System.setProperty(key, value);
                properties.setProperty(key, value);
             }
             injProperties=properties;
         } catch (IOException e) {
             e.printStackTrace(System.err);
             injProperties=null;
         }
    }
    /**
     * This method load an environment.properties files with encrypted or not encrypted values with jasypt
     * @param loader ClassLoader
     * @param encryptor PBE
     * @param env String
     */
    private void loadAndEncProperties(ClassLoader loader, final String env) {
         Properties properties =  new Properties();
         String version = System.getProperty("sec_key");
         try {
             String filePath = null;
             if(System.getProperty("path_str") == null){
                 properties.load(loader.getResourceAsStream("com/gpi/env/prop/"+env.trim()+".properties"));
             }else{
                 filePath = System.getProperty("path_str");
                 InputStream inStream = new FileInputStream(filePath);
                 properties.load(inStream);
                 properties.setProperty("seckey", version);
             }
             //System.out.println("2-Property key=seckey file value="+version);
             Enumeration<?> pE = properties.propertyNames();
             while (pE.hasMoreElements()) {
                String key = (String) pE.nextElement();
                String value = properties.getProperty(key);
                if(!"seckey".equalsIgnoreCase(key)){
                    //String encValue =  "ENC("+EncryptionUtil.encryptString(value, version)+")";
                    String encValue =  EncryptionUtil.encryptString(value, version);
                    properties.setProperty(key, encValue);
                }
             }
             injProperties=properties;;
         } catch (IOException e) {
             e.printStackTrace(System.err);
             injProperties=null;
         }
    }
    public void loadAndDecProperties(final String env) {
         Properties properties =  new Properties();
        String version = null;
         try {
             String filePath = null;
             if(System.getProperty("path_str") == null){
                 properties.load(loader.getResourceAsStream("com/gpi/env/prop/ENC-"+env.trim()+".properties"));
             }else{
                 filePath = System.getProperty("path_str");
                 InputStream inStream = new FileInputStream(filePath);
                 properties.load(inStream);
             }
             if(System.getProperty("sec_key") != null){
                 version = System.getProperty("sec_key");
                 properties.setProperty("seckey", version);
             }else if(properties.getProperty("seckey") != null){
                version = properties.getProperty("seckey");
                System.setProperty("sec_key", version);
             }
             Enumeration<?> pE = properties.propertyNames();
             while (pE.hasMoreElements()) {
               String key = (String) pE.nextElement();
               String encValue = properties.getProperty(key);
               if(!"seckey".equalsIgnoreCase(key)){
                    String decKey =  EncryptionUtil.decryptString(key, version);
                    String decValue =  EncryptionUtil.decryptString(encValue, version);
                    properties.setProperty(decKey, decValue);
                    System.setProperty(decKey, decValue);
                    //System.out.println("InjectProperties.loadAndDecProperties(): "+decKey+" = "+decValue);
               }else{
                   properties.setProperty(key, encValue);
               }
            }
             injProperties=properties;
        } catch (IOException e) {
            e.printStackTrace(System.err);
            injProperties=null;
        }
   }
    public void setEncKey(final String env){
        encKey=env;
        //System.out.println("From InjectProperties{}Ecn Key is: "+env);
        ClassLoader loader = getClass().getClassLoader();
        loadAndEncProperties(loader, env);
    }

    public Properties getInjProperties(){
        return injProperties;
    }
    public Properties getEncProperties(){
        return encProperties;
    }
    public Map<String, String> getMapProperties(){
        return mapProperties;
    }
    public String geEnvironment(){
        return environment;
    }
    public void setEnvironment(){
        environment=System.getProperty("env");
    }
    private Map<String, String> load(InputStream inStream){
        Map<String, String> datos= new HashMap<String, String>();
        String line;
        try (
            InputStreamReader isr = new InputStreamReader(inStream, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);)
        {
            while ((line = br.readLine()) != null) {
                if(line.length() > 0){
                    String[] values = line.split("=");
                    String key = values[0];
                    String value = values[1];
                    datos.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
            datos=null;
        }
        return datos;
    }
}
