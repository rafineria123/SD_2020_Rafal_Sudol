package pl.okazje.project.services;

import org.springframework.stereotype.Service;
import pl.okazje.project.entities.comments.Comment;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.ratings.CommentRating;
import pl.okazje.project.entities.ratings.DiscountRating;
import pl.okazje.project.entities.ratings.Rating;
import pl.okazje.project.entities.User;
import pl.okazje.project.repositories.RatingRepository;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final AuthenticationService authenticationService;
    private final DiscountService discountService;
    private final CommentService commentService;

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
            if (r.getUser().getUserId().equals(user.getUserId())) {
                isAlreadyRated = true;
                break;
            }
        }
        if(!isAlreadyRated) {
            DiscountRating newRating = new DiscountRating();
            newRating.setUser(user);
            newRating.setDiscount(discount);
            ratingRepository.save(newRating);
        }
    }

    public void removeRatingFromDiscount(Long id){
        User user = authenticationService.getCurrentUser().get();
        Discount discount = discountService.findById(id).get();
        for (Rating r : discount.getRatings()) {

            if (r.getUser().getUserId().equals(user.getUserId())) {
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
            if (r.getUser().getUserId().equals(user.getUserId())) {
                isAlreadyRated = true;
                break;
            }
        }
        if(!isAlreadyRated){
            CommentRating newRating = new CommentRating();
            newRating.setUser(user);
            newRating.setComment(comment);
            ratingRepository.save(newRating);
        }
    }

}
