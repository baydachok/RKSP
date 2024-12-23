package userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import userservice.service.UserService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @GetMapping("/user")
    public String getUser(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        var attributes = oAuth2User.getAttributes();
        String id = userService.getId((String) attributes.get("login"));
        model.addAttribute("id", id);
        model.addAttribute("login", attributes.get("login"));
        return "user";
    }
}
