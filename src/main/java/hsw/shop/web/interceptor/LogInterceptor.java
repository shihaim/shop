package hsw.shop.web.interceptor;

import hsw.shop.domain.Member;
import hsw.shop.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        Long memberId = getMemberId(request);

        request.setAttribute(LOG_ID, memberId);

        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            log.info("requestURI={}, handlerMethod={}", requestURI, hm);
        }

        log.info("REQUEST [{}][{}][{}]", memberId, requestURI, handler);


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandler [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        Long memberId = getMemberId(request);

        log.info("REQUEST [{}][{}][{}]", memberId, requestURI, handler);

        if (ex != null) {
            log.error("afterCompletion error", ex);
        }

    }

    private Long getMemberId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return -1L;
        }
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return member.getMemberId();
    }
}
