package com.backend.demo.repository.user;

import com.backend.demo.entity.user.ChillowMessage;
import com.backend.demo.model.ChatThreadMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChillowMessageRepository extends JpaRepository<ChillowMessage, String> {

    @Query(value = "select M.id as id,M.sender_id as senderId,M.message_html as messageHtml,M.timestamp as timeStamp," +
            "M.is_read_by_user_1 as isReadByUser1,  M.is_read_by_user_2 as isReadByUser2," +
            " U.name as name, (select file from chillow_user_image where sequence = 0 and user_id = M.sender_id) as " +
            "image from chillow_message M inner join chillow_user U " +
            "on M.sender_id = U.id where M.user_chat = :userChat", nativeQuery = true)
    List<ChatThreadMessage> findChatThreadMessages(String userChat);

    @Modifying
    @Query(value = "update ChillowMessage c  set c.isReadByUser1 = true where c.userChat = :userChat")
    int updateReadByFirstUserByUserChat(String userChat);

    @Modifying
    @Query(value = "update ChillowMessage c set c.isReadByUser2 = true where c.userChat = :userChat")
    int updateReadBySecondUserByUserChat(String userChat);

    int countByUserChatAndIsReadByUser1False(String userChat);

    int countByUserChatAndIsReadByUser2False(String userChat);
}
