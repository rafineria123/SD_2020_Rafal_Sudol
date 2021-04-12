package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Comment;
import pl.okazje.project.entities.User;
import pl.okazje.project.exceptions.CommentNotFoundException;
import pl.okazje.project.repositories.CommentRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final DiscountService discountService;
    private final AuthenticationService authenticationService;

    @Autowired
    public CommentService(CommentRepository commentRepository, DiscountService discountService, AuthenticationService authenticationService) {
        this.commentRepository = commentRepository;
        this.discountService = discountService;
        this.authenticationService = authenticationService;
    }

    public void save(Comment comment){
        commentRepository.save(comment);
    }

    public Optional<Comment> findById(Long id){
        return commentRepository.findById(id);
    }

    public void addCommentToDiscount(Long id, String content){
        Comment comment1 = new Comment();
        comment1.setDiscount(discountService.findById(id).get());
        comment1.setUser(authenticationService.getCurrentUser().get());
        comment1.setContent(content);
        comment1.setCr_date(new Date());
        this.save(comment1);
    }

    public void removeComment(Long id){
        commentRepository.delete(this.findById(id).get());
    }
}
