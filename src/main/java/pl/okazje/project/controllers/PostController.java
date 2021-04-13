package pl.okazje.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.entities.*;
import pl.okazje.project.repositories.*;
import pl.okazje.project.services.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/post")
public class PostController {

    private final TagRepository tagRepository;
    private final ShopRepository shopRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;
    private final RatingRepository ratingRepository;
    private final PostService postService;
    private final TagService tagService;
    private final ShopService shopService;
    private final RatingService ratingService;

    @Autowired
    public PostController(RatingService ratingService, TagRepository tagRepository, ShopRepository shopRepository, PostRepository postRepository, UserRepository userRepository,
                          CommentService commentService, RatingRepository ratingRepository, PostService postService, TagService tagService, ShopService shopService) {
        this.tagRepository = tagRepository;
        this.shopRepository = shopRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentService = commentService;
        this.ratingRepository = ratingRepository;
        this.postService = postService;
        this.tagService = tagService;
        this.shopService = shopService;
        this.ratingService = ratingService;
    }


    @GetMapping("/{id}")
    public ModelAndView getPost(@PathVariable("id") String id){
        ModelAndView modelAndView;
        Optional<Post> postOptional = postService.findById(Long.parseLong(id));
        if(postOptional.isPresent()){
            modelAndView = new ModelAndView("post");
            modelAndView.addObject("list_of_tags", tagService.findAll());
            modelAndView.addObject("list_of_shops", shopService.findAll());
            modelAndView.addObject("post", postOptional.get());
            return modelAndView;
        }
        modelAndView = new ModelAndView("error");
        return modelAndView;
    }



    @PostMapping("/addcomment")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String addComment(@ModelAttribute("discountidaddcomment") String postId, @ModelAttribute("comment") String comment) {
        commentService.addCommentToPost(Long.parseLong(postId), comment);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/removecomment")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String removeComment(@ModelAttribute("comment_id") String comment_id, HttpServletRequest request){
        commentService.removeComment(Long.parseLong(comment_id));
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/ratecomment")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String ratecomment(@ModelAttribute("commentid") String commentid, HttpServletRequest request) {
        ratingService.addRatingToComment(Long.parseLong(commentid));
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ModelAndView addPost() {
        ModelAndView modelAndView = new ModelAndView("add_post");
        modelAndView.addObject("list_of_tags", tagService.findAll());
        modelAndView.addObject("list_of_shops", shopService.findAll());
        return modelAndView;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ModelAndView addPost(@ModelAttribute("content") String content,@ModelAttribute("title") String title,@ModelAttribute("tag") String tag, @ModelAttribute("shop") String shop,
                                RedirectAttributes redir){
        ModelAndView modelAndView;
        if(postService.addPost(content, title, tag, shop)){
            RedirectView redirectView = new RedirectView("/post/"+postService.findByTitle(title).getPost_id());
            redir.addFlashAttribute("good_status", "Twoja post została dodany.");
            modelAndView =  new ModelAndView(redirectView);
            return modelAndView;
        }
            redir.addFlashAttribute("error", true);
            modelAndView = new ModelAndView(new RedirectView("/post/add"));
            return modelAndView;
    }

    @PostMapping("/remove")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String removePost(@ModelAttribute("post_id") String post_id){
        postService.deletePost(Long.parseLong(post_id));
        return "redirect:/post/"+post_id;

    }



}
