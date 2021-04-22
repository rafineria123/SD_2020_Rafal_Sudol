package pl.okazje.project.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.entities.*;
import pl.okazje.project.services.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/post")
public class PostController {

    private final CommentService commentService;
    private final PostService postService;
    private final TagService tagService;
    private final ShopService shopService;
    private final RatingService ratingService;

    public PostController(RatingService ratingService, CommentService commentService, PostService postService, TagService tagService, ShopService shopService) {
        this.commentService = commentService;
        this.postService = postService;
        this.tagService = tagService;
        this.shopService = shopService;
        this.ratingService = ratingService;
    }

    @GetMapping("/{id}")
    public ModelAndView getPostPage(@PathVariable("id") String id) {
        ModelAndView modelAndView;
        Optional<Post> postOptional = postService.findById(Long.parseLong(id));
        if (postOptional.isPresent()) {
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
    public String addCommentToPost(@ModelAttribute("discountidaddcomment") String postId, @ModelAttribute("comment") String comment) {
        commentService.addCommentToPost(Long.parseLong(postId), comment);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/removecomment")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String removeCommentFromPost(@ModelAttribute("comment_id") String comment_id, HttpServletRequest request) {
        commentService.removeComment(Long.parseLong(comment_id));
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/ratecomment")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String rateComment(@ModelAttribute("commentid") String commentid, HttpServletRequest request) {
        ratingService.addRatingToComment(Long.parseLong(commentid));
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ModelAndView getAddPostPage() {
        ModelAndView modelAndView = new ModelAndView("add_post");
        modelAndView.addObject("list_of_tags", tagService.findAll());
        modelAndView.addObject("list_of_shops", shopService.findAll());
        return modelAndView;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ModelAndView addPost(@ModelAttribute("content") String content, @ModelAttribute("title") String title, @ModelAttribute("tag") String tag, @ModelAttribute("shop") String shop,
                                RedirectAttributes redir) {
        ModelAndView modelAndView;
        if (postService.addPost(content, title, tag, shop)) {
            RedirectView redirectView = new RedirectView("/post/" + postService.findByTitle(title).getPostId());
            redir.addFlashAttribute("good_status", "Twoj post został dodany.");
            modelAndView = new ModelAndView(redirectView);
            return modelAndView;
        }
        redir.addFlashAttribute("error", true);
        modelAndView = new ModelAndView(new RedirectView("/post/add"));
        return modelAndView;
    }

    @PostMapping("/remove")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String removePost(@ModelAttribute("post_id") String post_id) {
        postService.deletePost(Long.parseLong(post_id));
        return "redirect:/post/" + post_id;
    }

}
