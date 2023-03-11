package pl.okazje.project.utills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.Post;
import pl.okazje.project.entities.comments.Comment;
import pl.okazje.project.entities.comments.DiscountComment;
import pl.okazje.project.entities.comments.PostComment;
import pl.okazje.project.entities.ratings.CommentRating;
import pl.okazje.project.entities.ratings.DiscountRating;
import pl.okazje.project.repositories.*;

import javax.transaction.Transactional;
import java.io.*;
import java.util.*;

@Component
public class DataBaseFiller {

    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RatingRepository ratingRepository;


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void updateDiscountCreateDate() {
        List<Discount> listOfDiscounts = new ArrayList<>();
        discountRepository.findAll().forEach(listOfDiscounts::add);
        Random r = new Random();
        //createdate updates
        for(int i = 0;i<12;i++){
            Date dt = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, r.nextInt(10)+1);
            dt = c.getTime();
            listOfDiscounts.get(i).setExpireDate(dt);
            dt = new Date();
            c.setTime(dt);
            c.add(Calendar.DATE, -(r.nextInt(10)+1));
            dt = c.getTime();
            listOfDiscounts.get(i).setCreateDate(dt);
        }
        discountRepository.saveAll(listOfDiscounts);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void addCommentsToDiscounts() throws IOException {
        List<DiscountComment> listOfDiscountComments = new ArrayList<>();
        List<Discount> listOfDiscounts = new ArrayList<>();
        Random r = new Random();
        discountRepository.findAll().forEach(listOfDiscounts::add);
        BufferedReader reader = new BufferedReader(
                new FileReader("C:/zdjeciaprojekt/comment-data.txt"));
        String line = reader.readLine();
        for(int i = 0;i<13;i++){

            DiscountComment dc = new DiscountComment();
            dc.setCreateDate(new Date());
            dc.setContent(line);
            Discount d = listOfDiscounts.get(r.nextInt(14)+1);
            dc.setUser(d.getUser());
            dc.setDiscount(d);
            listOfDiscountComments.add(dc);
            line = reader.readLine();

        }
        commentRepository.saveAll(listOfDiscountComments);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void addCommentsToPosts() throws IOException {
        List<Post> listOfPosts = new ArrayList<>();
        postRepository.findAll().forEach(listOfPosts::add);
        Random r = new Random();
        List<PostComment> listOfPostComments = new ArrayList<>();
        BufferedReader reader = new BufferedReader(
                new FileReader("C:/zdjeciaprojekt/comment-data.txt"));
        String line = reader.readLine();
        for(int i = 0;i<13;i++){

            PostComment pc = new PostComment();
            pc.setCreateDate(new Date());

            pc.setContent(line);
            Post p = listOfPosts.get(r.nextInt(4)+1);
            pc.setUser(p.getUser());
            pc.setPost(p);
            listOfPostComments.add(pc);
            line = reader.readLine();

        }


        commentRepository.saveAll(listOfPostComments);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void addRatingToDiscounts(){
        List<Discount> listOfDiscounts = new ArrayList<>();
        Random r = new Random();
        discountRepository.findAll().forEach(listOfDiscounts::add);
        for (Discount d:listOfDiscounts) {
            int generatedNumber = r.nextInt(3)+1;
            if(generatedNumber>0){
                DiscountRating dr = new DiscountRating();
                dr.setDiscount(d);
                dr.setUser(userRepository.findFirstByUserIdEquals(1));
                ratingRepository.save(dr);
            }
            if(generatedNumber>1){
                DiscountRating dr = new DiscountRating();
                dr.setDiscount(d);
                dr.setUser(userRepository.findFirstByUserIdEquals(2));
                ratingRepository.save(dr);
            }
            if(generatedNumber>2){
                DiscountRating dr = new DiscountRating();
                dr.setDiscount(d);
                dr.setUser(userRepository.findFirstByUserIdEquals(3));
                ratingRepository.save(dr);
            }
        }

    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void addRatingToComments(){
        List<Comment> listOfComments = new ArrayList<>();
        Random r = new Random();
        commentRepository.findAll().forEach(listOfComments::add);
        for (Comment c:listOfComments) {
            int generatedNumber = r.nextInt(3)+1;
            if(generatedNumber>0){
                CommentRating cr = new CommentRating();
                cr.setComment(c);
                cr.setUser(userRepository.findFirstByUserIdEquals(1));
                ratingRepository.save(cr);
            }
            if(generatedNumber>1){
                CommentRating cr = new CommentRating();
                cr.setComment(c);
                cr.setUser(userRepository.findFirstByUserIdEquals(2));
                ratingRepository.save(cr);
            }
            if(generatedNumber>2){
                CommentRating cr = new CommentRating();
                cr.setComment(c);
                cr.setUser(userRepository.findFirstByUserIdEquals(3));
                ratingRepository.save(cr);
            }
        }

    }
}
