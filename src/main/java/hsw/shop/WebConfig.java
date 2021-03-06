package hsw.shop;

import hsw.shop.web.argumentresolver.LoginMemberArgumentResolver;
import hsw.shop.web.interceptor.AdminCheckInterceptor;
import hsw.shop.web.interceptor.LogInterceptor;
import hsw.shop.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //로그
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/images/**", "/css/**", "/js/**", "/*.ico", "/error");

        //로그인을 하지 않을 경우 해당 경로만 이동 가능
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/api/v1/**",
                        "/", "/search",
                        "/member/sign-up", "/member/sign-in",
                        "/products/*",
                        "/images/**", "/css/**", "/js/**", "/image/**", "/*.ico", "/error");

        //ADMIN일 경우 상품 등록 가능
        registry.addInterceptor(new AdminCheckInterceptor())
                .order(3)
                .addPathPatterns("/product/**");
    }
}
