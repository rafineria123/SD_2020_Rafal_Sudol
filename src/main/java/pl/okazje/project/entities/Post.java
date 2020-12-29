package pl.okazje.project.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

//obnizka=cena pierwotna -> cena obecna
//kupon = przecena w gazetce/sklepie lokalnym
//kod = przecena online
@Entity
public class Post {

    public enum Status{ USUNIETE, OCZEKUJACE, ZATWIERDZONE}

    private static Date data = new Date();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;
    @Column(length = 500)
    private String title;
    @Column(length = 900)
    private String content;
    private Date creationdate;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;


    public Post() {
    }

    public static Date getData() {
        return data;
    }

    public static void setData(Date data) {
        Post.data = data;
    }

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
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

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
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
        return comments;
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

    public String getCreation_date_formated() {

        Format formatter = new SimpleDateFormat("dd.MM.yy");
        String s = formatter.format(this.creationdate);
        return s;
    }


}
