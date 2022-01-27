package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.services.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/discount")
public class DiscountController {



    @GetMapping("/{id}")
    public ModelAndView getDiscountPage(@PathVariable("id") Long id) {
        ModelAndView modelAndView;
        Optional<Discount> discount = discountService.findById(id);
        if (discount.isPresent()) {
            modelAndView = new ModelAndView("discount");
            modelAndView.addObject("list_of_tags", tagService.findAll());
            modelAndView.addObject("list_of_shops", shopService.findAll());
            modelAndView.addObject("discount", discount.get());
            return modelAndView;
        }
        modelAndView = new ModelAndView("error");
        return modelAndView;
    }

    private final CommentService commentService;
    private final DiscountService discountService;
    private final TagService tagService;
    private final ShopService shopService;
    private final RatingService ratingService;

    public DiscountController(CommentService commentService, DiscountService discountService, TagService tagService, ShopService shopService, RatingService ratingService) {
        this.commentService = commentService;
        this.discountService = discountService;
        this.tagService = tagService;
        this.shopService = shopService;
        this.ratingService = ratingService;
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ModelAndView getAddDiscountPage() {
        ModelAndView modelAndView = new ModelAndView("add_discount");
        modelAndView.addObject("list_of_tags", tagService.findAll());
        modelAndView.addObject("list_of_shops", shopService.findAll());
        return modelAndView;
    }

    @PostMapping(path = "/add", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ModelAndView addDiscount(@ModelAttribute("url") String url, @ModelAttribute("tag") String tag, @ModelAttribute("shop") String shop,
                                    @ModelAttribute("title") String title, @ModelAttribute("old_price") String old_price, @ModelAttribute("current_price") String current_price,
                                    @ModelAttribute("shipment_price") String shipment_price, @ModelAttribute("content") String content,
                                    @ModelAttribute("expire_date") String expire_date, @ModelAttribute("type") String typeBase, @ModelAttribute("discount") String typeSuffix, @RequestParam("image_url")
                                            MultipartFile file, HttpServletRequest request, RedirectAttributes redir) {
        ModelAndView modelAndView;
        if (discountService.addDiscount(url, tag, shop, title, old_price, current_price, shipment_price, content, expire_date, typeBase, typeSuffix, file)) {
            RedirectView redirectView = new RedirectView("/settings/discounts");
            redir.addFlashAttribute("good_status", "Twoja okazja została dodana i oczekuje na weryfikacje.");
            modelAndView = new ModelAndView(redirectView);
            return modelAndView;
        }
        RedirectView redirectView = new RedirectView("/discount/add");
        redir.addFlashAttribute("error", true);
        modelAndView = new ModelAndView(redirectView);
        return modelAndView;
    }

    @PostMapping("/addrate")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String addRatingToDiscount(@ModelAttribute("discountidadd") String discountId) {
        ratingService.addRatingToDiscount(Long.parseLong(discountId));
        return "redirect:/discount/" + discountId;
    }

    @PostMapping("/removerate")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String removeRatingFromDiscount(@ModelAttribute("discountidremove") String discountId) {
        ratingService.removeRatingFromDiscount(Long.parseLong(discountId));
        return "redirect:/discount/" + discountId;
    }

    @PostMapping("/addcomment")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String addCommentToDiscount(@ModelAttribute("discountidaddcomment") String discountId, @ModelAttribute("comment") String comment) {
        commentService.addCommentToDiscount(Long.parseLong(discountId), comment);
        return "redirect:/discount/" + discountId;
    }

    @PostMapping("/removecomment")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String removeCommentFromDiscount(@ModelAttribute("comment_id") String comment_id, HttpServletRequest request) {
        commentService.removeComment(Long.parseLong(comment_id));
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/acceptdiscount")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String acceptDiscount(@ModelAttribute("discount_id") String discount_id) {
        discountService.acceptDiscount(Long.parseLong(discount_id));
        return "redirect:/discount/" + discount_id;
    }

    @PostMapping("/removediscount")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String deleteDiscount(@ModelAttribute("discount_id") String discount_id, @ModelAttribute("reason") String reason) {
        discountService.deleteDiscount(Long.parseLong(discount_id), reason);
        return "redirect:/discount/" + discount_id;
    }

    @PostMapping("/ratecomment")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String addRatingToComment(@ModelAttribute("commentid") String commentid, HttpServletRequest request) {
        ratingService.addRatingToComment(Long.parseLong(commentid));
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/editdiscount")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ModelAndView getEditDiscountPage(@ModelAttribute("discount_id") String discountId){
        ModelAndView modelAndView;
        Optional<Discount> discount = discountService.findById(Long.parseLong(discountId));
        if (discount.isPresent()) {
            modelAndView = new ModelAndView("edit_discount");
            modelAndView.addObject("list_of_tags", tagService.findAll());
            modelAndView.addObject("list_of_shops", shopService.findAll());
            modelAndView.addObject("discount", discount.get());
            modelAndView.addObject("text", discount.get().getContent().replace("\n", "<br>").replace("\r", "<br>"));

            return modelAndView;
        }
        modelAndView = new ModelAndView("error");
        return modelAndView;
    }

    @PostMapping(path = "/edit", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ModelAndView editDiscount(@ModelAttribute("discount_id") String discountId, @ModelAttribute("url") String url, @ModelAttribute("tag") String tag, @ModelAttribute("shop") String shop,
                                    @ModelAttribute("title") String title, @ModelAttribute("old_price") String old_price, @ModelAttribute("current_price") String current_price,
                                    @ModelAttribute("shipment_price") String shipment_price, @ModelAttribute("content") String content,
                                    @ModelAttribute("expire_date") String expire_date, @ModelAttribute("type") String typeBase, @ModelAttribute("discount") String typeSuffix, @RequestParam("image_url")
                                            MultipartFile file, HttpServletRequest request, RedirectAttributes redir) {
        ModelAndView modelAndView;
        if (discountService.editDiscount(discountId, url, tag, shop, title, old_price, current_price, shipment_price, content, expire_date, typeBase, typeSuffix, file)) {
            RedirectView redirectView = new RedirectView("/settings/discounts");
            redir.addFlashAttribute("good_status", "Twoja okazja została zedytowana i oczekuje na weryfikacje.");
            modelAndView = new ModelAndView(redirectView);
            return modelAndView;
        }
        Optional<Discount> discount = discountService.findById(Long.parseLong(discountId));
        modelAndView = new ModelAndView("edit_discount");
        modelAndView.addObject("list_of_tags", tagService.findAll());
        modelAndView.addObject("list_of_shops", shopService.findAll());
        modelAndView.addObject("discount", discount.get());
        modelAndView.addObject("error", true);
        return modelAndView;
    }


}
