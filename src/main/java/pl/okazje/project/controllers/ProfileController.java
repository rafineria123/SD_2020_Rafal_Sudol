package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.okazje.project.entities.Ban;
import pl.okazje.project.entities.User;
import pl.okazje.project.repositories.*;
import pl.okazje.project.services.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final ShopService shopService;
    private final TagService tagService;

    public ProfileController(UserService userService, ShopService shopService, TagService tagService) {
        this.userService = userService;
        this.shopService = shopService;
        this.tagService = tagService;
    }

    @GetMapping("/{name}")
    public ModelAndView getProfilePage(@PathVariable("name") String name) {
        return getBaseModelAndView(false, name);
    }

    @GetMapping("/{name}/comments")
    public ModelAndView getProfileCommentsPage(@PathVariable("name") String name) {
        return getBaseModelAndView(true, name);
    }

    @PostMapping("/banuser")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String banUser(@ModelAttribute("user_id") int user_id, @ModelAttribute("reason") String reason, HttpServletRequest request) {
        userService.banUser(user_id, reason);
        return "redirect:" + request.getHeader("Referer");
    }

    private ModelAndView getBaseModelAndView(boolean isCommentPage, String name) {
        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("user", userService.findFirstByLogin(name).get());
        modelAndView.addObject("comments_page", isCommentPage);
        modelAndView.addObject("list_of_tags", tagService.findAll());
        modelAndView.addObject("list_of_shops", shopService.findAll());
        return modelAndView;
    }

}
