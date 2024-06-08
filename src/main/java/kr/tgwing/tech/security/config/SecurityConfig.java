package kr.tgwing.tech.security.config;

import kr.tgwing.tech.security.filter.JwtFilter;
import kr.tgwing.tech.security.util.JwtUtil;
import kr.tgwing.tech.security.filter.LoginFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Log4j2
@Configuration
public class SecurityConfig {

    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui/swagger-ui-standalone-preset.js",
            "/swagger-ui/swagger-initializer.js",
            "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-ui/swagger-ui.css",
            "/swagger-ui/index.css",
            "/swagger-ui/favicon-32x32.png",
            "/swagger-ui/favicon-16x16.png",
            "/api-docs/json/swagger-config",
            "/api-docs/json"
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("WebSecurity......................");

        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    private final AuthenticationConfiguration authenticationConfiguration;

    private final JwtUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean // 요청이 들어오면 SecurityFilterChain이 가로채서 인증, 인가를 체크함.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable) // 토큰 사용하기에 csrf 불가능
                .cors(AbstractHttpConfigurer::disable)

                // .formLogin(Customizer.withDefaults())// -> 로그인 화면 구성되면 사용해야함.
                // .logout((logout) -> logout
                // .clearAuthentication(true))

                .authorizeHttpRequests(request -> request
                        .requestMatchers(PERMIT_URL_ARRAY)
                        .permitAll()
                        .requestMatchers("/user/**", "/login")
                        .permitAll()
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/file/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/blog/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/projects/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    // @Bean
    // @ConditionalOnMissingBean(UserDetailsService.class)
    // InMemoryUserDetailsManager inMemoryUserDetailsManager() {
    // User.UserBuilder users = User.withDefaultPasswordEncoder();
    // UserDetails admin = users
    // .username("admin")
    // .password("admin")
    // .roles("ADMIN")
    // .build();
    // return new InMemoryUserDetailsManager(admin);
    // }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    } // 생성자 선언해둬야함... 해야되나?
}
