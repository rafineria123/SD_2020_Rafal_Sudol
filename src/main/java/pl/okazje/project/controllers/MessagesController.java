package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.okazje.project.entities.Conversation;
import pl.okazje.project.entities.User;
import pl.okazje.project.repositories.*;
import pl.okazje.project.services.*;

import java.util.Optional;

@Controller
public class MessagesController {

    private final ConversationService conversationService;
    private final ShopService shopService;
    private final TagService tagService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Autowired
    public MessagesController(ConversationService conversationService, ShopService shopService, TagService tagService, AuthenticationService authenticationService, UserService userService) {
        this.conversationService = conversationService;
        this.shopService = shopService;
        this.tagService = tagService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping("/messages/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ModelAndView getConversationPage(@PathVariable("id") String id) {
        ModelAndView modelAndView;
        Optional<Conversation> optionalConversation = conversationService.findById(Long.parseLong(id));
        if(optionalConversation.isPresent()){
            User currentUser = authenticationService.getCurrentUser().get();
            modelAndView = new ModelAndView("user_messages");
            modelAndView.addObject("list_of_tags", tagService.findAll());
            modelAndView.addObject("list_of_shops", shopService.findAll());
            modelAndView.addObject("list_of_conversations", currentUser.getConversationsSorted());
            modelAndView.addObject("current_conversation", optionalConversation.get());
            modelAndView.addObject("user", currentUser);
            modelAndView.addObject("current_id", Integer.parseInt(id));
        }else {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }

    @PostMapping("sendMessage")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String sendMessage(@ModelAttribute("new_message_conv_id") String conversationId, @ModelAttribute("new_message") String messageContent) {
        conversationService.sendMessage(conversationService.findById(Long.parseLong(conversationId)).get().getOtherUser(authenticationService.getCurrentUser().get()),messageContent);
        return "redirect:/messages/" + conversationId;
    }


    @GetMapping("/conversation/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public @ResponseBody
    String[][] getConversationBody(@PathVariable String id){
        return conversationService.getConversationBody(Long.parseLong(id));

    }

    @GetMapping("/user_conversations/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public @ResponseBody
    String[][] getAllConversations(){
        return conversationService.getAllCurrentUserConversationsAsArray();
    }

    @PostMapping("/sendNewMessage")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public @ResponseBody
    void sendNewMessage(@RequestParam("message") String message, @RequestParam("user") String otherUser){
        conversationService.sendMessage(userService.findFirstByLogin(otherUser).get(),message);
        }
}
