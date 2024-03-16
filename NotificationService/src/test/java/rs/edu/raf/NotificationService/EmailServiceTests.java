package rs.edu.raf.NotificationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Properties;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import rs.edu.raf.NotificationService.services.EmailService;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmailServiceTests {
    
    private GreenMail testSmtp;
    private JavaMailSenderImpl mailSender;
    @BeforeEach
    void setUp(){
        testSmtp = new GreenMail(ServerSetupTest.SMTP)
        .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"));
        testSmtp.start();

        mailSender = new JavaMailSenderImpl();
        mailSender.setPort(3025); // port for GreenMail server
        mailSender.setHost("localhost");
        mailSender.setUsername("user");
        mailSender.setPassword("admin");

        Properties mailProps = new Properties();
        mailProps.setProperty("mail.transport.protocol","smtp");
        mailProps.setProperty("mail.smtp.auth","true");
        mailProps.setProperty("mail.smtp.starttls.enable","true");
        mailProps.setProperty("mail.debug","false");
        
        mailSender.setJavaMailProperties(mailProps);

    }
    @AfterEach
    void cleanUp(){
        testSmtp.stop();
    }
    
    @Test
    void testSendSimpleEmail() throws MessagingException{
        EmailService emailService = new EmailService(mailSender);
        emailService.sendSimpleMailMessage("test@receiver.com",
         "test subject", "test text");

        var recivedMessages = testSmtp.getReceivedMessages();
        assertEquals(recivedMessages.length, 1);
        MimeMessage message = recivedMessages[0];
        assertEquals("test@receiver.com", message.getAllRecipients()[0].toString());
        assertEquals("test subject", message.getSubject());
        assertEquals("test text", GreenMailUtil.getBody(message));
    }

    @Test
    void testSendEmailThrowsExceptions(){
        EmailService emailService = new EmailService(mailSender);
        assertThrows(
            MailParseException.class,
           () -> emailService.sendSimpleMailMessage(
            "", "", ""
           )
        );
        String arr[] = {"fileName"};
        assertThrows(
            MailSendException.class,
           () -> emailService.sendMimeMessageWithAttachments(
            "test@receiver.com", "test subject", "test text",
            arr
           )
        );
    }
}
