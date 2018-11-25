/*--------------------------------------------------------------------------
     Copyright (c) 2005-2019, Jnr DevOps Farms LLC
     @url    : <a href="http://www.devopsfarms.com/">DevOps Farms</a>
---------------------------------------------------------------------------*/
package com.farms.app;

import com.farms.enc.EncryptionUtil;

public class DecryptSingleValue implements EncryptInt {

    public DecryptSingleValue() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        EncryptInt encr = new DecryptSingleValue();
        encr.exec();
    }
    /* (non-Javadoc)
     * @see com.farms.app.EncryptInt#exec()
     */
    public void exec() {
        String secKey = System.getProperty("secKey");
        String encValue = System.getProperty("strValue");
        System.out.println(EncryptionUtil.decryptString(encValue, secKey));
    }
}
