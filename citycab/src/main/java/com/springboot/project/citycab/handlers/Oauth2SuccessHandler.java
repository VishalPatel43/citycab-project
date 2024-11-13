//package com.springboot.project.citycab.handlers;
//
//import com.springboot.coding.securityApplication.entities.User;
//import com.springboot.coding.securityApplication.services.JWTService;
//import com.springboot.coding.securityApplication.services.UserService;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Set;
//
//import static com.springboot.coding.securityApplication.entities.enums.Role.USER;
//import static com.springboot.coding.securityApplication.entities.enums.SubscriptionPlan.FREE;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final UserService userService;
//    private final JWTService jwtService;
//
//    @Value("${deploy.env}")
//    private String deployEnv;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Authentication authentication) throws IOException, ServletException {
//
//        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
//        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();
//
//        String email = oAuth2User.getAttribute("email");
//        String name = oAuth2User.getAttribute("name");
////        String name = oAuth2User.getAttribute("given_name");
//        log.info("OAuth2User: {}", oAuth2User);
//        log.info("Email: {}", email);
//        log.info("Name: {}", name);
//
//        User user = userService.getUserByEmail(email);
//
//        if (user == null)
//            user = createUser(email, name);
//
//        String accessToken = jwtService.generateAccessToken(user);
//        String refreshToken = jwtService.generateRefreshToken(user);
//
//        setRefreshTokenCookie(response, refreshToken);
//
//        String frontEndUrl = "http://localhost:8080/api/home.html?token=" + accessToken;
//        log.info("FrontEndUrl: {}", frontEndUrl);
//        response.sendRedirect(frontEndUrl);
////        getRedirectStrategy().sendRedirect(request, response, frontEndUrl);
//    }
//
//    private User createUser(String email, String name) {
//        return userService.save(User.builder()
//                .name(name)
//                .email(email)
//                .roles(Set.of(USER))
//                .subscriptionPlan(FREE)
//                .build());
//    }
//
//    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
//        Cookie cookie = new Cookie("refreshToken", refreshToken);
//        cookie.setHttpOnly(true);
//        cookie.setSecure("production".equals(deployEnv));
//        cookie.setPath("/");
//        response.addCookie(cookie);
//    }
//}
