package team.incude.gsmc.v2.domain.health;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/health")
public class HealthCheckWebAdapter {

    @GetMapping("/check")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}