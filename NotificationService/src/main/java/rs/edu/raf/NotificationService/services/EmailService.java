package rs.edu.raf.NotificationService.services;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    public final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender){
        this.emailSender = emailSender;
    }

    public void sendSimpleMailMessage(String to, String subject, String text) throws MailException{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
    public void sendMimeMessageWithAttachments(String to, String subject, String text, String[] attachmentFilePaths) throws MessagingException, IOException{
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        
        //Add attachments
        for (var attachmentFilePath : attachmentFilePaths){
            var fileSystemResource = new FileSystemResource(new File(attachmentFilePath));
            helper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
        }

        emailSender.send(message);
    }
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException{
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        emailSender.send(message);
    }
    // For Complex messages (example: HTML with embeded images)
    public void sendMimeMessage(MimeMessage message){
        emailSender.send(message);
    }
}
