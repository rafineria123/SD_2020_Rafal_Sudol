package pl.okazje.project.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;
    @Column(unique = true)
    private String login;
    @Column(length = 700)
    private String password;
    private String email = "";
    private String status;
    private String ROLE = "USER";
    private Date cr_date;
    private boolean enabled = false;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "information_id")
    private Information information = new Information();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ban_id")
    private Ban ban;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "token_id", nullable = true)
    private Token token;


    @ManyToOne
    @JoinColumn(name="rank_id", nullable=true)
    private Rank rank;

    @OneToMany(mappedBy="user")
    private Set<Discount> discounts;

    @OneToMany(mappedBy="user")
    private Set<Post> posts;

    @OneToMany(mappedBy="user")
    private Set<Comment> comments;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "User_Conversation",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "conversation_id")
    )
    private Set<Conversation> conversations;

    @OneToMany(mappedBy="user")
    private Set<Rating> ratings;

    @OneToMany(mappedBy="user")
    private Set<Message> messages;




    public User() {
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

    public User(String login, String password, String email) {
        this.status = "active";
        this.cr_date = new Date();
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        final SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(ROLE);
        return Collections.singletonList(simpleGrantedAuthority);
    }

    public String getPassword() {

        return password;
    }

    @Override
    public String getUsername() {
        return login;
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

    public Date getCr_date() {
        return cr_date;
    }

    public void setCr_date(Date cr_date) {
        this.cr_date = cr_date;
    }

    public Information getInformation() {
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

    public String getROLE() {
        return ROLE;
    }

    public void setROLE(String ROLE) {
        this.ROLE = ROLE;
    }

    public ArrayList<Conversation> getConversationsSorted() {

        ArrayList<Conversation> list = new ArrayList<>(conversations);
        Collections.sort(list, (o1, o2) -> o2.getNewestMessageObject().getCr_date().compareTo(o1.getNewestMessageObject().getCr_date()));

        return list;
    }

    public int getCommentsAmount(){

        return new ArrayList<Comment>(comments).size();

    }

    public String getCr_date_formated() {

        Format formatter = new SimpleDateFormat("dd.MM.yy");
        String s = formatter.format(this.cr_date);
        return s;
    }

    public int getDiscountsAmount(){

        return new ArrayList<Discount>(discounts).size();

    }

    public boolean hasDiscount(Long discount_id){

        for (Discount d:discounts) {

            if (d.getDiscount_id()==discount_id){

                return true;
            }

        }
        return false;

    }
}

