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
import pl.okazje.project.services.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class HomeController {



    private final SessionService sessionService;
    private final DiscountService discountService;
    private final TagService tagService;
    private final ShopService shopService;
    private final int ITEMS_PER_PAGE = 8;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Autowired
    public HomeController(SessionService sessionService, DiscountService discountService, TagService tagService, ShopService shopService, AuthenticationService authenticationService, UserService userService) {
        this.sessionService = sessionService;
        this.discountService = discountService;
        this.tagService = tagService;
        this.shopService = shopService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView getDiscountsHomepage(HttpServletRequest request){
        return getBaseModelAndView(new HashMap(),0,"");
    }

    @GetMapping("/page/{id}")
    public ModelAndView getDiscountsPage(@PathVariable("id") String id) {
        return getBaseModelAndView(new HashMap(),Integer.parseInt(id)-1,"");
    }

    @GetMapping("/categories/{category}")
    public ModelAndView getDiscountsByTagHomepage(@PathVariable("category") String category, HttpServletRequest request) {
        HashMap<String,String> map = new HashMap<>();
        map.put("tag", category);
        return getBaseModelAndView(map,0,"/categories/"+category);
    }

    @GetMapping("/categories/{category}/page/{id}")
    public ModelAndView getDiscountsByTagPage(@PathVariable("category") String category, @PathVariable("id") String id, HttpServletRequest request) {
        HashMap<String,String> map = new HashMap<>();
        map.put("tag", category);
        return getBaseModelAndView(map,Integer.parseInt(id)-1,"/categories/"+category);
    }

    @GetMapping("/shops/{shop}")
    public ModelAndView getDiscountsByShopHomepage(@PathVariable("shop") String shop, HttpServletRequest request) {
        HashMap<String,String> map = new HashMap<>();
        map.put("shop", shop);
        return getBaseModelAndView(map,0,"/shops/"+shop);
    }

    @GetMapping("/shops/{shop}/page/{id}")
    public ModelAndView getDiscountsByShopPage(@PathVariable("shop") String shop, @PathVariable("id") String id, HttpServletRequest request) {
        HashMap<String,String> map = new HashMap<>();
        map.put("shop", shop);
        return getBaseModelAndView(map,Integer.parseInt(id)-1,"/shops/"+shop);
    }

    @GetMapping("/search/{search}")
    public ModelAndView getDiscountsBySearchHomepage(@PathVariable("search") String search, HttpServletRequest request) {
        HashMap<String,String> map = new HashMap<>();
        map.put("search", search);
        return getBaseModelAndView(map,0,"/search/"+search);
    }

    @GetMapping("/search/{search}/page/{id}")
    public ModelAndView getDiscountsBySearchPage(@PathVariable("search") String search, @PathVariable("id") String id, HttpServletRequest request) {
        HashMap<String,String> map = new HashMap<>();
        map.put("search", search);
        return getBaseModelAndView(map,Integer.parseInt(id)-1,"/search/"+search);
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

    @PostMapping("/search")
    public ModelAndView search(@ModelAttribute("searchform") String searchform) throws UnsupportedEncodingException {
        String encodedId = URLEncoder.encode(searchform, "UTF-8").replace("+", "%20");
        ModelAndView m = new ModelAndView(new RedirectView("/search/" + encodedId, true, true, false));
        return m;
    }

    private ModelAndView getBaseModelAndView(Map map, int currentPage, String addressPrefix){
        PagedListHolder discountsPages = new PagedListHolder(discountService.findAllIncludeSortingAndFiltering(map));
        discountsPages.setPageSize(ITEMS_PER_PAGE);
        discountsPages.setPage(currentPage);
        String[] arrayOfAddresses = new String[discountsPages.getPageCount()];
        for (int i = 0; i < arrayOfAddresses.length; i++) {
            arrayOfAddresses[i]=addressPrefix+"/page/" + (i + 1);
        }
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", discountsPages.getPageList());
        modelAndView.addObject("list_of_tags", tagService.findAll());
        modelAndView.addObject("list_of_shops", shopService.findAll());
        modelAndView.addObject("quantity_of_pages", discountsPages.getPageCount());
        modelAndView.addObject("number_of_page", discountsPages.getPage()+1);
        modelAndView.addObject("next_and_previous", addressPrefix+"/page/id");
        modelAndView.addObject("array_of_addresses", arrayOfAddresses);
        if(map.containsKey("search")){
            modelAndView.addObject("additional_results_info", map.get("search"));
        }
        if(map.containsKey("tag")){
            modelAndView.addObject("additional_results_info", map.get("tag"));
            modelAndView.addObject("additional_results_info_more", " kategorii");
        }
        if(map.containsKey("shop")){
            modelAndView.addObject("additional_results_info", map.get("shop"));
            modelAndView.addObject("additional_results_info_more", " sklepu");
        }
        return modelAndView;
    }
}
