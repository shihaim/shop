package hsw.shop.web.interceptor;

import hsw.shop.domain.Member;
import hsw.shop.domain.MemberRole;
import hsw.shop.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        Member sessionAttribute = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (sessionAttribute == null || sessionAttribute.getRole().equals(MemberRole.USER)) {
            log.info("어드민 계정이 아닌 유저 요청");
            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}
