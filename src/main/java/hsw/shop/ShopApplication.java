package hsw.shop;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.servlet.SessionTrackingMode;
import java.util.Collections;

@EnableJpaAuditing
@SpringBootApplication
public class ShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}

	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}

	/**
	 * https://velog.io/@k2hyun4/jsessionid-404-error
	 * 첫 로그인시 url/jsessionid;~~ 문제 해결 방법
	 */
	@Bean
	public ServletContextInitializer configSession() {
		return servletContext -> {
			servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));

			servletContext.getSessionCookieConfig().setHttpOnly(true);
		};
	}
}
