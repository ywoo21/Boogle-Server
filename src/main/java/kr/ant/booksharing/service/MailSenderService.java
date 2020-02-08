package kr.ant.booksharing.service;

import kr.ant.booksharing.dao.UserMapper;
import kr.ant.booksharing.repository.UserRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class MailSenderService {
    private final JavaMailSender javaMailSender;
    private final MailContentBuilderService mailContentBuilderService;


    public MailSenderService(final JavaMailSender javaMailSender,
                             final MailContentBuilderService mailContentBuilderService) {
        this.javaMailSender = javaMailSender;
        this.mailContentBuilderService = mailContentBuilderService;
    }

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
