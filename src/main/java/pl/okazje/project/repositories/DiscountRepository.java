package pl.okazje.project.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.Information;
import pl.okazje.project.entities.User;

import java.util.List;


@Repository
public interface DiscountRepository extends CrudRepository<Discount, Long>, PagingAndSortingRepository<Discount, Long> {

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count" +
            " FROM rating GROUP BY discount_id ) AS r ON d.discount_id = r.discount_id ORDER BY r.disc_count DESC " ,
            nativeQuery = true)
    public List<Discount> sortDiscountByRating();
    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count" +
            " FROM comment GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id ORDER BY c.disc_count DESC " ,
            nativeQuery = true)
    public List<Discount> sortDiscountByComments();

    @Query(value = "SELECT * from discount where discount.tag_id in (select tag_id from tag where tag.name=?1)" ,
            nativeQuery = true)
    public List<Discount> discountByTag(String tag);

    @Query(value = "SELECT * from discount where discount.shop_id in (select shop_id from shop where shop.name=?1)" ,
            nativeQuery = true)
    public List<Discount> discountByShop(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM rating " +
            "GROUP BY discount_id ) AS r ON d.discount_id = r.discount_id" +
            " where d.tag_id in (select tag_id from tag where tag.name=?1) ORDER BY r.disc_count DESC" ,
            nativeQuery = true)
    public List<Discount> sortDiscountByRatingWithGivenTag(String tag);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM comment" +
            " GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.tag_id in (select tag_id from tag" +
            " where tag.name=?1) ORDER BY c.disc_count DESC" ,
            nativeQuery = true)
    public List<Discount> sortDiscountByCommentsWithGivenTag(String tag);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM rating " +
            "GROUP BY discount_id ) AS r ON d.discount_id = r.discount_id" +
            " where d.shop_id in (select shop_id from shop where shop.name=?1) ORDER BY r.disc_count DESC" ,
            nativeQuery = true)
    public List<Discount> sortDiscountByRatingWithGivenShop(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM comment" +
            " GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.shop_id in (select shop_id from shop" +
            " where shop.name=?1) ORDER BY c.disc_count DESC" ,
            nativeQuery = true)
    public List<Discount> sortDiscountByCommentsWithGivenShop(String shop);

    @Query(value = "SELECT * from discount where discount.tag_id in (select tag_id from tag where tag.name=?1)" +
            " ORDER BY discount.creationdate DESC" ,
            nativeQuery = true)
    public List<Discount> sortDiscountByDateWithGivenTag(String tag);

    @Query(value = "SELECT * from discount where discount.shop_id in (select shop_id from shop where shop.name=?1)" +
            " ORDER BY discount.creationdate DESC" ,
            nativeQuery = true)
    public List<Discount> sortDiscountByDateWithGivenShop(String shop);


    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*)" +
            " AS disc_count FROM comment GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id" +
            " where d.shop_id in (select shop_id from shop where shop.name LIKE ?1) OR d.tag_id IN" +
            " (select tag_id from tag where tag.name LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.title LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.content LIKE ?1) ORDER BY d.creationdate DESC" ,
            nativeQuery = true)
    public List<Discount> discountBySearchInput(String search);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*)" +
            " AS disc_count FROM comment GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id" +
            " where d.shop_id in (select shop_id from shop where shop.name LIKE ?1) OR d.tag_id IN" +
            " (select tag_id from tag where tag.name LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.title LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.content LIKE ?1) ORDER BY c.disc_count DESC" ,
            nativeQuery = true)
    public List<Discount> sortDiscountByCommentsWithGivenSearchInput(String search);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*)" +
            " AS disc_count FROM rating GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id" +
            " where d.shop_id in (select shop_id from shop where shop.name LIKE ?1) OR d.tag_id IN" +
            " (select tag_id from tag where tag.name LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.title LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.content LIKE ?1) ORDER BY c.disc_count DESC" ,
            nativeQuery = true)
    public List<Discount> sortDiscountByRatingWithGivenSearchInput(String search);

    @Query(value = "Select * from discount where user_id=?1" ,
            nativeQuery = true)
    public List<Discount> discountByUserId(Integer id);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM comment" +
            " GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.user_id=?1 ORDER BY d.creation_date DESC" ,
            nativeQuery = true)
    public List<Discount> sortDiscountByDateWithGivenUserId(Integer id);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM comment" +
            " GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.user_id=?1 ORDER BY c.disc_count DESC" ,
            nativeQuery = true)
    public List<Discount> sortDiscountByCommentsWithGivenUserId(Integer id);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM rating" +
            " GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.user_id=?1 ORDER BY c.disc_count DESC" ,
            nativeQuery = true)
    public List<Discount> sortDiscountByRatingWithGivenUserId(Integer id);




}