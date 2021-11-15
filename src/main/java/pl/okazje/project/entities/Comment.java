package pl.okazje.project.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@commentId")
public class Comment {
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
    private Set<Rating> ratings;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="discountId")
    private Discount discount;

    @ManyToOne
    @JoinColumn(name="postId")
    private Post post;

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
        // TODO: change content in view when comment status is DELETED
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

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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

}
