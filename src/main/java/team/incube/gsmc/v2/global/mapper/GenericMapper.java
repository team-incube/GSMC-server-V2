package team.incube.gsmc.v2.global.mapper;

/**
 * 도메인 객체와 JPA 엔티티 간의 양방향 매핑을 정의하는 제네릭 인터페이스입니다.
 * <p>이 인터페이스는 매퍼 구현체에서 공통적으로 사용하는 구조를 추상화하며,
 * 도메인 모델과 영속성 계층의 엔티티 간 변환 로직을 표준화합니다.
 * @param <ENTITY> 변환 대상이 되는 JPA 엔티티 타입
 * @param <DOMAIN> 변환 대상이 되는 도메인 객체 타입
 * @author snowykte0426
 */
public interface GenericMapper<ENTITY, DOMAIN> {
    ENTITY toEntity(DOMAIN domain);

    DOMAIN toDomain(ENTITY entity);
}