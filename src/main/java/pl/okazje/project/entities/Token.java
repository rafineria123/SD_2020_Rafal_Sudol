package pl.okazje.project.entities;


import pl.okazje.project.exceptions.DataTooLongException;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;



@Entity
public class Token implements Serializable {
    private static final int EXPIRATION = 60 * 24;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long token_id;

    @Column(length = 700)
    private String token;

    @OneToOne(mappedBy = "token")
    private User user;

    private Date expiryDate = calculateExpiryDate(EXPIRATION);

    private static Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public Token() {
    }

    public static int getEXPIRATION() {
        return EXPIRATION;
    }

    public Long getToken_id() {
        return token_id;
    }

    public void setToken_id(Long token_id) {
        this.token_id = token_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        if(token.length()>700) throw new DataTooLongException(token);
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

}