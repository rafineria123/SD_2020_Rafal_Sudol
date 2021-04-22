package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.repositories.PostRepository;
import pl.okazje.project.repositories.ShopRepository;
import pl.okazje.project.repositories.TagRepository;
import pl.okazje.project.services.PostService;
import pl.okazje.project.services.SessionService;
import pl.okazje.project.services.ShopService;
import pl.okazje.project.services.TagService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/forum")
public class ForumController {

    private final int ITEMS_PER_PAGE = 4;

    private final SessionService sessionService;
    private final TagService tagService;
    private final ShopService shopService;
    private final PostService postService;

    public ForumController(SessionService sessionService, TagService tagService, ShopService shopService, PostService postService) {
        this.sessionService = sessionService;
        this.tagService = tagService;
        this.shopService = shopService;
        this.postService = postService;
    }

    @GetMapping("")
    public ModelAndView getForumHomepage() {
        return getBaseModelAndView(new HashMap(),0,"");
    }

    @GetMapping("/page/{id}")
    public ModelAndView getForumPage(@PathVariable("id") String id) {
        return getBaseModelAndView(new HashMap(),Integer.parseInt(id)-1,"");
    }

    @GetMapping("/search/{search}")
    public ModelAndView getPostsBySearchHomepage(@PathVariable("search") String search) {
        HashMap<String,String> map = new HashMap<>();
        map.put("search", search);
        return getBaseModelAndView(map,0,"/search/"+search);
    }

    @GetMapping("/search/{search}/page/{id}")
    public ModelAndView getPostsBySearchPage(@PathVariable("search") String search,@PathVariable("id") String id) {
        HashMap<String,String> map = new HashMap<>();
        map.put("search", search);
        return getBaseModelAndView(map,Integer.parseInt(id)-1,"/search/"+search);
    }

    @GetMapping("/categories/{category}/page/{id}")
    public ModelAndView getPostsByTagPage(@PathVariable("category") String category, @PathVariable("id") String id) {
        HashMap<String,String> map = new HashMap<>();
        map.put("tag", category);
        return getBaseModelAndView(map,Integer.parseInt(id)-1,"/categories/"+category);
    }

    @GetMapping("/categories/{category}")
    public ModelAndView getPostsByTagHomepage(@PathVariable("category") String category) {
        HashMap<String,String> map = new HashMap<>();
        map.put("tag", category);
        return getBaseModelAndView(map,0,"/categories/"+category);
    }
    @GetMapping("/shops/{shop}")
    public ModelAndView getPostsByShopHomepage(@PathVariable("shop") String shop) {
        HashMap<String,String> map = new HashMap<>();
        map.put("shop", shop);
        return getBaseModelAndView(map,0,"/shops/"+shop);
    }

    @GetMapping("/shops/{shop}/page/{id}")
    public ModelAndView getPostsByShopPage(@PathVariable("shop") String shop, @PathVariable("id") String id) {
        HashMap<String,String> map = new HashMap<>();
        map.put("shop", shop);
        return getBaseModelAndView(map,Integer.parseInt(id)-1,"/shops/"+shop);
    }

    @PostMapping("/sort")
    public String sort(@ModelAttribute("sort") String sort, HttpServletRequest request){
        sessionService.setCurrentSessionForumSortAttribute(sort);
        return "redirect:"+request.getHeader("referer");
    }

    @PostMapping("/search")
    public ModelAndView search(@ModelAttribute("searchform") String searchForm) throws UnsupportedEncodingException {
        String encodedId = URLEncoder.encode(searchForm, "UTF-8").replace("+", "%20");
        ModelAndView m = new ModelAndView(new RedirectView("/forum/search/" + encodedId, true, true, false));
        return m;
    }

    private ModelAndView getBaseModelAndView(Map map, int currentPage, String addressPrefix){
        PagedListHolder postsPages = new PagedListHolder(postService.findAllIncludeSorting(map));
        postsPages.setPageSize(ITEMS_PER_PAGE);
        postsPages.setPage(currentPage);
        String[] arrayOfAddresses = new String[postsPages.getPageCount()];
        for (int i = 0; i < arrayOfAddresses.length; i++) {
            arrayOfAddresses[i] = "/forum"+addressPrefix+"/page/" + (i + 1);
        }
        ModelAndView modelAndView = new ModelAndView("forum");
        modelAndView.addObject("list_of_posts", postsPages.getPageList());
        modelAndView.addObject("list_of_tags", tagService.findAll());
        modelAndView.addObject("list_of_shops", shopService.findAll());
        modelAndView.addObject("quantity_of_pages", postsPages.getPageCount());
        modelAndView.addObject("number_of_page", postsPages.getPage()+1);
        modelAndView.addObject("next_and_previous", "/forum"+addressPrefix+"/page/id");
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
