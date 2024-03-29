package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.services.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RegisterService registerService;
    private final ShopService shopService;
    private final TagService tagService;

    public AuthenticationController(AuthenticationService authenticationService, RegisterService registerService, ShopService shopService, TagService tagService) {
        this.authenticationService = authenticationService;
        this.registerService = registerService;
        this.shopService = shopService;
        this.tagService = tagService;
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
            redirectView = new RedirectView("/auth/register", true);
            redir.addFlashAttribute("bad_status", map.get("bad_status"));
        } else {
            redirectView = new RedirectView("/auth/login", true);
            redir.addFlashAttribute("good_status", map.get("good_status"));
        }
        return redirectView;
    }

    @GetMapping("/registrationConfirm")
    @PreAuthorize("!hasAnyAuthority('USER', 'ADMIN')")
    public RedirectView registerConfirmation(@RequestParam("token") String token, RedirectAttributes redir) {
        Map map = registerService.confirmRegistration(token);
        RedirectView redirectView;
        if (map.containsKey("bad_status")) {
            redirectView = new RedirectView("/auth/login", true);
            redir.addFlashAttribute("bad_status", map.get("bad_status"));
        } else {
            redirectView = new RedirectView("/auth/login", true);
            redir.addFlashAttribute("good_status", map.get("good_status"));
        }
        return redirectView;
    }

    @GetMapping("/regulations")
    public ModelAndView getRegulationsPage() {
        ModelAndView modelAndView;
        modelAndView = new ModelAndView("regulations");
        modelAndView.addObject("list_of_tags", tagService.findAll());
        modelAndView.addObject("list_of_shops", shopService.findAll());
        return modelAndView;
    }

    private ModelAndView getBaseModelAndView(String viewName) {
        ModelAndView modelAndView;
        if (authenticationService.getCurrentUser().isPresent()) {
            //redirect to homepage
            modelAndView = new ModelAndView(new RedirectView("", true, true, false));
            return modelAndView;
        }
        modelAndView = new ModelAndView(viewName);
        modelAndView.addObject("list_of_tags", tagService.findAll());
        modelAndView.addObject("list_of_shops", shopService.findAll());
        return modelAndView;
    }
}
