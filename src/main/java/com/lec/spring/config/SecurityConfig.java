package com.lec.spring.config;

import com.lec.spring.config.oauth.PrincipalOauth2UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

//@Configuration
//@EnableWebSecurity
//@Slf4j
//public class SecurityConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    private PrincipalOauth2UserService principalOauth2UserService;
//
//    @Autowired
//    public void setPrincipalOauth2UserService(PrincipalOauth2UserService principalOauth2UserService) {
//        this.principalOauth2UserService = principalOauth2UserService;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(csrf -> csrf.disable())
//                .addFilterBefore(new MemberAccessFilter(), FilterSecurityInterceptor.class)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/",
//                                "/member/sendEmail",
//                                "/member/verify-code",
//                                "/home",
//                                "/member/login",
//                                "/member/register",
//                                "/member/additional-info",
//                                "/member/password-recovery",
//                                "/matzips/**",
//                                "/member/**",
//                                "/member/password-recovery/**"
//                        ).permitAll()
//
//                        .requestMatchers(
//                                "/matzips/food-kinds/**",
//                                "/matzips/tags/**",
//                                "/admin/**"
//                        ).hasRole("ADMIN")
//
//                        .requestMatchers(
//
//                                "/matzips/*/visibility",
//                                "/matzips/reviews/**",
//                                "/matzip/reviewList/**",
//                                "/matzips/hint-tags/**",
//                                "/matzips/wish-list/**",
//                                "/member/*/friends/**",
//                                "/member/*/matzips/**",
//                                "/member/*/nickname",
//                                "/member/*/profile-img",
//                                "/member/*/wish-list/**"
//                        ).hasAnyRole("MEMBER", "ADMIN")
//
//                        .requestMatchers(
//                                "/members/{id}",
//                                "/matzips/**",
//                                "/member/**",
//                                "/member/*/friends",
//                                "/matzips/reviews",
//                                "/matzips/hints/**",
//                                "/member/additional-info",
//                                "/matzips/matzipsDetail/**"
//                        ).authenticated()
//                        .anyRequest().permitAll()
//
//                )
//                .formLogin(form -> form
//                        .loginPage("/member/login")
//                        .loginProcessingUrl("/member/login")
//                        .defaultSuccessUrl("/")
//                        .successHandler(new CustomLoginSuccessHandler("/home"))
//                        .failureHandler(new CustomLoginFailureHandler())
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/member/login")
//                        .successHandler(new CustomLoginSuccessHandler("/home"))
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userService(principalOauth2UserService)
//                        )
//                )
//                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
//                        .logoutUrl("/member/logout")
//                        .invalidateHttpSession(false)
//                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
//                )
//                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
//                        .accessDeniedHandler(new CustomAccessDeniedHandler())
//                )
//                .build();
//    }
//}

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    private PrincipalOauth2UserService principalOauth2UserService;

    @Autowired
    public void setPrincipalOauth2UserService(PrincipalOauth2UserService principalOauth2UserService) {
        this.principalOauth2UserService = principalOauth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(new MemberAccessFilter(), FilterSecurityInterceptor.class)
                .authorizeHttpRequests(auth -> auth
                        // 테스트를 위해 모든 요청 허용
                        .requestMatchers("/**").permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/member/login")
                        .loginProcessingUrl("/member/login")
                        .defaultSuccessUrl("/")
                        .successHandler(new CustomLoginSuccessHandler("/home"))
                        .failureHandler(new CustomLoginFailureHandler())
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/member/login")
                        .successHandler(new CustomLoginSuccessHandler("/home"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(principalOauth2UserService)
                        )
                )
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutUrl("/member/logout")
                        .invalidateHttpSession(false)
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                )
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )
                .build();
    }
}
