package main.filters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

			String jwt = request.getHeader(JwtConst.HEADER);
			if(jwt != null) {
				try {
					jwt  = jwt.substring(7);
					SecretKey key = Keys.hmacShaKeyFor(JwtConst.KEY.getBytes(StandardCharsets.UTF_8));

					Claims claim = Jwts.parserBuilder()
							.setSigningKey(key)
							.build()
							.parseClaimsJws(jwt)
							.getBody();

					String username = String.valueOf(claim.get("username"));
					String authorities = String.valueOf(claim.get("authorities"));

					List<GrantedAuthority> authority = new ArrayList<>();
					authority.add(new SimpleGrantedAuthority(authorities));

					Authentication  auth = new UsernamePasswordAuthenticationToken(username,null, authority);
					SecurityContextHolder.getContext().setAuthentication(auth);
				}catch (Exception ex){
					throw new BadCredentialsException("Invalid token received");
				}
			}
			filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

		String requestUri = request.getRequestURI();
		String method = request.getMethod();

		List<String> excludedPaths = Arrays.asList(
				"/api/slides:GET",
				"/api/users/*:GET",
				"/api/messages:POST" ,
				"/api/categories:GET" ,
				"/api/blogs/newest:GET",
				"/api/blogs/lastThreeImportant:GET",
				"/api/blogs/**:GET",
				"/api/blogs:GET" ,
				"/api/blogs/threeNewest:GET",
				"/api/comments/**:POST" ,
				"/auth/login:POST",
				"/auth/signup:POST" ,
				"/api/tags:GET");


		AntPathMatcher pathMatcher = new AntPathMatcher();

		for (String path : excludedPaths) {
			String[] parts = path.split(":");
			if (pathMatcher.match(parts[0], requestUri) && method.equals(parts[1])){
				return true;
			}
		}

		return false;
	}
	
}
