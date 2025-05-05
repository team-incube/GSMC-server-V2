package team.incude.gsmc.v2.global.annotation;

/**
 * 포트의 방향을 나타내는 열거형입니다.
 * <p>클린 아키텍처 또는 헥사고날 아키텍처에서 포트는 시스템의 내부(도메인)와 외부(어댑터)를 연결하는 역할을 하며,
 * 이 열거형은 포트가 INBOUND인지 OUTBOUND인지를 명시적으로 구분하는 데 사용됩니다.
 * <ul>
 *   <li>{@code INBOUND} - 외부 계층(Web, 이벤트 등)에서 도메인 로직으로 진입하는 방향</li>
 *   <li>{@code OUTBOUND} - 도메인 로직이 외부 시스템(DB, API 등)으로 나가는 방향</li>
 * </ul>
 * 이 값은 {@link team.incude.gsmc.v2.global.annotation.port.Port}, {@link team.incude.gsmc.v2.global.annotation.adapter.Adapter}와 같은 어노테이션에서 사용되어
 * 포트의 방향성을 명시합니다.
 */
public enum PortDirection {
    INBOUND,
    OUTBOUND
}