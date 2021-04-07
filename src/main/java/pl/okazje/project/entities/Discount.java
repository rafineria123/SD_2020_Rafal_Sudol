package pl.okazje.project.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

//promocja=cena pierwotna -> cena obecna
//kupon = przecena w gazetce/sklepie lokanlym
//kod = przecena online
@Entity
public class Discount {

    public enum Type{ KUPONNORMALNY,KUPONPROCENT,KODPROCENT, KODNORMALNY, OBNIZKA }
    public enum Status{ USUNIETE, OCZEKUJACE, ZATWIERDZONE }

    private static Date data = new Date();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discount_id;
    @Column(length = 200)
    private String title;
    @Column(length = 1500)
    private String content;
    @Column(length = 500)
    private String image_url;
    private Date creationdate;
    private Date expire_date;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Double old_price;
    private Double current_price;
    private Double shipment_price;
    @Column(length = 500)
    private String discount_link;
    @Enumerated(EnumType.STRING)
    private Type type = Type.OBNIZKA;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "discount")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "discount")
    private Set<Rating> ratings;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    public Discount(String title, String content, String image_url, Double old_price, Double current_price, Double shipment_price, String discount_link, User user, Tag tag, Shop shop) {

        this.title = title;
        this.content = content;
        this.image_url = image_url;
        this.creationdate = new Date();
        this.expire_date = new Date();
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

        if(this.image_url.contains("http")){
            return this.image_url;
        }
        return "/" +this.image_url;

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



    public boolean deleted() {

        if (this.status == null) {

            return false;

        } else if (this.status.equals(Status.USUNIETE)) {

            return true;

        } else {

            return false;

        }


    }

    public boolean deletedOrNotReady() {

        if (this.status == null) {

            return false;

        } else if (this.status.equals(Status.USUNIETE) || this.status.equals(Status.OCZEKUJACE)) {

            return true;

        } else {

            return false;

        }


    }

    public String getCreation_date_formated() {

        Format formatter = new SimpleDateFormat("dd.MM.yy");
        String s = formatter.format(this.creationdate);
        return s;
    }

    public int getDifference() {

        return (int) (100 - (current_price / old_price * 100));
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
        return status.name();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getOld_price() {
        return round(old_price,2);
    }

    public void setOld_price(Double old_price) {
        this.old_price = old_price;
    }

    public Double getCurrent_price() {
        return round(current_price,2);
    }

    public void setCurrent_price(Double current_price) {
        this.current_price = current_price;
    }

    public Double getShipment_price() {
        return round(shipment_price,2);
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

    public String getType() {
        return type.name();
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getCommentsSize() {

        if (comments == null) {
            return 0;
        } else {
            return comments.size();
        }

    }

    public ArrayList<Comment> getCommentsSorted(){

        ArrayList<Comment> list = new ArrayList(getComments());
        Collections.sort(list, (o1, o2) -> o2.getCr_date().compareTo(o1.getCr_date()));
        return  list;

    }

    public int getRatingsSize() {
        if (ratings == null) {
            return 0;
        } else {
            return ratings.size();
        }
    }

    public long getDataToNumber() {

        long daysBetween = data.getTime() - this.creationdate.getTime();
        return daysBetween;

    }

    public boolean isOutDated() {
        Date currentdate = new Date();
        if (currentdate.compareTo(expire_date) > 0) {
            return true;
        }
        return false;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public boolean isGlobalLink(){


        if(this.image_url.contains("http")){
            return true;
        }
        return false;

    }

    public String getTypeFormatted(){

        if(this.type.equals(Type.KODNORMALNY)||this.type.equals(Type.KODPROCENT)) return "KOD";
        if(this.type.equals(Type.KUPONNORMALNY)||this.type.equals(Type.KUPONPROCENT)) return "KUPON";
        return this.type.name();

    }
}
