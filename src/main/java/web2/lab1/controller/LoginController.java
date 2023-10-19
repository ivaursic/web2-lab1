package web2.lab1.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import web2.lab1.service.UserAccountService;

import java.security.Principal;
import java.util.Map;

//@Controller
//public class LoginController { //ne treba mi??
//
//    @Autowired
//    private UserAccountService accountService;
//
//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
//
//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        return "redirect:/login?logout";
//    }
//
//    @GetMapping("/user-info")
//    public String getUserInfo(Principal principal) {
//        // 'principal' contains user information after successful authentication
//        if (principal != null) {
//            // Access user details using Principal object
//            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
//            OAuth2User oauth2User = oauthToken.getPrincipal();
//            Map<String, Object> userDetails = oauth2User.getAttributes();
//            // Extract user information from 'userDetails' map
//            String email = (String) userDetails.get("email");
//            String name = (String) userDetails.get("name");
//            // Process user information as needed
//        }
//        // Handle the case where the user is not authenticated
//        return "redirect:/login";
//    }
//}
