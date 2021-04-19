package pl.okazje.project.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Post;

import java.util.List;


@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    @Query(value = "SELECT * from post" +
            " ORDER BY creationdate DESC" ,
            nativeQuery = true)
    List<Post> findAllByOrderByCreationdateDesc();

    @Query(value = "SELECT * from post where tag_id in (select tag_id from tag where tag.name=?1)" +
            " ORDER BY creationdate DESC" ,
            nativeQuery = true)
    List<Post> findAllByTagOrderByCreationdateDesc(String tag);

    @Query(value = "SELECT * from post where shop_id in (select shop_id from shop where shop.name=?1)" +
            " ORDER BY creationdate DESC" ,
            nativeQuery = true)
    List<Post> findAllByShopOrderByCreationdateDesc(String shop);

    @Query(value = "SELECT d.* FROM post AS d LEFT JOIN ( SELECT post_id, COUNT(*) AS disc_count" +
            " FROM comment GROUP BY post_id ) AS c ON d.post_id = c.post_id ORDER BY  c.disc_count DESC " ,
            nativeQuery = true)
    List<Post> findAllByOrderByCommentDesc();

    @Query(value = "SELECT d.* FROM post AS d LEFT JOIN ( SELECT post_id, COUNT(*) AS disc_count" +
            " FROM comment GROUP BY post_id ) AS c ON d.post_id = c.post_id where d.tag_id in (select tag_id from tag where tag.name=?1) ORDER BY  c.disc_count DESC " ,
            nativeQuery = true)
    List<Post> findAllByTagOrderByCommentDesc(String tag);

    @Query(value = "SELECT d.* FROM post AS d LEFT JOIN ( SELECT post_id, COUNT(*) AS disc_count" +
            " FROM comment GROUP BY post_id ) AS c ON d.post_id = c.post_id where d.shop_id in (select shop_id from shop where shop.name=?1) ORDER BY  c.disc_count DESC " ,
            nativeQuery = true)
    List<Post> findAllByShopOrderByCommentDesc(String shop);

    @Query(value = "SELECT * from post"+
            " where content like ?1 or title like ?1 or user_id in (SELECT user_id from user where login like ?1) ORDER BY creationdate DESC" ,
            nativeQuery = true)
    List<Post> findAllBySearchOrderByCreationdateDesc(String search);

    @Query(value = "SELECT d.* FROM post AS d LEFT JOIN ( SELECT post_id, COUNT(*)" +
            " AS disc_count FROM comment GROUP BY post_id ) AS c ON d.post_id = c.post_id" +
            " where d.content like ?1 or d.title like ?1 or d.user_id in (SELECT user_id from user where login like ?1)" +
            " ORDER BY c.disc_count DESC" ,
            nativeQuery = true)
    List<Post> findAllBySearchOrderByCommentDesc(String search);

    @Query(value = "SELECT * from post where user_id in " +
            "(select user_id from user where login = ?1) ORDER BY creationdate DESC" ,
            nativeQuery = true)
    List<Post> findAllByUserOrderByCreationdateDesc(String user);

    @Query(value = "SELECT d.* FROM post AS d LEFT JOIN ( SELECT post_id, COUNT(*) AS disc_count" +
            " FROM comment GROUP BY post_id ) AS c ON d.post_id = c.post_id where d.user_id in (select user_id from user where login = ?1) ORDER BY  c.disc_count DESC " ,
            nativeQuery = true)
    List<Post> findAllByUserOrderByCommentDesc(String user);

    Post findFirstByTitleOrderByCreationdateDesc(String title);

}