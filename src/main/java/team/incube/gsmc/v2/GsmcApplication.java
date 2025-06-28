package team.incube.gsmc.v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * GSMC 애플리케이션의 진입점입니다.
 * <p>이 클래스는 Spring Boot 애플리케이션을 시작하며, 필요한 모든 설정과 컴포넌트를 자동으로 구성합니다.
 * <p>애플리케이션은 기본적으로 `src/main/resources/application.yml` 파일에서 설정을 읽어들입니다.
 * <p>애플리케이션을 실행하려면 `main` 메서드를 호출하면 됩니다.
 * @author suuuuuuminnnnnn
 */
@SpringBootApplication
public class GsmcApplication {

	public static void main(String[] args) {
		SpringApplication.run(GsmcApplication.class, args);
	}

}
