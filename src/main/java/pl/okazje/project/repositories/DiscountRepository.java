package pl.okazje.project.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Discount;

import java.util.List;
import java.util.Optional;


@Repository
public interface DiscountRepository extends CrudRepository<Discount, Long>, PagingAndSortingRepository<Discount, Long> {

    @Query(value = "SELECT * from discount ORDER BY discount.createDate DESC",
            nativeQuery = true)
    List<Discount> findAllByOrderByCreateDateDesc();

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count" +
            " FROM discountrating GROUP BY discountId ) AS r ON d.discountId = r.discountId ORDER BY r.disc_count DESC ",
            nativeQuery = true)
    List<Discount> findAllByOrderByRatingDesc();

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count" +
            " FROM discountrating GROUP BY discountId ) AS r ON d.discountId = r.discountId where d.createDate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY r.disc_count DESC ",
            nativeQuery = true)
    List<Discount> findAllByCreateDateBetweenNowAndYesterdayOrderByRatingDesc();

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count" +
            " FROM discountrating GROUP BY discountId ) AS r ON d.discountId = r.discountId where d.createDate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY r.disc_count DESC ",
            nativeQuery = true)
    List<Discount> findAllByCreateDateBetweenNowAndLastWeekOrderByRatingDesc();

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count" +
            " FROM discountcomment GROUP BY discountId ) AS c ON d.discountId = c.discountId ORDER BY  c.disc_count DESC ",
            nativeQuery = true)
    List<Discount> findAllByOrderByCommentDesc();

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count" +
            " FROM discountcomment GROUP BY discountId ) AS c ON d.discountId = c.discountId where d.createDate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY  c.disc_count DESC ",
            nativeQuery = true)
    List<Discount> findAllByCreateDateBetweenNowAndYesterdayOrderByCommentDesc();

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count" +
            " FROM discountcomment GROUP BY discountId ) AS c ON d.discountId = c.discountId where d.createDate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY  c.disc_count DESC ",
            nativeQuery = true)
    List<Discount> findAllByCreateDateBetweenNowAndLastWeekOrderByCommentDesc();

    @Query(value = "SELECT * from discount where discount.tagId in (select tagId from tag where tag.name=?1) ORDER BY discount.createDate DESC",
            nativeQuery = true)
    List<Discount> findAllByTagOrderByCreateDateDesc(String tag);

    @Query(value = "SELECT * from discount where discount.shopId in (select shopId from shop where shop.name=?1) ORDER BY discount.createDate DESC",
            nativeQuery = true)
    List<Discount> findAllByShopOrderByCreateDateDesc(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count FROM discountrating " +
            "GROUP BY discountId ) AS r ON d.discountId = r.discountId" +
            " where d.tagId in (select tagId from tag where tag.name=?1) ORDER BY r.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByTagOrderByRatingDesc(String tag);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count FROM discountrating " +
            "GROUP BY discountId ) AS r ON d.discountId = r.discountId" +
            " where d.tagId in (select tagId from tag where tag.name=?1) AND d.createDate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY r.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByTagAndCreateDateBetweenNowAndYesterdayOrderByRatingDesc(String tag);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count FROM discountrating " +
            "GROUP BY discountId ) AS r ON d.discountId = r.discountId" +
            " where d.tagId in (select tagId from tag where tag.name=?1) AND d.createDate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY r.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByTagAndCreateDateBetweenNowAndLastWeekOrderByRatingDesc(String tag);


    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count FROM discountcomment" +
            " GROUP BY discountId ) AS c ON d.discountId = c.discountId where d.tagId in (select tagId from tag" +
            " where tag.name=?1) ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByTagOrderByCommentDesc(String tag);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count FROM discountcomment" +
            " GROUP BY discountId ) AS c ON d.discountId = c.discountId where d.tagId in (select tagId from tag" +
            " where tag.name=?1) AND d.createDate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByTagAndCreateDateBetweenNowAndYesterdayOrderByCommentDesc(String tag);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count FROM discountcomment" +
            " GROUP BY discountId ) AS c ON d.discountId = c.discountId where d.tagId in (select tagId from tag" +
            " where tag.name=?1) AND d.createDate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByTagAndCreateDateBetweenNowAndLastWeekOrderByCommentDesc(String tag);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count FROM discountrating " +
            "GROUP BY discountId ) AS r ON d.discountId = r.discountId" +
            " where d.shopId in (select shopId from shop where shop.name=?1) ORDER BY r.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByShopOrderByRatingDesc(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count FROM discountrating " +
            "GROUP BY discountId ) AS r ON d.discountId = r.discountId" +
            " where d.shopId in (select shopId from shop where shop.name=?1) AND d.createDate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY r.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByShopAndCreateDateBetweenNowAndYesterdayOrderByRatingDesc(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count FROM discountrating " +
            "GROUP BY discountId ) AS r ON d.discountId = r.discountId" +
            " where d.shopId in (select shopId from shop where shop.name=?1) AND d.createDate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY r.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByShopAndCreateDateBetweenNowAndLastWeekOrderByRatingDesc(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count FROM discountcomment" +
            " GROUP BY discountId ) AS c ON d.discountId = c.discountId where d.shopId in (select shopId from shop" +
            " where shop.name=?1) ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByShopOrderByCommentDesc(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count FROM discountcomment" +
            " GROUP BY discountId ) AS c ON d.discountId = c.discountId where d.shopId in (select shopId from shop" +
            " where shop.name=?1) AND d.createDate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByShopAndCreateDateBetweenNowAndYesterdayOrderByCommentDesc(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count FROM discountcomment" +
            " GROUP BY discountId ) AS c ON d.discountId = c.discountId where d.shopId in (select shopId from shop" +
            " where shop.name=?1) AND d.createDate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByShopAndCreateDateBetweenNowAndLastWeekOrderByCommentDesc(String shop);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*)" +
            " AS disc_count FROM discountcomment GROUP BY discountId ) AS c ON d.discountId = c.discountId" +
            " where d.shopId in (select shopId from shop where shop.name LIKE ?1) OR d.tagId IN" +
            " (select tagId from tag where tag.name LIKE ?1) OR d.discountId IN (select discountId" +
            " from discount where discount.title LIKE ?1) OR d.discountId IN (select discountId" +
            " from discount where discount.content LIKE ?1) ORDER BY d.createDate DESC",
            nativeQuery = true)
    List<Discount> FindAllBySearchOrderByCreateDateDesc(String search);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*)" +
            " AS disc_count FROM discountcomment GROUP BY discountId ) AS c ON d.discountId = c.discountId" +
            " where d.shopId in (select shopId from shop where shop.name LIKE ?1) OR d.tagId IN" +
            " (select tagId from tag where tag.name LIKE ?1) OR d.discountId IN (select discountId" +
            " from discount where discount.title LIKE ?1) OR d.discountId IN (select discountId" +
            " from discount where discount.content LIKE ?1) ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> FindAllBySearchOrderByCommentDesc(String search);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*)" +
            " AS disc_count FROM discountcomment GROUP BY discountId ) AS c ON d.discountId = c.discountId" +
            " where d.shopId in (select shopId from shop where shop.name LIKE ?1) OR d.tagId IN" +
            " (select tagId from tag where tag.name LIKE ?1) OR d.discountId IN (select discountId" +
            " from discount where discount.title LIKE ?1) OR d.discountId IN (select discountId" +
            " from discount where discount.content LIKE ?1) AND d.createDate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> FindAllBySearchAndCreateDateBetweenNowAndYesterdayOrderByCommentDesc(String search);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*)" +
            " AS disc_count FROM discountcomment GROUP BY discountId ) AS c ON d.discountId = c.discountId" +
            " where d.shopId in (select shopId from shop where shop.name LIKE ?1) OR d.tagId IN" +
            " (select tagId from tag where tag.name LIKE ?1) OR d.discountId IN (select discountId" +
            " from discount where discount.title LIKE ?1) OR d.discountId IN (select discountId" +
            " from discount where discount.content LIKE ?1) AND d.createDate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> FindAllBySearchAndCreateDateBetweenNowAndLastWeekOrderByCommentDesc(String search);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*)" +
            " AS disc_count FROM discountrating GROUP BY discountId ) AS c ON d.discountId = c.discountId" +
            " where d.shopId in (select shopId from shop where shop.name LIKE ?1) OR d.tagId IN" +
            " (select tagId from tag where tag.name LIKE ?1) OR d.discountId IN (select discountId" +
            " from discount where discount.title LIKE ?1) OR d.discountId IN (select discountId" +
            " from discount where discount.content LIKE ?1) ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> FindAllBySearchOrderByRatingDesc(String search);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*)" +
            " AS disc_count FROM discountrating GROUP BY discountId ) AS c ON d.discountId = c.discountId" +
            " where d.shopId in (select shopId from shop where shop.name LIKE ?1) OR d.tagId IN" +
            " (select tagId from tag where tag.name LIKE ?1) OR d.discountId IN (select discountId" +
            " from discount where discount.title LIKE ?1) OR d.discountId IN (select discountId" +
            " from discount where discount.content LIKE ?1) AND d.createDate BETWEEN (NOW() - INTERVAL 1 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> FindAllBySearchAndCreateDateBetweenNowAndYesterdayOrderByRatingDesc(String search);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*)" +
            " AS disc_count FROM discountrating GROUP BY discountId ) AS c ON d.discountId = c.discountId" +
            " where d.shopId in (select shopId from shop where shop.name LIKE ?1) OR d.tagId IN" +
            " (select tagId from tag where tag.name LIKE ?1) OR d.discountId IN (select discountId" +
            " from discount where discount.title LIKE ?1) OR d.discountId IN (select discountId" +
            " from discount where discount.content LIKE ?1) AND d.createDate BETWEEN (NOW() - INTERVAL 7 DAY) AND NOW() ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> FindAllBySearchAndCreateDateBetweenNowAndLastWeekOrderByRatingDesc(String search);

    @Query(value = "Select * from discount where userId=?1 ORDER BY discount.createDate DESC",
            nativeQuery = true)
    List<Discount> findAllByUserIdOrderByCreateDateDesc(Integer id);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count FROM discountcomment" +
            " GROUP BY discountId ) AS c ON d.discountId = c.discountId where d.userId=?1 ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByUserIdOrderByCommentDesc(Integer id);

    @Query(value = "SELECT d.* FROM discount AS d LEFT JOIN ( SELECT discountId, COUNT(*) AS disc_count FROM discountrating" +
            " GROUP BY discountId ) AS c ON d.discountId = c.discountId where d.userId=?1 ORDER BY c.disc_count DESC",
            nativeQuery = true)
    List<Discount> findAllByUserIdOrderByRatingDesc(Integer id);

    List<Discount> findAllByStatusEquals(Discount.Status status);

    @Query(value = "SELECT * FROM discount where discountId in (Select discountId from discountrating where userId in(Select userId from user where login = ?1))",
            nativeQuery = true)
    List<Discount> findAllByUserAndLiked(String user);

    Optional<Discount> findFirstByTitleEquals(String title);

}