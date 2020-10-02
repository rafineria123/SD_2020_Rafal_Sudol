package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pl.okazje.project.entities.*;
import pl.okazje.project.repositories.*;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        Pageable firstPageWithTwoElements = PageRequest.of(0, 2);
        Page<Discount> allProducts = discountRepository.findAll(firstPageWithTwoElements);

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", allProducts.getContent());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", allProducts.getTotalPages());
        modelAndView.addObject("number_of_page", 1);
        return modelAndView;
    }

    @GetMapping("/page/{id}")
    public ModelAndView homePage(@PathVariable("id") String id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());
        Pageable firstPageWithTwoElements = PageRequest.of(Integer.parseInt(id)-1, 2);
        Page<Discount> allProducts = discountRepository.findAll(firstPageWithTwoElements);

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", allProducts.getContent());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", allProducts.getTotalPages());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
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
    public ModelAndView add_discount_get() {
        ModelAndView modelAndView = new ModelAndView("add_discount");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("error", false);
        return modelAndView;
    }

    @PostMapping(path="/add/discount", consumes = {"multipart/form-data"})
    public ModelAndView add_discount(
            @ModelAttribute("url") String url, @ModelAttribute("tag") String tag, @ModelAttribute("shop") String shop,
            @ModelAttribute("title") String title, @ModelAttribute("old_price") String old_price, @ModelAttribute("current_price") String current_price,
            @ModelAttribute("shipment_price") String shipment_price, @ModelAttribute("content") String content,
            @ModelAttribute("expire_date") String expire_date, @RequestParam("image_url")
                    MultipartFile file, HttpServletRequest request
    ) throws ParseException, IOException {
        Discount discount = new Discount();
        try {

            String uploadDir = "/static/images";
            String realPath = request.getServletContext().getRealPath(uploadDir);

            File transferFile = new File("C:/projekt inz/project/src/main/resources/static/images/" + file.getOriginalFilename());
            file.transferTo(transferFile);

            discount.setContent(content);
            discount.setCr_date(new Date());
            discount.setCurrent_price(Double.parseDouble(current_price));
            discount.setOld_price(Double.parseDouble(old_price));
            discount.setShipment_price(Double.parseDouble(shipment_price));
            discount.setDiscount_link(url);
            discount.setTag(tagRepository.findById(Long.parseLong(tag)).get());
            discount.setShop(shopRepository.findById(Long.parseLong(shop)).get());
            discount.setTitle(title);
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(expire_date);
            discount.setExpire_date(date1);
            discount.setStatus("");
            discount.setUser(uzytkownik);
            discount.setImage_url("images/"+file.getOriginalFilename());
            discountRepository.save(discount);

        }catch (Exception e){

            ModelAndView modelAndView = new ModelAndView("add_discount");
            modelAndView.addObject("list_of_tags", tagRepository.findAll());
            modelAndView.addObject("list_of_shops", shopRepository.findAll());
            modelAndView.addObject("error", true);
            return modelAndView;
        }

        ModelAndView modelAndView = new ModelAndView("redirect:/discount/"+discount.getDiscount_id());

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

    @PostMapping("/ratecomment")
    public String ratecomment(@ModelAttribute("commentid") String commentid){

        Comment comment = commentRepository.findById((Long.parseLong(commentid))).get();
        for(Rating r:comment.getRatings()){

            if(r.getUser().getUser_id().equals(uzytkownik.getUser_id())){
                return "redirect:/discount/"+comment.getDiscount().getDiscount_id().toString();

            }

        }

        Rating newrating = new Rating();
        newrating.setUser(uzytkownik);
        newrating.setComment(comment);
        ratingRepository.save(newrating);

        return "redirect:/discount/"+comment.getDiscount().getDiscount_id().toString();

    }

}
