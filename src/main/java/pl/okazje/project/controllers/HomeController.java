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
import pl.okazje.project.repositories.DiscountRepository;
import pl.okazje.project.repositories.ShopRepository;
import pl.okazje.project.repositories.TagRepository;
import pl.okazje.project.repositories.UserRepository;
import pl.okazje.project.services.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
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

    private final SessionService sessionService;
    private final AuthenticationService authenticationService;
    private final DiscountService discountService;
    private final TagService tagService;
    private final ShopService shopService;

    @Autowired
    public HomeController(SessionService sessionService, AuthenticationService authenticationService, DiscountService discountService, TagService tagService, ShopService shopService) {
        this.sessionService = sessionService;
        this.authenticationService = authenticationService;
        this.discountService = discountService;
        this.tagService = tagService;
        this.shopService = shopService;
    }

    @GetMapping("/")
    public ModelAndView getAllDiscounts(HttpServletRequest request){
        // TODO: GUEST CANNOT FILTER
        PagedListHolder page = new PagedListHolder(discountService.findAllByOrderByCreationdateDesc());
        page.setPageSize(LoginAndRegisterController.page_size_for_home);
        page.setPage(0);
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagService.findAll());
        modelAndView.addObject("list_of_shops", shopService.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous", "/page/id");
        modelAndView.addObject("picked_sort", 3);
        modelAndView.addObject("sort_buttons_prefix", "/page/1/sort/");
        List<String> listOfAddresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {
            listOfAddresses.add("/page/" + i);
        }
        modelAndView.addObject("list_of_adresses", listOfAddresses);
        return modelAndView;
    }

    @GetMapping("/page/{id}/sort/{sort}")
    public ModelAndView homePageSort(@PathVariable("id") String id, @PathVariable("sort") String sort, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("home");
        if (sort.equals("date")) {
            PagedListHolder page = new PagedListHolder(discountRepository.findAllByOrderByCreationdateDesc());
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByOrderByCreationdateDesc());
                    Iterator<Discount> i = list.iterator();
                    while (i.hasNext()) {
                        Discount d = i.next();
                        if(d.isOutDated()){
                            i.remove();
                        }
                    }

                    page = new PagedListHolder(list);


                }
            }
            page.setPageSize(LoginAndRegisterController.page_size_for_home);
            page.setPage(0);
            modelAndView.addObject("list_of_discounts", page.getPageList());
            modelAndView.addObject("quantity_of_pages", page.getPageCount());
            List<String> listOfAdresses = new ArrayList<>();
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/page/" + i + "/sort/" + sort);

            }
            modelAndView.addObject("list_of_adresses", listOfAdresses);
            modelAndView.addObject("next_and_previous", "/page/id/sort/date");
            modelAndView.addObject("picked_sort", 3);

        }
        if (sort.equals("most-comments")) {
            PagedListHolder page = new PagedListHolder(discountRepository.findAllByCreationdateBetweenNowAndYesterdayOrderByCommentDesc());
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByCreationdateBetweenNowAndYesterdayOrderByCommentDesc());
                    Iterator<Discount> i = list.iterator();
                    while (i.hasNext()) {
                        Discount d = i.next();
                        if(d.isOutDated()){
                            i.remove();
                        }
                    }

                    page = new PagedListHolder(list);


                }
            }
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
            PagedListHolder page = new PagedListHolder(discountRepository.findAllByCreationdateBetweenNowAndYesterdayOrderByRatingDesc());
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByCreationdateBetweenNowAndYesterdayOrderByRatingDesc());
                    Iterator<Discount> i = list.iterator();
                    while (i.hasNext()) {
                        Discount d = i.next();
                        if(d.isOutDated()){
                            i.remove();
                        }
                    }

                    page = new PagedListHolder(list);


                }
            }
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
    public ModelAndView homePageSortTime(@PathVariable("id") String id, @PathVariable("sort") String sort, @PathVariable String time, HttpServletRequest request) {

        PagedListHolder page = new PagedListHolder();
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("home");
        if (sort.equals("most-comments")) {
            modelAndView.addObject("picked_sort", 2);
            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.findAllByOrderByCommentDesc());
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByOrderByCommentDesc());
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.findAllByCreationdateBetweenNowAndLastWeekOrderByCommentDesc());
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByCreationdateBetweenNowAndLastWeekOrderByCommentDesc());
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.findAllByCreationdateBetweenNowAndYesterdayOrderByCommentDesc());
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByCreationdateBetweenNowAndYesterdayOrderByCommentDesc());
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 1);
            }
        }
        if (sort.equals("top-rated")) {
            modelAndView.addObject("picked_sort", 1);
            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.findAllByOrderByRatingDesc());
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByOrderByRatingDesc());
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.findAllByCreationdateBetweenNowAndLastWeekOrderByRatingDesc());
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByCreationdateBetweenNowAndLastWeekOrderByRatingDesc());
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.findAllByCreationdateBetweenNowAndYesterdayOrderByRatingDesc());
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByCreationdateBetweenNowAndYesterdayOrderByRatingDesc());
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
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
    public ModelAndView getAllDiscounts(@PathVariable("id") String id, HttpServletRequest request) {

        PagedListHolder page = new PagedListHolder(discountRepository.findAllByOrderByCreationdateDesc());
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByOrderByCreationdateDesc());
                Iterator<Discount> i = list.iterator();
                while (i.hasNext()) {
                    Discount d = i.next();
                    if(d.isOutDated()){
                        i.remove();
                    }
                }

                page = new PagedListHolder(list);


            }
        }
        page.setPageSize(LoginAndRegisterController.page_size_for_home);
        page.setPage(Integer.parseInt(id)-1);


        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);

        return modelAndView;
    }

    @GetMapping("/categories/{category}")
    public ModelAndView category(@PathVariable("category") String category, HttpServletRequest request) {
        PagedListHolder page = new PagedListHolder(discountRepository.findAllByTagOrderByCreationdateDesc(category));
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByTagOrderByCreationdateDesc(category));
                Iterator<Discount> i = list.iterator();
                while (i.hasNext()) {
                    Discount d = i.next();
                    if(d.isOutDated()){
                        i.remove();
                    }
                }

                page = new PagedListHolder(list);


            }
        }
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
    public ModelAndView categoryPage(@PathVariable("category") String category, @PathVariable("id") String id, HttpServletRequest request) {
        PagedListHolder page = new PagedListHolder(discountRepository.findAllByTagOrderByCreationdateDesc(category));
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByTagOrderByCreationdateDesc(category));
                Iterator<Discount> i = list.iterator();
                while (i.hasNext()) {
                    Discount d = i.next();
                    if(d.isOutDated()){
                        i.remove();
                    }
                }

                page = new PagedListHolder(list);


            }
        }
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
    public ModelAndView categoryPageSortTime(@PathVariable("category") String category, @PathVariable("id") String id, @PathVariable("sort") String sort, @PathVariable("time") String time,
                                              HttpServletRequest request) {
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("home");
        PagedListHolder page = null;

        if (sort.equals("most-comments")) {
            modelAndView.addObject("picked_sort", 2);
            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.findAllByTagOrderByCommentDesc(category));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByTagOrderByCommentDesc(category));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }

                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(category));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(category));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(category));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(category));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 1);
            }
        }

        if (sort.equals("top-rated")) {
            modelAndView.addObject("picked_sort", 1);
            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.findAllByTagOrderByRatingDesc(category));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByTagOrderByRatingDesc(category));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(category));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(category));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(category));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(category));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
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
    public ModelAndView categoryPageSort(@PathVariable("category") String category, @PathVariable("id") String id, @PathVariable("sort") String sort, HttpServletRequest request) {
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("home");
        PagedListHolder page = null;
        if (sort.equals("date")) {
            modelAndView.addObject("picked_sort", 3);
            modelAndView.addObject("next_and_previous", "/categories/" + category + "/page/id/sort/date");
            page = new PagedListHolder(discountRepository.findAllByTagOrderByCreationdateDesc(category));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByTagOrderByCreationdateDesc(category));
                    Iterator<Discount> i = list.iterator();
                    while (i.hasNext()) {
                        Discount d = i.next();
                        if(d.isOutDated()){
                            i.remove();
                        }
                    }

                    page = new PagedListHolder(list);


                }
            }
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
            page = new PagedListHolder(discountRepository.findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(category));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(category));
                    Iterator<Discount> i = list.iterator();
                    while (i.hasNext()) {
                        Discount d = i.next();
                        if(d.isOutDated()){
                            i.remove();
                        }
                    }

                    page = new PagedListHolder(list);


                }
            }
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
            page = new PagedListHolder(discountRepository.findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(category));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(category));
                    Iterator<Discount> i = list.iterator();
                    while (i.hasNext()) {
                        Discount d = i.next();
                        if(d.isOutDated()){
                            i.remove();
                        }
                    }

                    page = new PagedListHolder(list);


                }
            }
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
    public ModelAndView shop(@PathVariable("shop") String shop, HttpServletRequest request) {
        PagedListHolder page = new PagedListHolder(discountRepository.findAllByShopOrderByCreationdateDesc(shop));
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByShopOrderByCreationdateDesc(shop));
                Iterator<Discount> i = list.iterator();
                while (i.hasNext()) {
                    Discount d = i.next();
                    if(d.isOutDated()){
                        i.remove();
                    }
                }

                page = new PagedListHolder(list);


            }
        }
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
    public ModelAndView shopPage(@PathVariable("shop") String shop, @PathVariable("id") String id, HttpServletRequest request) {
        PagedListHolder page = new PagedListHolder(discountRepository.findAllByShopOrderByCreationdateDesc(shop));
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByShopOrderByCreationdateDesc(shop));
                Iterator<Discount> i = list.iterator();
                while (i.hasNext()) {
                    Discount d = i.next();
                    if(d.isOutDated()){
                        i.remove();
                    }
                }

                page = new PagedListHolder(list);


            }
        }
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
    public ModelAndView shopPageSort(@PathVariable("shop") String shop, @PathVariable("id") String id, @PathVariable("sort") String sort, HttpServletRequest request) {
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("home");
        PagedListHolder page = null;
        if (sort.equals("date")) {
            modelAndView.addObject("picked_sort", 3);
            modelAndView.addObject("next_and_previous", "/shops/" + shop + "/page/id/sort/date");
            page = new PagedListHolder(discountRepository.findAllByShopOrderByCreationdateDesc(shop));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByShopOrderByCreationdateDesc(shop));
                    Iterator<Discount> i = list.iterator();
                    while (i.hasNext()) {
                        Discount d = i.next();
                        if(d.isOutDated()){
                            i.remove();
                        }
                    }

                    page = new PagedListHolder(list);


                }
            }
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
            page = new PagedListHolder(discountRepository.findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(shop));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(shop));
                    Iterator<Discount> i = list.iterator();
                    while (i.hasNext()) {
                        Discount d = i.next();
                        if(d.isOutDated()){
                            i.remove();
                        }
                    }

                    page = new PagedListHolder(list);


                }
            }
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
            page = new PagedListHolder(discountRepository.findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(shop));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(shop));
                    Iterator<Discount> i = list.iterator();
                    while (i.hasNext()) {
                        Discount d = i.next();
                        if(d.isOutDated()){
                            i.remove();
                        }
                    }

                    page = new PagedListHolder(list);


                }
            }
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
    public ModelAndView shopPageSortTime(@PathVariable("shop") String shop, @PathVariable("id") String id, @PathVariable("sort") String sort, @PathVariable("time") String time,
                                         HttpServletRequest request) {
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("home");
        PagedListHolder page = null;

        if (sort.equals("most-comments")) {
            modelAndView.addObject("picked_sort", 2);

            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.findAllByShopOrderByCommentDesc(shop));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByShopOrderByCommentDesc(shop));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(shop));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(shop));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(shop));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(shop));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 1);
            }

        }

        if (sort.equals("top-rated")) {
            modelAndView.addObject("picked_sort", 1);
            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.findAllByShopOrderByRatingDesc(shop));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByShopOrderByRatingDesc(shop));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(shop));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(shop));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(shop));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(shop));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
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
    public ModelAndView search(@PathVariable("search") String search, HttpServletRequest request) {
        PagedListHolder page = new PagedListHolder(discountRepository.FindAllBySearchOrderByCreationdateDesc("%" + search + "%"));
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.FindAllBySearchOrderByCreationdateDesc("%" + search + "%"));
                Iterator<Discount> i = list.iterator();
                while (i.hasNext()) {
                    Discount d = i.next();
                    if(d.isOutDated()){
                        i.remove();
                    }
                }

                page = new PagedListHolder(list);


            }
        }
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
    public ModelAndView searchPage(@PathVariable("search") String search, @PathVariable("id") String id, HttpServletRequest request) {
        PagedListHolder page = new PagedListHolder(discountRepository.FindAllBySearchOrderByCreationdateDesc("%" + search + "%"));
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.FindAllBySearchOrderByCreationdateDesc("%" + search + "%"));
                Iterator<Discount> i = list.iterator();
                while (i.hasNext()) {
                    Discount d = i.next();
                    if(d.isOutDated()){
                        i.remove();
                    }
                }

                page = new PagedListHolder(list);


            }
        }
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
    public ModelAndView searchPageSort(@PathVariable("search") String search, @PathVariable("id") String id, @PathVariable("sort") String sort, HttpServletRequest request) {
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("home");
        PagedListHolder page = null;
        if (sort.equals("date")) {
            modelAndView.addObject("picked_sort", 3);
            modelAndView.addObject("next_and_previous", "/search/" + search + "/page/id/sort/date");
            page = new PagedListHolder(discountRepository.FindAllBySearchOrderByCreationdateDesc("%" + search + "%"));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.FindAllBySearchOrderByCreationdateDesc("%" + search + "%"));
                    Iterator<Discount> i = list.iterator();
                    while (i.hasNext()) {
                        Discount d = i.next();
                        if(d.isOutDated()){
                            i.remove();
                        }
                    }

                    page = new PagedListHolder(list);


                }
            }
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
            page = new PagedListHolder(discountRepository.FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc("%" + search + "%"));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc("%" + search + "%"));
                    Iterator<Discount> i = list.iterator();
                    while (i.hasNext()) {
                        Discount d = i.next();
                        if(d.isOutDated()){
                            i.remove();
                        }
                    }

                    page = new PagedListHolder(list);


                }
            }
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
            page = new PagedListHolder(discountRepository.FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc("%" + search + "%"));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc("%" + search + "%"));
                    Iterator<Discount> i = list.iterator();
                    while (i.hasNext()) {
                        Discount d = i.next();
                        if(d.isOutDated()){
                            i.remove();
                        }
                    }

                    page = new PagedListHolder(list);


                }
            }
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
    public ModelAndView searchPageSortTime(@PathVariable("search") String search, @PathVariable("id") String id, @PathVariable("sort") String sort, @PathVariable("time") String time,
                                           HttpServletRequest request) {
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("home");
        PagedListHolder page = null;
        if (sort.equals("most-comments")) {
            modelAndView.addObject("picked_sort", 2);
            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.FindAllBySearchOrderByCommentDesc("%" + search + "%"));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.FindAllBySearchOrderByCommentDesc("%" + search + "%"));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc("%" + search + "%"));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc("%" + search + "%"));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc("%" + search + "%"));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc("%" + search + "%"));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 1);
            }


        }

        if (sort.equals("top-rated")) {
            modelAndView.addObject("picked_sort", 1);
            if (time.equals("all")) {
                page = new PagedListHolder(discountRepository.FindAllBySearchOrderByRatingDesc("%" + search + "%"));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.FindAllBySearchOrderByRatingDesc("%" + search + "%"));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 3);
            }
            if (time.equals("week")) {
                page = new PagedListHolder(discountRepository.FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc("%" + search + "%"));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc("%" + search + "%"));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
                modelAndView.addObject("picked_time", 2);
            }
            if (time.equals("day")) {
                page = new PagedListHolder(discountRepository.FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc("%" + search + "%"));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc("%" + search + "%"));
                        Iterator<Discount> i = list.iterator();
                        while (i.hasNext()) {
                            Discount d = i.next();
                            if(d.isOutDated()){
                                i.remove();
                            }
                        }

                        page = new PagedListHolder(list);


                    }
                }
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

    @PostMapping("/filter")
    public String filter(HttpServletRequest request){
        if(request.getSession().getAttribute("filter")==null){
            request.getSession().setAttribute("filter", "true");
        }else{
            if(request.getSession().getAttribute("filter").equals("true")){
                request.getSession().setAttribute("filter", "false");
            }else{
                request.getSession().setAttribute("filter","true");
            }
        }

        return "redirect:"+request.getHeader("referer");
    }
}
