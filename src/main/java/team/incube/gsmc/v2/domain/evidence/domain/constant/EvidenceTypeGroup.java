package team.incube.gsmc.v2.domain.evidence.domain.constant;

import java.util.Set;

public class EvidenceTypeGroup {

    private EvidenceTypeGroup() {}

    public static final Set<EvidenceType> OTHER_TYPES = Set.of(
            EvidenceType.FOREIGN_LANGUAGE,
            EvidenceType.CERTIFICATE,
            EvidenceType.TOPCIT,
            EvidenceType.READ_A_THON,
            EvidenceType.TOEIC,
            EvidenceType.TOEFL,
            EvidenceType.TEPS,
            EvidenceType.TOEIC_SPEAKING,
            EvidenceType.OPIC,
            EvidenceType.JPT,
            EvidenceType.CPT,
            EvidenceType.HSK
    );
}
