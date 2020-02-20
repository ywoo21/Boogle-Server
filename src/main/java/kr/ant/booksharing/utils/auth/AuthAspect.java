package kr.ant.booksharing.utils.auth;

import kr.ant.booksharing.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthAspect {

    private static final Log LOG = LogFactory.getLog(AuthAspect.class);

    private final static String AUTHORIZATION = "Authorization";

    /**
     * 실패 시 기본 반환 Response
     */
    private final static DefaultRes DEFAULT_RES = DefaultRes.res(401, "인증 실패");
    private final static ResponseEntity<DefaultRes> UNAUTHORIZED_RES = new ResponseEntity<>(DEFAULT_RES, HttpStatus.UNAUTHORIZED);

    private final HttpServletRequest httpServletRequest;

    private final UserService userService;

    public AuthAspect(final HttpServletRequest httpServletRequest, final UserService userService) {
        this.httpServletRequest = httpServletRequest;
        this.userService = userService;
    }

    //항상 @annotation 패키지 이름을 실제 사용할 annotation 경로로 맞춰줘야 한다.
    @Around("@annotation(kr.ant.booksharing.utils.auth.Auth)")
    public Object around(final ProceedingJoinPoint pjp) throws Throwable {
        final String jwt = httpServletRequest.getHeader(AUTHORIZATION);
        int userIdx;
        if(jwt.equals("")) userIdx = -1;
        //if (jwt == null) return UNAUTHORIZED_RES;
        userIdx = userService.authorization(jwt);
        if (userIdx == -1) return UNAUTHORIZED_RES;

        httpServletRequest.setAttribute("userIdx", userIdx);

        return pjp.proceed(pjp.getArgs());
    }

    private static class DefaultRes<T> {

        private int status;

        private String message;

        private T data;

        DefaultRes(final int status, final String message, final T t) {
            this.status = status;
            this.message = message;
            this.data = t;
        }

        DefaultRes(final int status, final String message) {
            this(status, message, null);
        }

        static <T> DefaultRes<T> res(final int status, final String message) {
            return res(status, message, null);
        }

        static <T> DefaultRes<T> res(final int status, final String message, final T t) {
            return new DefaultRes<T>(status, message, t);
        }

        public static final DefaultRes FAIL_DEFAULT_RES = new DefaultRes(500, "INTERNAL_SERVER_ERROR");

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }
    }
}