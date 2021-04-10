package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
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
import pl.okazje.project.services.ConversationService;
import pl.okazje.project.services.SendMail;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Controller
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
    SendMail sendMail;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ParsingBot parsingBot;

    @GetMapping("/settings")
    public ModelAndView settings() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());
        if (uzytkownik.getInformation() == null) {
            uzytkownik.setInformation(new Information());
        }

        ModelAndView modelAndView = new ModelAndView("user_profile_main");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("user", uzytkownik);

        return modelAndView;

    }

    @GetMapping("/settings/discounts")
    public ModelAndView settingsDiscounts() {

        ModelAndView modelAndView = new ModelAndView("user_profile_discounts");


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());
        PagedListHolder page = new PagedListHolder(discountRepository.findAllByUseridOrderByCreationdateDesc(uzytkownik.getUser_id()));
        page.setPageSize(2); // number of items per page
        page.setPage(0);
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("number_of_page", 1);
        modelAndView.addObject("next_and_previous", "/settings/discounts/page/id");
        modelAndView.addObject("picked_sort", 3);
        modelAndView.addObject("sort_buttons_prefix", "/settings/discounts/page/1/sort/");
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/settings/discounts/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;


    }

    @GetMapping("/settings/posts")
    public ModelAndView settingsPosts() {

        ModelAndView modelAndView = new ModelAndView("user_profile_posts");


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());

        modelAndView.addObject("list_of_posts", postRepository.FindAllByUserOrderByCreationdateDesc(uzytkownik.getLogin()));
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("user", uzytkownik);

        return modelAndView;


    }

    @GetMapping("/settings/admin/discounts")
    public ModelAndView settingsAdminDiscounts() {

        ModelAndView modelAndView = new ModelAndView("user_admin_profile_discounts");


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());
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
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());
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


    @GetMapping("/settings/discounts/page/{id}")
    public ModelAndView pageSettingsDiscounts(@PathVariable("id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());
        PagedListHolder page = new PagedListHolder(discountRepository.findAllByUseridOrderByCreationdateDesc(uzytkownik.getUser_id()));
        page.setPageSize(2); // number of items per page
        page.setPage(Integer.parseInt(id) - 1);
        ModelAndView modelAndView = new ModelAndView("user_profile_discounts");
        modelAndView.addObject("list_of_discounts", page.getPageList());
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("quantity_of_pages", page.getPageCount());
        modelAndView.addObject("number_of_page", Integer.parseInt(id));
        modelAndView.addObject("next_and_previous", "/settings/discounts/page/id");
        modelAndView.addObject("sort_buttons_prefix", "/settings/discounts/page/1/sort/");
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("picked_sort", 3);
        List<String> listOfAdresses = new ArrayList<>();
        for (int i = 1; i <= page.getPageCount(); i++) {

            listOfAdresses.add("/settings/discounts/page/" + i);

        }
        modelAndView.addObject("list_of_adresses", listOfAdresses);
        return modelAndView;

    }

    @GetMapping("/settings/discounts/page/{id}/sort/{sort}")
    public ModelAndView pageSettingsDiscountsSort(@PathVariable("id") String id, @PathVariable("sort") String sort) {
        List<String> listOfAdresses = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("user_profile_discounts");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());
        PagedListHolder page = null;
        if (sort.equals("date")) {
            modelAndView.addObject("picked_sort", 3);
            modelAndView.addObject("next_and_previous", "/settings/discounts/page/id/sort/date");
            page = new PagedListHolder(discountRepository.findAllByUseridOrderByCreationdateDesc(uzytkownik.getUser_id()));
            page.setPageSize(2);
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/settings/discounts/page/" + i + "/sort/date");

            }
        }

        if (sort.equals("most-comments")) {
            modelAndView.addObject("picked_sort", 2);
            modelAndView.addObject("next_and_previous", "/settings/discounts/page/id/sort/most-comments");
            page = new PagedListHolder(discountRepository.findAllByUseridOrderByCommentDesc(uzytkownik.getUser_id()));
            page.setPageSize(2);
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/settings/discounts/page/" + i + "/sort/most-comments");

            }
        }

        if (sort.equals("top-rated")) {
            modelAndView.addObject("picked_sort", 1);
            modelAndView.addObject("next_and_previous", "/settings/discounts/page/id/sort/top-rated");
            page = new PagedListHolder(discountRepository.findAllByUseridOrderByRatingDesc(uzytkownik.getUser_id()));
            page.setPageSize(2);
            for (int i = 1; i <= page.getPageCount(); i++) {

                listOfAdresses.add("/settings/discounts/page/" + i + "/sort/top-rated");

            }
        }

        page.setPage(Integer.parseInt(id) - 1);
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
    public ModelAndView profileMessages() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());


        ModelAndView modelAndView = new ModelAndView("user_profile_messages");
        modelAndView.addObject("list_of_conversations", uzytkownik.getConversationsSorted());
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;

    }

    @PostMapping("/settings/messages")
    public RedirectView newMessageToUser(@ModelAttribute("login") String login, @ModelAttribute("message") String message, RedirectAttributes redir) throws MessagingException {

        RedirectView redirectView = new RedirectView("/settings/messages", true);

        if (userRepository.findFirstByLogin(login) == null) {
            redir.addFlashAttribute("bad_status", "Użytkownik o takim loginie nie istnieje.");
            return redirectView;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());

        if (login.equals(uzytkownik.getLogin())) {

            redir.addFlashAttribute("bad_status", "Nie możesz pisac wiadomości do siebie.");
            return redirectView;

        }

        ArrayList<User> uzytkownicy = new ArrayList<>();
        uzytkownicy.add(uzytkownik);
        uzytkownicy.add(userRepository.findFirstByLogin(login));
        Conversation conversation;

        if (conversationService.findByUsers(uzytkownicy.get(0).getUser_id(), uzytkownicy.get(1).getUser_id()).isPresent()) {
            conversation = conversationService.findByUsers(uzytkownicy.get(0).getUser_id(), uzytkownicy.get(1).getUser_id()).get();
            Message messageobject = new Message(message, new Date(), Message.Status.NEW, conversation, uzytkownik);
            messageRepository.save(messageobject);
            Message newmessageobject = conversation.getOtherUserNewMessage(uzytkownik);
            if (!newmessageobject.getContent().equals("")) {
                newmessageobject.setStatus(Message.Status.SEEN);
                messageRepository.save(newmessageobject);
            }

            ArrayList<String> list =new ArrayList<>(userRepository.findAllExpiry_timeByUsername(uzytkownicy.get(1).getLogin()));
            if(!list.isEmpty()){

                for (String s:list) {
                    if(Long.parseLong(s)-System.currentTimeMillis()<0){
                        sendMail.sendingMail(uzytkownicy.get(1).getEmail(),
                                "NORGIE - Otrzymales nową wiadomość","Witaj "+uzytkownicy.get(1).getLogin()+",\n Otrzymałeś nową wiadomość od "
                                        +uzytkownicy.get(0).getLogin()+"\n Treść wiadomości: "+newmessageobject.getContent());
                    }

                }

            }else {

                sendMail.sendingMail(uzytkownicy.get(1).getEmail(),"NORGIE - Otrzymales nową wiadomość","Witaj "+uzytkownicy.get(1).getLogin()+",\n Otrzymałeś nową wiadomość od "
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
        Message m = new Message(message, new Date(), Message.Status.NEW, conversation, uzytkownik);
        messageRepository.save(m);
        ArrayList<String> list =new ArrayList<>(userRepository.findAllExpiry_timeByUsername(uzytkownicy.get(1).getLogin()));
        if(!list.isEmpty()){

            for (String s:list) {
                if(Long.parseLong(s)-System.currentTimeMillis()<0){
                    sendMail.sendingMail(uzytkownicy.get(1).getEmail(),
                            "NORGIE - Otrzymales nową wiadomość","Witaj "+uzytkownicy.get(1).getLogin()+",\n Otrzymałeś nową wiadomość od "
                                    +uzytkownicy.get(0).getLogin()+"\n Treść wiadomości: "+m.getContent());
                }

            }

        }else {

            sendMail.sendingMail(uzytkownicy.get(1).getEmail(),
                    "NORGIE - Otrzymales nową wiadomość","Witaj "+uzytkownicy.get(1).getLogin()+",\n Otrzymałeś nową wiadomość od "
                            +uzytkownicy.get(0).getLogin()+"\n Treść wiadomości: "+m.getContent());

        }


        redirectView = new RedirectView("/messages/" + conversation.getConversation_id(), true);
        return redirectView;


    }

    @PostMapping("changeUserDetails")
    public RedirectView changeUserDetails(@ModelAttribute("name") String name, @ModelAttribute("surname") String surname, @ModelAttribute("email") String email, RedirectAttributes redir) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());

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
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());

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
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());

        if (!passwordEncoder.matches(currentpassword, uzytkownik.getPassword())) {

            redir.addFlashAttribute("bad_status", "Twoje obecne hasło się nie zgadza.");
            return redirectView;

        }

        uzytkownik.setPassword(passwordEncoder.encode(newpassword));
        userRepository.save(uzytkownik);
        redir.addFlashAttribute("status", "Twoje hasło zostało zmienione.");
        sendMail.sendingMail(uzytkownik.getEmail(),"Norgie - Hasło zostało zmienione", "Witaj "+uzytkownik.getLogin()+", \n hasło do twojego konta zostało zmienione.\n\nJeśli nie zostało ono zaktualizowane przez Ciebie, powiadom nas o tym jak najszybciej.");
        return redirectView;

    }

    @GetMapping("/settings/liked/page/{id}")
    public ModelAndView liked(@PathVariable("id") String id){

        ModelAndView modelAndView = new ModelAndView("user_profile_liked");


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());
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
    public ModelAndView functions() {

        ModelAndView modelAndView = new ModelAndView("user_admin_profile_functions");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());
        modelAndView.addObject("user", uzytkownik);
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;


    }

    @PostMapping("/settings/admin/xkom")
    public @ResponseBody void xkom(){

        parsingBot.fetchXkom();


    }

    @PostMapping("/settings/admin/amazon")
    public @ResponseBody void amazon(){

        parsingBot.fetchAmazon("https://www.amazon.com/Best-Sellers-Womens-Fashion/zgbs/fashion/", "Moda");


    }

    //[0] - status
    //[1] - informacja o skanowaniu
    //[2] - ilosc poprawnie przeskanowanych
    //[3] - blednie przeskanowane promocje
    //[4] - ilosc dodanych nowych promocji
    @GetMapping("/settings/admin/status")
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


}
