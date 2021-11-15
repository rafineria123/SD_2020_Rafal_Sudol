package pl.okazje.project.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@userId")
public class User implements UserDetails {

    private static final long serialVersionUID = -1113427347189094885L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(unique = true, length = 25)
    private String login;
    @Column(length = 700)
    private String password;
    @Column(length = 50)
    private String email;
    private String status;
    private String role;
    private Date createDate;
    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "informationId")
    private Information information;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "banId")
    private Ban ban;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tokenId", nullable = true)
    private Token token;

    @ManyToOne
    @JoinColumn(name = "rankId", nullable = true)
    private Rank rank;

    @OneToMany(mappedBy = "user")
    private Set<Discount> discounts;

    @OneToMany(mappedBy = "user")
    private Set<Post> posts;

    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "User_Conversation",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "conversationId")
    )
    private Set<Conversation> conversations;

    @OneToMany(mappedBy = "user")
    private Set<Rating> ratings;

    @OneToMany(mappedBy = "user")
    private Set<Message> messages;

    public User() {
        this.email = "";
        this.role = "USER";
        this.enabled = false;
    }

    public Ban getBan() {
        return ban;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setBan(Ban ban) {
        this.ban = ban;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer user_id) {
        this.userId = user_id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date cr_date) {
        this.createDate = cr_date;
    }

    public Information getInformation() {
        if (information == null) {
            return new Information();
        }
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Set<Discount> getDiscounts() {
        if (discounts == null) return new HashSet<Discount>();
        return discounts;
    }

    public void setDiscounts(Set<Discount> discounts) {
        this.discounts = discounts;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Comment> getComments() {
        if (comments == null) return new HashSet<Comment>();
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Conversation> getConversations() {
        if (conversations == null) return new HashSet<Conversation>();
        return conversations;
    }

    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String ROLE) {
        this.role = ROLE;
    }

    public ArrayList<Conversation> getConversationsSorted() {
        ArrayList<Conversation> conversations = new ArrayList<>(getConversations());
        Collections.sort(conversations, (o1, o2) -> o2.getNewestMessage().getCreateDate().compareTo(o1.getNewestMessage().getCreateDate()));
        return conversations;
    }

    public int getCommentsAmount() {
        return getComments().size();
    }

    public String getCreateDateFormatted() {
        Format formatter = new SimpleDateFormat("dd.MM.yy");
        String s = formatter.format(this.createDate);
        return s;
    }

    public int getDiscountsAmount() {
        return getDiscounts().size();
    }

    public boolean hasName() {
        return information != null && information.getName() != null && !information.getName().isEmpty() && !information.getName().equals("");
    }

    public boolean hasSurname() {
        return information != null&&information.getSurname() != null&&!information.getSurname().isEmpty()&&!information.getSurname().equals("");
    }

    public boolean hasDiscount(Long discount_id) {
        for (Discount d : getDiscounts()) {
            if (d.getDiscountId().equals(discount_id)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPost(Long post_id) {
        for (Post p : getPosts()) {
            if (p.getPostId().equals(post_id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
        return Collections.singletonList(simpleGrantedAuthority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.ban == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}

