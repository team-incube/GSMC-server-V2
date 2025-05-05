package team.incude.gsmc.v2.global.annotation.adapter;

import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.global.annotation.PortDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 애플리케이션 아키텍처에서 어댑터 계층을 나타내는 사용자 정의 어노테이션입니다.
 * <p>클린 아키텍처 또는 헥사고날 아키텍처에서 Adapter는 외부 시스템(Web, Persistence 등)과의 경계를 나타내며,
 * 이 어노테이션을 통해 클래스의 역할을 명시적으로 구분할 수 있습니다.
 * <p>{@link Component}를 포함하므로 Spring Bean으로 자동 등록되며, {@link PortDirection}을 통해 어댑터의 방향(INBOUND/OUTBOUND)을 지정합니다.
 * <p>주로 포트 인터페이스를 구현하는 클래스 또는 외부와 상호작용하는 구현체에 사용됩니다.
 * @see PortDirection
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface Adapter {
    PortDirection direction();
}