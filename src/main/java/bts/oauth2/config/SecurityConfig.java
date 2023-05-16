package bts.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

//        http
//            .csrf().disable()
//
//            .exceptionHandling()
//            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//            .accessDeniedHandler(jwtAccessDeniedHandler)
//
//            .and()
//            .headers()
//            .frameOptions()
//            .sameOrigin()
//
//            .and()
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//            .and()
//            .authenticationProvider((req)->
//                req
//                    .requestMatchers("/user/authenticate", "/user/signup").permitAll()
//                    .requestMatchers("/oauth","oauth/kakao",".oauth/naver/**").permitAll()
//                    .anyRequest().authenticated()
//            )
//            .apply(new JwtSecurityConfig(tokenProvider));


//        http.formLogin().disable();
//        return http.build();

        http
            .formLogin().disable()


            .cors().and().csrf().disable()
            .authorizeRequests()
            .anyRequest().permitAll();

        return http.build();

//        http
//            .cors()
//            .and()
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않음
//            .and()
//            .csrf().disable() // csrf 미사용
//            .headers().frameOptions().disable()
//            .and()
//            .formLogin().disable() // 로그인 폼 미사용
//            .httpBasic().disable() // Http basic Auth 기반으로 로그인 인증창이 열림(disable 시 인증창 열리지 않음)
//            .exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint()) // 인증,인가가 되지 않은 요청 시 발생
//            .and()
//            .authorizeRequests()
//            .antMatchers("/auth/**", "/oauth2/**").permitAll() // Security 허용 Url
//            .anyRequest().permitAll() // 그 외엔 모두 인증 필요
//            .and()
//            .oauth2Login()
//            .authorizationEndpoint().baseUri("/oauth2/authorization") // 소셜 로그인 Url
//            .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository()) // 인증 요청을 쿠키에 저장하고 검색
//            .and()
//            .redirectionEndpoint().baseUri("/10.10.10.197:8080/auth/*") // 소셜 인증 후 Redirect Url
//            .and()
//            .userInfoEndpoint().userService(customOAuth2UserService) // 소셜의 회원 정보를 받아와 가공처리
//            .and()
//            .successHandler(oAuth2AuthenticationSuccessHandler) // 인증 성공 시 Handler
//            .failureHandler(oAuth2AuthenticationFailureHandler); // 인증 실패 시 Handler
//
//        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
