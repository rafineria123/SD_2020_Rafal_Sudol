package pl.okazje.project.controllers;

import antlr.ASTNULLType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.Post;
import pl.okazje.project.repositories.DiscountRepository;
import pl.okazje.project.repositories.PostRepository;
import pl.okazje.project.repositories.ShopRepository;
import pl.okazje.project.repositories.TagRepository;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostsController {

    @Autowired
    ShopRepository shopRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    PostRepository postRepository;

    static int page_size_for_home = 4;

    @GetMapping("/forum")
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

}
