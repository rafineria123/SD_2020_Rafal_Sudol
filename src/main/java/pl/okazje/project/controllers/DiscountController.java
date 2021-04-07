package pl.okazje.project.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.entities.Comment;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.Rating;
import pl.okazje.project.entities.User;
import pl.okazje.project.repositories.*;
import pl.okazje.project.services.CommentService;
import pl.okazje.project.services.SendMail;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class DiscountController {

    DiscountRepository discountRepository;
    ShopRepository shopRepository;
    TagRepository tagRepository;
    UserRepository userRepository;
    CommentService commentService;
    RatingRepository ratingRepository;
    SendMail sendMail;

    public DiscountController(DiscountRepository discountRepository, ShopRepository shopRepository, TagRepository tagRepository,
                              UserRepository userRepository, CommentService commentService, RatingRepository ratingRepository, SendMail sendMail) {
        this.discountRepository = discountRepository;
        this.shopRepository = shopRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.commentService = commentService;
        this.ratingRepository = ratingRepository;
        this.sendMail = sendMail;
    }



    @GetMapping("/discount/{id}")
    public ModelAndView discount(@PathVariable("id") Long id) {
        if (discountRepository.findById(id).get().deletedOrNotReady()) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("USER") || r.getAuthority().equals("ADMIN"))) {

                User uzytkownik1 = userRepository.findFirstByLogin(authentication.getName());
                if (uzytkownik1.hasDiscount(id) || uzytkownik1.getROLE().equals("ADMIN")) {

                    ModelAndView modelAndView = new ModelAndView("discount");
                    modelAndView.addObject("list_of_tags", tagRepository.findAll());
                    modelAndView.addObject("list_of_shops", shopRepository.findAll());
                    modelAndView.addObject("discount", discountRepository.findById(id).get());
                    return modelAndView;

                }
                ModelAndView modelAndView = new ModelAndView("error");
                return modelAndView;


            } else {
                ModelAndView modelAndView = new ModelAndView("error");
                return modelAndView;
            }


        }

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
    @PostMapping(path = "/add/discount", consumes = {"multipart/form-data"})
    public ModelAndView add_discount(
            @ModelAttribute("url") String url, @ModelAttribute("tag") String tag, @ModelAttribute("shop") String shop,
            @ModelAttribute("title") String title, @ModelAttribute("old_price") String old_price, @ModelAttribute("current_price") String current_price,
            @ModelAttribute("shipment_price") String shipment_price, @ModelAttribute("content") String content,
            @ModelAttribute("expire_date") String expire_date, @ModelAttribute("type") String type, @ModelAttribute("discount") String rodzaj, @RequestParam("image_url")
                    MultipartFile file, HttpServletRequest request, RedirectAttributes redir
    ) throws ParseException, IOException {
        Discount discount = new Discount();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User uzytkownik = userRepository.findFirstByLogin(authentication.getName());
            if(content.isEmpty()||title.isEmpty()||url.isEmpty()){
                throw new IllegalArgumentException();
            }


            Path currentPath = Paths.get("");
            Path absolutePath = currentPath.toAbsolutePath();

            File transferFile = new File(absolutePath+"/src/main/resources/static/images/" + file.getOriginalFilename());
            file.transferTo(transferFile);

            if(type.equals("OBNIZKA")){

                discount.setCurrent_price(Double.parseDouble(current_price));
                discount.setOld_price(Double.parseDouble(old_price));
                discount.setShipment_price(Double.parseDouble(shipment_price));
                discount.setType(Discount.Type.OBNIZKA);

            }

            if(type.equals("KOD")){

                discount.setOld_price(Double.parseDouble(old_price));
                if(rodzaj.equals("%")){

                    discount.setType(Discount.Type.KODPROCENT);

                }
                if(rodzaj.equals("PLN")){

                    discount.setType(Discount.Type.KODNORMALNY);

                }


            }

            if(type.equals("KUPON")){

                discount.setOld_price(Double.parseDouble(old_price));
                if(rodzaj.equals("%")){

                    discount.setType(Discount.Type.KUPONPROCENT);

                }
                if(rodzaj.equals("PLN")){

                    discount.setType(Discount.Type.KUPONNORMALNY);

                }


            }


            discount.setContent(content);
            discount.setCreationdate(new Date());
            discount.setDiscount_link(url);

            discount.setTag(tagRepository.findById(Long.parseLong(tag)).get());
            discount.setShop(shopRepository.findById(Long.parseLong(shop)).get());
            discount.setTitle(title);
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(expire_date);
            discount.setExpire_date(date1);
            discount.setStatus(Discount.Status.OCZEKUJACE);
            discount.setUser(uzytkownik);
            discount.setImage_url("images/" + file.getOriginalFilename());
            discountRepository.save(discount);


        } catch (Exception e) {
            e.printStackTrace();

            ModelAndView modelAndView = new ModelAndView("add_discount");
            modelAndView.addObject("list_of_tags", tagRepository.findAll());
            modelAndView.addObject("list_of_shops", shopRepository.findAll());
            modelAndView.addObject("error", true);
            return modelAndView;
        }

        RedirectView redirectView = new RedirectView("/settings/discounts");
        redir.addFlashAttribute("good_status", "Twoja okazja została dodana i oczekuje na weryfikacje.");
        ModelAndView modelAndView =  new ModelAndView(redirectView);

        return modelAndView;
    }

    @PostMapping("/addrate")
    public String addrate(@ModelAttribute("discountidadd") String discountid, @ModelAttribute("redirect") String redirect) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());

        Discount discount = discountRepository.findById(Long.parseLong(discountid)).get();
        for (Rating r : discount.getRatings()) {

            if (r.getUser().getUser_id().equals(uzytkownik.getUser_id())) {
                return "redirect:/discount/"+discountid;

            }

        }


        Rating newrating = new Rating();
        newrating.setUser(uzytkownik);
        newrating.setDiscount(discount);
        ratingRepository.save(newrating);

        return "redirect:/discount/" + discountid;

    }

    @PostMapping("/removerate")
    public String removerate(@ModelAttribute("discountidremove") String discountid, @ModelAttribute("redirect_remove") String redirect) {

        Discount discount = discountRepository.findById(Long.parseLong(discountid)).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());
        for (Rating r : discount.getRatings()) {

            if (r.getUser().getUser_id().equals(uzytkownik.getUser_id())) {
                ratingRepository.delete(r);
                return "redirect:/discount/"+discountid;

            }

        }

        return "redirect:/discount/"+discountid;

    }

    @PostMapping("/addcomment")
    public String addcomment(@ModelAttribute("discountidaddcomment") String discountaddcomment, @ModelAttribute("comment") String comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());
        Comment comment1 = new Comment();
        comment1.setDiscount(discountRepository.findById(Long.parseLong(discountaddcomment)).get());
        comment1.setUser(uzytkownik);
        comment1.setContent(comment);
        comment1.setCr_date(new Date());
        commentService.save(comment1);

        return "redirect:/discount/" + Long.parseLong(discountaddcomment);
    }

    @PostMapping("/removecomment")
    public String removecomment(@ModelAttribute("comment_id") String comment_id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik1 = userRepository.findFirstByLogin(authentication.getName());
        Comment comment = commentService.findById(Long.parseLong(comment_id));
        if(uzytkownik1.getROLE().equals("ADMIN")){


            comment.setStatus("Usuniete");
            commentService.save(comment);

        }


        return "redirect:/discount/"+comment.getDiscount().getDiscount_id();

    }

    @PostMapping("/acceptdiscount")
    public String acceptdiscount(@ModelAttribute("discount_id") String discount_id) throws MessagingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik1 = userRepository.findFirstByLogin(authentication.getName());
        if(uzytkownik1.getROLE().equals("ADMIN")){

            Discount disc = discountRepository.findById(Long.parseLong(discount_id)).get();
            disc.setStatus(Discount.Status.ZATWIERDZONE);
            discountRepository.save(disc);
            sendMail.sendingMail(disc.getUser().getEmail(),"NORGIE - Okazja zatwierdzona","Twoja okazja została zweryfikowana i zatwierdzona," +
                    " juz niedługo pojawi się na stronie głównej.\n" +
                    "Tytuł okazji: "+disc.getTitle());

        }

        return "redirect:/discount/"+discount_id;

    }

    @PostMapping("/removediscount")
    public String removediscount(@ModelAttribute("discount_id") String discount_id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik1 = userRepository.findFirstByLogin(authentication.getName());
        if(uzytkownik1.getROLE().equals("ADMIN")){

            Discount disc = discountRepository.findById(Long.parseLong(discount_id)).get();
            disc.setStatus(Discount.Status.USUNIETE);
            discountRepository.save(disc);

        }

        return "redirect:/discount/"+discount_id;

    }

    @PostMapping("/ratecomment")
    public String ratecomment(@ModelAttribute("commentid") String commentid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());
        Comment comment = commentService.findById((Long.parseLong(commentid)));
        for (Rating r : comment.getRatings()) {

            if (r.getUser().getUser_id().equals(uzytkownik.getUser_id())) {
                return "redirect:/discount/" + comment.getDiscount().getDiscount_id().toString();

            }

        }

        Rating newrating = new Rating();
        newrating.setUser(uzytkownik);
        newrating.setComment(comment);
        ratingRepository.save(newrating);

        return "redirect:/discount/" + comment.getDiscount().getDiscount_id().toString();

    }

}
