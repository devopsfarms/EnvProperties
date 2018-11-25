/*--------------------------------------------------------------------------
    Jnr DevOps Farms LLC, Copyrigths, 2005-2019.
    @url    : <a href="http://www.devopsfarms.com/">DevOps Farms</a>
---------------------------------------------------------------------------*/
package com.farms.enc;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Enumeration;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import com.farms.env.InjectProperties;

/**
 * @author jesus.n.rodriguez
 *
 */
public final class EncryptionUtil {
     private static final String SALT_PW = 0x2f + "F!!91qlfsn" + 0x22;
     private static final int KEY_OBTENTION_ITERATIONS = 1000;
     private static final String BC_ALGORITHM_NAME_PBE_MD5_TRIPLE_DES = "PBEWithMD5AndTripleDES";
     private static final String STRING_OUTPUT_TYPE_HEXADECIMAL = "hexadecimal";
   /**
    *
    */
   public EncryptionUtil() {
    // TODO Auto-generated constructor stub
   }

   /**
    * @param args
    */
   public static void main(String[] args) {
       System.out.println("value to encrypt:Nucleus123!");
       System.out.println("SecKey:1.13.0.1000");
       String encValue = encryptString("Nucleus123!", "1.13.0.1000");
       System.out.println("Encrypted value: "+encValue);
       //
       String decValue =  decryptString(encValue, "1.13.0.1000");
       System.out.println("Decrypted: "+decValue);
       //Encrypting URLS:
       System.out.println("Url to encrypt:https://G1NLDBISA001:8443/dmswebservice/merchantdocument/");
       encValue = encryptString("https://G1NLDBISA001:8443/dmswebservice/merchantdocument/", "1.13.0.1000");
       System.out.println("Encrypted: "+encValue);
       decValue =  decryptString(encValue, "1.13.0.1000");
       System.out.println("Decrypted URL: "+decValue);
       //For a file:
       //System.setProperty("path_str", "/projects/gpnet/properties/enc/dev-IBNGDN1-IBS_CSP_IS_Staging.properties");
       System.setProperty("path_str", "/projects/gpnet/properties/enc/dev-gp-dms-webservice-app.properties");
       InjectProperties prop = new InjectProperties("dev");
       Properties properties = prop.getInjProperties();
       Enumeration<?> pE = properties.propertyNames();
       while (pE.hasMoreElements()) {
          String key = (String) pE.nextElement();
          String value = properties.getProperty(key);
          encValue = encryptString(value, "1.13.0.1000");
          System.out.println("Encrypted property value: "+encValue);
          value =  decryptString(encValue, "1.13.0.1000");
          System.out.println("Decrypted: "+value);

       }
   }

   /**
    *
    * @param data String to be encoded,zipped, and 64Encoded
    * @return
    */
   /*
   public static String encryptString(String data, String secKey){
        StandardPBEStringEncryptor passwordEncryptor = new StandardPBEStringEncryptor();
        passwordEncryptor.setPassword(secKey);
        data = passwordEncryptor.encrypt(data);
        return encodeString(data);
   }
   */
   public static String encryptString2(String data, String secKey){
        StandardPBEStringEncryptor passwordEncryptor = new StandardPBEStringEncryptor();
        passwordEncryptor.setPassword(secKey);
        data = passwordEncryptor.encrypt(data);
        return data;
  }
   /**
    * Encrypt a Message Text to a Hexadecimal String usable for a WEB Url.
    *
    * @param message to be encrypted
    * @return String of a Encrypted Hexadecimal String
    */
   public static String encryptString(String data, String secKey) {
       StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
       encryptor.setPassword(secKey);
       //Next: for strong encryption; disabled for now.
       //encryptor.setAlgorithm(BC_ALGORITHM_NAME_PBE_MD5_TRIPLE_DES);
       //encryptor.setKeyObtentionIterations(KEY_OBTENTION_ITERATIONS);
       encryptor.setStringOutputType(STRING_OUTPUT_TYPE_HEXADECIMAL);
       return encryptor.encrypt(data);
   }
   /**
    *
    * @param data encrypted
    * @return
    */
   /*
   public static String decryptString(String encryptedData, String secKey){
        StandardPBEStringEncryptor passwordEncryptor = new StandardPBEStringEncryptor();
        passwordEncryptor.setPassword(secKey);
        encryptedData = getDecodeString(encryptedData);
        return passwordEncryptor.decrypt(encryptedData);
   }
   */
  public static String decryptString(String encryptedData, String secKey){
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(secKey);
        encryptor.setStringOutputType(STRING_OUTPUT_TYPE_HEXADECIMAL);
        return encryptor.decrypt(encryptedData);
  }
    /**   Converts an object to an array of bytes . Uses the Logging utilities in j2sdk1.4 for
     *    reporting exceptions.
     *    @param object the object to convert.
     *    @return the associated byte array.
     */
    public static byte[] toBytes(final Object object)
    {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try
        {
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
            oos.writeObject(object);
        }
        catch(java.io.IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
        return baos.toByteArray();
    }
    /**
     *
     * @param appName
     * @param filePath
     * @return
     */
   public static Document createBrokerXml(String appName, String filePath){
    Document doc = DocumentHelper.createDocument();
    Element root = doc.addElement("IntegrationNodeConnectionParameters");
    String secKey=null;
    try{
        Properties properties = new Properties();
        FileInputStream input = new FileInputStream(filePath);
        properties.load(input);
        Enumeration<?> pE = properties.propertyNames();
        Enumeration<?> pEE = properties.propertyNames();
        while (pE.hasMoreElements()) {
            String key = (String) pE.nextElement();
            String value = properties.getProperty(key);
            if("seckey".equalsIgnoreCase(key)){
                secKey = value;
                break;
            }
        }
        System.out.println("Secret key: "+secKey);
        while (pEE.hasMoreElements()) {
            String encKey = (String) pEE.nextElement();
            String encValue = properties.getProperty(encKey);
            if(!"seckey".equalsIgnoreCase(encKey)){
                String value = EncryptionUtil.decryptString(encValue, secKey);
                String key = EncryptionUtil.decryptString(encKey, secKey);
                Element node = root.addAttribute(key, value.trim());
                if("xmlns".equalsIgnoreCase(key)){
                    Namespace ns = new Namespace("", value);
                            doc.getRootElement().add(ns);
                        }
                    }
                }
                writeXmlFile(appName, doc);
            } catch (IOException e) {
                 e.printStackTrace(System.err);
                 doc = DocumentHelper.createDocument();
            }
            return doc;
       }
     /**
       * Write a well formed XML into a file.
       * @param doc XML in Document format
       * @param filePath String
       * @param fileName String
       */
      public static void writeXmlFile(String app, final Document doc)
      {
           File fout = new File(app.trim()+".broker");
           if(fout.exists())
               fout.delete();
           //
           try
           {
               // Pretty print the document to System.out
               OutputFormat format = OutputFormat.createPrettyPrint();
               XMLWriter writer = new XMLWriter(new FileWriter(fout.getPath()), format );
               writer.write(doc);
               writer.close();
           }
           catch(IOException e)
           {
               String msg = "EncryptionUtil.writeXmlFile(): "+e.toString();
               e.printStackTrace();
           }
       }
      /**
       *
       * @param msg
       * @param fileName
       */
      public static void writeFile(String msg, String fileName){
             try
             {
                 byte data[] = msg.getBytes();
                 File fout = new File(fileName);
                 if(fout.exists())
                     fout.delete();
                 //
                 RandomAccessFile rout = new RandomAccessFile(fout, "rw");
                 rout.seek(fout.length());
                 rout.write(data);
                 rout.write('\n');
                 rout.write('\r');
                 rout.close();
             }
             catch (IOException e)
             {
                 String msg1 = "Error: " + e.getMessage() + " while writing " + fileName;
                 e.printStackTrace();
             }
        }
}
