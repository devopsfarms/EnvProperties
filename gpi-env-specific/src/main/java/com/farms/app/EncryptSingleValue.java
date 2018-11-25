/*--------------------------------------------------------------------------
     Copyright (c) 2005-2019, Jnr DevOps Farms LLC
     @url    : <a href="http://www.devopsfarms.com/">DevOps Farms</a>
---------------------------------------------------------------------------*/
package com.farms.app;

import com.farms.enc.EncryptionUtil;

/**
 * @author jesus.n.rodriguez
 *
 */
public class EncryptSingleValue implements EncryptInt {

    /**
     * @param args
     */
    public static void main(String[] args) {
        EncryptInt encr = new EncryptSingleValue();
        encr.exec();
    }
    /* (non-Javadoc)
     * @see com.farms.app.EncryptInt#exec()
     */
    public void exec() {
        String secKey = System.getProperty("secKey");
        String strValue = System.getProperty("strValue");
        System.out.println(EncryptionUtil.encryptString(strValue, secKey));
    }
}
