package com.github.wxz.common.email.au;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -11:02
 */
public class MyAuthenticator extends Authenticator {
    private static volatile MyAuthenticator myAuthenticator;

    private MyAuthenticator() {
    }

    public static MyAuthenticator getInstance() {
        if (myAuthenticator == null) {
            synchronized (MyAuthenticator.class) {
                if (myAuthenticator == null) {
                    myAuthenticator = new MyAuthenticator();
                }
            }
        }
        return myAuthenticator;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication("itsupport@apass.cn", "support0511");
    }

}
