package team.incube.gsmc.v2.global.annotation.port;

import team.incube.gsmc.v2.global.annotation.PortDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 아키텍처 계층에서 포트 역할을 나타내는 사용자 정의 어노테이션입니다.
 * <p>클린 아키텍처 또는 헥사고날 아키텍처에서 포트는 도메인 로직과 어댑터 계층을 연결하는 인터페이스를 나타냅니다.
 * 이 어노테이션은 해당 인터페이스가 INBOUND 또는 OUTBOUND 포트임을 명시하기 위해 사용됩니다.
 * <p>{@link PortDirection} 값을 통해 포트의 방향성을 지정합니다.
 * - INBOUND: 외부 요청(예: Web, Event 등)이 도메인 로직에 진입하는 방향
 * - OUTBOUND: 도메인 로직이 외부 시스템(예: DB, API 등)을 호출하는 방향
 * 이 어노테이션은 주로 인터페이스 선언에 붙여 구조적 명확성과 문서화를 돕습니다.
 * @see PortDirection
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface Port {
    PortDirection direction();
}