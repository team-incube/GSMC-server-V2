package team.incude.gsmc.v2.global.thirdparty.email.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Spring Email 전송을 위한 설정 클래스입니다.
 * <p>SMTP 기반의 메일 서버 정보를 외부 설정에서 주입받아 {@link JavaMailSender} Bean을 구성합니다.
 * <p>메일 서버 연결 시 TLS 보안 및 인증을 활성화하며, {@code mail.smtp.*} 속성을 통해 세부 설정을 적용합니다.
 * 주요 설정:
 * <ul>
 *   <li>{@code mail.host} - SMTP 서버 호스트</li>
 *   <li>{@code mail.port} - SMTP 서버 포트</li>
 *   <li>{@code mail.username} - 인증용 계정 아이디</li>
 *   <li>{@code mail.password} - 인증용 계정 비밀번호</li>
 * </ul>
 * 이 설정은 {@link JavaMailSender}를 주입받는 서비스나 어댑터에서 사용됩니다.
 * @author jihoonwjj
 */
@Configuration
public class EmailConfig {

    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private int port;
    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;

    @Bean
    public JavaMailSender emailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.timeout", "5000");
        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }
}
