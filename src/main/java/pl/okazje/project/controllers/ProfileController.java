package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.okazje.project.entities.Ban;
import pl.okazje.project.entities.User;
import pl.okazje.project.repositories.*;
import pl.okazje.project.services.SendMail;

import javax.mail.MessagingException;

@Controller
public class ProfileController {

    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BanRepository banRepository;
    @Autowired
    SendMail sendMail;

    @GetMapping("/profile/{name}")
    public ModelAndView profile(@PathVariable("name") String name) {

        ModelAndView modelAndView = new ModelAndView("profile");
        User uzytkownik = userRepository.findUserByLogin(name);
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("comments_page", false);
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;

    }

    @GetMapping("/profile/{name}/comments")
    public ModelAndView profile_comments(@PathVariable("name") String name) {

        ModelAndView modelAndView = new ModelAndView("profile");
        User uzytkownik = userRepository.findUserByLogin(name);
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("comments_page", true);
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;

    }

    @PostMapping("/banuser")
    public String banuser(@ModelAttribute("user_id") int user_id, @ModelAttribute("reason") String reason) throws MessagingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik1 = userRepository.findUserByLogin(authentication.getName());
        if(uzytkownik1.getROLE().equals("ADMIN")){

            Ban ban = new Ban();
            ban.setDuration("");
            ban.setReason(reason);
            ban.setUser(userRepository.findUserById(user_id));
            banRepository.save(ban);
            User uzytkownik2 = userRepository.findUserById(user_id);
            uzytkownik2.setBan(ban);
            userRepository.save(uzytkownik2);
            userRepository.banUser(uzytkownik2.getLogin());
            sendMail.sendingMail(uzytkownik2.getEmail(),"Ban - Twoje konto zostało zbanowane", "Witaj "+uzytkownik2.getLogin()+", \n Złamałeś regulamin strony co poskutkowalo blokadą konta.\n Powód blokady: "+ban.getReason()+"\n\nJeśli nie zgadzasz sie z ta blokadą, powiadom nas o tym jak najszybciej.");

        }

        return "redirect:/profile/"+userRepository.findUserById(user_id).getLogin();

    }

}
