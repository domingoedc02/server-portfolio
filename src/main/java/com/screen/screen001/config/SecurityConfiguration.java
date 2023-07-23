package com.screen.screen001.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;


  // @Bean
  // public UserDetailsService userDetailsService() {
  //   UserDetails user = User
  // }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
        
        .authorizeHttpRequests( (request) -> request
          .requestMatchers(
                "/api/v1/auth/authentication", "/api/v1/auth/register", "/screen001/delete/**", "/screen001/forgotpassword", "/screen001/resetpassword"
          ).permitAll()
          // .requestMatchers("/screen001/menu").hasAuthority("ROLE_USER")
          .anyRequest().authenticated()
        )
        // .and()
        //   .sessionManagement()
        //   .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        // .and()
        .formLogin((form) -> form
            .loginPage("/screen001/login")
            .loginProcessingUrl("/api/v1/auth/authenticate")
            // .successHandler(successHandler)
            .defaultSuccessUrl("/screen001/menu", true)
            .failureUrl("/screen001/login?error=true")
            .usernameParameter("memberId")
            .passwordParameter("password")
            .permitAll()
        )
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .logout()
        .logoutUrl("/api/v1/auth/logout")
        .logoutSuccessUrl("/screen001/login?logout")
        // .addLogoutHandler(logoutHandler)
        // .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
    ;

    

    return http.build();
  }
}
