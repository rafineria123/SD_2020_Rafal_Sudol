package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.entities.*;
import pl.okazje.project.repositories.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    InformationRepository informationRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    MessageRepository messageRepository;

    private static User uzytkownik = null;

    private static int page_size_for_home = 8;

    private static int page_size_for_cat_and_shops = 4;

    @GetMapping("/")
    public ModelAndView homePage() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());
        Pageable pageable = PageRequest.of(0, page_size_for_home, Sort.by("creationdate").descending());
        Page<Discount> allProducts = discountRepository.findAll(pageable);

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", allProducts.getContent());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", allProducts.getTotalPages());
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous","/page/id");
        modelAndView.addObject("picked_sort", 3);
        modelAndView.addObject("sort_buttons_prefix", "/page/1/sort/");
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1;i<=allProducts.getTotalPages();i++){

            listOfAdresses.add("/page/"+i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;
    }


    @GetMapping("/page/{id}/sort/{sort}")
    public ModelAndView homePageSort(@PathVariable("id") String id,@PathVariable("sort") String sort) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());
        Pageable pageable = null;
        ModelAndView modelAndView = new ModelAndView("home");
        if(sort.equals("date")){
            pageable = PageRequest.of(Integer.parseInt(id)-1, page_size_for_home, Sort.by("creationdate").descending());
            Page<Discount> allProducts = discountRepository.findAll(pageable);
            modelAndView.addObject("list_of_discounts", allProducts.getContent());
            modelAndView.addObject("quantity_of_pages", allProducts.getTotalPages());
            List<String> listOfAdresses = new ArrayList<>();
            for (int i = 1;i<=allProducts.getTotalPages();i++){

                listOfAdresses.add("/page/"+i+"/sort/"+sort);

            }
            modelAndView.addObject("list_of_adresses", listOfAdresses);
            modelAndView.addObject("next_and_previous","/page/id/sort/date");
            modelAndView.addObject("picked_sort", 3);

        }
        if(sort.equals("most-comments")){
            PagedListHolder page = new PagedListHolder(discountRepository.sortDiscountByComments());
            page.setPageSize(page_size_for_home); // number of items per page
            page.setPage(Integer.parseInt(id)-1);      // set to first page
            modelAndView.addObject("list_of_discounts", page.getPageList());
            modelAndView.addObject("quantity_of_pages", page.getPageCount());
            modelAndView.addObject("picked_sort", 2);
            List<String> listOfAdresses = new ArrayList<>();
            for (int i = 1;i<=page.getPageCount();i++){

                listOfAdresses.add("/page/"+i+"/sort/"+sort);

            }
            modelAndView.addObject("list_of_adresses", listOfAdresses);
            modelAndView.addObject("next_and_previous","/page/id/sort/most-comments");
        }
        if(sort.equals("top-rated")){
            PagedListHolder page = new PagedListHolder(discountRepository.sortDiscountByRating());
            page.setPageSize(page_size_for_home); // number of items per page
            page.setPage(Integer.parseInt(id)-1);      // set to first page
            modelAndView.addObject("list_of_discounts", page.getPageList());
            modelAndView.addObject("quantity_of_pages", page.getPageCount());
            modelAndView.addObject("picked_sort", 1);
            List<String> listOfAdresses = new ArrayList<>();
            for (int i = 1;i<=page.getPageCount();i++){

                listOfAdresses.add("/page/"+i+"/sort/"+sort);

            }
            modelAndView.addObject("list_of_adresses", listOfAdresses);
            modelAndView.addObject("next_and_previous","/page/id/sort/top-rated");
        }

        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("sort_buttons_prefix", "/page/1/sort/");
        return modelAndView;
    }

    @GetMapping("/page/{id}")
    public ModelAndView homePage(@PathVariable("id") String id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());

        Pageable pageable = PageRequest.of(Integer.parseInt(id)-1, page_size_for_home, Sort.by("creationdate").descending());
        Page<Discount> allProducts = discountRepository.findAll(pageable);


        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", allProducts.getContent());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", allProducts.getTotalPages());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous","/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1;i<=allProducts.getTotalPages();i++){

            listOfAdresses.add("/page/"+i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);

        return modelAndView;
    }


    @GetMapping("/categories/{category}")
    public ModelAndView category(@PathVariable("category") String category) {
        PagedListHolder page = new PagedListHolder(discountRepository.discountByTag(category));
        page.setPageSize(page_size_for_cat_and_shops); // number of items per page
        page.setPage(0);

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous","/categories/"+category+"/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/categories/"+category+"/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1;i<=page.getPageCount();i++){

            listOfAdresses.add("/categories/"+category+"/page/"+i);

        }
        modelAndView.addObject("additional_results_info", category);
        modelAndView.addObject("additional_results_info_more", " kategorii");
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;

    }

    @GetMapping("/categories/{category}/page/{id}")
    public ModelAndView categoryPage(@PathVariable("category") String category, @PathVariable("id") String id) {
        PagedListHolder page = new PagedListHolder(discountRepository.discountByTag(category));
        page.setPageSize(page_size_for_cat_and_shops); // number of items per page
        page.setPage(Integer.parseInt(id)-1);
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous","/categories/"+category+"/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/categories/"+category+"/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1;i<=page.getPageCount();i++){

            listOfAdresses.add("/categories/"+category+"/page/"+i);

        }
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
        if(sort.equals("date")){
            modelAndView.addObject("picked_sort", 3);
            modelAndView.addObject("next_and_previous","/categories/"+category+"/page/id/sort/date");
            page = new PagedListHolder(discountRepository.sortDiscountByDateWithGivenTag(category));
            page.setPageSize(page_size_for_cat_and_shops);
            for (int i = 1;i<=page.getPageCount();i++){

                listOfAdresses.add("/categories/"+category+"/page/"+i+"/sort/date");

            }
        }

        if(sort.equals("most-comments")){
            modelAndView.addObject("picked_sort", 2);
            modelAndView.addObject("next_and_previous","/categories/"+category+"/page/id/sort/most-comments");
            page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenTag(category));
            page.setPageSize(page_size_for_cat_and_shops);
            for (int i = 1;i<=page.getPageCount();i++){

                listOfAdresses.add("/categories/"+category+"/page/"+i+"/sort/most-comments");

            }
        }

        if(sort.equals("top-rated")){
            modelAndView.addObject("picked_sort", 1);
            modelAndView.addObject("next_and_previous","/categories/"+category+"/page/id/sort/top-rated");
            page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenTag(category));
            page.setPageSize(page_size_for_cat_and_shops);
            for (int i = 1;i<=page.getPageCount();i++){

                listOfAdresses.add("/categories/"+category+"/page/"+i+"/sort/top-rated");

            }
        }

        page.setPage(Integer.parseInt(id)-1);
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));

        modelAndView.addObject("sort_buttons_prefix", "/categories/"+category+"/page/1/sort/");
        modelAndView.addObject("additional_results_info", category);
        modelAndView.addObject("additional_results_info_more", " kategorii");
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;

    }


    @GetMapping("/shops/{shop}")
    public ModelAndView shop(@PathVariable("shop") String shop) {
        PagedListHolder page = new PagedListHolder(discountRepository.discountByShop(shop));
        page.setPageSize(page_size_for_cat_and_shops); // number of items per page
        page.setPage(0);

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous","/shops/"+shop+"/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/shops/"+shop+"/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1;i<=page.getPageCount();i++){

            listOfAdresses.add("/shops/"+shop+"/page/"+i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", shop);
        modelAndView.addObject("additional_results_info_more", " sklepu");
        return modelAndView;
    }

    @GetMapping("/shops/{shop}/page/{id}")
    public ModelAndView shopPage(@PathVariable("shop") String shop, @PathVariable("id") String id) {
        PagedListHolder page = new PagedListHolder(discountRepository.discountByShop(shop));
        page.setPageSize(page_size_for_cat_and_shops); // number of items per page
        page.setPage(Integer.parseInt(id)-1);
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous","/shops/"+shop+"/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/shops/"+shop+"/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1;i<=page.getPageCount();i++){

            listOfAdresses.add("/shops/"+shop+"/page/"+i);

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
        if(sort.equals("date")){
            modelAndView.addObject("picked_sort", 3);
            modelAndView.addObject("next_and_previous","/shops/"+shop+"/page/id/sort/date");
            page = new PagedListHolder(discountRepository.sortDiscountByDateWithGivenShop(shop));
            page.setPageSize(page_size_for_cat_and_shops);
            for (int i = 1;i<=page.getPageCount();i++){

                listOfAdresses.add("/shops/"+shop+"/page/"+i+"/sort/date");

            }
        }

        if(sort.equals("most-comments")){
            modelAndView.addObject("picked_sort", 2);
            modelAndView.addObject("next_and_previous","/shops/"+shop+"/page/id/sort/most-comments");
            page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenShop(shop));
            page.setPageSize(page_size_for_cat_and_shops);
            for (int i = 1;i<=page.getPageCount();i++){

                listOfAdresses.add("/shops/"+shop+"/page/"+i+"/sort/most-comments");

            }
        }

        if(sort.equals("top-rated")){
            modelAndView.addObject("picked_sort", 1);
            modelAndView.addObject("next_and_previous","/shops/"+shop+"/page/id/sort/top-rated");
            page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenShop(shop));
            page.setPageSize(page_size_for_cat_and_shops);
            for (int i = 1;i<=page.getPageCount();i++){

                listOfAdresses.add("/shops/"+shop+"/page/"+i+"/sort/top-rated");

            }
        }

        page.setPage(Integer.parseInt(id)-1);
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));

        modelAndView.addObject("sort_buttons_prefix", "/shops/"+shop+"/page/1/sort/");

        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", shop);
        modelAndView.addObject("additional_results_info_more", " sklepu");
        return modelAndView;

    }

    @GetMapping("/discount/{id}")
    public ModelAndView discount(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("discount");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("discount", discountRepository.findById(id).get());

        return modelAndView;
    }



    @GetMapping("/add/discount")
    public ModelAndView add_discount_get() {
        ModelAndView modelAndView = new ModelAndView("add_discount");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("error", false);
        return modelAndView;
    }

    @PostMapping(path="/add/discount", consumes = {"multipart/form-data"})
    public ModelAndView add_discount(
            @ModelAttribute("url") String url, @ModelAttribute("tag") String tag, @ModelAttribute("shop") String shop,
            @ModelAttribute("title") String title, @ModelAttribute("old_price") String old_price, @ModelAttribute("current_price") String current_price,
            @ModelAttribute("shipment_price") String shipment_price, @ModelAttribute("content") String content,
            @ModelAttribute("expire_date") String expire_date, @RequestParam("image_url")
                    MultipartFile file, HttpServletRequest request
    ) throws ParseException, IOException {
        Discount discount = new Discount();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            uzytkownik = userRepository.findUserByLogin(authentication.getName());

            String uploadDir = "/static/images";
            String realPath = request.getServletContext().getRealPath(uploadDir);

            File transferFile = new File("C:/projekt inz/project/src/main/resources/static/images/" + file.getOriginalFilename());
            file.transferTo(transferFile);

            discount.setContent(content);
            discount.setCreationdate(new Date());
            discount.setCurrent_price(Double.parseDouble(current_price));
            discount.setOld_price(Double.parseDouble(old_price));
            discount.setShipment_price(Double.parseDouble(shipment_price));
            discount.setDiscount_link(url);
            discount.setTag(tagRepository.findById(Long.parseLong(tag)).get());
            discount.setShop(shopRepository.findById(Long.parseLong(shop)).get());
            discount.setTitle(title);
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(expire_date);
            discount.setExpire_date(date1);
            discount.setStatus("");
            discount.setUser(uzytkownik);
            discount.setImage_url("images/"+file.getOriginalFilename());
            discountRepository.save(discount);

        }catch (Exception e){

            ModelAndView modelAndView = new ModelAndView("add_discount");
            modelAndView.addObject("list_of_tags", tagRepository.findAll());
            modelAndView.addObject("list_of_shops", shopRepository.findAll());
            modelAndView.addObject("error", true);
            return modelAndView;
        }

        ModelAndView modelAndView = new ModelAndView("redirect:/discount/"+discount.getDiscount_id());

        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;
    }

    @PostMapping("/search")
    public ModelAndView searchForm(@ModelAttribute("searchform") String searchform) throws UnsupportedEncodingException {
        String encodedId = URLEncoder.encode(searchform, "UTF-8");
        ModelAndView m = new ModelAndView(new RedirectView("/search/" + encodedId, true, true, false));
        return m;

    }

    @GetMapping("/search/{search}")
    public ModelAndView search(@PathVariable("search") String search){


        PagedListHolder page = new PagedListHolder(discountRepository.discountBySearchInput("%"+search+"%"));
        page.setPageSize(1); // number of items per page
        page.setPage(0);

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous","/search/"+search+"/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/search/"+search+"/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1;i<=page.getPageCount();i++){

            listOfAdresses.add("/search/"+search+"/page/"+i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", search);
        return modelAndView;

    }

    @GetMapping("/search/{search}/page/{id}")
    public ModelAndView searchPage(@PathVariable("search") String search, @PathVariable("id") String id) {
        PagedListHolder page = new PagedListHolder(discountRepository.discountBySearchInput("%"+search+"%"));
        page.setPageSize(1); // number of items per page
        page.setPage(Integer.parseInt(id)-1);
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous","/search/"+search+"/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/search/"+search+"/page/1/sort/");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1;i<=page.getPageCount();i++){

            listOfAdresses.add("/search/"+search+"/page/"+i);

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
        if(sort.equals("date")){
            modelAndView.addObject("picked_sort", 3);
            modelAndView.addObject("next_and_previous","/search/"+search+"/page/id/sort/date");
            page = new PagedListHolder(discountRepository.discountBySearchInput("%"+search+"%"));
            page.setPageSize(1);
            for (int i = 1;i<=page.getPageCount();i++){

                listOfAdresses.add("/search/"+search+"/page/"+i+"/sort/date");

            }
        }

        if(sort.equals("most-comments")){
            modelAndView.addObject("picked_sort", 2);
            modelAndView.addObject("next_and_previous","/search/"+search+"/page/id/sort/most-comments");
            page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenSearchInput("%"+search+"%"));
            page.setPageSize(1);
            for (int i = 1;i<=page.getPageCount();i++){

                listOfAdresses.add("/search/"+search+"/page/"+i+"/sort/most-comments");

            }
        }

        if(sort.equals("top-rated")){
            modelAndView.addObject("picked_sort", 1);
            modelAndView.addObject("next_and_previous","/search/"+search+"/page/id/sort/top-rated");
            page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenSearchInput("%"+search+"%"));
            page.setPageSize(1);
            for (int i = 1;i<=page.getPageCount();i++){

                listOfAdresses.add("/search/"+search+"/page/"+i+"/sort/top-rated");

            }
        }

        page.setPage(Integer.parseInt(id)-1);
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));

        modelAndView.addObject("sort_buttons_prefix", "/search/"+search+"/page/1/sort/");

        modelAndView.addObject("list_of_adresses", listOfAdresses);
        modelAndView.addObject("additional_results_info", search);
        return modelAndView;

    }



    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;
    }

    @PostMapping("/addrate")
    public String addrate(@ModelAttribute("discountidadd") String discountid, @ModelAttribute("redirect") String redirect){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());

        Discount discount = discountRepository.findById(Long.parseLong(discountid)).get();
        for(Rating r:discount.getRatings()){

            if(r.getUser().getUser_id().equals(uzytkownik.getUser_id())){
                return "redirect:/";

            }

        }


        Rating newrating = new Rating();
        newrating.setUser(uzytkownik);
        newrating.setDiscount(discount);
        ratingRepository.save(newrating);

        return "redirect:/"+redirect;

    }

    @PostMapping("/removerate")
    public String removerate(@ModelAttribute("discountidremove") String discountid,@ModelAttribute("redirect_remove") String redirect){

        Discount discount = discountRepository.findById(Long.parseLong(discountid)).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());
        for(Rating r:discount.getRatings()){

            if(r.getUser().getUser_id().equals(uzytkownik.getUser_id())){
                ratingRepository.delete(r);
                return "redirect:/";

            }

        }

        return "redirect:/"+redirect;

    }

    @PostMapping("/addcomment")
    public String addcomment(@ModelAttribute("discountidaddcomment") String discountaddcomment, @ModelAttribute("comment") String comment){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());
        Comment comment1 = new Comment();
        comment1.setDiscount(discountRepository.findById(Long.parseLong(discountaddcomment)).get());
        comment1.setUser(uzytkownik);
        comment1.setContent(comment);
        comment1.setCr_date(new Date());
        commentRepository.save(comment1);

        return "redirect:/discount/"+Long.parseLong(discountaddcomment);
    }

    @PostMapping("/register")
    public RedirectView register(@ModelAttribute("login") String login, @ModelAttribute("password") String password,@ModelAttribute("repeated_password") String repeated_password,
                                 RedirectAttributes redir, @ModelAttribute("email") String email, @ModelAttribute("reg") String reg){

        RedirectView redirectView= new RedirectView("/register",true);

        if(login.length()<5){

            redir.addFlashAttribute("bad_status","Wprowadzony login jest za krótki.");
            return redirectView;

        }

        if(password.length()<5){

            redir.addFlashAttribute("bad_status","Wprowadzone hasło jest za krótkie.");
            return redirectView;

        }

        if(userRepository.findUserByLogin(login)!=null){

            redir.addFlashAttribute("bad_status","Ten login jest juz zajęty.");
            return redirectView;

        }



        if(!password.equals(repeated_password)){
            redir.addFlashAttribute("bad_status","Hasła muszą się powtarzać w obydwu polach.");
            return redirectView;
        }

        System.out.println(reg);

        if(!reg.equals("on")){

            redir.addFlashAttribute("bad_status","Musisz zaakceptować regulamin.");
            return redirectView;

        }





        User user1 = new User();
        user1.setCr_date(new Date());
        user1.setLogin(login);
        user1.setPassword(passwordEncoder.encode(password));
        user1.setEmail(email);
        userRepository.save(user1);

        redir.addFlashAttribute("good_status","Zostałeś pomyślnie zarejestrowany. Możesz sie teraz zalogować.");
        redirectView= new RedirectView("/login",true);

        return redirectView;

    }

    @PostMapping("/ratecomment")
    public String ratecomment(@ModelAttribute("commentid") String commentid){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());
        Comment comment = commentRepository.findById((Long.parseLong(commentid))).get();
        for(Rating r:comment.getRatings()){

            if(r.getUser().getUser_id().equals(uzytkownik.getUser_id())){
                return "redirect:/discount/"+comment.getDiscount().getDiscount_id().toString();

            }

        }

        Rating newrating = new Rating();
        newrating.setUser(uzytkownik);
        newrating.setComment(comment);
        ratingRepository.save(newrating);

        return "redirect:/discount/"+comment.getDiscount().getDiscount_id().toString();

    }

    @GetMapping("/settings")
    public ModelAndView settings(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());
        if (uzytkownik.getInformation()==null){
            uzytkownik.setInformation(new Information());
        }

        ModelAndView modelAndView = new ModelAndView("user_profile_main");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("user",uzytkownik);
        return modelAndView;

    }

    @GetMapping("/settings/discounts")
    public ModelAndView settingsDiscounts(){

        ModelAndView modelAndView = new ModelAndView("user_profile_discounts");


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());
        PagedListHolder page = new PagedListHolder(discountRepository.sortDiscountByDateWithGivenUserId(uzytkownik.getUser_id()));
        page.setPageSize(2); // number of items per page
        page.setPage(0);
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous","/settings/discounts/page/id");
        modelAndView.addObject("picked_sort", 3);
        modelAndView.addObject("sort_buttons_prefix", "/settings/discounts/page/1/sort/");
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1;i<=page.getPageCount();i++){

            listOfAdresses.add("/settings/discounts/page/"+i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;


    }

    @GetMapping("/settings/discounts/page/{id}")
    public ModelAndView pageSettingsDiscounts(@PathVariable("id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());
        PagedListHolder page = new PagedListHolder(discountRepository.discountByUserId(uzytkownik.getUser_id()));
        page.setPageSize(2); // number of items per page
        page.setPage(Integer.parseInt(id)-1);
        ModelAndView modelAndView = new ModelAndView("user_profile_discounts");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous","/settings/discounts/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/settings/discounts/page/1/sort/");
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1;i<=page.getPageCount();i++){

            listOfAdresses.add("/settings/discounts/page/"+i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;

    }

    @GetMapping("/settings/discounts/page/{id}/sort/{sort}")
    public ModelAndView pageSettingsDiscountsSort( @PathVariable("id") String id, @PathVariable("sort") String sort) {
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("user_profile_discounts");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());
        PagedListHolder page = null;
        if(sort.equals("date")){
            modelAndView.addObject("picked_sort", 3);
            modelAndView.addObject("next_and_previous","/settings/discounts/page/id/sort/date");
            page = new PagedListHolder(discountRepository.sortDiscountByDateWithGivenUserId(uzytkownik.getUser_id()));
            page.setPageSize(2);
            for (int i = 1;i<=page.getPageCount();i++){

                listOfAdresses.add("/settings/discounts/page/"+i+"/sort/date");

            }
        }

        if(sort.equals("most-comments")){
            modelAndView.addObject("picked_sort", 2);
            modelAndView.addObject("next_and_previous","/settings/discounts/page/id/sort/most-comments");
            page = new PagedListHolder(discountRepository.sortDiscountByCommentsWithGivenUserId(uzytkownik.getUser_id()));
            page.setPageSize(2);
            for (int i = 1;i<=page.getPageCount();i++){

                listOfAdresses.add("/settings/discounts/page/"+i+"/sort/most-comments");

            }
        }

        if(sort.equals("top-rated")){
            modelAndView.addObject("picked_sort", 1);
            modelAndView.addObject("next_and_previous","/settings/discounts/page/id/sort/top-rated");
            page = new PagedListHolder(discountRepository.sortDiscountByRatingWithGivenUserId(uzytkownik.getUser_id()));
            page.setPageSize(2);
            for (int i = 1;i<=page.getPageCount();i++){

                listOfAdresses.add("/settings/discounts/page/"+i+"/sort/top-rated");

            }
        }

        page.setPage(Integer.parseInt(id)-1);
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("user", uzytkownik);

        modelAndView.addObject("sort_buttons_prefix", "/settings/discounts/page/1/sort/");

        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;

    }

    @GetMapping("/settings/messages")
    public ModelAndView profileMessages(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());


        ModelAndView modelAndView = new ModelAndView("user_profile_messages");
        modelAndView.addObject("list_of_conversations",  uzytkownik.getConversationsSorted());
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;

    }

    @PostMapping("/settings/messages")
    public RedirectView newMessageToUser(@ModelAttribute("login") String login,@ModelAttribute("message") String message, RedirectAttributes redir) {

        RedirectView redirectView= new RedirectView("/settings/messages",true);

        if(userRepository.findUserByLogin(login)==null){
            redir.addFlashAttribute("bad_status","Użytkownik o takim loginie nie istnieje.");
            return redirectView;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());

        if(login.equals(uzytkownik.getLogin())){

            redir.addFlashAttribute("bad_status","Nie możesz pisac wiadomości do siebie.");
            return redirectView;

        }

        ArrayList<User> uzytkownicy = new ArrayList<>();
        uzytkownicy.add(uzytkownik);
        uzytkownicy.add(userRepository.findUserByLogin(login));
        Conversation conversation = conversationRepository.findConversationWhereUsers(uzytkownicy.get(0).getUser_id(),uzytkownicy.get(1).getUser_id());

        if (conversation!=null){



            Message messageobject = new Message(message, new Date(), "nieodczytane", conversation, uzytkownik);
            messageRepository.save(messageobject);
            Message newmessageobject = conversation.getOtherUserNewMessage(uzytkownik);
            if(!newmessageobject.getContent().equals("")) {
                newmessageobject.setStatus("odczytane");
                messageRepository.save(newmessageobject);
            }


            redirectView= new RedirectView("/messages/"+conversation.getConversation_id(),true);
            return redirectView;


        }

        conversation = new Conversation();
        HashSet<User> list_of_users = new HashSet<>();
        list_of_users.add(uzytkownicy.get(0));
        list_of_users.add(uzytkownicy.get(1));
        ArrayList<Message> list_of_messages = new ArrayList<>();
        conversationRepository.save(conversation);
        Conversation finalConversation = conversation;
        list_of_users.forEach(o -> o.getConversations().add(finalConversation));
        ArrayList<User> lista_uzytkownikow = new ArrayList<>(list_of_users);
        lista_uzytkownikow.forEach(o -> userRepository.save(o));
        Message m = new Message(message, new Date(), "nieodczytane", conversation, uzytkownik);
        messageRepository.save(m);






        redirectView= new RedirectView("/messages/"+conversation.getConversation_id(),true);
        return redirectView;


    }



    @GetMapping("/messages")
    public ModelAndView messages(){

        ModelAndView modelAndView = new ModelAndView("user_messages");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;

    }

    @GetMapping("/profile/{name}")
    public ModelAndView profile(@PathVariable("name") String name){

        ModelAndView modelAndView = new ModelAndView("profile");
        uzytkownik = userRepository.findUserByLogin(name);
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("comments_page", false);
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;

    }

    @GetMapping("/profile/{name}/comments")
    public ModelAndView profile_comments(@PathVariable("name") String name){

        ModelAndView modelAndView = new ModelAndView("profile");
        uzytkownik = userRepository.findUserByLogin(name);
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("comments_page", true);
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;

    }

    @PostMapping("changeUserDetails")
    public RedirectView changeUserDetails(@ModelAttribute("name") String name,@ModelAttribute("surname") String surname,@ModelAttribute("email") String email, RedirectAttributes redir) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());

        uzytkownik.setEmail(email);
        Information info = new Information();
        if (uzytkownik.getInformation() == null) {

            info.setName(name);
            info.setSurname(surname);
            info.setUser(uzytkownik);

        } else {
            info = uzytkownik.getInformation();
            info.setName(name);
            info.setSurname(surname);
        }

        informationRepository.save(info);
        uzytkownik.setInformation(info);
        userRepository.save(uzytkownik);

        RedirectView redirectView= new RedirectView("/settings",true);
        redir.addFlashAttribute("status","Zmiany pomyślnie zapisane.");
        return redirectView;
    }

    @PostMapping("changeDescription")
    public RedirectView changeDescription(@ModelAttribute("description") String description, RedirectAttributes redir) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());

        Information info = new Information();
        if (uzytkownik.getInformation() == null) {

            info.setDescription(description);


        } else {
            info = uzytkownik.getInformation();
            info.setDescription(description);
        }

        informationRepository.save(info);
        uzytkownik.setInformation(info);
        userRepository.save(uzytkownik);

        RedirectView redirectView= new RedirectView("/settings",true);
        redir.addFlashAttribute("status","Zmiany pomyślnie zapisane.");
        return redirectView;
    }

    @PostMapping("changePassword")
    public RedirectView changePassword(@ModelAttribute("currentpassword") String currentpassword, @ModelAttribute("newpassword") String newpassword, @ModelAttribute("newpasswordconfirmation") String newpasswordconfirmation, RedirectAttributes redir) {

        RedirectView redirectView= new RedirectView("/settings",true);

        if (!newpassword.equals(newpasswordconfirmation)){
            redir.addFlashAttribute("bad_status","Hasła muszą się powtarzać w dwóch ostatnich polach.");
            return redirectView;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());

        if(!passwordEncoder.matches(currentpassword,uzytkownik.getPassword())){

            redir.addFlashAttribute("bad_status","Twoje obecne hasło się nie zgadza.");
            return redirectView;

        }

        uzytkownik.setPassword(passwordEncoder.encode(newpassword));
        userRepository.save(uzytkownik);
        redir.addFlashAttribute("status","Twoje hasło zostało zmienione.");
        return redirectView;

    }

    @GetMapping("/messages/{id}")
    public ModelAndView pageMessages( @PathVariable("id") String id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());

        if (!conversationRepository.findById(Long.parseLong(id)).isPresent()){

            ModelAndView modelAndView = new ModelAndView("error");
            return modelAndView;

        }


        if(conversationRepository.findById(Long.parseLong(id)).get().getUsers().contains(uzytkownik)){

            Message newmessageobject = conversationRepository.findById(Long.parseLong(id)).get().getOtherUserNewMessage(uzytkownik);
            if(!newmessageobject.getContent().equals("")) {
                newmessageobject.setStatus("odczytane");
                messageRepository.save(newmessageobject);
            }




            ModelAndView modelAndView = new ModelAndView("user_messages");
            modelAndView.addObject("list_of_tags", tagRepository.findAll());
            modelAndView.addObject("list_of_shops", shopRepository.findAll());
            modelAndView.addObject( "list_of_conversations",  uzytkownik.getConversationsSorted());
            modelAndView.addObject("current_conversation", conversationRepository.findById(Long.parseLong(id)));
            modelAndView.addObject("user", uzytkownik);
            modelAndView.addObject( "current_id",  Integer.parseInt(id));

            return modelAndView;

        }

        ModelAndView modelAndView = new ModelAndView("error");
        return modelAndView;


    }

    @PostMapping("sendMessage")
    public String sendMessage(@ModelAttribute("new_message_conv_id") String new_message_conv_id, @ModelAttribute("new_message") String new_message) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        uzytkownik = userRepository.findUserByLogin(authentication.getName());


        Message message = new Message();
        message.setStatus("nieodczytane");
        message.setContent(new_message);
        message.setConversation(conversationRepository.findById(Long.parseLong(new_message_conv_id)).get());
        message.setCr_date(new Date());
        message.setUser(uzytkownik);

        messageRepository.save(message);


        return "redirect:/messages/"+new_message_conv_id;
    }






    }
