package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.User;
import pl.okazje.project.repositories.DiscountRepository;
import pl.okazje.project.repositories.ShopRepository;
import pl.okazje.project.repositories.TagRepository;
import pl.okazje.project.repositories.UserRepository;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public ModelAndView homePage() throws MessagingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findUserByLogin(authentication.getName());
        Pageable pageable = PageRequest.of(0, LoginAndRegisterController.page_size_for_home, Sort.by("creationdate").descending());
        Page<Discount> allProducts = discountRepository.findAll(pageable);


        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", allProducts.getContent());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", allProducts.getTotalPages());
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous", "/page/id");
        modelAndView.addObject("picked_sort", 3);
        modelAndView.addObject("sort_buttons_prefix", "/page/1/sort/");
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= allProducts.getTotalPages(); i++) {

            listOfAdresses.add("/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
//        Thread t = new Thread(() -> {
//            try {
//                sendMail.sendingMail("rafineria123@gmail.com","witam","witam witam");
//            } catch (MessagingException e) {
//                e.printStackTrace();
//            }
//
//        });
//        t.start();

        return modelAndView;
    }

    @GetMapping("/page/{id}/sort/{sort}")
    public ModelAndView homePageSort(@PathVariable("id") String id, @PathVariable("sort") String sort) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findUserByLogin(authentication.getName());
        Pageable pageable;
        ModelAndView modelAndView = new ModelAndView("home");
        if (sort.equals("date")) {
            pageable = PageRequest.of(Integer.parseInt(id) - 1, LoginAndRegisterController.page_size_for_home, Sort.by("creationdate").descending());
            Page<Discount> allProducts = discountRepository.findAll(pageable);
            modelAndView.addObject("list_of_discounts", allProducts.getContent());
            modelAndView.addObject("quantity_of_pages", allProducts.getTotalPages());
            List<String> listOfAdresses = new ArrayList<>();
            for (int i = 1; i <= allProducts.getTotalPages(); i++) {

                listOfAdresses.add("/page/" + i + "/sort/" + sort);

            }
            modelAndView.addObject("list_of_adresses", listOfAdresses);
            modelAndView.addObject("next_and_previous", "/page/id/sort/date");
            modelAndView.addObject("picked_sort", 3);

        }
        if (sort.equals("most-comments")) {
            PagedListHolder page = new PagedListHolder(discountRepository.sortDiscountByCommentsDay());
            page.setPageSize(LoginAndRegisterController.page_size_for_home); // number of items per page
            page.setPage(Integer.parseInt(id) - 1);      // set to first page
            modelAndView.addObject("list_of_discounts", page.getPageList());
            modelAndView.addObject("quantity_of_pages", page.getPageCount());
            modelAndView.addObject("picked_sort", 2);
            modelAndView.addObject("time_buttons_prefix", "/page/1/sort/most-comments/time/");
            modelAndView.addObject("picked_time", 1);
            List<String> listOfAdresses = new ArrayList<>();
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/page/" + i + "/sort/" + sort);

            }
            modelAndView.addObject("list_of_adresses", listOfAdresses);
            modelAndView.addObject("next_and_previous", "/page/id/sort/most-comments");
        }
        if (sort.equals("top-rated")) {
            PagedListHolder page = new PagedListHolder(discountRepository.sortDiscountByRatingDay());
            page.setPageSize(LoginAndRegisterController.page_size_for_home); // number of items per page
            page.setPage(Integer.parseInt(id) - 1);      // set to first page
            modelAndView.addObject("list_of_discounts", page.getPageList());
            modelAndView.addObject("quantity_of_pages", page.getPageCount());
            modelAndView.addObject("time_buttons_prefix", "/page/1/sort/top-rated/time/");
            modelAndView.addObject("picked_time", 1);
            modelAndView.addObject("picked_sort", 1);
            List<String> listOfAdresses = new ArrayList<>();
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/page/" + i + "/sort/" + sort);

            }
            modelAndView.addObject("list_of_adresses", listOfAdresses);
            modelAndView.addObject("next_and_previous", "/page/id/sort/top-rated");
        }

        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("sort_buttons_prefix", "/page/1/sort/");
        return modelAndView;
    }

    @GetMapping("/page/{id}/sort/{sort}/time/{time}")
    public ModelAndView homePageSortTime(@PathVariable("id") String id, @PathVariable("sort") String sort, @PathVariable String time) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findUserByLogin(authentication.getName());
        PagedListHolder page = new PagedListHolder();
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("home");
        if (sort.equals("most-comments")) {
            modelAndView.addObject("picked_sort", 2);
            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.sortDiscountByComments());
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWeek());
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsDay());
                modelAndView.addObject("picked_time", 1);
            }
        }
        if (sort.equals("top-rated")) {
            modelAndView.addObject("picked_sort", 1);
            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.sortDiscountByRating());
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWeek());
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.sortDiscountByRatingDay());
                modelAndView.addObject("picked_time", 1);
            }
        }
        page.setPageSize(LoginAndRegisterController.page_size_for_home); // number of items per page
        page.setPage(Integer.parseInt(id) - 1);      // set to first page
        for (int i = 1; i <= page.getPageCount(); i++) {
            listOfAdresses.add("/page/" + i + "/sort/" + sort + "/time/" + time);
        }
        modelAndView.addObject("next_and_previous", "/page/id/sort/" + sort + "/time/" + time);
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("time_buttons_prefix", "/page/1/sort/" + sort + "/time/");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("sort_buttons_prefix", "/page/1/sort/");
        return modelAndView;
    }

    @GetMapping("/page/{id}")
    public ModelAndView homePage(@PathVariable("id") String id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findUserByLogin(authentication.getName());

        Pageable pageable = PageRequest.of(Integer.parseInt(id) - 1, LoginAndRegisterController.page_size_for_home, Sort.by("creationdate").descending());
        Page<Discount> allProducts = discountRepository.findAll(pageable);


        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", allProducts.getContent());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", allProducts.getTotalPages());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= allProducts.getTotalPages(); i++) {

            listOfAdresses.add("/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);

        return modelAndView;
    }

    @GetMapping("/categories/{category}")
    public ModelAndView category(@PathVariable("category") String category) {
        PagedListHolder page = new PagedListHolder(discountRepository.discountByTag(category));
        page.setPageSize(LoginAndRegisterController.page_size_for_cat_and_shops); // number of items per page
        page.setPage(0);

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous", "/categories/" + category + "/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/categories/" + category + "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/categories/" + category + "/page/" + i);

        }
        modelAndView.addObject("additional_results_info", category);
        modelAndView.addObject("additional_results_info_more", " kategorii");
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;

    }

    @GetMapping("/categories/{category}/page/{id}")
    public ModelAndView categoryPage(@PathVariable("category") String category, @PathVariable("id") String id) {
        PagedListHolder page = new PagedListHolder(discountRepository.discountByTag(category));
        page.setPageSize(LoginAndRegisterController.page_size_for_cat_and_shops); // number of items per page
        page.setPage(Integer.parseInt(id) - 1);
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/categories/" + category + "/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/categories/" + category + "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/categories/" + category + "/page/" + i);

        }
        modelAndView.addObject("additional_results_info", category);
        modelAndView.addObject("additional_results_info_more", " kategorii");
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;

    }

    @GetMapping("/categories/{category}/page/{id}/sort/{sort}/time/{time}")
    public ModelAndView categoryPageSortTime(@PathVariable("category") String category, @PathVariable("id") String id, @PathVariable("sort") String sort, @PathVariable("time") String time) {
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("home");
        PagedListHolder page = null;

        if (sort.equals("most-comments")) {
            modelAndView.addObject("picked_sort", 2);
            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenTag(category));
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenTagWeek(category));
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenTagDay(category));
                modelAndView.addObject("picked_time", 1);
            }
        }

        if (sort.equals("top-rated")) {
            modelAndView.addObject("picked_sort", 1);
            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenTag(category));
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenTagWeek(category));
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenTagDay(category));
                modelAndView.addObject("picked_time", 1);
            }
        }

        page.setPage(Integer.parseInt(id) - 1);
        page.setPageSize(LoginAndRegisterController.page_size_for_cat_and_shops);
        for (int i = 1; i <= page.getPageCount(); i++) {
            listOfAdresses.add("/categories/" + category + "/page/" + i + "/sort/" + sort + "/time/" + time);
        }
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/categories/" + category + "/page/id/sort/" + sort + "/time/" + time);
        modelAndView.addObject("time_buttons_prefix", "/categories/" + category + "/page/1/sort/" + sort + "/time/");
        modelAndView.addObject("sort_buttons_prefix", "/categories/" + category + "/page/1/sort/");
        modelAndView.addObject("additional_results_info", category);
        modelAndView.addObject("additional_results_info_more", " kategorii");
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;

    }

    @GetMapping("/categories/{category}/page/{id}/sort/{sort}")
    public ModelAndView categoryPageSort(@PathVariable("category") String category, @PathVariable("id") String id, @PathVariable("sort") String sort) {
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("home");
        PagedListHolder page = null;
        if (sort.equals("date")) {
            modelAndView.addObject("picked_sort", 3);
            modelAndView.addObject("next_and_previous", "/categories/" + category + "/page/id/sort/date");
            page = new PagedListHolder(discountRepository.sortDiscountByDateWithGivenTag(category));
            page.setPageSize(LoginAndRegisterController.page_size_for_cat_and_shops);
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/categories/" + category + "/page/" + i + "/sort/date");

            }
        }

        if (sort.equals("most-comments")) {
            modelAndView.addObject("picked_sort", 2);
            modelAndView.addObject("next_and_previous", "/categories/" + category + "/page/id/sort/most-comments");
            modelAndView.addObject("time_buttons_prefix", "/categories/" + category + "/page/1/sort/most-comments/time/");
            modelAndView.addObject("picked_time", 1);
            page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenTagDay(category));
            page.setPageSize(LoginAndRegisterController.page_size_for_cat_and_shops);
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/categories/" + category + "/page/" + i + "/sort/most-comments");

            }
        }

        if (sort.equals("top-rated")) {
            modelAndView.addObject("picked_sort", 1);
            modelAndView.addObject("next_and_previous", "/categories/" + category + "/page/id/sort/top-rated");
            modelAndView.addObject("time_buttons_prefix", "/categories/" + category + "/page/1/sort/top-rated/time/");
            modelAndView.addObject("picked_time", 1);
            page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenTagDay(category));
            page.setPageSize(LoginAndRegisterController.page_size_for_cat_and_shops);
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/categories/" + category + "/page/" + i + "/sort/top-rated");

            }
        }

        page.setPage(Integer.parseInt(id) - 1);
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));

        modelAndView.addObject("sort_buttons_prefix", "/categories/" + category + "/page/1/sort/");
        modelAndView.addObject("additional_results_info", category);
        modelAndView.addObject("additional_results_info_more", " kategorii");
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;

    }


    @GetMapping("/shops/{shop}")
    public ModelAndView shop(@PathVariable("shop") String shop) {
        PagedListHolder page = new PagedListHolder(discountRepository.discountByShop(shop));
        page.setPageSize(LoginAndRegisterController.page_size_for_cat_and_shops); // number of items per page
        page.setPage(0);

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous", "/shops/" + shop + "/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/shops/" + shop + "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/shops/" + shop + "/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", shop);
        modelAndView.addObject("additional_results_info_more", " sklepu");
        return modelAndView;
    }

    @GetMapping("/shops/{shop}/page/{id}")
    public ModelAndView shopPage(@PathVariable("shop") String shop, @PathVariable("id") String id) {
        PagedListHolder page = new PagedListHolder(discountRepository.discountByShop(shop));
        page.setPageSize(LoginAndRegisterController.page_size_for_cat_and_shops); // number of items per page
        page.setPage(Integer.parseInt(id) - 1);
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/shops/" + shop + "/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/shops/" + shop + "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/shops/" + shop + "/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", shop);
        modelAndView.addObject("additional_results_info_more", " sklepu");
        return modelAndView;

    }

    @GetMapping("/shops/{shop}/page/{id}/sort/{sort}")
    public ModelAndView shopPageSort(@PathVariable("shop") String shop, @PathVariable("id") String id, @PathVariable("sort") String sort) {
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("home");
        PagedListHolder page = null;
        if (sort.equals("date")) {
            modelAndView.addObject("picked_sort", 3);
            modelAndView.addObject("next_and_previous", "/shops/" + shop + "/page/id/sort/date");
            page = new PagedListHolder(discountRepository.sortDiscountByDateWithGivenShop(shop));
            page.setPageSize(LoginAndRegisterController.page_size_for_cat_and_shops);
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/shops/" + shop + "/page/" + i + "/sort/date");

            }
        }

        if (sort.equals("most-comments")) {
            modelAndView.addObject("picked_sort", 2);
            modelAndView.addObject("next_and_previous", "/shops/" + shop + "/page/id/sort/most-comments");
            modelAndView.addObject("time_buttons_prefix", "/shops/" + shop + "/page/1/sort/most-comments/time/");
            modelAndView.addObject("picked_time", 1);
            page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenShopDay(shop));
            page.setPageSize(LoginAndRegisterController.page_size_for_cat_and_shops);
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/shops/" + shop + "/page/" + i + "/sort/most-comments");

            }
        }

        if (sort.equals("top-rated")) {
            modelAndView.addObject("picked_sort", 1);
            modelAndView.addObject("next_and_previous", "/shops/" + shop + "/page/id/sort/top-rated");
            modelAndView.addObject("time_buttons_prefix", "/shops/" + shop + "/page/1/sort/top-rated/time/");
            modelAndView.addObject("picked_time", 1);
            page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenShopDay(shop));
            page.setPageSize(LoginAndRegisterController.page_size_for_cat_and_shops);
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/shops/" + shop + "/page/" + i + "/sort/top-rated");

            }
        }

        page.setPage(Integer.parseInt(id) - 1);
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));

        modelAndView.addObject("sort_buttons_prefix", "/shops/" + shop + "/page/1/sort/");

        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", shop);
        modelAndView.addObject("additional_results_info_more", " sklepu");
        return modelAndView;

    }

    @GetMapping("/shops/{shop}/page/{id}/sort/{sort}/time/{time}")
    public ModelAndView shopPageSortTime(@PathVariable("shop") String shop, @PathVariable("id") String id, @PathVariable("sort") String sort, @PathVariable("time") String time) {
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("home");
        PagedListHolder page = null;

        if (sort.equals("most-comments")) {
            modelAndView.addObject("picked_sort", 2);

            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenShop(shop));
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenShopWeek(shop));
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenShopDay(shop));
                modelAndView.addObject("picked_time", 1);
            }

        }

        if (sort.equals("top-rated")) {
            modelAndView.addObject("picked_sort", 1);
            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenShop(shop));
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenShopWeek(shop));
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenShopDay(shop));
                modelAndView.addObject("picked_time", 1);
            }
        }

        page.setPage(Integer.parseInt(id) - 1);
        page.setPageSize(LoginAndRegisterController.page_size_for_cat_and_shops);
        for (int i = 1; i <= page.getPageCount(); i++) {
            listOfAdresses.add("/shops/" + shop + "/page/" + i + "/sort/" + sort + "/time/" + time);
        }
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/shops/" + shop + "/page/id/sort/" + sort + "/time/" + time);
        modelAndView.addObject("time_buttons_prefix", "/shops/" + shop + "/page/1/sort/" + sort + "/time/");
        modelAndView.addObject("sort_buttons_prefix", "/shops/" + shop + "/page/1/sort/");
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", shop);
        modelAndView.addObject("additional_results_info_more", " sklepu");
        return modelAndView;

    }

    @PostMapping("/search")
    public ModelAndView searchForm(@ModelAttribute("searchform") String searchform) throws UnsupportedEncodingException {
        String encodedId = URLEncoder.encode(searchform, "UTF-8").replace("+", "%20");
        ModelAndView m = new ModelAndView(new RedirectView("/search/" + encodedId, true, true, false));
        return m;

    }

    @GetMapping("/search/{search}")
    public ModelAndView search(@PathVariable("search") String search) {


        PagedListHolder page = new PagedListHolder(discountRepository.discountBySearchInput("%" + search + "%"));
        page.setPageSize(LoginAndRegisterController.page_size_for_cat_and_shops); // number of items per page
        page.setPage(0);

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous", "/search/" + search + "/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/search/" + search + "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/search/" + search + "/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", search);
        return modelAndView;

    }

    @GetMapping("/search/{search}/page/{id}")
    public ModelAndView searchPage(@PathVariable("search") String search, @PathVariable("id") String id) {
        PagedListHolder page = new PagedListHolder(discountRepository.discountBySearchInput("%" + search + "%"));
        page.setPageSize(1); // number of items per page
        page.setPage(Integer.parseInt(id) - 1);
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/search/" + search + "/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/search/" + search + "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/search/" + search + "/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", search);
        return modelAndView;

    }

    @GetMapping("/search/{search}/page/{id}/sort/{sort}")
    public ModelAndView searchPageSort(@PathVariable("search") String search, @PathVariable("id") String id, @PathVariable("sort") String sort) {
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("home");
        PagedListHolder page = null;
        if (sort.equals("date")) {
            modelAndView.addObject("picked_sort", 3);
            modelAndView.addObject("next_and_previous", "/search/" + search + "/page/id/sort/date");
            page = new PagedListHolder(discountRepository.discountBySearchInput("%" + search + "%"));
            page.setPageSize(1);
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/search/" + search + "/page/" + i + "/sort/date");

            }
        }

        if (sort.equals("most-comments")) {
            modelAndView.addObject("picked_sort", 2);
            modelAndView.addObject("next_and_previous", "/search/" + search + "/page/id/sort/most-comments");
            modelAndView.addObject("time_buttons_prefix", "/search/" + search + "/page/1/sort/most-comments/time/");
            modelAndView.addObject("picked_time", 1);
            page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenSearchInputDay("%" + search + "%"));
            page.setPageSize(1);
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/search/" + search + "/page/" + i + "/sort/most-comments");

            }
        }

        if (sort.equals("top-rated")) {
            modelAndView.addObject("picked_sort", 1);
            modelAndView.addObject("next_and_previous", "/search/" + search + "/page/id/sort/top-rated");
            modelAndView.addObject("time_buttons_prefix", "/search/" + search + "/page/1/sort/top-rated/time/");
            modelAndView.addObject("picked_time", 1);
            page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenSearchInputDay("%" + search + "%"));
            page.setPageSize(1);
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/search/" + search + "/page/" + i + "/sort/top-rated");

            }
        }

        page.setPage(Integer.parseInt(id) - 1);
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));

        modelAndView.addObject("sort_buttons_prefix", "/search/" + search + "/page/1/sort/");

        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", search);
        return modelAndView;

    }

    @GetMapping("/search/{search}/page/{id}/sort/{sort}/time/{time}")
    public ModelAndView searchPageSortTime(@PathVariable("search") String search, @PathVariable("id") String id, @PathVariable("sort") String sort, @PathVariable("time") String time) {
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("home");
        PagedListHolder page = null;
        if (sort.equals("most-comments")) {
            modelAndView.addObject("picked_sort", 2);
            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenSearchInput("%" + search + "%"));
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenSearchInputWeek("%" + search + "%"));
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenSearchInputDay("%" + search + "%"));
                modelAndView.addObject("picked_time", 1);
            }


        }

        if (sort.equals("top-rated")) {
            modelAndView.addObject("picked_sort", 1);
            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenSearchInput("%" + search + "%"));
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenSearchInputWeek("%" + search + "%"));
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenSearchInputDay("%" + search + "%"));
                modelAndView.addObject("picked_time", 1);
            }
        }

        page.setPage(Integer.parseInt(id) - 1);
        page.setPageSize(LoginAndRegisterController.page_size_for_cat_and_shops);
        for (int i = 1; i <= page.getPageCount(); i++) {
            listOfAdresses.add("/search/" + search + "/page/" + i + "/sort/" + sort + "/time/" + time);
        }
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/search/" + search + "/page/id/sort/" + sort + "/time/" + time);
        modelAndView.addObject("time_buttons_prefix", "/search/" + search + "/page/1/sort/" + sort + "/time/");
        modelAndView.addObject("sort_buttons_prefix", "/search/" + search + "/page/1/sort/");
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", search);
        return modelAndView;

    }

}
