package team.incude.gsmc.v2.domain.evidence.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidenceApplicationPort;

@RestController
@RequestMapping("/api/v2/evidence")
@RequiredArgsConstructor
public class EvidenceWebAdapter {

    private final EvidenceApplicationPort evidenceApplicationPort;

    @Get
}