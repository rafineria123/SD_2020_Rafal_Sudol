package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.okazje.project.entities.*;
import pl.okazje.project.repositories.*;

import javax.jws.soap.SOAPBinding;
import java.lang.reflect.Array;
import java.util.ArrayList;
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

    @GetMapping("/")
    public ModelAndView homePage() {

        Shop shop1 = new Shop("Biedronka","images/shop_1.png");
        Shop shop2 = new Shop("Avon","images/shop_2.png");
        Shop shop3 = new Shop("X-kom" ,"images/shop_3.png");
        Shop shop4 = new Shop("Tesco","images/shop_4.png");
        Shop shop5 = new Shop("Empik" ,"images/shop_3.png");
        Shop shop6 = new Shop("Media Markt","images/shop_4.png");


        shopRepository.save(shop1);
        shopRepository.save(shop2);
        shopRepository.save(shop3);
        shopRepository.save(shop4);
        shopRepository.save(shop5);
        shopRepository.save(shop6);


        Tag tag1 = new Tag("Elektronika");
        Tag tag2 = new Tag("Motoryzacja");
        Tag tag3 = new Tag("Gaming");
        Tag tag4 = new Tag("Moda");
        Tag tag5 = new Tag("Artykuły Spożywcze");
        Tag tag6 = new Tag("Sport i turystyka");

        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);
        tagRepository.save(tag4);
        tagRepository.save(tag5);
        tagRepository.save(tag6);


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user1 = new User("rafineria",encoder.encode("password"),"rafineria@gmail.com");
        User user2 = new User("rafineria123",encoder.encode("password123"),"rafineria123@gmail.com");

        userRepository.save(user1);
        userRepository.save(user2);

        //String title, String content, String image_url, Double old_price, Double current_price, Double shipment_price, String discount_link, User user, Tag tag, Shop shop

        Discount discount1 = new Discount("Baterie alkaliczne AA",
                "W Kaufland dostępne w dobrej cenie baterie alkaliczne AA oraz AAA 10szt. (wychodzi 1zł za sztukę). Dostępna duża ilość. Znalezione w Pszczynie, ale z informacji innych użytkowników okazja ogólnopolska.",
                "images/discount_1.jpg",255.0,220.0,15.0,"https://www.biedronka.pl/pl/dom-nf-10-06",user1,tag1,shop6);
        Discount discount2 = new Discount("Brzoskwinia @ Biedronka",
                "Oferta obowiązuje od 12.06 do 13.06.2020 r.",
                "images/discount_2.jpg",10.0,5.0,0.0,"https://www2.pl.avon.com/pl-home",user1,tag5,shop1);
        Discount discount3 = new Discount("Xbox Game Pass - Czerwiec",
                "W ramach usługi Xbox Game Pass w czerwcu zagramy w : Xbox Game Pass - nowe gry Battlefleet Gothic Armada 2 (PC) – 11 czerwca Battletech (PC) – 11 czerwca Dungeon of the End.",
                "images/discount_3.jpg",100.0,1.0,0.0,
                "https://www.x-kom.pl/p/568960-smartfon-telefon-realme-x3-superzoom-12256gb-glacier-blue-120hz.html",user1,tag3,shop5);
        Discount discount4 = new Discount("Kawa Dallmayr Classic Krafti.",
                "W Biedronce od 12.06 kawa mielona Dallmayr Classic Krafti.",
                "images/discount_4.jpg",20.0,15.0,0.0,"https://www2.pl.avon.com/pl-home",user2,tag3,shop1);
        Discount discount5 = new Discount("IPRee namiot 3 w 1 dla 5-8 osób",
                "Dostawa gratis Przecena z 129.99$ na 84.99$. Wersja S kosztuje po przecenie 55.24$.",
                "images/discount_5.jpg",500.0,350.0,10.0,"https://www2.pl.avon.com/pl-home",user2,tag6,shop4);
        Discount discount6 = new Discount("Płyn do chłodnic samochodowych",
                "W Tesco w Jarosławiu przecena płynu zimowego Borygo. Zostało jeszcze minimum 10 sztuk. Oferta lokalna. Małe miasto, ale może komuś się przyda ta informacja.",
                "images/discount_6.jpg",35.0,17.0,0.0,"https://www2.pl.avon.com/pl-home",user2,tag2,shop2);


        discountRepository.save(discount1);
        discountRepository.save(discount2);
        discountRepository.save(discount3);
        discountRepository.save(discount4);
        discountRepository.save(discount5);
        discountRepository.save(discount6);

        Comment comment1 = new Comment("Słownik podpowiedział nie to słowo co powinien. Moja wina że nie sprawdziłem. Ale nie zmienia to faktu że komentarze" +
                " w tym stylu co napisałeś są bez sensu. Co to ma wnosić do obecnej sytuacji." +
                " Kiedyś może mogło być drożej, mogło być taniej. Dzisiaj taka cena jest najtańsza i na tym polega ten serwis.");
        comment1.setUser(discount3.getUser());
        comment1.setDiscount(discount3);
        commentRepository.save(comment1);

        Rating rating1 = new Rating();
        rating1.setUser(discount3.getUser());
        rating1.setDiscount(discount3);
        Rating rating2 = new Rating();
        rating2.setUser(discount3.getUser());
        rating2.setDiscount(discount3);
        Rating rating3 = new Rating();
        rating3.setUser(discount3.getUser());
        rating3.setComment(comment1);
        ratingRepository.save(rating1);
        ratingRepository.save(rating2);
        ratingRepository.save(rating3);


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
        return modelAndView;
    }
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView("register");
        return modelAndView;
    }





}
