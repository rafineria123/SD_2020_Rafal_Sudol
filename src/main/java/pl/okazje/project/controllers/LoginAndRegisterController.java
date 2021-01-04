package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.entities.*;
import pl.okazje.project.events.OnRegistrationCompleteEvent;
import pl.okazje.project.repositories.*;
import pl.okazje.project.services.DiscountService;
import pl.okazje.project.services.SendMail;
import pl.okazje.project.services.UserService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    SendMail sendMail;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    TokenRepository tokenRepository;




    public final static int page_size_for_home = 8;

    public final static int page_size_for_cat_and_shops = 8;



    @GetMapping("/login")
    public ModelAndView login() {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("USER") || r.getAuthority().equals("ADMIN"))) {


            ModelAndView modelAndView = new ModelAndView(new RedirectView("", true, true, false));
            return modelAndView;


        }

        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;
    }




    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;
    }



    @PostMapping("/register")
    public RedirectView register(@ModelAttribute("login") String login, @ModelAttribute("password") String password, @ModelAttribute("repeated_password") String repeated_password,
                                 RedirectAttributes redir, @ModelAttribute("email") String email, @ModelAttribute("reg") String reg, HttpServletRequest request) {

        RedirectView redirectView = new RedirectView("/register", true);

        if (login.length() < 5) {

            redir.addFlashAttribute("bad_status", "Wprowadzony login jest za krótki.");
            return redirectView;

        }

        if (password.length() < 5) {

            redir.addFlashAttribute("bad_status", "Wprowadzone hasło jest za krótkie.");
            return redirectView;

        }

        if (userRepository.findUserByLogin(login) != null) {

            redir.addFlashAttribute("bad_status", "Ten login jest juz zajęty.");
            return redirectView;

        }


        if (!password.equals(repeated_password)) {
            redir.addFlashAttribute("bad_status", "Hasła muszą się powtarzać w obydwu polach.");
            return redirectView;
        }


        if (!reg.equals("on")) {

            redir.addFlashAttribute("bad_status", "Musisz zaakceptować regulamin.");
            return redirectView;

        }


        User user1 = new User();
        user1.setCr_date(new Date());
        user1.setLogin(login);
        user1.setPassword(passwordEncoder.encode(password));
        user1.setEmail(email);
        userRepository.save(user1);
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user1,
                request.getLocale(), appUrl));


        redir.addFlashAttribute("good_status", "Na twój mail została wysłana wiadomość. Może to potrwać kilka minut. Zatwierdz swoje konto klikając w link aktywacyjny.");
        redirectView = new RedirectView("/login", true);

        return redirectView;

    }

    @GetMapping("/registrationConfirm")
    public RedirectView registrationConfirm(@RequestParam("token") String token, RedirectAttributes redir){



        Token verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {

            RedirectView redirectView = new RedirectView("/login",true);
            redir.addFlashAttribute("bad_status", "Taki token nie istnieje.");
            return redirectView;

        }


        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {

            RedirectView redirectView = new RedirectView("/login",true);
            redir.addFlashAttribute("bad_status", "Twój token wygasł.");
            return redirectView;

        }

        user.setEnabled(true);
        userRepository.save(user);
        RedirectView redirectView = new RedirectView("/login",true);
        redir.addFlashAttribute("good_status", "Możesz się teraz zalogować.");
        return redirectView;

    }
}
