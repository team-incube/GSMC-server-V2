package team.incude.gsmc.v2.domain.evidence.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidenceApplicationPort;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.presentation.data.request.*;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.*;

import java.util.UUID;

/**
 * 증빙자료 관련 HTTP 요청을 처리하는 Web 어댑터 클래스입니다.
 * <p>{@link EvidenceApplicationPort}를 통해 도메인 유스케이스를 실행하며,
 * 증빙자료 생성, 수정, 삭제, 조회 및 임시저장 기능을 제공합니다.
 * <p>기본 경로: {@code /api/v2/evidence}
 * @author snowykte0426, suuuuuuminnnnnn
 */
@RestController
@RequestMapping("/api/v2/evidence")
@RequiredArgsConstructor
public class EvidenceWebAdapter {

    private final EvidenceApplicationPort evidenceApplicationPort;

    /**
     * 현재 사용자에 대한 증빙자료 목록을 조회합니다.
     * @param type 필터링할 증빙자료 타입 (선택)
     * @return 증빙자료 목록
     */
    @GetMapping("/current")
    public ResponseEntity<GetEvidencesResponse> getEvidencesByType(@RequestParam(name = "type", required = false) EvidenceType type) {
        GetEvidencesResponse response = evidenceApplicationPort
                .findEvidenceByCurrentUserAndType(type);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 특정 학생의 증빙자료 목록을 조회합니다.
     * @param studentCode 학생 코드
     * @param type 증빙자료 타입 (선택)
     * @param status 검토 상태 (선택)
     * @return 증빙자료 목록
     */
    @GetMapping("/{studentCode}")
    public ResponseEntity<GetEvidencesResponse> getEvidenceByStudentCode(
            @PathVariable String studentCode,
            @RequestParam(name = "type", required = false) EvidenceType type,
            @RequestParam(name = "status", required = false) ReviewStatus status) {
        GetEvidencesResponse response = evidenceApplicationPort
                .findEvidenceByStudentCodeAndTypeAndStatus(studentCode, type, status);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 검색 조건(학생 코드, 제목, 타입)에 따라 증빙자료를 조회합니다.
     * @param studentCode 학생 코드 (선택)
     * @param title 제목 (선택)
     * @param type 증빙자료 타입 (선택)
     * @return 검색된 증빙자료 목록
     */
    @GetMapping("/search")
    public ResponseEntity<GetEvidencesResponse> searchEvidence(
            @RequestParam(name = "studentCode", required = false) String studentCode,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "type", required = false) EvidenceType type) {
        GetEvidencesResponse response = evidenceApplicationPort
                .findEvidenceByStudentCodeAndTitleAndType(studentCode, title, type);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 활동 증빙자료를 생성합니다.
     * @param request 활동 증빙자료 생성 요청
     * @return 201 CREATED
     */
    @PostMapping("/current/activity")
    public ResponseEntity<Void> createActivityEvidence(@Valid @ModelAttribute CreateActivityEvidenceRequest request) {
        evidenceApplicationPort.createActivityEvidence(request.categoryName(), request.title(), request.content(), request.file(), request.imageUrl(), request.activityType(), request.draftId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 독서 증빙자료를 생성합니다.
     * @param request 독서 증빙자료 생성 요청
     * @return 201 CREATED
     */
    @PostMapping("/current/reading")
    public ResponseEntity<Void> createReadingEvidence(@Valid @RequestBody CreateReadingEvidenceRequest request) {
        evidenceApplicationPort.createReadingEvidence(request.title(), request.author(), request.page(), request.content(), request.draftId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 기타 증빙자료를 생성합니다.
     * @param request 기타 증빙자료 생성 요청
     * @return 201 CREATED
     */
    @PostMapping("/current/other")
    public ResponseEntity<Void> createOtherEvidence(@Valid @ModelAttribute CreateOtherEvidenceRequest request) {
        evidenceApplicationPort.createOtherEvidence(request.categoryName(), request.file());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 점수 기반 기타 증빙자료를 생성합니다.
     * @param request 점수 기반 기타 증빙자료 생성 요청
     * @return 201 CREATED
     */
    @PostMapping("/current/scoring")
    public ResponseEntity<Void> createOtherScoringEvidence(@Valid @ModelAttribute CreateOtherScoringEvidenceRequest request) {
        evidenceApplicationPort.createOtherScoringEvidence(request.categoryName(), request.file(), request.value());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 활동 증빙자료를 수정합니다 (계열: 전공).
     * @param evidenceId 수정할 증빙자료 ID
     * @param request 수정 요청 데이터
     * @return 204 No Content
     */
    @PatchMapping("/major/{evidenceId}")
    public ResponseEntity<Void> patchMajorEvidence(
            @PathVariable Long evidenceId,
            @Valid @ModelAttribute PatchActivityEvidenceRequest request) {
        evidenceApplicationPort.updateMajorEvidenceByCurrentUser(evidenceId, request.title(), request.content(), request.file(), request.imageUrl());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 활동 증빙자료를 수정합니다 (계열: 인문).
     * @param evidenceId 수정할 증빙자료 ID
     * @param request 수정 요청 데이터
     * @return 204 No Content
     */
    @PatchMapping("/humanities/{evidenceId}")
    public ResponseEntity<Void> patchHumanityEvidence(
            @PathVariable Long evidenceId,
            @Valid @ModelAttribute PatchActivityEvidenceRequest request) {
        evidenceApplicationPort.updateHumanitiesEvidenceByCurrentUser(evidenceId, request.title(), request.content(), request.file(), request.imageUrl());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 독서 증빙자료를 수정합니다.
     * @param evidenceId 수정할 증빙자료 ID
     * @param request 수정 요청 데이터
     * @return 204 No Content
     */
    @PatchMapping("/reading/{evidenceId}")
    public ResponseEntity<Void> patchReadingEvidence(
            @PathVariable Long evidenceId,
            @Valid @RequestBody PatchReadingEvidenceRequest request) {
        evidenceApplicationPort.updateReadingEvidenceByCurrentUser(evidenceId, request.title(), request.author(), request.content(), request.page());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 기타 증빙자료를 수정합니다.
     * @param evidenceId 수정할 증빙자료 ID
     * @param request 수정 요청 데이터
     * @return 204 No Content
     */
    @PatchMapping("/other/{evidenceId}")
    public ResponseEntity<Void> patchOtherEvidence(
            @PathVariable Long evidenceId,
            @ModelAttribute PatchOtherEvidenceRequest request) {
        evidenceApplicationPort.updateOtherEvidenceByCurrentUser(evidenceId, request.file(), request.imageUrl());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 점수 기반 기타 증빙자료를 수정합니다.
     * @param evidenceId 수정할 증빙자료 ID
     * @param request 수정 요청 데이터
     * @return 204 No Content
     */
    @PatchMapping("/scoring/{evidenceId}")
    public ResponseEntity<Void> patchScoringEvidence(
            @PathVariable Long evidenceId,
            @ModelAttribute PatchOtherScoringEvidenceRequest request) {
        evidenceApplicationPort.updateOtherScoringEvidenceByCurrentUser(evidenceId, request.file(), request.value(), request.imageUrl());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 증빙자료의 상태를 변경합니다.
     * @param evidenceId 증빙자료 ID
     * @param request 상태 변경 요청
     * @return 204 No Content
     */
    @PatchMapping("/{evidenceId}/status")
    public ResponseEntity<Void> patchStatusEvidence(
            @PathVariable Long evidenceId,
            @Valid @RequestBody PatchStatusEvidenceRequest request) {
        evidenceApplicationPort.updateReviewStatus(evidenceId, request.status());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 현재 사용자의 특정 증빙자료를 삭제합니다.
     * @param evidenceId 삭제할 증빙자료 ID
     * @return 204 No Content
     */
    @DeleteMapping("/current/{evidenceId}")
    public ResponseEntity<Void> deleteEvidence(@PathVariable Long evidenceId) {
        evidenceApplicationPort.deleteEvidence(evidenceId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 활동 증빙자료 임시저장을 생성합니다.
     * @param reqeust 임시저장 생성 요청
     * @return 생성된 임시저장 정보
     */
    @PostMapping("/current/draft/activity")
    public ResponseEntity<CreateDraftEvidenceResponse> createDraftActivity(@RequestBody CreateDraftActivityEvidenceReqeust reqeust) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evidenceApplicationPort.createDraftActivityEvidence(
                reqeust.draftId(), reqeust.categoryName(), reqeust.title(), reqeust.content(), reqeust.file(), reqeust.imageUrl(), reqeust.activityType()));
    }

    /**
     * 독서 증빙자료 임시저장을 생성합니다.
     * @param reqeust 임시저장 생성 요청
     * @return 생성된 임시저장 정보
     */
    @PostMapping("/current/draft/reading")
    public ResponseEntity<CreateDraftEvidenceResponse> createDraftReading(@RequestBody CreateReadingEvidenceRequest reqeust) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evidenceApplicationPort.createDraftReadingEvidence(
                reqeust.draftId(), reqeust.title(), reqeust.author(), reqeust.page(), reqeust.content()));
    }

    /**
     * 활동 증빙자료 임시저장을 조회합니다.
     * @param draftId 임시저장 ID
     * @return 임시저장 정보
     */
    @GetMapping("/draft/activity/{draftId}")
    public ResponseEntity<GetDraftActivityEvidenceResponse> getDraftActivity(@PathVariable UUID draftId) {
        return ResponseEntity.status(HttpStatus.OK).body(evidenceApplicationPort.findDraftActivityEvidenceByDraftId(draftId));
    }

    /**
     * 독서 증빙자료 임시저장을 조회합니다.
     * @param draftId 임시저장 ID
     * @return 임시저장 정보
     */
    @GetMapping("/draft/reading/{draftId}")
    public ResponseEntity<GetDraftReadingEvidenceResponse> getDraftReading(@PathVariable UUID draftId) {
        return ResponseEntity.status(HttpStatus.OK).body(evidenceApplicationPort.findDraftReadingEvidenceByDraftId(draftId));
    }

    /**
     * 현재 사용자의 증빙자료 임시저장을 조회합니다.
     * @return 임시저장 목록
     */
    @GetMapping("/current/draft")
    public ResponseEntity<GetDraftEvidenceResponse> getCurrentDraft() {
        return ResponseEntity.status(HttpStatus.OK).body(evidenceApplicationPort.findDraftEvidenceByCurrentUser());
    }
}