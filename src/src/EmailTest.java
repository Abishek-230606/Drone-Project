import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailTest {
    public static void main(String[] args) {
        final String fromEmail = "jsabishek77@gmail.com";
        final String password = "veig xhwe aaie rgxt";
        final String toEmail = "jsabishek236@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Make sure activation.jar is in classpath
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Test Email");
            message.setText("This is a test email from Jakarta Mail.");

            Transport.send(message);
            System.out.println("✅ Email sent!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
