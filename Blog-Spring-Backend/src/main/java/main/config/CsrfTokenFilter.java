package main.config;

import java.io.IOException;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CsrfTokenFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {


		CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		if(csrf.getHeaderName() != null) {
			 response.setHeader(csrf.getHeaderName(), csrf.getToken());
		}
		
		filterChain.doFilter(request, response);
	}

}
