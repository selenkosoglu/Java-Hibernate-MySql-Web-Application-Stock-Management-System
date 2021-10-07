package utils;

import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Stateless
public class MailSend {

    public boolean sendEmail(String fromEmail, String username, String password, String toEmail,  String pathUrl){
        boolean sendMessageControl = true;
        try {
            Properties props = System.getProperties();
            props.put("mail.smtp.host","smtp.gmail.com");
            props.put("mail.smtp.auth","true");
            props.put("mail.smtp.port","465");
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.port","465");
            props.put("mail.smtp.socketFactory.fallback","false");

            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(true);
            Message mailMessage = new MimeMessage(mailSession);
            String message = "Sayın Kullanıcı alttaki linke tıklayarak " +
                    "şifrenizi değiştirebilirsiniz\n\n" + pathUrl;
            mailMessage.setFrom(new InternetAddress(fromEmail));
            mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            mailMessage.setContent(message, "text/plain; charset=UTF-8");
            mailMessage.setSubject("Depo Proje - Şifre Değiştir");

            Transport transport = mailSession.getTransport("smtp");
            transport.connect("smtp.gmail.com",username, password);

            transport.sendMessage(mailMessage, mailMessage.getAllRecipients());

        } catch (MessagingException e) {
            System.err.println("SendMessage Error : " + e);
            sendMessageControl = false;
        }
        return sendMessageControl;
    }

}