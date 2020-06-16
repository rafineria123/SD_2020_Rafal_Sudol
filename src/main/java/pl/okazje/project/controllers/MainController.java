package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.okazje.project.entities.*;
import pl.okazje.project.repositories.*;

import javax.jws.soap.SOAPBinding;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    CommentRepository commentRepository;

    private static User uzytkownik = null;

    @GetMapping("/")
    public ModelAndView homePage() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());


        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", discountRepository.findAll());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;
    }

    @GetMapping("/categories/{id}")
    public ModelAndView category(@PathVariable("id") String id) {

        List<Discount> list = new ArrayList<>();
        for (Discount discount: discountRepository.findAll()) {
            if(discount.getTag().getName().toLowerCase().equals(id.toLowerCase())){
                list.add(discount);
            }
        }
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", list);
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;

    }

    @GetMapping("/accessdenied")
    public ModelAndView accessdenied() {
        return new ModelAndView("accessdenied");
    }

    @GetMapping("/shops/{id}")
    public ModelAndView shop(@PathVariable("id") String id) {
        List<Discount> list = new ArrayList<>();
        for (Discount discount: discountRepository.findAll()) {
            if(discount.getShop().getName().toLowerCase().equals(id.toLowerCase())){
                list.add(discount);
            }
        }
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", list);
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;

    }

    @GetMapping("/discount/{id}")
    public ModelAndView discount(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("discount");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("discount", discountRepository.findById(id).get());

        return modelAndView;
    }



    @GetMapping("/add/discount")
    public ModelAndView add_discount() {
        ModelAndView modelAndView = new ModelAndView("add_discount");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;
    }
    @GetMapping("/login")
    public ModelAndView login() {
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

    @PostMapping("/addrate")
    public String addrate(@ModelAttribute("discountidadd") String discountid){

        System.out.println(discountid);

        Discount discount = discountRepository.findById(Long.parseLong(discountid)).get();
        for(Rating r:discount.getRatings()){

            if(r.getUser().getUser_id().equals(uzytkownik.getUser_id())){
                System.out.println("2");
                return "redirect:/";

            }

        }

        System.out.println("3");

        Rating newrating = new Rating();
        newrating.setUser(uzytkownik);
        newrating.setDiscount(discount);
        ratingRepository.save(newrating);

        return "redirect:/";

    }

    @PostMapping("/removerate")
    public String removerate(@ModelAttribute("discountidremove") String discountid){

        Discount discount = discountRepository.findById(Long.parseLong(discountid)).get();
        for(Rating r:discount.getRatings()){

            if(r.getUser().getUser_id().equals(uzytkownik.getUser_id())){
                ratingRepository.delete(r);
                return "redirect:/";

            }

        }

        return "redirect:/";

    }

    @PostMapping("/addcomment")
    public String addcomment(@ModelAttribute("discountidaddcomment") String discountaddcomment, @ModelAttribute("comment") String comment){

        Comment comment1 = new Comment();
        comment1.setDiscount(discountRepository.findById(Long.parseLong(discountaddcomment)).get());
        comment1.setUser(uzytkownik);
        comment1.setContent(comment);
        comment1.setCr_date(new Date());
        commentRepository.save(comment1);

        return "redirect:/discount/"+Long.parseLong(discountaddcomment);
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("login") String login, @ModelAttribute("password") String password, @ModelAttribute("email") String email){

        User user1 = new User();
        user1.setCr_date(new Date());
        user1.setLogin(login);
        BCryptPasswordEncoder enkoder = new BCryptPasswordEncoder();
        user1.setPassword(enkoder.encode(password));
        user1.setEmail(email);
        userRepository.save(user1);

        return "redirect:/login";
    }

}
