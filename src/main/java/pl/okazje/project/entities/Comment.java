package pl.okazje.project.entities;

import pl.okazje.project.exceptions.DataTooLongException;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Comment {
    public enum Status{POSTED, DELETED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comment_id;
    @Column(length = 700)
    private String content;
    @Enumerated(EnumType.STRING)
    private Status status = Status.POSTED;
    private Date cr_date = new Date();

    @OneToMany(mappedBy="comment")
    private Set<Rating> ratings;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="discount_id")
    private Discount discount;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    public Comment() {
    }


    public Long getComment_id() {
        return comment_id;
    }

    public void setComment_id(Long comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        if (this.getStatus()==null||this.getStatus().equals(Status.POSTED)){
            return content;
        }else{
            return "<Komentarz usunięty przez Moderatora>";
        }
    }

    public void setContent(String content) {
        if(content.length()>700){
            throw new DataTooLongException(content.substring(0, Math.min(content.length(), 50))+"...");
        }
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
        long diff = date.getTime() - cr_date.getTime();

        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    public long getDataToNumber(){
        return new Date().getTime() - this.cr_date.getTime();
    }

}
