package com.backend.demo.repository.swipe;

import com.backend.demo.dto.swipe.MatchData;
import com.backend.demo.entity.swipe.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, String> {
    @Query(value = "select u.name as name,u.email as email ,m.user_id as userId, m.matched_user_id as matchedUserId, " +
            "            m.id as id, cui.file as profileImage ,cui.deleted ,m.user_chat as userChat , m.room_owner as roomOwner," +
            "            m.user_id_messages as unreadMessages, m.updated_at as updatedAt" +
            "             from match m left outer join chillow_user_image cui on " +
            "            cui.user_id = m.matched_user_id left outer join chillow_user u on" +
            "            u.id = m.matched_user_id " +
            "            where (m.user_id=:userId ) " +
            "            and m.deleted = false  " +
            "             and cui.sequence = 0 and cui.deleted =false order by m.updated_at asc;", nativeQuery = true)
    List<MatchData> findMatchesOfUserId(@Param("userId") String userId);

    @Query(value = "select u.name as name,u.email as email ,m.user_id as matchedUserId, m.matched_user_id as userId,  " +
            "            m.id as id, cui.file as profileImage ,cui.deleted ,m.user_chat as userChat , m.room_owner as roomOwner,  " +
            "           m.matched_user_id_messages as unreadMessages, m.updated_at as updatedAt  " +
            "             from match m left join chillow_user_image cui on   " +
            "            cui.user_id = m.user_id left join chillow_user u on  " +
            "            u.id = m.user_id  " +
            "            where (m.matched_user_id= :userId )  " +
            "            and m.deleted = false   " +
            "             and cui.sequence = 0 and cui.deleted =false order by m.updated_at asc;", nativeQuery = true)
    List<MatchData> findMatchesOfMatchedUserId(@Param("userId") String userId);

    List<Match> findByUserChatAndIsDeletedFalse(String userChat);

    List<Match> findAllByUserChatInAndIsDeletedFalse(List<String> chats);

    List<Match> findByUserIdAndIsDeletedFalse(String userId);

    boolean existsByUserIdAndMatchedUserIdAndIsDeletedFalse(String userId, String matchUserId);

    Optional<Match> findByUserIdAndMatchedUserIdAndIsDeletedFalse(String userId, String matcherUserId);

    List<Match> findByUserIdOrderByCreatedAtDesc(String userId);

    List<Match> findByUserChatContainingOrderByCreatedAtDesc(String userId);
}
