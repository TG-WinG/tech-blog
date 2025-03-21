package kr.tgwing.tech.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.tgwing.tech.security.service.CustomUserDetails;
import kr.tgwing.tech.security.service.JwtBlackListService;
import kr.tgwing.tech.security.util.JwtUtil;
import kr.tgwing.tech.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("token is null");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("authorization now...");

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            log.info("token is useless...");
            filterChain.doFilter(request, response);
            return;
        }

        //토큰에서 username과 role 획득
        String studentId = jwtUtil.getStudentId(token);
        String role = jwtUtil.getRole(token);

        //userEntity를 생성하여 값 set
        User userEntity = new User();
        userEntity.setStudentNumber(studentId);
        userEntity.setPassword("temppassword");
        userEntity.setRole(role);

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        if ("/api/validate".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
            log.info("유효성 검사 토큰 요청 확인");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("token validation result: success!");
            return;
        }

        //스프링 시큐리티 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
