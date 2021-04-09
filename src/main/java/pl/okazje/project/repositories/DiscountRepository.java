package pl.okazje.project.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.*;

import java.util.List;
import java.util.Optional;


@Repository
public interface DiscountRepository extends CrudRepository<Discount, Long>, PagingAndSortingRepository<Discount, Long> {

    @Query(value = "SELECT * from discount ORDER BY discount.creationdate DESC",
            nativeQuery = true)
    List<Discount> findAllByOrderByCreationdateDesc();

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count" +
            " FROM rating GROUP BY discount_id ) AS r ON d.discount_id = r.discount_id ORDER BY r.disc_count DESC ",
            nativeQuery = true)
    List<Discount> findAllByOrderByRatingDesc();

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count" +
            " FROM rating GROUP BY discount_id ) AS r ON d.discount_id = r.discount_id where d.creationdate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY r.disc_count DESC ",
            nativeQuery = true)
    List<Discount> findAllByCreationdateBetweenNowAndYesterdayOrderByRatingDesc();

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count" +
            " FROM rating GROUP BY discount_id ) AS r ON d.discount_id = r.discount_id where d.creationdate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY r.disc_count DESC ",
            nativeQuery = true)
    List<Discount> findAllByCreationdateBetweenNowAndLastWeekOrderByRatingDesc();

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count" +
            " FROM comment GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id ORDER BY  c.disc_count DESC ",
            nativeQuery = true)
    List<Discount> findAllByOrderByCommentDesc();

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count" +
            " FROM comment GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.creationdate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY  c.disc_count DESC ",
            nativeQuery = true)
    List<Discount> findAllByCreationdateBetweenNowAndYesterdayOrderByCommentDesc();

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count" +
            " FROM comment GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.creationdate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY  c.disc_count DESC ",
            nativeQuery = true)
    List<Discount> findAllByCreationdateBetweenNowAndLastWeekOrderByCommentDesc();

    @Query(value = "SELECT * from discount where discount.tag_id in (select tag_id from tag where tag.name=?1) ORDER BY discount.creationdate DESC",
            nativeQuery = true)
    List<Discount> findAllByTagOrderByCreationdateDesc(String tag);

    @Query(value = "SELECT * from discount where discount.shop_id in (select shop_id from shop where shop.name=?1) ORDER BY discount.creationdate DESC",
            nativeQuery = true)
    List<Discount> findAllByShopOrderByCreationdateDesc(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM rating " +
            "GROUP BY discount_id ) AS r ON d.discount_id = r.discount_id" +
            " where d.tag_id in (select tag_id from tag where tag.name=?1) ORDER BY r.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByTagOrderByRatingDesc(String tag);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM rating " +
            "GROUP BY discount_id ) AS r ON d.discount_id = r.discount_id" +
            " where d.tag_id in (select tag_id from tag where tag.name=?1) AND d.creationdate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY r.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(String tag);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM rating " +
            "GROUP BY discount_id ) AS r ON d.discount_id = r.discount_id" +
            " where d.tag_id in (select tag_id from tag where tag.name=?1) AND d.creationdate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY r.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(String tag);


    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM comment" +
            " GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.tag_id in (select tag_id from tag" +
            " where tag.name=?1) ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByTagOrderByCommentDesc(String tag);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM comment" +
            " GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.tag_id in (select tag_id from tag" +
            " where tag.name=?1) AND d.creationdate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(String tag);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM comment" +
            " GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.tag_id in (select tag_id from tag" +
            " where tag.name=?1) AND d.creationdate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(String tag);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM rating " +
            "GROUP BY discount_id ) AS r ON d.discount_id = r.discount_id" +
            " where d.shop_id in (select shop_id from shop where shop.name=?1) ORDER BY r.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByShopOrderByRatingDesc(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM rating " +
            "GROUP BY discount_id ) AS r ON d.discount_id = r.discount_id" +
            " where d.shop_id in (select shop_id from shop where shop.name=?1) AND d.creationdate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY r.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM rating " +
            "GROUP BY discount_id ) AS r ON d.discount_id = r.discount_id" +
            " where d.shop_id in (select shop_id from shop where shop.name=?1) AND d.creationdate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY r.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM comment" +
            " GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.shop_id in (select shop_id from shop" +
            " where shop.name=?1) ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByShopOrderByCommentDesc(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM comment" +
            " GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.shop_id in (select shop_id from shop" +
            " where shop.name=?1) AND d.creationdate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM comment" +
            " GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.shop_id in (select shop_id from shop" +
            " where shop.name=?1) AND d.creationdate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*)" +
            " AS disc_count FROM comment GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id" +
            " where d.shop_id in (select shop_id from shop where shop.name LIKE ?1) OR d.tag_id IN" +
            " (select tag_id from tag where tag.name LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.title LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.content LIKE ?1) ORDER BY d.creationdate DESC",
            nativeQuery = true)
    List<Discount> FindAllBySearchOrderByCreationdateDesc(String search);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*)" +
            " AS disc_count FROM comment GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id" +
            " where d.shop_id in (select shop_id from shop where shop.name LIKE ?1) OR d.tag_id IN" +
            " (select tag_id from tag where tag.name LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.title LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.content LIKE ?1) ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> FindAllBySearchOrderByCommentDesc(String search);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*)" +
            " AS disc_count FROM comment GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id" +
            " where d.shop_id in (select shop_id from shop where shop.name LIKE ?1) OR d.tag_id IN" +
            " (select tag_id from tag where tag.name LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.title LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.content LIKE ?1) AND d.creationdate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(String search);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*)" +
            " AS disc_count FROM comment GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id" +
            " where d.shop_id in (select shop_id from shop where shop.name LIKE ?1) OR d.tag_id IN" +
            " (select tag_id from tag where tag.name LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.title LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.content LIKE ?1) AND d.creationdate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(String search);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*)" +
            " AS disc_count FROM rating GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id" +
            " where d.shop_id in (select shop_id from shop where shop.name LIKE ?1) OR d.tag_id IN" +
            " (select tag_id from tag where tag.name LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.title LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.content LIKE ?1) ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> FindAllBySearchOrderByRatingDesc(String search);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*)" +
            " AS disc_count FROM rating GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id" +
            " where d.shop_id in (select shop_id from shop where shop.name LIKE ?1) OR d.tag_id IN" +
            " (select tag_id from tag where tag.name LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.title LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.content LIKE ?1) AND d.creationdate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(String search);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*)" +
            " AS disc_count FROM rating GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id" +
            " where d.shop_id in (select shop_id from shop where shop.name LIKE ?1) OR d.tag_id IN" +
            " (select tag_id from tag where tag.name LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.title LIKE ?1) OR d.discount_id IN (select discount_id" +
            " from discount where discount.content LIKE ?1) AND d.creationdate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(String search);

    @Query(value = "Select * from discount where user_id=?1 ORDER BY discount.creationdate DESC",
            nativeQuery = true)
    List<Discount> findAllByUseridOrderByCreationdateDesc(Integer id);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM comment" +
            " GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.user_id=?1 ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByUseridOrderByCommentDesc(Integer id);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discount_id, COUNT(*) AS disc_count FROM rating" +
            " GROUP BY discount_id ) AS c ON d.discount_id = c.discount_id where d.user_id=?1 ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByUseridOrderByRatingDesc(Integer id);

    List<Discount> findAllByStatusEquals(Discount.Status status);

    @Query(value = "SELECT * FROM discount where discount_id in (Select discount_id from rating where user_id in(Select user_id from user where login = ?1))",
            nativeQuery = true)
    List<Discount> findAllByUserAndLiked(String user);

    Optional<Discount> findFirstByTitleEquals(String title);

}