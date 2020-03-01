package kr.ant.booksharing.service;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

    public MimeMessagePreparator createMimeMessage(final String receiverEmail, final String subject, final String content) {

        MimeMessagePreparator messagePreparator = miemMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(miemMessage);
            messageHelper.setFrom("boogleforus@gmail.com");
            messageHelper.setTo(receiverEmail);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);

        };
        return messagePreparator;
    }

}
