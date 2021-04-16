package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.Shop;
import pl.okazje.project.entities.Tag;
import pl.okazje.project.repositories.DiscountRepository;
import pl.okazje.project.repositories.ShopRepository;
import pl.okazje.project.repositories.TagRepository;
import pl.okazje.project.repositories.UserRepository;
import pl.okazje.project.services.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Controller
public class HomeController {



    private final SessionService sessionService;
    private final DiscountService discountService;
    private final TagService tagService;
    private final ShopService shopService;
    private final int ITEMS_PER_PAGE = 8;

    @Autowired
    public HomeController(SessionService sessionService, DiscountService discountService, TagService tagService, ShopService shopService) {
        this.sessionService = sessionService;
        this.discountService = discountService;
        this.tagService = tagService;
        this.shopService = shopService;
    }

    public ModelAndView getBaseModelAndView(PagedListHolder discountPages,int currentPage, String addressPrefix){
        discountPages.setPageSize(ITEMS_PER_PAGE);
        discountPages.setPage(currentPage);

        List<String> listOfAddresses = new ArrayList<>();
        for (int i = 1; i <= discountPages.getPageCount(); i++) {
            listOfAddresses.add(addressPrefix+"/page/" + i);
        }

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", discountPages.getPageList());
        modelAndView.addObject("list_of_tags", tagService.findAll());
        modelAndView.addObject("list_of_shops", shopService.findAll());
        modelAndView.addObject("quantity_of_pages", discountPages.getPageCount());
        modelAndView.addObject("number_of_page", discountPages.getPage()+1);
        modelAndView.addObject("next_and_previous", addressPrefix+"/page/id");
        modelAndView.addObject("list_of_adresses", listOfAddresses);
        return modelAndView;
    }

    @GetMapping("/")
    public ModelAndView getDiscountHomepage(HttpServletRequest request){
        return getBaseModelAndView(new PagedListHolder(discountService.findAllIncludeSortingAndFiltering(new HashMap())),0,"");
    }

    @GetMapping("/page/{id}")
    public ModelAndView getDiscountPage(@PathVariable("id") String id) {
        return getBaseModelAndView(new PagedListHolder(discountService.findAllIncludeSortingAndFiltering(new HashMap())),Integer.parseInt(id)-1,"");
    }

    @GetMapping("/categories/{category}")
    public ModelAndView getDiscountByTagHomepage(@PathVariable("category") String category, HttpServletRequest request) {
        HashMap<String,String> map = new HashMap<>();
        map.put("tag", category);
        return getBaseModelAndView(new PagedListHolder(discountService.findAllIncludeSortingAndFiltering(map)),0,"/categories/"+category);
    }

    @GetMapping("/categories/{category}/page/{id}")
    public ModelAndView categoryPage(@PathVariable("category") String category, @PathVariable("id") String id, HttpServletRequest request) {
        HashMap<String,String> map = new HashMap<>();
        map.put("tag", category);
        return getBaseModelAndView(new PagedListHolder(discountService.findAllIncludeSortingAndFiltering(map)),Integer.parseInt(id)-1,"/categories/"+category);
    }

    @GetMapping("/shops/{shop}")
    public ModelAndView shop(@PathVariable("shop") String shop, HttpServletRequest request) {
        HashMap<String,String> map = new HashMap<>();
        map.put("shop", shop);
        return getBaseModelAndView(new PagedListHolder(discountService.findAllIncludeSortingAndFiltering(map)),0,"/shops/"+shop);
    }

    @GetMapping("/shops/{shop}/page/{id}")
    public ModelAndView shopPage(@PathVariable("shop") String shop, @PathVariable("id") String id, HttpServletRequest request) {
        HashMap<String,String> map = new HashMap<>();
        map.put("shop", shop);
        return getBaseModelAndView(new PagedListHolder(discountService.findAllIncludeSortingAndFiltering(map)),Integer.parseInt(id)-1,"/shops/"+shop);
    }

    @PostMapping("/search")
    public ModelAndView searchForm(@ModelAttribute("searchform") String searchform) throws UnsupportedEncodingException {
        String encodedId = URLEncoder.encode(searchform, "UTF-8").replace("+", "%20");
        ModelAndView m = new ModelAndView(new RedirectView("/search/" + encodedId, true, true, false));
        return m;
    }

    @GetMapping("/search/{search}")
    public ModelAndView search(@PathVariable("search") String search, HttpServletRequest request) {
        HashMap<String,String> map = new HashMap<>();
        map.put("search", search);
        return getBaseModelAndView(new PagedListHolder(discountService.findAllIncludeSortingAndFiltering(map)),0,"/search/"+search);
    }

    @GetMapping("/search/{search}/page/{id}")
    public ModelAndView searchPage(@PathVariable("search") String search, @PathVariable("id") String id, HttpServletRequest request) {
        HashMap<String,String> map = new HashMap<>();
        map.put("search", search);
        return getBaseModelAndView(new PagedListHolder(discountService.findAllIncludeSortingAndFiltering(map)),Integer.parseInt(id)-1,"/search/"+search);
    }

    @PostMapping("/filter")
    public String filter(HttpServletRequest request){
        sessionService.toggleCurrentSessionFilter();
        return "redirect:"+request.getHeader("referer");
    }
    @PostMapping("/sort")
    public String sort(@ModelAttribute("sort") String sort, @ModelAttribute("date") String date, HttpServletRequest request){
        sessionService.setCurrentSessionSortAndDateAttributes(sort, date);
        return "redirect:"+request.getHeader("referer");
    }
}
