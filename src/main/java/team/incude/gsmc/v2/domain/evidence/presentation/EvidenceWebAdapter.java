package team.incude.gsmc.v2.domain.evidence.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidenceApplicationPort;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.presentation.data.request.*;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;

@RestController
@RequestMapping("/api/v2/evidence")
@RequiredArgsConstructor
public class EvidenceWebAdapter {

    private final EvidenceApplicationPort evidenceApplicationPort;

    @GetMapping("/current")
    public ResponseEntity<GetEvidencesResponse> getEvidencesByType(@RequestParam(name = "type", required = false) EvidenceType type) {
        GetEvidencesResponse response = evidenceApplicationPort
                .findEvidenceByCurrentUserAndType(type);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{studentCode}")
    public ResponseEntity<GetEvidencesResponse> getEvidenceByStudentCode(
            @PathVariable String studentCode,
            @RequestParam(name = "type", required = false) EvidenceType type,
            @RequestParam(name = "status", required = false) ReviewStatus status) {
        GetEvidencesResponse response = evidenceApplicationPort
                .findEvidenceByStudentCodeAndTypeAndStatus(studentCode, type, status);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<GetEvidencesResponse> searchEvidence(
            @RequestParam(name = "studentCode", required = false) String studentCode,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "type", required = false) EvidenceType type) {
        GetEvidencesResponse response = evidenceApplicationPort
                .findEvidenceByStudentCodeAndTitleAndType(studentCode, title, type);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/current/activity")
    public ResponseEntity<Void> createActivityEvidence(@ModelAttribute CreateActivityEvidenceRequest request) {
        evidenceApplicationPort.createActivityEvidence(request.categoryName(), request.title(), request.content(), request.file(), request.imageUrl(), request.activityType(), request.draftId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/current/reading")
    public ResponseEntity<Void> createReadingEvidence(@RequestBody CreateReadingEvidenceRequest request) {
        evidenceApplicationPort.createReadingEvidence(request.title(), request.author(), request.page(), request.content(), request.draftId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/current/other")
    public ResponseEntity<Void> createOtherEvidence(@ModelAttribute CreateOtherEvidenceRequest request) {
        evidenceApplicationPort.createOtherEvidence(request.categoryName(), request.file());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/current/scoring")
    public ResponseEntity<Void> createOtherScoringEvidence(@ModelAttribute CreateOtherScoringEvidenceRequest request) {
        evidenceApplicationPort.createOtherScoringEvidence(request.categoryName(), request.file(), request.value());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/major/{evidenceId}")
    public ResponseEntity<Void> patchMajorEvidence(
            @PathVariable Long evidenceId,
            @ModelAttribute PatchActivityEvidenceRequest request) {
        evidenceApplicationPort.updateMajorEvidenceByCurrentUser(evidenceId, request.title(), request.content(), request.file(), request.imageUrl());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/humanities/{evidenceId}")
    public ResponseEntity<Void> patchHumanityEvidence(
            @PathVariable Long evidenceId,
            @ModelAttribute PatchActivityEvidenceRequest request) {
        evidenceApplicationPort.updateHumanitiesEvidenceByCurrentUser(evidenceId, request.title(), request.content(), request.file(), request.imageUrl());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/reading/{evidenceId}")
    public ResponseEntity<Void> patchReadingEvidence(
            @PathVariable Long evidenceId,
            @RequestBody PatchReadingEvidenceRequest request) {
        evidenceApplicationPort.updateReadingEvidenceByCurrentUser(evidenceId, request.title(), request.author(), request.content(), request.page());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/other/{evidenceId}")
    public ResponseEntity<Void> patchOtherEvidence(
            @PathVariable Long evidenceId,
            @ModelAttribute PatchOtherEvidenceRequest request) {
        evidenceApplicationPort.updateOtherEvidenceByCurrentUser(evidenceId, request.file(), request.imageUrl());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/scoring/{evidenceId}")
    public ResponseEntity<Void> patchScoringEvidence(
            @PathVariable Long evidenceId,
            @ModelAttribute PatchOtherScoringEvidenceRequest request) {
        evidenceApplicationPort.updateOtherScoringEvidenceByCurrentUser(evidenceId, request.file(), request.value(), request.imageUrl());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{evidenceId}/status")
    public ResponseEntity<Void> patchStatusEvidence(
            @PathVariable Long evidenceId,
            @RequestBody PatchStatusEvidenceRequest request) {
        evidenceApplicationPort.updateReviewStatus(evidenceId, request.status());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/current/{evidenceId}")
    public ResponseEntity<Void> deleteEvidence(@PathVariable Long evidenceId) {
        evidenceApplicationPort.deleteEvidence(evidenceId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}