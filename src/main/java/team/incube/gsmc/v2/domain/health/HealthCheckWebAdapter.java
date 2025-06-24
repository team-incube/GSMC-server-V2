package team.incube.gsmc.v2.domain.health;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서버의 상태를 확인하기 위한 Web 어댑터 클래스입니다.
 * <p>{@code GET /api/v2/health/check} 엔드포인트를 통해 서버가 정상적으로 동작 중인지 확인할 수 있습니다.
 * 주로 로드밸런서, 모니터링 시스템, 배포 도구 등이 이 엔드포인트를 호출하여 서버 상태를 점검합니다.
 * @author snowykte0426
 */
@RestController
@RequestMapping("/api/v2/health")
public class HealthCheckWebAdapter {

    @GetMapping("/check")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}