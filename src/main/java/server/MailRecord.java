package server;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class MailRecord {
    private static String emailAccount = "583076221@qq.com";
    private static String emailPassword = "suturtqsyxbebbab";
    private static String emailSMTPHost = "smtp.qq.com";
    public static String receiveMailAccout = "liuyichen@eugeneliu.xyz";
    private MailRecord(){}
    public static void sendMail(){
        Properties properties = new Properties();//用于连接邮件服务器的参数配置(发送邮件时才需要用到)
        properties.setProperty("mail.transport,protocol","smtp");
        properties.setProperty("mail.host",emailSMTPHost);
        properties.setProperty("mail.smtp.auth","true");
        properties.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.port","465");
        properties.setProperty("mail.smtp.socketFactory.port","465");
        javax.mail.Session session = javax.mail.Session.getInstance(properties);//根据参数配置，创建会话对象(为了发送邮件准备的)
        //session.setDebug(true);
        MimeMessage mimeMessage = createMimeMessage(session,emailAccount,receiveMailAccout);
        Transport transport = null;
        try {
            transport = session.getTransport("smtp");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            transport.connect(emailAccount,emailPassword);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static MimeMessage createMimeMessage(javax.mail.Session session,String sendMail,String receieveMail){
        MimeMessage mimeMessage = null;
        try {
            //From: 发件人
            //      其中 InternetAddress 的三个参数分别为: 邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
            mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(sendMail,"EugeneLiu","UTF-8"));
            mimeMessage.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(receieveMail,"EugeneLiu","UTF-8"));
            mimeMessage.setSubject("服务器启动时间","UTF-8");
            mimeMessage.setContent("时间戳记录","text/html;charset=utf-8");
            mimeMessage.setSentDate(new Date());
            mimeMessage.saveChanges();

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mimeMessage;
    }

    public static void main(String[] args) {
        sendMail();
    }
}
