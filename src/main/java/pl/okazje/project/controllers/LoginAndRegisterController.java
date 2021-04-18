package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.entities.*;
import pl.okazje.project.repositories.*;
import pl.okazje.project.services.AuthenticationService;
import pl.okazje.project.services.EmailService;
import pl.okazje.project.services.RegisterService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class LoginAndRegisterController {

    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmailService emailService;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    TokenRepository tokenRepository;


    private final AuthenticationService authenticationService;
    private final RegisterService registerService;

    @Autowired
    public LoginAndRegisterController(AuthenticationService authenticationService, RegisterService registerService) {
        this.authenticationService = authenticationService;
        this.registerService = registerService;
    }

    @GetMapping("/login")
    public ModelAndView getLoginHomepage() {
        return getBaseModelAndView("login");
    }

    @GetMapping("/register")
    public ModelAndView getRegisterHomepage() {
        return getBaseModelAndView("register");
    }


    @PostMapping("/register")
    @PreAuthorize("!hasAnyAuthority('USER', 'ADMIN')")
    public RedirectView register(@ModelAttribute("login") String login, @ModelAttribute("password") String password, @ModelAttribute("repeated_password") String repeated_password,
                                 RedirectAttributes redir, @ModelAttribute("email") String email, @ModelAttribute("reg") String statute, HttpServletRequest request) {
        Map map = registerService.registerUser(login, password, repeated_password, email, statute);
        RedirectView redirectView;
        if (map.containsKey("bad_status")) {
            redirectView = new RedirectView("/register", true);
            redir.addFlashAttribute("bad_status", map.get("bad_status"));
        } else {
            redirectView = new RedirectView("/login", true);
            redir.addFlashAttribute("good_status", map.get("good_status"));
        }
        return redirectView;
    }

    @GetMapping("/registrationConfirm")
    @PreAuthorize("!hasAnyAuthority('USER', 'ADMIN')")
    public RedirectView registrationConfirm(@RequestParam("token") String token, RedirectAttributes redir) {
        Map map = registerService.confirmRegistration(token);
        RedirectView redirectView;
        if (map.containsKey("bad_status")) {
            redirectView = new RedirectView("/login", true);
            redir.addFlashAttribute("bad_status", map.get("bad_status"));
        }else {
            redirectView = new RedirectView("/login", true);
            redir.addFlashAttribute("good_status", map.get("good_status"));
        }
        return redirectView;
    }

    private ModelAndView getBaseModelAndView(String viewName) {
        ModelAndView modelAndView;
        if (authenticationService.getCurrentUser().isPresent()) {
            modelAndView = new ModelAndView(new RedirectView("", true, true, false));
            return modelAndView;
        }
        modelAndView = new ModelAndView(viewName);
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;
    }
}
