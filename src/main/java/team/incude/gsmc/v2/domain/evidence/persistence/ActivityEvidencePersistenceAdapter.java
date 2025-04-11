package team.incude.gsmc.v2.domain.evidence.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.ActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.exception.ActivityEvidenceNotFountException;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.ActivityEvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.ActivityEvidenceJpaRepository;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;

import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QActivityEvidenceJpaEntity.activityEvidenceJpaEntity;
import static team.incude.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;
import static team.incude.gsmc.v2.domain.score.persistence.entity.QScoreJpaEntity.scoreJpaEntity;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class ActivityEvidencePersistenceAdapter implements ActivityEvidencePersistencePort {

    private final ActivityEvidenceJpaRepository activityEvidenceJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ActivityEvidenceMapper activityEvidenceMapper;
    private final MemberMapper memberMapper;

    @Override
    public List<ActivityEvidence> findActivityEvidenceByMemberAndEvidenceType(Member member, EvidenceType evidenceType) {
        return jpaQueryFactory
                .selectFrom(activityEvidenceJpaEntity)
                .leftJoin(activityEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .leftJoin(evidenceJpaEntity.score, scoreJpaEntity).fetchJoin()
                .where(
                        scoreJpaEntity.member.eq(memberMapper.toEntity(member)),
                        evidenceJpaEntity.evidenceType.eq(evidenceType)
                )
                .fetch()
                .stream()
                .map(activityEvidenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<ActivityEvidence> findActivityEvidenceByMemberAndTypeAndTitle(Member member, EvidenceType evidenceType, String title) {
        return jpaQueryFactory
                .selectFrom(activityEvidenceJpaEntity)
                .leftJoin(activityEvidenceJpaEntity.evidence, evidenceJpaEntity).fetchJoin()
                .leftJoin(evidenceJpaEntity.score, scoreJpaEntity).fetchJoin()
                .where(
                        memberEq(member),
                        evidenceTypeEq(evidenceType),
                        titleEq(title)
                )
                .fetch()
                .stream()
                .map(activityEvidenceMapper::toDomain)
                .toList();
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
            throw new ActivityEvidenceNotFountException();
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

    private BooleanExpression memberEq(Member member) {
        if (member == null) return null;
        return scoreJpaEntity.member.eq(memberMapper.toEntity(member));
    }

    private BooleanExpression evidenceTypeEq(EvidenceType evidenceType) {
        if (evidenceType == null) return null;
        return evidenceJpaEntity.evidenceType.eq(evidenceType);
    }

    private BooleanExpression titleEq(String title) {
        if (title == null || title.isBlank()) return null;
        return activityEvidenceJpaEntity.title.eq(title);
    }


}
