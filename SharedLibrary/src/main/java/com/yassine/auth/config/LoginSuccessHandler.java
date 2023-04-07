package com.yassine.auth.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  protected String determineTargetUrl(Authentication authentication) {
    String url = "/login?error=true";
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    List<String> roles = new ArrayList<>();
    for (GrantedAuthority authority : authorities)
      roles.add(authority.getAuthority());
    if (roles.contains("ADMIN"))
      url = "/admin/dashboard";
    else if (roles.contains("USER"))
      url = "/index";
    return url;
  }

  @Override
  protected void handle(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    String targetUrl = determineTargetUrl(authentication);
    if (response.isCommitted())
      return;
    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    redirectStrategy.sendRedirect(request, response, targetUrl);
  }
}
