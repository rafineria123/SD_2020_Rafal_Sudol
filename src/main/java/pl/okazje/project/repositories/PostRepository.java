package pl.okazje.project.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Post;

import java.util.List;


@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    @Query(value = "SELECT * from post" +
            " ORDER BY createDate DESC" ,
            nativeQuery = true)
    List<Post> findAllByOrderByCreateDateDesc();

    @Query(value = "SELECT * from post where tagId in (select tagId from tag where tag.name=?1)" +
            " ORDER BY createDate DESC" ,
            nativeQuery = true)
    List<Post> findAllByTagOrderByCreateDateDesc(String tag);

    @Query(value = "SELECT * from post where shopId in (select shopId from shop where shop.name=?1)" +
            " ORDER BY createDate DESC" ,
            nativeQuery = true)
    List<Post> findAllByShopOrderByCreateDateDesc(String shop);

    @Query(value = "SELECT d.* FROM post AS d LEFT JOIN ( SELECT postId, COUNT(*) AS disc_count" +
            " FROM comment GROUP BY postId ) AS c ON d.postId = c.postId ORDER BY  c.disc_count DESC " ,
            nativeQuery = true)
    List<Post> findAllByOrderByCommentDesc();

    @Query(value = "SELECT d.* FROM post AS d LEFT JOIN ( SELECT postId, COUNT(*) AS disc_count" +
            " FROM comment GROUP BY postId ) AS c ON d.postId = c.postId where d.tagId in (select tagId from tag where tag.name=?1) ORDER BY  c.disc_count DESC " ,
            nativeQuery = true)
    List<Post> findAllByTagOrderByCommentDesc(String tag);

    @Query(value = "SELECT d.* FROM post AS d LEFT JOIN ( SELECT postId, COUNT(*) AS disc_count" +
            " FROM comment GROUP BY postId ) AS c ON d.postId = c.postId where d.shopId in (select shopId from shop where shop.name=?1) ORDER BY  c.disc_count DESC " ,
            nativeQuery = true)
    List<Post> findAllByShopOrderByCommentDesc(String shop);

    @Query(value = "SELECT * from post"+
            " where content like ?1 or title like ?1 or userId in (SELECT userId from user where login like ?1) ORDER BY createDate DESC" ,
            nativeQuery = true)
    List<Post> findAllBySearchOrderByCreateDateDesc(String search);

    @Query(value = "SELECT d.* FROM post AS d LEFT JOIN ( SELECT postId, COUNT(*)" +
            " AS disc_count FROM comment GROUP BY postId ) AS c ON d.postId = c.postId" +
            " where d.content like ?1 or d.title like ?1 or d.userId in (SELECT userId from user where login like ?1)" +
            " ORDER BY c.disc_count DESC" ,
            nativeQuery = true)
    List<Post> findAllBySearchOrderByCommentDesc(String search);

    @Query(value = "SELECT * from post where userId in " +
            "(select userId from user where login = ?1) ORDER BY createDate DESC" ,
            nativeQuery = true)
    List<Post> findAllByUserOrderByCreateDateDesc(String user);

    @Query(value = "SELECT d.* FROM post AS d LEFT JOIN ( SELECT postId, COUNT(*) AS disc_count" +
            " FROM comment GROUP BY postId ) AS c ON d.postId = c.postId where d.userId in (select userId from user where login = ?1) ORDER BY  c.disc_count DESC " ,
            nativeQuery = true)
    List<Post> findAllByUserOrderByCommentDesc(String user);

    Post findFirstByTitleOrderByCreateDateDesc(String title);

}