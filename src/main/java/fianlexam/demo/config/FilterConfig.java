package fianlexam.demo.config;

import fianlexam.demo.filter.UserLoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;

/**
 * @author Brandon.
 * @date 2019/6/1.
 * @time 21:12.
 */

@Configuration
public class FilterConfig {
    @Bean
    public Filter UserLoginFilter(){
        return new UserLoginFilter();
    }
    @Bean
    public FilterRegistrationBean IdentityRegistration(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new DelegatingFilterProxy("UserLoginFilter"));
        filterRegistrationBean.addUrlPatterns("/play","/room","/createRoom","/checkRoom","/ready","/clear","/regret");
        filterRegistrationBean.setName("UserLoginFilter");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
