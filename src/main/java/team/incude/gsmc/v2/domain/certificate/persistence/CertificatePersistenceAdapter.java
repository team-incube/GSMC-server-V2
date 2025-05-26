package team.incude.gsmc.v2.domain.certificate.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.domain.certificate.exception.CertificateNotFoundException;
import team.incude.gsmc.v2.domain.certificate.persistence.mapper.CertificateMapper;
import team.incude.gsmc.v2.domain.certificate.persistence.projection.CertificateProjection;
import team.incude.gsmc.v2.domain.certificate.persistence.repository.CertificateJpaRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.Optional;

import static team.incude.gsmc.v2.domain.certificate.persistence.entity.QCertificateJpaEntity.certificateJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;
import static team.incude.gsmc.v2.domain.member.persistence.entity.QStudentDetailJpaEntity.studentDetailJpaEntity;

/**
 * 자격증 도메인의 영속성 어댑터 클래스입니다.
 * <p>{@link CertificatePersistencePort}의 구현체로, DB 접근 로직을 포함하며 JPA와 QueryDSL을 활용해 자격증 정보를 조회·저장·삭제합니다.
 * 도메인 객체와 JPA 엔티티 간 변환을 위해 {@link CertificateMapper}를 사용합니다.
 * <p>{@code @Adapter(direction = PortDirection.OUTBOUND)}로 정의되어 있으며,
 * 도메인 계층에서 저장소에 접근할 수 있도록 연결해주는 역할을 합니다.
 * 주요 책임:
 * <ul>
 *   <li>비관적 락을 사용한 자격증 단건 조회</li>
 *   <li>학생 코드 기반 자격증 목록 조회</li>
 *   <li>자격증 저장 및 삭제</li>
 * </ul>
 * @author snowykte0426
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class CertificatePersistenceAdapter implements CertificatePersistencePort {

    private final CertificateJpaRepository certificateJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final CertificateMapper certificateMapper;


    /**
     * 주어진 회원 ID와 자격증 이름을 기준으로 자격증이 존재하는지 확인합니다.
     * @param memberId 회원의 고유 ID
     * @param name 자격증 이름
     * @return 해당 회원이 소유한 자격증이 존재하면 true, 그렇지 않으면 false
     */
    @Override
    public Boolean existsByMemberIdAndName(Long memberId, String name) {
        return jpaQueryFactory
                .selectOne()
                .from(certificateJpaEntity)
                .where(certificateJpaEntity.member.id.eq(memberId)
                        .and(certificateJpaEntity.name.eq(name)))
                .fetchFirst() != null;
    }

    /**
     * 비관적 락(PESSIMISTIC_WRITE)을 걸고 ID에 해당하는 자격증을 조회하고 비관적 락을 설정합니다.
     * @param id 자격증의 고유 ID
     * @return 조회된 자격증 도메인 객체
     * @throws CertificateNotFoundException 조회 실패 시 발생
     */
    @Override
    public Certificate findCertificateByIdWithLock(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(certificateJpaEntity)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .where(certificateJpaEntity.id.eq(id))
                        .fetchOne()
        ).map(certificateMapper::toDomain).orElseThrow(CertificateNotFoundException::new);
    }

    /**
     * 학생 코드에 해당하는 자격증 목록을 조회합니다.
     * @param studentCode 학생 고유 코드
     * @return 자격증 도메인 객체 리스트
     */
    @Override
    public List<Certificate> findCertificateByStudentDetailStudentCode(String studentCode) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        CertificateProjection.class,
                        certificateJpaEntity.id,
                        certificateJpaEntity.name,
                        certificateJpaEntity.acquisitionDate,
                        certificateJpaEntity.evidence.fileUri
                ))
                .from(certificateJpaEntity)
                .join(certificateJpaEntity.member, memberJpaEntity)
                .join(studentDetailJpaEntity)
                .on(studentDetailJpaEntity.member.id.eq(memberJpaEntity.id))
                .where(studentDetailJpaEntity.studentCode.eq(studentCode))
                .fetch()
                .stream()
                .map(certificateMapper::fromProjection)
                .toList();
    }

    @Override
    public List<Certificate> findCertificateByMemberIdWithLock(Long memberId) {
        return jpaQueryFactory
                .selectFrom(certificateJpaEntity)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .where(certificateJpaEntity.member.id.eq(memberId))
                .fetch()
                .stream()
                .map(certificateMapper::toDomain)
                .toList();
    }

    /**
     * 자격증 도메인 객체를 저장합니다.
     * @param certificate 저장할 자격증 도메인 객체
     * @return 저장된 자격증 도메인 객체
     */
    @Override
    public Certificate saveCertificate(Certificate certificate) {
        return certificateMapper.toDomain(certificateJpaRepository.save(certificateMapper.toEntity(certificate)));
    }

    /**
     * ID에 해당하는 자격증을 삭제합니다.
     * @param id 삭제할 자격증 ID
     */
    @Override
    public void deleteCertificateById(Long id) {
        certificateJpaRepository.deleteById(id);
    }
}