package kr.tgwing.tech.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // request를 filterchain에서 가로채도록 하는 장치
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean // 요청이 들어오면 SecurityFilterChain이 가로채서 인증, 인가를 체크함.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable) // 토큰 사용하기에 csrf 불가능
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
//                    request.requestMatchers("/tgwing.kr", "/tgwing.kr/register", "/tgwing.kr/login").permitAll();
                    request.requestMatchers("/**").permitAll();
                    // 3개의 url에서는 token인증없이 접근 가능.
                    request.anyRequest().authenticated();
                    // 그 외의 url에서는 token인증없이 접근 불가능.
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // jwt를 사용하기 때문에 session을 사용하지 않음.
//                .addFilterBefore(new JwtFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    } // 생성자 선언해둬야함... 해야되나?

}