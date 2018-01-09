package com.github.wxz.common.email.au;

import javax.mail.Multipart;
import java.io.File;
import java.util.HashMap;
import java.util.Properties;

/**
 * 163邮箱  企业邮箱
 * @author xianzhi.wang
 * @date 2017/12/19 -11:02
 */
public class MailSenderInfo {
    /**
     * 发送邮件的服务器的IP和端口
     */
    private String mailServerHost;

    private String mailServerPort = "25";

    /**
     *  邮件发送者的地址
     */
    private String fromAddress;

    /**
     * 邮件接收者的地址
     */
    private String toAddress;
    /**
     * 邮件接收抄送者的地址
     */
    private String ccAddress;
    /**
     * 登陆邮件发送服务器的用户名和密码
     */
    private String userName;
    private String password;
    /**
     * 是否需要身份验证
     */
    private boolean validate = false;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件的文本内容
     */
    private String content;
    /**
     * 邮件附件的文件名
     */

    private HashMap<String, File> fileHashMap;
    private Multipart multipart;

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public Multipart getMultipart() {
        return multipart;
    }

    public void setMultipart(Multipart multipart) {
        this.multipart = multipart;
    }

    /**
     * 获得邮件会话属性
     */
    public Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", this.mailServerHost);
        p.put("mail.smtp.port", this.mailServerPort);
        p.put("mail.smtp.auth", validate ? "true" : "false");
        p.put("mail.smtp.localhost", "127.0.0.1");
        return p;
    }

    public HashMap<String, File> getFileHashMap() {
        return fileHashMap;
    }

    public void setFileHashMap(HashMap<String, File> fileHashMap) {
        this.fileHashMap = fileHashMap;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String textContent) {
        this.content = textContent;
    }
}
