package pl.okazje.project.services;

import org.springframework.stereotype.Service;
import pl.okazje.project.entities.comments.Comment;
import pl.okazje.project.entities.comments.DiscountComment;
import pl.okazje.project.entities.comments.PostComment;
import pl.okazje.project.repositories.CommentRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final DiscountService discountService;
    private final AuthenticationService authenticationService;
    private final PostService postService;

    public CommentService(CommentRepository commentRepository, DiscountService discountService, AuthenticationService authenticationService, PostService postService) {
        this.commentRepository = commentRepository;
        this.discountService = discountService;
        this.authenticationService = authenticationService;
        this.postService = postService;
    }

    public void save(Comment comment){
        commentRepository.save(comment);
    }

    public Optional<Comment> findById(Long id){
        return commentRepository.findById(id);
    }

    public void addCommentToDiscount(Long id, String content){
        DiscountComment comment1 = new DiscountComment();
        comment1.setDiscount(discountService.findById(id).get());
        comment1.setUser(authenticationService.getCurrentUser().get());
        comment1.setContent(content);
        comment1.setCreateDate(new Date());
        this.save(comment1);
    }

    public void addCommentToPost(Long id, String content){
        PostComment comment1 = new PostComment();
        comment1.setPost(postService.findById(id).get());
        comment1.setUser(authenticationService.getCurrentUser().get());
        comment1.setContent(content);
        comment1.setCreateDate(new Date());
        this.save(comment1);
    }

    public void removeComment(Long id){
        Comment tempComment = this.findById(id).get();
        tempComment.setStatus(Comment.Status.DELETED);
        this.save(tempComment);
    }
}
