package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.ParsingBot;
import pl.okazje.project.entities.*;
import pl.okazje.project.repositories.*;
import pl.okazje.project.services.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class SettingsController {

    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ConversationService conversationService;
    @Autowired
    InformationRepository informationRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ParsingBot parsingBot;

    private final ShopService shopService;
    private final TagService tagService;
    private final AuthenticationService authenticationService;
    private final DiscountService discountService;
    private final PostService postService;
    private final int ITEMS_PER_PAGE = 4;

    public SettingsController(ShopService shopService, TagService tagService, AuthenticationService authenticationService, DiscountService discountService, PostService postService) {
        this.shopService = shopService;
        this.tagService = tagService;
        this.authenticationService = authenticationService;
        this.discountService = discountService;
        this.postService = postService;
    }


    @GetMapping("/settings")
    public ModelAndView settings() {
        return getBaseModelAndView("user_profile_main");
    }

    @GetMapping("/settings/discounts")
    public ModelAndView getsettingsDiscountsHomepage() {
        return getDiscountBaseModelAndView(0);
    }

    @GetMapping("/settings/discounts/page/{id}")
    public ModelAndView getSettingsDiscountsPage(@PathVariable("id") String id) {
       return getDiscountBaseModelAndView(Integer.parseInt(id)-1);
    }

    @GetMapping("/settings/posts")
    public ModelAndView getsettingsPostsHomepage() {
        return getPostBaseModelAndView(0);
    }

    @GetMapping("/settings/posts/page/{id}")
    public ModelAndView getsettingsPostsPage(@PathVariable("id") String id) {
        return getPostBaseModelAndView(Integer.parseInt(id));
    }

    @GetMapping("/settings/admin/discounts")
    public ModelAndView settingsAdminDiscounts() {

        ModelAndView modelAndView = new ModelAndView("user_admin_profile_discounts");


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName()).get();
        PagedListHolder page = new PagedListHolder(discountRepository.findAllByStatusEquals(Discount.Status.AWAITING));
        page.setPageSize(2); // number of items per page
        page.setPage(0);
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous", "/settings/admin/discounts/page/id");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/settings/admin/discounts/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;


    }

    @GetMapping("/settings/admin/discounts/page/{id}")
    public ModelAndView pageSettingsAdminDiscounts(@PathVariable("id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName()).get();
        PagedListHolder page = new PagedListHolder(discountRepository.findAllByStatusEquals(Discount.Status.AWAITING));
        page.setPageSize(2); // number of items per page
        page.setPage(Integer.parseInt(id) - 1);
        ModelAndView modelAndView = new ModelAndView("user_admin_profile_discounts");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/settings/admin/discounts/page/id");
        modelAndView.addObject("user", uzytkownik);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/settings/admin/discounts/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;

    }

    @GetMapping("/settings/messages")
    public ModelAndView profileMessages() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName()).get();


        ModelAndView modelAndView = new ModelAndView("user_profile_messages");
        modelAndView.addObject("list_of_conversations", uzytkownik.getConversationsSorted());
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;

    }

    @GetMapping("/settings/liked/page/{id}")
    public ModelAndView liked(@PathVariable("id") String id){

        ModelAndView modelAndView = new ModelAndView("user_profile_liked");


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName()).get();
        PagedListHolder page = new PagedListHolder(discountRepository.findAllByUserAndLiked(uzytkownik.getLogin()));
        page.setPageSize(2); // number of items per page
        page.setPage(Integer.parseInt(id)-1);
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/settings/liked/page/id");
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/settings/liked/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;

    }

    @GetMapping("/settings/admin/functions")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ModelAndView functions() {

        ModelAndView modelAndView = new ModelAndView("user_admin_profile_functions");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName()).get();
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;


    }

    @PostMapping("/settings/messages")
    public RedirectView newMessageToUser(@ModelAttribute("login") String login, @ModelAttribute("message") String message, RedirectAttributes redir){

        RedirectView redirectView = new RedirectView("/settings/messages", true);

        if (userRepository.findFirstByLogin(login) == null) {
            redir.addFlashAttribute("bad_status", "Użytkownik o takim loginie nie istnieje.");
            return redirectView;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName()).get();

        if (login.equals(uzytkownik.getLogin())) {

            redir.addFlashAttribute("bad_status", "Nie możesz pisac wiadomości do siebie.");
            return redirectView;

        }

        ArrayList<User> uzytkownicy = new ArrayList<>();
        uzytkownicy.add(uzytkownik);
        uzytkownicy.add(userRepository.findFirstByLogin(login).get());
        Conversation conversation;

        if (conversationService.findByUsers(uzytkownicy.get(0).getUser_id(), uzytkownicy.get(1).getUser_id()).isPresent()) {
            conversation = conversationService.findByUsers(uzytkownicy.get(0).getUser_id(), uzytkownicy.get(1).getUser_id()).get();
            Message messageobject = new Message();
            messageobject.setContent(message);
            messageobject.setCr_date(new Date());
            messageobject.setStatus(Message.Status.NEW);
            messageobject.setConversation(conversation);
            messageobject.setUser(uzytkownik);
            messageRepository.save(messageobject);
            Message newmessageobject = conversation.getOtherUserNewMessage(uzytkownik).get();
            if (!newmessageobject.getContent().equals("")) {
                newmessageobject.setStatus(Message.Status.SEEN);
                messageRepository.save(newmessageobject);
            }

            ArrayList<String> list =new ArrayList<>(userRepository.findExpiry_timeByUsername(uzytkownicy.get(1).getLogin()));
            if(!list.isEmpty()){

                for (String s:list) {
                    if(Long.parseLong(s)-System.currentTimeMillis()<0){
                        emailService.sendEmail(uzytkownicy.get(1).getEmail(),
                                "NORGIE - Otrzymales nową wiadomość","Witaj "+uzytkownicy.get(1).getLogin()+",\n Otrzymałeś nową wiadomość od "
                                        +uzytkownicy.get(0).getLogin()+"\n Treść wiadomości: "+newmessageobject.getContent());
                    }

                }

            }else {

                emailService.sendEmail(uzytkownicy.get(1).getEmail(),"NORGIE - Otrzymales nową wiadomość","Witaj "+uzytkownicy.get(1).getLogin()+",\n Otrzymałeś nową wiadomość od "
                        +uzytkownicy.get(0).getLogin()+"\n Treść wiadomości: "+newmessageobject.getContent());

            }


            redirectView = new RedirectView("/messages/" + conversation.getConversation_id(), true);
            return redirectView;


        }

        conversation = new Conversation();
        HashSet<User> list_of_users = new HashSet<>();
        list_of_users.add(uzytkownicy.get(0));
        list_of_users.add(uzytkownicy.get(1));
        ArrayList<Message> list_of_messages = new ArrayList<>();
        conversationService.save(conversation);
        Conversation finalConversation = conversation;
        list_of_users.forEach(o -> o.getConversations().add(finalConversation));
        ArrayList<User> lista_uzytkownikow = new ArrayList<>(list_of_users);
        lista_uzytkownikow.forEach(o -> userRepository.save(o));
        Message m = new Message();
        m.setContent(message);
        m.setCr_date(new Date());
        m.setStatus(Message.Status.NEW);
        m.setConversation(conversation);
        m.setUser(uzytkownik);
        messageRepository.save(m);
        ArrayList<String> list =new ArrayList<>(userRepository.findExpiry_timeByUsername(uzytkownicy.get(1).getLogin()));
        if(!list.isEmpty()){

            for (String s:list) {
                if(Long.parseLong(s)-System.currentTimeMillis()<0){
                    emailService.sendEmail(uzytkownicy.get(1).getEmail(),
                            "NORGIE - Otrzymales nową wiadomość","Witaj "+uzytkownicy.get(1).getLogin()+",\n Otrzymałeś nową wiadomość od "
                                    +uzytkownicy.get(0).getLogin()+"\n Treść wiadomości: "+m.getContent());
                }

            }

        }else {

            emailService.sendEmail(uzytkownicy.get(1).getEmail(),
                    "NORGIE - Otrzymales nową wiadomość","Witaj "+uzytkownicy.get(1).getLogin()+",\n Otrzymałeś nową wiadomość od "
                            +uzytkownicy.get(0).getLogin()+"\n Treść wiadomości: "+m.getContent());

        }


        redirectView = new RedirectView("/messages/" + conversation.getConversation_id(), true);
        return redirectView;


    }

    @PostMapping("changeUserDetails")
    public RedirectView changeUserDetails(@ModelAttribute("name") String name, @ModelAttribute("surname") String surname, @ModelAttribute("email") String email, RedirectAttributes redir) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName()).get();

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

        RedirectView redirectView = new RedirectView("/settings", true);
        redir.addFlashAttribute("status", "Zmiany pomyślnie zapisane.");
        return redirectView;
    }

    @PostMapping("changeDescription")
    public RedirectView changeDescription(@ModelAttribute("description") String description, RedirectAttributes redir) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName()).get();

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

        RedirectView redirectView = new RedirectView("/settings", true);
        redir.addFlashAttribute("status", "Zmiany pomyślnie zapisane.");
        return redirectView;
    }

    @PostMapping("changePassword")
    public RedirectView changePassword(@ModelAttribute("currentpassword") String currentpassword, @ModelAttribute("newpassword") String newpassword, @ModelAttribute("newpasswordconfirmation") String newpasswordconfirmation, RedirectAttributes redir) throws MessagingException {

        RedirectView redirectView = new RedirectView("/settings", true);

        if (!newpassword.equals(newpasswordconfirmation)) {
            redir.addFlashAttribute("bad_status", "Hasła muszą się powtarzać w dwóch ostatnich polach.");
            return redirectView;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName()).get();

        if (!passwordEncoder.matches(currentpassword, uzytkownik.getPassword())) {

            redir.addFlashAttribute("bad_status", "Twoje obecne hasło się nie zgadza.");
            return redirectView;

        }

        uzytkownik.setPassword(passwordEncoder.encode(newpassword));
        userRepository.save(uzytkownik);
        redir.addFlashAttribute("status", "Twoje hasło zostało zmienione.");
        emailService.sendEmail(uzytkownik.getEmail(),"Norgie - Hasło zostało zmienione", "Witaj "+uzytkownik.getLogin()+", \n hasło do twojego konta zostało zmienione.\n\nJeśli nie zostało ono zaktualizowane przez Ciebie, powiadom nas o tym jak najszybciej.");
        return redirectView;

    }


    @PostMapping("/settings/admin/xkom")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public @ResponseBody void xkom(){

        parsingBot.fetchXkom();


    }

    @PostMapping("/settings/admin/amazon")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public @ResponseBody void amazon(){

        parsingBot.fetchAmazon("https://www.amazon.com/Best-Sellers-Womens-Fashion/zgbs/fashion/", "Moda");


    }

    //[0] - status
    //[1] - informacja o skanowaniu
    //[2] - ilosc poprawnie przeskanowanych
    //[3] - blednie przeskanowane promocje
    //[4] - ilosc dodanych nowych promocji
    @GetMapping("/settings/admin/status")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public @ResponseBody String[] statusXkom(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String array[] = new String[5];
        if (ParsingBot.status == 1){

            array[0] = "working";
            return array;

        }
        if(ParsingBot.status == 0){

            array[0] = "ok";
            array[1] = ParsingBot.info;
            array[2] = ParsingBot.good_scan;
            array[3] = ParsingBot.bad_scan;
            array[4] = ParsingBot.new_prom;
            return array;

        }


        return array;

    }

    private ModelAndView getBaseModelAndView(String viewName){
        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.addObject("list_of_tags", tagService.findAll());
        modelAndView.addObject("list_of_shops", shopService.findAll());
        modelAndView.addObject("user", authenticationService.getCurrentUser().get());
        return modelAndView;
    }

    private ModelAndView getDiscountBaseModelAndView(int currentPage){
        ModelAndView modelAndView = getBaseModelAndView("user_profile_discounts");
        PagedListHolder<Discount> discountPages = new PagedListHolder(discountService.findAllByUserIncludeSorting(authenticationService.getCurrentUser().get()));
        discountPages.setPageSize(ITEMS_PER_PAGE);
        discountPages.setPage(currentPage);
        modelAndView.addObject("list_of_discounts", discountPages.getPageList());
        modelAndView.addObject("quantity_of_pages", discountPages.getPageCount());
        modelAndView.addObject("number_of_page", discountPages.getPage()+1);
        modelAndView.addObject("next_and_previous", "/settings/discounts/page/id");
        String[] arrayOfAddresses = new String[discountPages.getPageCount()];
        for (int i = 0; i < arrayOfAddresses.length; i++) {
            arrayOfAddresses[i] = "/settings/discounts/page/" + (i + 1);
        }
        modelAndView.addObject("array_of_addresses", arrayOfAddresses);
        return modelAndView;
    }

    private ModelAndView getPostBaseModelAndView(int currentPage){
        ModelAndView modelAndView = getBaseModelAndView("user_profile_posts");
        PagedListHolder<Post> postPages = new PagedListHolder(postService.findAllByUserIncludeSorting(authenticationService.getCurrentUser().get()));
        postPages.setPageSize(ITEMS_PER_PAGE);
        postPages.setPage(currentPage);
        modelAndView.addObject("list_of_posts", postPages.getPageList());
        modelAndView.addObject("quantity_of_pages", postPages.getPageCount());
        modelAndView.addObject("number_of_page", postPages.getPage()+1);
        modelAndView.addObject("next_and_previous", "/settings/posts/page/id");
        String[] arrayOfAddresses = new String[postPages.getPageCount()];
        for (int i = 0; i < arrayOfAddresses.length; i++) {
            arrayOfAddresses[i] = "/settings/posts/page/" + (i + 1);
        }
        modelAndView.addObject("array_of_addresses", arrayOfAddresses);
        return modelAndView;
    }


}
