package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.okazje.project.entities.Conversation;
import pl.okazje.project.entities.Message;
import pl.okazje.project.entities.User;
import pl.okazje.project.repositories.*;
import pl.okazje.project.services.ConversationService;
import pl.okazje.project.services.SendMail;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class MessagesController {

    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ConversationService conversationService;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    SendMail sendMail;

    @GetMapping("/messages")
    public ModelAndView messages() {

        ModelAndView modelAndView = new ModelAndView("user_messages");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;

    }




    @GetMapping("/messages/{id}")
    public ModelAndView pageMessages(@PathVariable("id") String id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());

        if (!conversationService.findById(Long.parseLong(id)).isPresent()) {

            ModelAndView modelAndView = new ModelAndView("error");
            return modelAndView;

        }


        if (conversationService.findById(Long.parseLong(id)).get().getUsers().contains(uzytkownik)) {

            Message newmessageobject = conversationService.findById(Long.parseLong(id)).get().getOtherUserNewMessage(uzytkownik);
            if (!newmessageobject.getContent().equals("")) {
                newmessageobject.setStatus("odczytane");
                messageRepository.save(newmessageobject);
            }


            ModelAndView modelAndView = new ModelAndView("user_messages");
            modelAndView.addObject("list_of_tags", tagRepository.findAll());
            modelAndView.addObject("list_of_shops", shopRepository.findAll());
            modelAndView.addObject("list_of_conversations", uzytkownik.getConversationsSorted());
            modelAndView.addObject("current_conversation", conversationService.findById(Long.parseLong(id)).get());
            modelAndView.addObject("user", uzytkownik);
            modelAndView.addObject("current_id", Integer.parseInt(id));

            return modelAndView;

        }

        ModelAndView modelAndView = new ModelAndView("error");
        return modelAndView;


    }

    @PostMapping("sendMessage")
    public String sendMessage(@ModelAttribute("new_message_conv_id") String new_message_conv_id, @ModelAttribute("new_message") String new_message) throws MessagingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findFirstByLogin(authentication.getName());


        Message message = new Message();
        message.setStatus("nieodczytane");
        message.setContent(new_message);
        message.setConversation(conversationService.findById(Long.parseLong(new_message_conv_id)).get());
        message.setCr_date(new Date());
        message.setUser(uzytkownik);
        User otheruser = conversationService.findById(Long.parseLong(new_message_conv_id)).get().getOtherUser(uzytkownik);
        ArrayList<String> list =new ArrayList<>(userRepository.getUserSession(otheruser.getLogin()));
        if(!list.isEmpty()){

            for (String s:list) {
                if(Long.parseLong(s)-System.currentTimeMillis()<0){
                    sendMail.sendingMail(otheruser.getEmail(),
                            "NORGIE - Otrzymales nową wiadomość","Witaj "+otheruser.getLogin()+",\n Otrzymałeś nową wiadomość od "
                                    +uzytkownik.getLogin()+"\n Treść wiadomości: "+message.getContent());
                }

            }

        }else {

            sendMail.sendingMail(otheruser.getEmail(),
                    "NORGIE - Otrzymales nową wiadomość","Witaj "+otheruser.getLogin()+",\n Otrzymałeś nową wiadomość od "
                            +uzytkownik.getLogin()+"\n Treść wiadomości: "+message.getContent());

        }



        messageRepository.save(message);


        return "redirect:/messages/" + new_message_conv_id;
    }


    @GetMapping("/conversation/{id}")
    public @ResponseBody
    String[][] conversation(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik1 = userRepository.findFirstByLogin(authentication.getName());
        User uzytkownik2 = conversationService.findById(Long.parseLong(id)).get().getOtherUser(uzytkownik1);

        ArrayList<Message> list = conversationService.findByUsers(uzytkownik1.getUser_id(), uzytkownik2.getUser_id()).get().getMessagesSorted();
        String array[][] = new String[list.size()][2];

        for (int i = 0; i < list.size(); i++) {

            array[i][0] = list.get(i).getContent();
            if (list.get(i).getUser().getUser_id() == uzytkownik1.getUser_id()) array[i][1] = "right";
            if (list.get(i).getUser().getUser_id() == uzytkownik2.getUser_id()) array[i][1] = "left";

        }


        return array;

    }

    @GetMapping("/user_conversations/{id}")
    public @ResponseBody
    String[][] userConversations(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik1 = userRepository.findFirstByLogin(authentication.getName());

        ArrayList<Conversation> list = uzytkownik1.getConversationsSorted();
        String array[][] = new String[list.size()][6];

        for (int i = 0; i < list.size(); i++) {

            array[i][0] = list.get(i).getOtherUser(uzytkownik1).getLogin();
            array[i][1] = list.get(i).getNewestMessage(uzytkownik1);
            if (list.get(i).hasNewMessage(uzytkownik1)) array[i][2] = "nieodczytane";
            if (!list.get(i).hasNewMessage(uzytkownik1)) array[i][2] = "odczytane";
            array[i][3] = list.get(i).getConversation_id().toString();
            array[i][4] = list.get(i).getOtherUser(uzytkownik1).getLogin();
            array[i][5] = list.get(i).getNewestMessage(uzytkownik1);

        }


        return array;

    }

    @PostMapping("/sendNewMessage")
    public @ResponseBody
    String sendNewMessage(HttpServletRequest request, HttpServletResponse response, @RequestParam("message") String message, @RequestParam("user") String user2) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik1 = userRepository.findFirstByLogin(authentication.getName());
        User uzytkownik2 = userRepository.findFirstByLogin(user2);


        Message m = new Message();
        m.setUser(uzytkownik1);
        m.setCr_date(new Date());
        m.setConversation(conversationService.findByUsers(uzytkownik1.getUser_id(),uzytkownik2.getUser_id()).get());
        m.setContent(message);
        m.setStatus("nieodczytane");
        messageRepository.save(m);

        ArrayList<String> list =new ArrayList<>(userRepository.getUserSession(uzytkownik2.getLogin()));
        if(!list.isEmpty()){

            for (String s:list) {
                if(Long.parseLong(s)-System.currentTimeMillis()<0){
                    sendMail.sendingMail(uzytkownik2.getEmail(),
                            "NORGIE - Otrzymales nową wiadomość","Witaj "+uzytkownik2.getLogin()+",\n Otrzymałeś nową wiadomość od "
                                    +uzytkownik1.getLogin()+"\n Treść wiadomości: "+m.getContent());
                }

            }

        }else {

            sendMail.sendingMail(uzytkownik2.getEmail(),
                    "NORGIE - Otrzymales nową wiadomość","Witaj "+uzytkownik2.getLogin()+",\n Otrzymałeś nową wiadomość od "
                            +uzytkownik1.getLogin()+"\n Treść wiadomości: "+m.getContent());

        }

        return "";


    }

}
