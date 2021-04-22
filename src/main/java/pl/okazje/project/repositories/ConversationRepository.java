package pl.okazje.project.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Conversation;

import java.util.Optional;


@Repository
public interface ConversationRepository extends CrudRepository<Conversation, Long> {

    @Query(value = "SELECT * from conversation where conversationId in (SELECT conversationId FROM user_conversation WHERE" +
            " userId =?1 and conversationId in(select conversationId from user_conversation where userId=?2));" ,
            nativeQuery = true)
    Optional<Conversation> findByUsers(int uid1, int uid2);

}
