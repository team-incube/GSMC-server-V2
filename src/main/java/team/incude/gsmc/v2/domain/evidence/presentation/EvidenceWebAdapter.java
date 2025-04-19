package team.incude.gsmc.v2.domain.evidence.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.incude.gsmc.v2.domain.evidence.application.usecase.service.CreateActivityEvidenceService;
import team.incude.gsmc.v2.domain.evidence.presentation.data.request.CreateActivityEvidenceRequest;

@RestController
@RequestMapping("/api/v2/evidence")
@RequiredArgsConstructor
public class EvidenceWebAdapter {

    private final CreateActivityEvidenceService createActivityEvidenceService;

    @PostMapping("/current/activity")
    public ResponseEntity<Void> createActivityEvidence(@RequestBody CreateActivityEvidenceRequest request) {
        createActivityEvidenceService.execute(request.categoryName(), request.title(), request.content(), request.file(), request.activityType());
        return ResponseEntity.ok().build();
    }
}