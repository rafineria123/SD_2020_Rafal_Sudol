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
    private String status="";
    private Date cr_date = new Date();

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

    }

    public Long getComment_id() {
        return comment_id;
    }

    public void setComment_id(Long comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        if (this.getStatus()==null){
            return content;
        }else if(this.getStatus().equals("Usuniete")){
            return "<Komentarz usunięty przez Moderatora>";
        }else{

            return content;

        }


    }

    public void setContent(String content) {
        this.content = content;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
        long diff = date.getTime() - cr_date.getTime();

        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));

        return diffDays;

    }

    public long getDataToNumber(){

        long daysBetween = new Date().getTime() - this.cr_date.getTime();
        return daysBetween;

    }

}
