package hsw.shop;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hsw.shop.domain.Member;
import hsw.shop.domain.MemberRole;
import hsw.shop.web.dto.MemberCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

	@Component
	@RequiredArgsConstructor
	class InitClass {

		private final InitService initService;

		@PostConstruct
		public void initDB() {
			initService.init();
		}
	}

	@Service
	@Transactional
	class InitService {
		@PersistenceContext
		private EntityManager em;

		public void init() {
			Member user = MemberCreateDto.builder()
					.id("hsw")
					.password("1234")
					.phone("010-1234-5678")
					.name("hsw")
					.email("hsw@test.com")
					.zipcode("서울")
					.address1("어딘가")
					.address2("여기에 살고 있음.")
					.role(MemberRole.USER)
					.build().toEntity();

			Member admin = MemberCreateDto.builder()
					.id("admin")
					.password("1234")
					.phone("010-1234-5678")
					.name("admin")
					.email("admin@test.com")
					.zipcode("서울")
					.address1("어딘가")
					.address2("여기에 살고 있음.")
					.role(MemberRole.ADMIN)
					.build().toEntity();

			em.persist(user);
			em.persist(admin);
		}
	}
}
