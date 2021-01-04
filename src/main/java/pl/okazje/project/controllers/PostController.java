package pl.okazje.project.controllers;

import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.entities.*;
import pl.okazje.project.repositories.*;

import java.util.Date;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    TagRepository tagRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    RatingRepository ratingRepository;

    @GetMapping("/{id}")
    public ModelAndView postPage(@PathVariable("id") String id){

        ModelAndView modelAndView = new ModelAndView("post");

        if (postRepository.findById(Long.parseLong(id)).get().getStatus().equals(Post.Status.USUNIETE)) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("USER") || r.getAuthority().equals("ADMIN"))) {

                User uzytkownik1 = userRepository.findUserByLogin(authentication.getName());
                if (uzytkownik1.hasPost(Long.parseLong(id)) || uzytkownik1.getROLE().equals("ADMIN")) {

                    modelAndView.addObject("list_of_tags", tagRepository.findAll());
                    modelAndView.addObject("list_of_shops", shopRepository.findAll());
                    modelAndView.addObject("post", postRepository.findById(Long.parseLong(id)).get());
                    return modelAndView;

                }
                modelAndView = new ModelAndView("error");
                return modelAndView;


            } else {
                modelAndView = new ModelAndView("error");
                return modelAndView;
            }


        }

        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        modelAndView.addObject("post", postRepository.findById(Long.parseLong(id)).get());

        return modelAndView;

    }



    @PostMapping("/addcomment")
    public String addcomment(@ModelAttribute("discountidaddcomment") String discountaddcomment, @ModelAttribute("comment") String comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findUserByLogin(authentication.getName());
        Comment comment1 = new Comment();
        comment1.setPost(postRepository.findById(Long.parseLong(discountaddcomment)).get());
        comment1.setUser(uzytkownik);
        comment1.setContent(comment);
        comment1.setCr_date(new Date());
        commentRepository.save(comment1);

        return "redirect:/post/" + Long.parseLong(discountaddcomment);
    }

    @PostMapping("/removecomment")
    public String removecomment(@ModelAttribute("comment_id") String comment_id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik1 = userRepository.findUserByLogin(authentication.getName());
        Comment comment = commentRepository.findById(Long.parseLong(comment_id)).get();
        if(uzytkownik1.getROLE().equals("ADMIN")){


            comment.setStatus("Usuniete");
            commentRepository.save(comment);

        }


        return "redirect:/post/"+comment.getPost().getPost_id();

    }

    @PostMapping("/ratecomment")
    public String ratecomment(@ModelAttribute("commentid") String commentid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findUserByLogin(authentication.getName());
        Comment comment = commentRepository.findById((Long.parseLong(commentid))).get();
        for (Rating r : comment.getRatings()) {

            if (r.getUser().getUser_id().equals(uzytkownik.getUser_id())) {
                return "redirect:/discount/" + comment.getPost().getPost_id().toString();

            }

        }

        Rating newrating = new Rating();
        newrating.setUser(uzytkownik);
        newrating.setComment(comment);
        ratingRepository.save(newrating);

        return "redirect:/post/" + comment.getPost().getPost_id().toString();

    }

    @GetMapping("/add")
    public ModelAndView addPost() {
        ModelAndView modelAndView = new ModelAndView("add_post");
        modelAndView.addObject("list_of_tags", tagRepository.findAll());
        modelAndView.addObject("list_of_shops", shopRepository.findAll());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addPost(@ModelAttribute("content") String content,@ModelAttribute("title") String title, RedirectAttributes redir){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik = userRepository.findUserByLogin(authentication.getName());
        ModelAndView modelAndView;

        Post post = new Post();
        try {
            if(title.isEmpty()||content.isEmpty()){
                throw new IllegalArgumentException();
            }
            post.setTitle(title);
            post.setContent(content);
            post.setCreationdate(new Date());
            post.setStatus(Post.Status.ZATWIERDZONE);
            post.setUser(uzytkownik);
        }catch (Exception e){


            redir.addFlashAttribute("error", true);
            modelAndView = new ModelAndView(new RedirectView("/post/add"));
            return modelAndView;


        }

        postRepository.save(post);
        RedirectView redirectView = new RedirectView("/post/"+post.getPost_id());
        redir.addFlashAttribute("good_status", "Twoja post została dodany.");
        modelAndView =  new ModelAndView(redirectView);
        return modelAndView;



    }

    @PostMapping("/remove")
    public String removePost(@ModelAttribute("post_id") String post_id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User uzytkownik1 = userRepository.findUserByLogin(authentication.getName());
        if(uzytkownik1.getROLE().equals("ADMIN")){

            Post post = postRepository.findById(Long.parseLong(post_id)).get();
            post.setStatus(Post.Status.USUNIETE);
            postRepository.save(post);

        }

        return "redirect:/post/"+post_id;

    }



}
