package pl.okazje.project.entities;

import org.apache.commons.math3.util.Precision;
import pl.okazje.project.exceptions.DataTooLongException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

/*
   promocja =cena pierwotna -> cena obecna
   kupon = przecena w gazetce/sklepie lokanlym
   kod = przecena online
*/
@Entity
public class Discount {

    public enum Type{KUPONNORMALNY, KUPONPROCENT, KODPROCENT, KODNORMALNY, OBNIZKA}
    public enum Status{DELETED, AWAITING, ACCEPTED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountId;
    @Column(length = 200)
    private String title;
    @Column(length = 1500)
    private String content;
    private String imageUrl;
    private Date createDate;
    private Date expireDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Double oldPrice;
    private Double currentPrice;
    private Double shipmentPrice;
    @Column(length = 500)
    private String discountLink;
    @Enumerated(EnumType.STRING)
    private Type type = Type.OBNIZKA;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToMany(mappedBy = "discount")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "discount")
    private Set<Rating> ratings;

    @ManyToOne
    @JoinColumn(name = "tagId", nullable = false)
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "shopId", nullable = false)
    private Shop shop;

    public Discount() {
    }

    public Long getDiscountId() {
        return discountId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title.length()>200)throw new DataTooLongException(title);
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if(content.length()>1500)throw new DataTooLongException(content);
        this.content = content;
    }

    public String getImageUrl() {
        if(this.imageUrl.contains("http")){
            return this.imageUrl;
        }
        return "/" +this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        if(imageUrl.length()>500)throw new DataTooLongException(imageUrl);
        this.imageUrl = imageUrl;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getExpireDateFormatted() {
        Format formatter = new SimpleDateFormat("dd.MM.yy");
        String s = formatter.format(this.expireDate);

        return s;
    }

    public boolean isDeleted() {
        if (this.status == null) {
            return false;
        } else if (this.status.equals(Status.DELETED)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAwaiting() {
        if (this.status == null) {
            return false;
        } else if (this.status.equals(Status.AWAITING)) {
            return true;
        } else {
            return false;
        }
    }

    public String getCreateDateFormatted() {
        Format formatter = new SimpleDateFormat("dd.MM.yy");
        String s = formatter.format(this.createDate);

        return s;
    }

    public int getDifference() {
        return (int) (100 - (currentPrice / oldPrice * 100));
    }

    public String getDiscountLink() {
        return discountLink;
    }

    public void setDiscountLink(String discountLink) {
        if(discountLink.length()>500) throw new DataTooLongException(discountLink);
        this.discountLink = discountLink;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getStatus() {
        return status.name();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getOldPrice() {
        return Precision.round(oldPrice,2);
    }

    public void setOldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Double getCurrentPrice() {
        return Precision.round(currentPrice,2);
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Double getShipmentPrice() {
        return Precision.round(shipmentPrice,2);
    }

    public void setShipmentPrice(Double shipmentPrice) {
        this.shipmentPrice = shipmentPrice;
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
        Collections.sort(list, (o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
        return  list;
    }

    public int getRatingsSize() {
        if (ratings == null) {
            return 0;
        } else {
            return ratings.size();
        }
    }

    public boolean isOutDated() {
        Date currentDate = new Date();
        if (currentDate.compareTo(expireDate) > 0) {
            return true;
        }
        return false;
    }

    public long getDataToNumber() {
        long daysBetween = new Date().getTime() - this.createDate.getTime();
        return daysBetween;
    }

    public String getTypeFormatted(){
        if(this.type.equals(Type.KODNORMALNY)||this.type.equals(Type.KODPROCENT)) return "KOD";
        if(this.type.equals(Type.KUPONNORMALNY)||this.type.equals(Type.KUPONPROCENT)) return "KUPON";
        return this.type.name();
    }
}
