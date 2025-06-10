package team.incude.gsmc.v2.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.incude.gsmc.v2.global.filter.logging.HttpLoggingFilter;

/**
 * HTTP 요청 및 응답 로깅을 위한 필터 설정 클래스입니다.
 * <p>이 클래스는 {@link HttpLoggingFilter}를 등록하여 모든 HTTP 요청과 응답을 로깅합니다.
 * <p>필터는 최우선 순위로 설정되어, 다른 필터보다 먼저 실행됩니다.
 * <p>모든 URL 패턴에 대해 적용되며, 개발 및 디버깅 목적으로 유용합니다.
 * @author jihoonwjj
 */
@Configuration
public class WebFilterConfig {

    @Bean
    public FilterRegistrationBean<HttpLoggingFilter> loggingFilter() {
        FilterRegistrationBean<HttpLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HttpLoggingFilter());
        registrationBean.addUrlPatterns("/*");  // 모든 URL에 적용
        registrationBean.setOrder(Integer.MIN_VALUE);  // 최우선 순위
        registrationBean.setName("httpLoggingFilter");
        return registrationBean;
    }
}