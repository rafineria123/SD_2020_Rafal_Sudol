package pl.okazje.project.controllers;

import antlr.ASTNULLType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.Post;
import pl.okazje.project.repositories.DiscountRepository;
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
    DiscountRepository discountRepository;
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

        PagedListHolder page = new PagedListHolder(postRepository.sortPostsByDateWithGivenStatus("ZATWIERDZONE"));
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

        PagedListHolder page = new PagedListHolder(postRepository.sortPostsByDateWithGivenStatus("ZATWIERDZONE"));
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
            page = new PagedListHolder(postRepository.sortPostsByComments());

        }
        if(sort.equals("date")){

            modelAndView.addObject("picked_sort", 3);
            page = new PagedListHolder(postRepository.sortPostsByDateWithGivenStatus("ZATWIERDZONE"));

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

        PagedListHolder page = new PagedListHolder(postRepository.postsBySearchInput("%" + search + "%"));
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

        PagedListHolder page = new PagedListHolder(postRepository.postsBySearchInput("%" + search + "%"));
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
            page = new PagedListHolder(postRepository.sortPostsByCommentsWithGivenSearchInput("%"+search+"%"));

        }
        if(sort.equals("date")){

            modelAndView.addObject("picked_sort", 3);
            page = new PagedListHolder(postRepository.postsBySearchInput("%"+search+"%"));

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


}