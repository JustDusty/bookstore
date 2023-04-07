package com.yassine.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.yassine.auth.service.OAuth2UserServiceImpl;
import com.yassine.auth.service.UserServiceImpl;

@Configuration

public class WebSecurityConfig {

  @Autowired
  private LoginSuccessHandler loginSuccessHandler;

  @Autowired
  private OAuth2UserServiceImpl oauthUserService;


  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }



  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authenticationProvider(authenticationProvider());


    http.authorizeHttpRequests()
        // URL matching for accessibility
        .requestMatchers("/", "/login", "/register/**", "/oauth2/**", "/forgotPassword",
            "/forgotPassword/**", "/index", "/index/**", "/shop/**", "/shop", "/contact")
        .permitAll().requestMatchers("/cart/**", "checkout/**").hasAnyAuthority("USER")
        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN").requestMatchers("/**")
        .hasAnyAuthority("USER", "ADMIN").anyRequest().authenticated().and()
        .oauth2Login(login -> login.loginPage("/login").defaultSuccessUrl("/").userInfoEndpoint()
            .userService(oauthUserService));

    http.formLogin(login -> login.loginPage("/login").failureUrl("/login?error=true")
        .successHandler(loginSuccessHandler).usernameParameter("email")
        .passwordParameter("password"))
        // logout
        .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/index").invalidateHttpSession(true).deleteCookies("JSESSIONID"))
        .exceptionHandling(handling -> handling.accessDeniedPage("/access-denied"));
    //


    // http.sessionManagement(management -> management.sessionFixation().migrateSession()
    // .maximumSessions(1).maxSessionsPreventsLogin(false).expiredUrl("/login?expired")
    // .sessionRegistry(sessionRegistry()).and().invalidSessionUrl("/login"));

    http.csrf().disable();
    // http.requiresChannel().anyRequest().requiresSecure();


    http.headers(headers -> headers.frameOptions().sameOrigin());


    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserServiceImpl();
  }



  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers("/images/**", "/js/**", "webjars/**", "/css/**",
        "/shopp/**", "/img/**", "/fonts/**", "/placeholder/**");
  }

}
