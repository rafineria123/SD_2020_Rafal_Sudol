package pl.okazje.project.entities;


import javax.persistence.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
public class Post {

    public enum Status {DELETED, AWAITING, ACCEPTED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @Column(length = 500)
    private String title;
    @Column(length = 1500)
    private String content;
    private Date createDate;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "banId")
    private Ban ban;

    @ManyToOne
    @JoinColumn(name = "tagId", nullable = false)
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "shopId", nullable = false)
    private Shop shop;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;

    public Post() {
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Comment> getComments() {
        if(comments == null) return new HashSet<Comment>();
        return comments;
    }

    public Ban getBan() {
        return ban;
    }

    public void setBan(Ban ban) {
        this.ban = ban;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public int getCommentsSize() {
        if (comments == null) {
            return 0;
        } else {
            return comments.size();
        }
    }

    public String getCreateDateFormatted() {
        Format formatter = new SimpleDateFormat("dd.MM.yy");
        String s = formatter.format(this.createDate);
        return s;
    }

    public ArrayList<Comment> getCommentsSorted() {
        ArrayList<Comment> list = new ArrayList(getComments());
        Collections.sort(list, (o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
        return list;
    }

    public boolean isDeleted() {
        if (this.status == null || !this.status.equals(Status.DELETED)) {
            return false;
        }
        return true;
    }

    public boolean isAwaiting() {
        if (this.status == null || !this.status.equals(Status.AWAITING)) {
            return false;
        }
        return true;
    }

}
