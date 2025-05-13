package team.incude.gsmc.v2.global.thirdparty.email.usecase.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import team.incude.gsmc.v2.global.thirdparty.email.usecase.EmailSendUseCase;
import team.incude.gsmc.v2.global.thirdparty.email.exception.EmailSendFailedException;

@Service
@RequiredArgsConstructor
public class EmailSendService implements EmailSendUseCase {

    private static final String EMAIL_SUBJECT = "GSMC 이메일 인증";
    private static final String CONTACT_EMAIL = "GSMC.OfficialEmail@gmail.com";
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async
    public void execute(String to, String authCode) {
        try {
            Context context = new Context();
            context.setVariable("authCode", authCode);
            context.setVariable("officialEmail", CONTACT_EMAIL);
            String html = templateEngine.process("MailTemplate", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(EMAIL_SUBJECT);
            helper.setText(html, true);
            ClassPathResource logoImage = new ClassPathResource("static/image/GSMC_logo.png");
            helper.addInline("gsmcLogo", logoImage);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendFailedException();
        }
    }
}
