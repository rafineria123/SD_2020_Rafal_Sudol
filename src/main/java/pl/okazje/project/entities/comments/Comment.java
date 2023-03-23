package pl.okazje.project.entities.comments;

import pl.okazje.project.entities.ratings.CommentRating;
import pl.okazje.project.entities.ratings.Rating;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.Post;
import pl.okazje.project.entities.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity(name = "comment")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Comment {
    public enum Status{POSTED, DELETED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(length = 700)
    private String content;

    @Enumerated(EnumType.STRING)
    private Status status = Status.POSTED;

    private Date createDate;

    @OneToMany(mappedBy="comment")
    private Set<CommentRating> ratings;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    private User user;

    public Comment() {
        createDate = new Date();
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        if(this.status == Status.DELETED){
            return "Komentarz został zablokowany przez moderatora.";
        }
            return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Set<CommentRating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<CommentRating> ratings) {
        this.ratings = ratings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getRatingsSize(){
        if(ratings == null){
            return 0;
        }else {
            return ratings.size();
        }
    }

    public int getDateDifference(){
        Date date = new Date();
        long diff = date.getTime() - this.createDate.getTime();
        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    public long getDataToNumber(){
        return new Date().getTime() - this.createDate.getTime();
    }

    public boolean isDiscountComment(){
        if(this instanceof DiscountComment)return true;
        return false;
    }

}
