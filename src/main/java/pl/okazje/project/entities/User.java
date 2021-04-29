package pl.okazje.project.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.okazje.project.exceptions.DataTooLongException;

import javax.persistence.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
public class User implements UserDetails {

    private static final long serialVersionUID = -1113427347189094885L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(unique = true,length = 25)
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
    @JoinColumn(name="rankId", nullable=true)
    private Rank rank;

    @OneToMany(mappedBy="user")
    private Set<Discount> discounts;

    @OneToMany(mappedBy="user")
    private Set<Post> posts;

    @OneToMany(mappedBy="user")
    private Set<Comment> comments;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "User_Conversation",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "conversationId")
    )
    private Set<Conversation> conversations;

    @OneToMany(mappedBy="user")
    private Set<Rating> ratings;

    @OneToMany(mappedBy="user")
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
        if(login.length()>25) throw new DataTooLongException(login);
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
        if(password.length()>700) throw new DataTooLongException(password);
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email.length()>50) throw new DataTooLongException(email);
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
        if(information==null){
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
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Conversation> getConversations() {
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
        ArrayList<Conversation> list = new ArrayList<>(conversations);
        Collections.sort(list, (o1, o2) -> o2.getNewestMessageObject().getCreateDate().compareTo(o1.getNewestMessageObject().getCreateDate()));
        return list;
    }

    public int getCommentsAmount(){
        return new ArrayList<Comment>(comments).size();
    }

    public String getCreateDateFormatted() {
        Format formatter = new SimpleDateFormat("dd.MM.yy");
        String s = formatter.format(this.createDate);
        return s;
    }

    public int getDiscountsAmount(){
        return new ArrayList<Discount>(discounts).size();
    }

    public boolean hasName(){
        if(information!=null){
            if(information.getName()!=null){
                if(!information.getName().isEmpty()||!information.getName().equals("")){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasSurname(){
        if(this.information!=null){
            if(information.getSurname()!=null){
                if(!information.getSurname().isEmpty()||!information.getSurname().equals("")){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasDiscount(Long discount_id){
        for (Discount d:discounts) {
            if (d.getDiscountId()==discount_id){
                return true;
            }
        }
        return false;
    }

    public boolean hasPost(Long post_id){
        for (Post p:posts) {
            if (p.getPostId()==post_id){
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
        if (this.ban!=null){
            return false;
        }
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}

