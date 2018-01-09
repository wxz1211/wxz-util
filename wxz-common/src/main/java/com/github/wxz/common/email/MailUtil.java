package com.github.wxz.common.email;


import com.github.wxz.common.email.au.MailSend;
import com.github.wxz.common.email.au.MailSenderInfo;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;

/**
 * 适用 163邮箱  企业邮箱
 *
 * @author xianzhi.wang
 * @date 2017/12/19 -11:10
 */
public class MailUtil {
    /**
     * 发送邮件
     *
     * @param sendToAddress 发送至
     * @param copyToAddress 抄送给
     */
    public static void sendMail(String sendToAddress, String copyToAddress) {
        MailSenderInfo mailSenderInfo = new MailSenderInfo();
        mailSenderInfo.setMailServerHost("SMTP.263.net");
        mailSenderInfo.setMailServerPort("25");
        mailSenderInfo.setValidate(true);
        mailSenderInfo.setUserName("");
        // 您的邮箱密码
        mailSenderInfo.setPassword("");
        mailSenderInfo.setFromAddress("");
        mailSenderInfo.setToAddress(sendToAddress);
        mailSenderInfo.setCcAddress(copyToAddress);


        Multipart msgPart = new MimeMultipart();
        //正文
        MimeBodyPart body = new MimeBodyPart();
        try {
            body.setContent(mailSenderInfo.getContent(), "text/html; charset=utf-8");
            msgPart.addBodyPart(body);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSenderInfo.setMultipart(msgPart);
        MailSend mailSend = new MailSend();
        mailSend.sendTextMail(mailSenderInfo);
    }


    /**
     * 发送邮件
     *
     * @param sendToAddress 发送至
     * @param copyToAddress 抄送给
     * @param fileName      附件
     */
    public static void sendMail(String sendToAddress, String copyToAddress, String fileName) {
        MailSenderInfo mailSenderInfo = new MailSenderInfo();
        mailSenderInfo.setMailServerHost("SMTP.263.net");
        mailSenderInfo.setMailServerPort("25");
        mailSenderInfo.setValidate(true);
        mailSenderInfo.setUserName("");
        // 您的邮箱密码
        mailSenderInfo.setPassword("");
        mailSenderInfo.setFromAddress("");
        mailSenderInfo.setSubject(fileName);
        mailSenderInfo.setContent(fileName);
        mailSenderInfo.setToAddress(sendToAddress);
        mailSenderInfo.setCcAddress(copyToAddress);


        Multipart msgPart = new MimeMultipart();
        //正文
        MimeBodyPart body = new MimeBodyPart();
        //附件
        MimeBodyPart attach = new MimeBodyPart();
        try {
            attach.setDataHandler(new DataHandler(new FileDataSource("/" + fileName + ".xlsx")));
            attach.setFileName(MimeUtility.encodeText(fileName + ".xlsx"));
            msgPart.addBodyPart(attach);
            body.setContent(mailSenderInfo.getContent(), "text/html; charset=utf-8");
            msgPart.addBodyPart(body);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mailSenderInfo.setMultipart(msgPart);
        MailSend mailSend = new MailSend();
        mailSend.sendTextMail(mailSenderInfo);
    }
}
