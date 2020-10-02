package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Comment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comment_id;
    @Column(length = 700)
    private String content;
    private Integer upper_comment_id;
    private Date cr_date;

    @OneToMany(mappedBy="comment")
    private Set<Rating> ratings;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="discount_id", nullable=true)
    private Discount discount;

    @ManyToOne
    @JoinColumn(name="post_id", nullable=true)
    private Post post;

    public Comment() {
    }

    public Comment(String content) {

        this.content = content;
        this.cr_date = new Date();

    }

    public Long getComment_id() {
        return comment_id;
    }

    public void setComment_id(Long comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUpper_comment_id() {
        return upper_comment_id;
    }

    public void setUpper_comment_id(Integer upper_comment_id) {
        this.upper_comment_id = upper_comment_id;
    }

    public Date getCr_date() {
        return cr_date;
    }

    public void setCr_date(Date cr_date) {
        this.cr_date = cr_date;
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

    public int getRatingsSize(){
        if(ratings == null){
            return 0;
        }else {
            return ratings.size();
        }
    }

    public int getDateDifference(){

        Date date = new Date();
        long diff = date.getTime() - cr_date.getTime();

        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));

        return diffDays;

    }

    public long getDataToNumber(){

        long daysBetween = new Date().getTime() - this.cr_date.getTime();
        return daysBetween;

    }

}
