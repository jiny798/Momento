package jiny.futurevia.service.infra.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("!local")
@Service
@RequiredArgsConstructor
@Slf4j
public class HtmlEmailService implements EmailService {

//    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(EmailMessage emailMessage) {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper;
//        try {
//            mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
//            mimeMessageHelper.setTo(emailMessage.getTo());
//            mimeMessageHelper.setSubject(emailMessage.getSubject());
//            mimeMessageHelper.setText(emailMessage.getMessage(), false);
//            javaMailSender.send(mimeMessage);
//            log.info("sent email: {}", emailMessage.getMessage());
//        } catch (MessagingException e) {
//            log.error("failed to send email", e);
//        }
    }
}