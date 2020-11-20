package pl.okazje.project.entities;

import javax.persistence.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Entity
public class Discount {

    private static Date data = new Date();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discount_id;
    private String title;
    private String content;
    private String image_url;
    private Date creationdate;
    private Date expire_date;
    private String status;
    private Double old_price;
    private Double current_price;
    private Double shipment_price;
    private String discount_link;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @OneToMany(mappedBy="discount")
    private Set<Comment> comments;

    @OneToMany(mappedBy="discount")
    private Set<Rating> ratings;

    @ManyToOne
    @JoinColumn(name="tag_id", nullable=false)
    private Tag tag;

    @ManyToOne
    @JoinColumn(name="shop_id", nullable=false)
    private Shop shop;

    public Discount(String title, String content, String image_url, Double old_price, Double current_price, Double shipment_price, String discount_link, User user, Tag tag, Shop shop) {

        this.title = title;
        this.content = content;
        this.image_url = image_url;
        this.creationdate = new Date();
        this.expire_date = new Date();
        this.status = "active";
        this.old_price = old_price;
        this.current_price = current_price;
        this.shipment_price = shipment_price;
        this.discount_link = discount_link;
        this.user = user;
        this.tag = tag;
        this.shop = shop;


    }

    public Discount() {
    }



    public Long getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(Long discount_id) {
        this.discount_id = discount_id;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date cr_date) {
        this.creationdate = cr_date;
    }

    public Date getExpire_date() {
        return expire_date;
    }

    public String getExpire_date_formated() {

        Format formatter = new SimpleDateFormat("dd.MM.yy");
        String s = formatter.format(this.expire_date);
        return s;
    }

    public boolean deleted(){

        if (this.status==null){

            return false;

        }else if(this.status.equals("Usuniete")){

            return true;

        }else{

            return false;

        }



    }

    public boolean deletedOrNotReady(){

        if (this.status==null){

            return false;

        }else if(this.status.equals("Usuniete")||this.status.equals("Niezatwierdzone")){

            return true;

        }else{

            return false;

        }



    }

    public String getCreation_date_formated() {

        Format formatter = new SimpleDateFormat("dd.MM.yy");
        String s = formatter.format(this.creationdate);
        return s;
    }

    public int getDifference(){

        return (int)(100-(current_price/old_price * 100));
    }

    public String getDiscount_link() {
        return discount_link;
    }

    public void setDiscount_link(String discount_link) {
        this.discount_link = discount_link;
    }

    public void setExpire_date(Date expire_date) {
        this.expire_date = expire_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getOld_price() {
        return old_price;
    }

    public void setOld_price(Double old_price) {
        this.old_price = old_price;
    }

    public Double getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(Double current_price) {
        this.current_price = current_price;
    }

    public Double getShipment_price() {
        return shipment_price;
    }

    public void setShipment_price(Double shipment_price) {
        this.shipment_price = shipment_price;
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

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
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

    public int getCommentsSize(){

        if(comments == null){
            return 0;
        }else {
            return comments.size();
        }

    }

    public int getRatingsSize(){
        if(ratings == null){
            return 0;
        }else {
            return ratings.size();
        }
    }

    public long getDataToNumber(){

            long daysBetween = data.getTime() - this.creationdate.getTime();
            return daysBetween;

    }
}
