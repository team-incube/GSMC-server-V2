package team.incude.gsmc.v2.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.incude.gsmc.v2.global.filter.logging.HttpLoggingFilter;

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