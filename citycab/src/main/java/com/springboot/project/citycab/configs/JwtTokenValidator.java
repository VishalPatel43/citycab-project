//package com.springboot.project.citycab.configs;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.crypto.SecretKey;
//import java.io.IOException;
//import java.util.List;
//
//public class JwtTokenValidator extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//        // Using this we get the jwt token from the header
//        String jwt = request.getHeader(JwtConstant.JWT_HEADER); // Authorization --> User pass (header) token here
//
//        if (jwt != null) {
//            // Bearer token --> so we remove the first 7 characters from the bearer token
//            jwt = jwt.substring(7);
//
//            try {
//
//                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
//
//                Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
//
//                String email = String.valueOf(claims.get("email")); // which we send in the LoginRequest
//
////                String phoneNo = String.valueOf(claims.get("phoneNo")); // null coz we send the email as phoneNo and password
//
//                // We get all the authorities from the token --> ROLE_USER, ROLE_ADMIN in one string
//                String authorities = String.valueOf(claims.get("authorities"));
//
//                System.out.println("Authorities: -------- " + authorities);
//                System.out.println("Email: -------- " + email);
////                System.out.println("PhoneNo: -------- " + phoneNo);
//
//                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
//                Authentication authentication = new UsernamePasswordAuthenticationToken(
//                        email, null, auths);
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            } catch (Exception e) {
//                throw new BadCredentialsException("invalid token...");
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
