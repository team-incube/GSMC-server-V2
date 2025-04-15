package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.exception.ActivityEvidenceNotFoundException;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.ActivityEvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.ActivityEvidenceJpaRepository;
import team.incude.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QActivityEvidenceJpaEntity.activityEvidenceJpaEntity;
import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QCategoryJpaEntity.categoryJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class ActivityEvidencePersistenceAdapter implements ActivityEvidencePersistencePort {

    private final ActivityEvidenceJpaRepository activityEvidenceJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ActivityEvidenceMapper activityEvidenceMapper;
    private final MemberMapper memberMapper;

    @Override
    public List<ActivityEvidence> findActivityEvidenceByEmailAndEvidenceType(String email, EvidenceType evidenceType) {
        return jpaQueryFactory
                .selectFrom(activityEvidenceJpaEntity)
                .leftJoin(activityEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .leftJoin(evidenceJpaEntity.score, scoreJpaEntity).fetchJoin()
                .leftJoin(scoreJpaEntity.member, memberJpaEntity).fetchJoin()
                .leftJoin(scoreJpaEntity.category, categoryJpaEntity).fetchJoin()
                .where(
                        memberEmailEq(email),
                        evidenceTypeEq(evidenceType)
                )
                .fetch()
                .stream()
                .map(activityEvidenceMapper::toDomain)
                .toList();
    }

    @Override
    public ActivityEvidence findActivityEvidenceById(Long id) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(activityEvidenceJpaEntity)
                .leftJoin(activityEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .where(evidenceJpaEntity.id.eq(id))
                .fetchOne()
        ).map(activityEvidenceMapper::toDomain).orElseThrow(ActivityEvidenceNotFoundException::new);
    }

    @Override
    public ActivityEvidence saveActivityEvidence(ActivityEvidence activityEvidence) {
        return activityEvidenceMapper.toDomain(activityEvidenceJpaRepository.save(activityEvidenceMapper.toEntity(activityEvidence)));
    }

    @Override
    public void deleteActivityEvidenceById(Long evidenceId) {
        long deletedCount = jpaQueryFactory
                .delete(activityEvidenceJpaEntity)
                .where(activityEvidenceJpaEntity.id.eq(evidenceId))
                .execute();

        if (deletedCount == 0) {
            throw new ActivityEvidenceNotFoundException();
        }
    }

    @Override
    public Boolean existsActivityEvidenceByEvidenceId(Long evidenceId) {
        Integer result = jpaQueryFactory
                .selectOne()
                .from(activityEvidenceJpaEntity)
                .where(activityEvidenceJpaEntity.id.eq(evidenceId))
                .fetchFirst();

        return result != null;
    }

    private BooleanExpression memberEmailEq(String email) {
        if (email == null) return null;
        return memberJpaEntity.email.eq(email);
    }

    private BooleanExpression evidenceTypeEq(EvidenceType evidenceType) {
        if (evidenceType == null) return null;
        return evidenceJpaEntity.evidenceType.eq(evidenceType);
    }
}