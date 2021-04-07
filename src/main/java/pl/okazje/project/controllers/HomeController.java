package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.*;
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
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
    public ModelAndView homePage(HttpServletRequest request) throws MessagingException {

        PagedListHolder page = new PagedListHolder(discountRepository.sortDiscountByDate());
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByDate());
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

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous", "/page/id");
        modelAndView.addObject("picked_sort", 3);
        modelAndView.addObject("sort_buttons_prefix", "/page/1/sort/");
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;
    }

    @GetMapping("/page/{id}/sort/{sort}")
    public ModelAndView homePageSort(@PathVariable("id") String id, @PathVariable("sort") String sort, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("home");
        if (sort.equals("date")) {
            PagedListHolder page = new PagedListHolder(discountRepository.sortDiscountByDate());
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByDate());
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
            PagedListHolder page = new PagedListHolder(discountRepository.sortDiscountByCommentsDay());
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsDay());
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
            PagedListHolder page = new PagedListHolder(discountRepository.sortDiscountByRatingDay());
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingDay());
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
                page = new PagedListHolder(discountRepository.sortDiscountByComments());
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByComments());
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
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWeek());
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsWeek());
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
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsDay());
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsDay());
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
                page = new PagedListHolder(discountRepository.sortDiscountByRating());
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRating());
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
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWeek());
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingWeek());
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
                page = new PagedListHolder(discountRepository.sortDiscountByRatingDay());
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingDay());
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
    public ModelAndView homePage(@PathVariable("id") String id, HttpServletRequest request) {

        PagedListHolder page = new PagedListHolder(discountRepository.sortDiscountByDate());
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByDate());
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
        PagedListHolder page = new PagedListHolder(discountRepository.discountByTag(category));
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.discountByTag(category));
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
        PagedListHolder page = new PagedListHolder(discountRepository.discountByTag(category));
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.discountByTag(category));
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
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenTag(category));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsWithGivenTag(category));
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
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenTagWeek(category));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsWithGivenTagWeek(category));
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
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenTagDay(category));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsWithGivenTagDay(category));
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
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenTag(category));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingWithGivenTag(category));
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
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenTagWeek(category));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingWithGivenTagWeek(category));
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
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenTagDay(category));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingWithGivenTagDay(category));
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
            page = new PagedListHolder(discountRepository.sortDiscountByDateWithGivenTag(category));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByDateWithGivenTag(category));
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
            page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenTagDay(category));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsWithGivenTagDay(category));
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
            page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenTagDay(category));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingWithGivenTagDay(category));
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
        PagedListHolder page = new PagedListHolder(discountRepository.discountByShop(shop));
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.discountByShop(shop));
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
        PagedListHolder page = new PagedListHolder(discountRepository.discountByShop(shop));
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.discountByShop(shop));
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
            page = new PagedListHolder(discountRepository.sortDiscountByDateWithGivenShop(shop));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByDateWithGivenShop(shop));
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
            page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenShopDay(shop));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsWithGivenShopDay(shop));
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
            page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenShopDay(shop));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingWithGivenShopDay(shop));
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
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenShop(shop));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsWithGivenShop(shop));
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
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenShopWeek(shop));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsWithGivenShopWeek(shop));
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
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenShopDay(shop));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsWithGivenShopDay(shop));
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
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenShop(shop));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingWithGivenShop(shop));
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
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenShopWeek(shop));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingWithGivenShopWeek(shop));
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
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenShopDay(shop));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingWithGivenShopDay(shop));
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


        PagedListHolder page = new PagedListHolder(discountRepository.discountBySearchInput("%" + search + "%"));
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.discountBySearchInput("%" + search + "%"));
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
        PagedListHolder page = new PagedListHolder(discountRepository.discountBySearchInput("%" + search + "%"));
        if(request.getSession().getAttribute("filter")!=null){
            if(request.getSession().getAttribute("filter").equals("true")){

                ArrayList<Discount> list = new ArrayList<>(discountRepository.discountBySearchInput("%" + search + "%"));
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
            page = new PagedListHolder(discountRepository.discountBySearchInput("%" + search + "%"));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.discountBySearchInput("%" + search + "%"));
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
            page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenSearchInputDay("%" + search + "%"));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsWithGivenSearchInputDay("%" + search + "%"));
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
            page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenSearchInputDay("%" + search + "%"));
            if(request.getSession().getAttribute("filter")!=null){
                if(request.getSession().getAttribute("filter").equals("true")){

                    ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingWithGivenSearchInputDay("%" + search + "%"));
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
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenSearchInput("%" + search + "%"));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsWithGivenSearchInput("%" + search + "%"));
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
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenSearchInputWeek("%" + search + "%"));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsWithGivenSearchInputWeek("%" + search + "%"));
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
                page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenSearchInputDay("%" + search + "%"));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByCommentsWithGivenSearchInputDay("%" + search + "%"));
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
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenSearchInput("%" + search + "%"));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingWithGivenSearchInput("%" + search + "%"));
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
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenSearchInputWeek("%" + search + "%"));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingWithGivenSearchInputWeek("%" + search + "%"));
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
                page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenSearchInputDay("%" + search + "%"));
                if(request.getSession().getAttribute("filter")!=null){
                    if(request.getSession().getAttribute("filter").equals("true")){

                        ArrayList<Discount> list = new ArrayList<>(discountRepository.sortDiscountByRatingWithGivenSearchInputDay("%" + search + "%"));
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
