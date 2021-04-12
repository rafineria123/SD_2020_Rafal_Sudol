package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Comment;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.Rating;
import pl.okazje.project.entities.User;
import pl.okazje.project.repositories.RatingRepository;

import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final AuthenticationService authenticationService;
    private final DiscountService discountService;
    private final CommentService commentService;

    @Autowired
    public RatingService(CommentService commentService, DiscountService discountService,RatingRepository ratingRepository, AuthenticationService authenticationService) {
        this.ratingRepository = ratingRepository;
        this.authenticationService = authenticationService;
        this.discountService = discountService;
        this.commentService = commentService;
    }


    public void addRatingToDiscount(Long id){
        User user = authenticationService.getCurrentUser().get();
        Discount discount = discountService.findById(id).get();
        boolean isAlreadyRated = false;
        for (Rating r : discount.getRatings()) {
            if (r.getUser().getUser_id().equals(user.getUser_id())) {
                isAlreadyRated = true;
                break;
            }
        }
        if(!isAlreadyRated) {
            Rating newRating = new Rating();
            newRating.setUser(user);
            newRating.setDiscount(discount);
            ratingRepository.save(newRating);
        }
    }

    public void removeRatingFromDiscount(Long id){
        User user = authenticationService.getCurrentUser().get();
        Discount discount = discountService.findById(id).get();
        for (Rating r : discount.getRatings()) {

            if (r.getUser().getUser_id().equals(user.getUser_id())) {
                ratingRepository.delete(r);
                break;
            }

        }
    }

    public void addRatingToComment(Long id){
        Comment comment = commentService.findById(id).get();
        User user = authenticationService.getCurrentUser().get();
        boolean isAlreadyRated = false;
        for (Rating r : comment.getRatings()) {
            if (r.getUser().getUser_id().equals(user.getUser_id())) {
                isAlreadyRated = true;
                break;
            }
        }
        if(!isAlreadyRated){
            Rating newrating = new Rating();
            newrating.setUser(user);
            newrating.setComment(comment);
            ratingRepository.save(newrating);
        }
    }

}
