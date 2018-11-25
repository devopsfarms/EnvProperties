/*--------------------------------------------------------------------------
     Copyright (c) 2005-2019, Jnr DevOps Farms LLC
     @url    : <a href="http://www.devopsfarms.com/">DevOps Farms</a>
---------------------------------------------------------------------------*/
package com.farms.app;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import java.io.File;
import java.util.List;
/**
 * @author jesus.n.rodriguez
 *
 */
public class EncryptUtilClass implements EncryptInt {

    /**Defined commands:
     * 1. encrypt_value
     * 2. decrypt_value
     * 3. encrypt_file
     * 4. decrypt_file
     * 5. ex: ./encrypt_util.sh encrypt_file filePath null env
     * 6. ex: ./encrypt_util.sh decrypt_file filePath null null
     * 7: ex: ./encrypt_util.sh encrypt_file null null qa
     * 8: ex: ./encrypt_util.sh decrypt_file null null local
     * 9: ex: ./encrypt_util.sh encrypt_value jesus 1234 null
     * 10:ex: ./encrypt_util.sh decrypt_value 0MA53RYAqKs9IDMAAAA= 1234 null
     */
    public EncryptUtilClass() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Arguments: "+args.length);
        if(args.length < 3){
            System.out.println("FATAL ERROR:EncryptUtilClass: You should specify 3 or more arguments.");
            System.exit(0);
        }else{
            int i=0;
            String[] valores = args;
            for(String valor:valores){
                if(valor.equalsIgnoreCase("null") || valor.equals("") || valor.length()==0){
                    valores[i]=null;
                }
                i++;
            }
            String command = valores[0];
            String path_str = valores[1];
            String sec_key = valores[2];
            String env_value = valores[3];
            //
            if(sec_key != null){
                System.setProperty("sec_key",sec_key);
                System.setProperty("secKey",sec_key);
            }
            if(path_str != null){
                System.setProperty("path_str",path_str);
                System.setProperty("strValue",path_str);
            }
            if(command != null)
                System.setProperty("command",command);
            if(env_value != null)
                System.setProperty("env",env_value);
            //
            EncryptInt build = new EncryptUtilClass();
            build.exec();
        }
    }
    public void exec() {
        //
        String scmCommand = System.getProperty("command").trim();
        Document doc = parse("enc_commands.xml");
        if(doc != null){
            List<Node>fields = doc.selectNodes("//command");
            if(getAttributeNode(scmCommand, fields) != null){
                if(System.getProperty("command") != null || !System.getProperty("command").equalsIgnoreCase("null")){
                    List<Node> profileNodes = doc.selectNodes("//command");
                    for(Node node:profileNodes){
                        String name = node.valueOf("./@name");
                        String clas = node.valueOf("./@class");
                        if(scmCommand.equalsIgnoreCase(name)){
                            try{
                                Class objClass = Class.forName(clas);
                                Object object = objClass.newInstance();
                                EncryptInt newObj = (EncryptInt)object;
                                newObj.exec();
                            }catch(Exception e){
                                System.out.println("ERROR:EncryptUtilClass: "+e.getMessage());
                                e.printStackTrace();
                                System.exit(0);
                            }
                            break;
                        }else{
                            continue;
                        }
                    }
                }else{
                    System.out.println("ERROR:EncryptUtilClass: you must specifiy command name; given name ="+System.getProperty("command"));
                }
            }else{
                System.out.println("ERROR:EncryptUtilClass: command "+scmCommand+" is not defined in enc_commands.xml.");
                System.exit(0);
            }
        }else{
            System.out.println("ERROR:EncryptUtilClass: enc_commands.xml does not exists.");
            System.exit(0);
        }
    }
    public static Document parse(final String filePath)
    {
        Document document=null;
        try
        {
            SAXReader reader = new SAXReader();
            document = reader.read(new File(filePath));
        }
        catch(DocumentException e)
        {
            String msg = "Error Parsing..."+e.getMessage();
            document = null;
        }
        return document;
    }
    /**
    * @nameValue is the value for "name" attribute
    * @fiels     is the list of nodes.
    */
    public static Node getAttributeNode(final String nameValue, final List<Node> fields)
    {
        String nomb=null;
        Node node = null, nod=null;
        for(int i=0; i<fields.size(); i++)
        {
            nod = fields.get(i);
            nomb = nod.valueOf("./@name");
            if(nomb.trim().equalsIgnoreCase(nameValue.trim()))
            {
                node = nod;
                break;
            }
        }
        return node;
    }
}

