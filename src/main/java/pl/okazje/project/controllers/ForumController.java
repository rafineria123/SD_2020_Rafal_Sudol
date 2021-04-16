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

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    ShopRepository shopRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    PostRepository postRepository;

    static int page_size_for_home = 4;

    @GetMapping("")
    public ModelAndView homePage() throws MessagingException {
        ModelAndView modelAndView = new ModelAndView("home_posts");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("picked_sort", 3);
        modelAndView.addObject("sort_buttons_prefix", "/forum/page/1/sort/");

        PagedListHolder page = new PagedListHolder(postRepository.findAllByOrderByCreationdateDesc("ACCEPTED"));
        page.setPageSize(page_size_for_home); // number of items per page
        page.setPage(0);
        modelAndView.addObject("list_of_posts", page.getPageList());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous", "/forum/page/id");
        modelAndView.addObject("picked_sort", 3);
        modelAndView.addObject("sort_buttons_prefix", "/forum/page/1/sort/");
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/forum/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);

        return modelAndView;
    }

    @GetMapping("/page/{id}")
    public ModelAndView homePage(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView("home_posts");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("picked_sort", 3);
        modelAndView.addObject("sort_buttons_prefix", "/forum/page/1/sort/");

        PagedListHolder page = new PagedListHolder(postRepository.findAllByOrderByCreationdateDesc("ZATWIERDZONE"));
        page.setPageSize(page_size_for_home); // number of items per page
        page.setPage(Integer.parseInt(id) - 1);
        modelAndView.addObject("list_of_posts", page.getPageList());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/forum/page/id");
        modelAndView.addObject("picked_sort", 3);
        modelAndView.addObject("sort_buttons_prefix", "/forum/page/1/sort/");
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/forum/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);

        return modelAndView;
    }

    @GetMapping("/page/{id}/sort/{sort}")
    public ModelAndView homePage(@PathVariable("id") String id,@PathVariable("sort") String sort) {
        ModelAndView modelAndView = new ModelAndView("home_posts");
        PagedListHolder page = new PagedListHolder();
        if(sort.equals("most-comments")){

            modelAndView.addObject("picked_sort", 2);
            page = new PagedListHolder(postRepository.findAllByOrderByCommentDesc());

        }
        if(sort.equals("date")){

            modelAndView.addObject("picked_sort", 3);
            page = new PagedListHolder(postRepository.findAllByOrderByCreationdateDesc("ZATWIERDZONE"));

        }

        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        page.setPageSize(page_size_for_home); // number of items per page
        page.setPage(Integer.parseInt(id) - 1);
        modelAndView.addObject("list_of_posts", page.getPageList());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/forum/page/id/sort/"+sort);
        modelAndView.addObject("sort_buttons_prefix", "/forum/page/1/sort/");
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/forum/page/" + i+"/sort/"+sort);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);

        return modelAndView;
    }

    @PostMapping("/search")
    public ModelAndView searchForm(@ModelAttribute("searchform") String searchform) throws UnsupportedEncodingException {
        String encodedId = URLEncoder.encode(searchform, "UTF-8").replace("+", "%20");
        ModelAndView m = new ModelAndView(new RedirectView("/forum/search/" + encodedId, true, true, false));
        return m;

    }

    @GetMapping("/search/{search}")
    public ModelAndView search(@PathVariable("search") String search) {

        PagedListHolder page = new PagedListHolder(postRepository.findAllBySearchOrderByCreationdateDesc("%" + search + "%"));
        page.setPageSize(page_size_for_home); // number of items per page
        page.setPage(0);

        ModelAndView modelAndView = new ModelAndView("home_posts");
        modelAndView.addObject("list_of_posts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous", "/forum/search/" + search + "/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/forum/search/" + search + "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/forum/search/" + search + "/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", search);
        return modelAndView;

    }

    @GetMapping("/search/{search}/page/{id}")
    public ModelAndView search(@PathVariable("search") String search,@PathVariable("id") String id) {

        PagedListHolder page = new PagedListHolder(postRepository.findAllBySearchOrderByCreationdateDesc("%" + search + "%"));
        page.setPageSize(page_size_for_home); // number of items per page
        page.setPage(Integer.parseInt(id) - 1);

        ModelAndView modelAndView = new ModelAndView("home_posts");
        modelAndView.addObject("list_of_posts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/forum/search/" + search + "/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/forum/search/" + search + "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/forum/search/" + search + "/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", search);
        return modelAndView;

    }

    @GetMapping("search/{search}/page/{id}/sort/{sort}")
    public ModelAndView search(@PathVariable("search") String search,@PathVariable("id") String id,@PathVariable("sort") String sort) {
        ModelAndView modelAndView = new ModelAndView("home_posts");
        PagedListHolder page = new PagedListHolder();
        if(sort.equals("most-comments")){

            modelAndView.addObject("picked_sort", 2);
            page = new PagedListHolder(postRepository.findAllBySearchOrderByCommentDesc("%"+search+"%"));

        }
        if(sort.equals("date")){

            modelAndView.addObject("picked_sort", 3);
            page = new PagedListHolder(postRepository.findAllBySearchOrderByCreationdateDesc("%"+search+"%"));

        }

        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        page.setPageSize(page_size_for_home); // number of items per page
        page.setPage(Integer.parseInt(id) - 1);
        modelAndView.addObject("list_of_posts", page.getPageList());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/forum/search/" + search+"/page/id/sort/"+sort);
        modelAndView.addObject("sort_buttons_prefix", "/forum/search/" + search+"/page/1/sort/");
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/forum/search/"+search+"/page/"+i+"/sort/"+sort);

        }
        modelAndView.addObject("additional_results_info", search);
        modelAndView.addObject("list_of_adresses", listOfAdresses);

        return modelAndView;
    }

    @GetMapping("/categories/{category}/page/{id}/sort/{sort}")
    public ModelAndView categories(@PathVariable("category") String category, @PathVariable("id") String id, @PathVariable("sort") String sort) {
        ModelAndView modelAndView = new ModelAndView("home_posts");
        PagedListHolder page = new PagedListHolder();
        if(sort.equals("most-comments")){

            modelAndView.addObject("picked_sort", 2);
            page = new PagedListHolder(postRepository.findAllByTagOrderByCommentDesc(category));

        }
        if(sort.equals("date")){

            modelAndView.addObject("picked_sort", 3);
            page = new PagedListHolder(postRepository.findAllByTagOrderByCreationdateDesc("ZATWIERDZONE", category));

        }

        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        page.setPageSize(page_size_for_home); // number of items per page
        page.setPage(Integer.parseInt(id) - 1);
        modelAndView.addObject("list_of_posts", page.getPageList());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/forum/categories/" + category + "/page/id/sort/"+sort);
        modelAndView.addObject("sort_buttons_prefix", "/forum/categories/" + category + "/page/1/sort/");
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/forum/categories/"+category+"/page/"+i+"/sort/"+sort);

        }
        modelAndView.addObject("additional_results_info", category);
        modelAndView.addObject("additional_results_info_more", " kategorii");
        modelAndView.addObject("list_of_adresses", listOfAdresses);

        return modelAndView;
    }

    @GetMapping("/categories/{category}/page/{id}")
    public ModelAndView categories(@PathVariable("category") String category, @PathVariable("id") String id) {

        PagedListHolder page = new PagedListHolder(postRepository.findAllByTagOrderByCreationdateDesc("ZATWIERDZONE", category));
        page.setPageSize(page_size_for_home); // number of items per page
        page.setPage(Integer.parseInt(id) - 1);

        ModelAndView modelAndView = new ModelAndView("home_posts");
        modelAndView.addObject("list_of_posts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id) - 1);
        modelAndView.addObject("next_and_previous", "/forum/categories/" + category + "/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/forum/categories/" + category + "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/forum/categories/" + category + "/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", category);
        modelAndView.addObject("additional_results_info_more", " kategorii");
        return modelAndView;

    }

    @GetMapping("/categories/{category}")
    public ModelAndView categories(@PathVariable("category") String category) {

        PagedListHolder page = new PagedListHolder(postRepository.findAllByTagOrderByCreationdateDesc("ZATWIERDZONE", category));
        page.setPageSize(page_size_for_home); // number of items per page
        page.setPage(0);

        ModelAndView modelAndView = new ModelAndView("home_posts");
        modelAndView.addObject("list_of_posts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous", "/forum/categories/" + category + "/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/forum/categories/" + category + "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/forum/categories/" + category + "/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", category);
        modelAndView.addObject("additional_results_info_more", " kategorii");
        return modelAndView;

    }
    @GetMapping("/shops/{shop}")
    public ModelAndView shops(@PathVariable("shop") String shop) {

        PagedListHolder page = new PagedListHolder(postRepository.findAllByShopOrderByCreationdateDesc("ZATWIERDZONE", shop));
        page.setPageSize(page_size_for_home); // number of items per page
        page.setPage(0);

        ModelAndView modelAndView = new ModelAndView("home_posts");
        modelAndView.addObject("list_of_posts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous", "/forum/shops/" + shop + "/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/forum/shops/" + shop + "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/forum/shops/" + shop + "/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", shop);
        modelAndView.addObject("additional_results_info_more", " sklepu");
        return modelAndView;

    }

    @GetMapping("/shops/{shop}/page/{id}")
    public ModelAndView shops(@PathVariable("shop") String shop, @PathVariable("id") String id) {

        PagedListHolder page = new PagedListHolder(postRepository.findAllByShopOrderByCreationdateDesc("ZATWIERDZONE", shop));
        page.setPageSize(page_size_for_home); // number of items per page
        page.setPage(Integer.parseInt(id) - 1);

        ModelAndView modelAndView = new ModelAndView("home_posts");
        modelAndView.addObject("list_of_posts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id) - 1);
        modelAndView.addObject("next_and_previous", "/forum/shops/" + shop + "/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/forum/shops/" + shop + "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/forum/shops/" + shop + "/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", shop);
        modelAndView.addObject("additional_results_info_more", " sklepu");
        return modelAndView;

    }

    @GetMapping("/shops/{shop}/page/{id}/sort/{sort}")
    public ModelAndView shops(@PathVariable("shop") String shop, @PathVariable("id") String id, @PathVariable("sort") String sort) {
        ModelAndView modelAndView = new ModelAndView("home_posts");
        PagedListHolder page = new PagedListHolder();
        if(sort.equals("most-comments")){

            modelAndView.addObject("picked_sort", 2);
            page = new PagedListHolder(postRepository.findAllByShopOrderByCommentDesc(shop));

        }
        if(sort.equals("date")){

            modelAndView.addObject("picked_sort", 3);
            page = new PagedListHolder(postRepository.findAllByShopOrderByCreationdateDesc("ZATWIERDZONE", shop));

        }

        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        page.setPageSize(page_size_for_home); // number of items per page
        page.setPage(Integer.parseInt(id) - 1);
        modelAndView.addObject("list_of_posts", page.getPageList());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/forum/shops/" + shop + "/page/id/sort/"+sort);
        modelAndView.addObject("sort_buttons_prefix", "/forum/shops/" + shop + "/page/1/sort/");
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/forum/shops/"+shop+"/page/"+i+"/sort/"+sort);

        }
        modelAndView.addObject("additional_results_info", shop);
        modelAndView.addObject("additional_results_info_more", " sklepu");
        modelAndView.addObject("list_of_adresses", listOfAdresses);

        return modelAndView;
    }






}
