package pl.okazje.project.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Conversation;


@Repository
public interface ConversationRepository extends CrudRepository<Conversation, Long> {

    @Query(value = "SELECT * from conversation where conversation_id in (SELECT conversation_id FROM user_conversation WHERE" +
            " user_id =?1 and conversation_id in(select conversation_id from user_conversation where user_id=?2));" ,
            nativeQuery = true)
    Conversation findByUsers(int uid1, int uid2);

}
