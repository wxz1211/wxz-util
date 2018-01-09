package com.github.wxz.common.email.au;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -11:02
 */
public class MailSend {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailSend.class);

    public boolean sendTextMail(MailSenderInfo mailInfo) {

        Properties pro = mailInfo.getProperties();
        Session sendMailSession = Session.getDefaultInstance(pro, MyAuthenticator.getInstance());
        try {
            Message mailMessage = new MimeMessage(sendMailSession);
            Address from = new InternetAddress(mailInfo.getFromAddress());
            mailMessage.setFrom(from);

            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailInfo.getToAddress()));
            if (mailInfo.getCcAddress() != null) {
                mailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailInfo.getCcAddress()));
            }
            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new Date());
            if (mailInfo.getMultipart() != null) {
                mailMessage.setContent(mailInfo.getMultipart());
            } else {
                String mailContent = mailInfo.getContent();
                mailMessage.setText(mailContent);
            }
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            LOGGER.error("send email error ..", ex);
            return false;
        }
    }
}
