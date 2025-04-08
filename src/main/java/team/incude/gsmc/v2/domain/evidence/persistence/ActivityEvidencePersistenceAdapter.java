package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.ActivityEvidenceJpaEntity;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.ActivityEvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.EvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.ActivityEvidenceJpaRepository;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;

import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QActivityEvidenceJpaEntity.activityEvidenceJpaEntity;
import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class ActivityEvidencePersistenceAdapter implements ActivityEvidencePersistencePort {

    private final ActivityEvidenceJpaRepository activityEvidenceJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ActivityEvidenceMapper activityEvidenceMapper;

    @Override
    public List<ActivityEvidence> findMajorEvidenceByMember(Member member) {
        return List.of();
    }

    @Override
    public List<ActivityEvidence> findHumanitiesEvidenceByMember(Member member) {
        return List.of();
    }

    @Override
    public ActivityEvidence findActivityEvidenceByEvidenceIdAndEvidenceType(Long evidenceId, EvidenceType evidenceType) {
        ActivityEvidenceJpaEntity jpaEntity = jpaQueryFactory
                .selectFrom(activityEvidenceJpaEntity)
                .leftJoin(activityEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .where(
                        evidenceJpaEntity.id.eq(evidenceId),
                        evidenceJpaEntity.evidenceType.eq(evidenceType)
                )
                .fetchOne();

        return activityEvidenceMapper.toDomain(jpaEntity);
    }

    @Override
    public void saveActivityEvidence(ActivityEvidence activityEvidence) {
        activityEvidenceJpaRepository.save(activityEvidenceMapper.toEntity(activityEvidence));
    }

    @Override
    public void deleteActivityEvidenceByEvidenceId(Long evidenceId) {
        jpaQueryFactory
                .delete(activityEvidenceJpaEntity)
                .where(activityEvidenceJpaEntity.evidence.id.eq(evidenceId))
                .execute();
    }
}
