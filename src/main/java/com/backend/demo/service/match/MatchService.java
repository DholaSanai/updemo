package com.backend.demo.service.match;

import com.backend.demo.dto.swipe.MatchData;
import com.backend.demo.dto.swipe.MatchDto;
import com.backend.demo.entity.swipe.Match;
import com.backend.demo.exceptions.NoMatchExists;
import com.backend.demo.repository.swipe.MatchRepository;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    public String getUserChat(String userId, String shownUserId) {
        if (userId.compareTo(shownUserId) < 0) {
            return userId + "_" + shownUserId + "_chat";
        }
        return shownUserId + "_" + userId + "_chat";
    }

    public String getUserChatReverse(String userId, String shownUserId) {
        return shownUserId + "_" + userId + "_chat";
    }

    public List<MatchData> getMatch(String userId) {
        List<MatchData> userMatches = matchRepository.findMatchesOfUserId(userId);
        userMatches.addAll(matchRepository.findMatchesOfMatchedUserId(userId));
        return userMatches;
    }

    public List<MatchDto> getSingleMatch(String userChat) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return matchRepository.findByUserChatAndIsDeletedFalse(userChat).stream()
                .map(match -> mapper.map(match, MatchDto.class))
                .collect(Collectors.toList());
    }

    public List<MatchDto> getSingleMatch(List<String> chats) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return matchRepository.findAllByUserChatInAndIsDeletedFalse(chats).stream()
                .map(match -> mapper.map(match, MatchDto.class))
                .collect(Collectors.toList());
    }

    public Match saveNewMatch(String userId, String matchUserId) {
        if (matchRepository.existsByUserIdAndMatchedUserIdAndIsDeletedFalse(userId, matchUserId)) {
            return null;
        }
        Match match = new Match(UUID.randomUUID().toString(), userId, matchUserId, 0, 0,
                getUserChat(userId, matchUserId), "", false, LocalDateTime.now(), LocalDateTime.now());
        return matchRepository.save(match);
    }

    public Boolean deleteUserMatches(String id) {
        if (StringUtils.isEmpty(id)) {
            return false;
        }
        List<Match> allMatches = matchRepository.findByUserIdAndIsDeletedFalse(id);
        allMatches.forEach(eachMatch -> eachMatch.setIsDeleted(true));
        matchRepository.saveAll(allMatches);
        return true;
    }

    public Boolean updateMatchDate(String userChat) {
        if (StringUtils.isEmpty(userChat)) {
            return false;
        }
        List<Match> matches = matchRepository.findByUserChatAndIsDeletedFalse(userChat);
        matches.forEach(m -> {
            m.setUpdatedAt(LocalDateTime.now());
        });

        matchRepository.saveAll(matches);
        return true;

    }

    public Boolean updateMatchMessages(String userChat, String userId, Integer count) {
        if (StringUtils.isEmpty(userChat)) {
            return false;
        }
        List<Match> matches = matchRepository.findByUserChatAndIsDeletedFalse(userChat);
        if (CollectionUtils.isEmpty(matches)) {
            throw new NoMatchExists("No such match exists for id : " + userChat);
        }
        matches.forEach(m -> {
            m.setUpdatedAt(LocalDateTime.now());
            if (m.getMatchedUserId().equals(userId)) {
                m.setMatchedUserIdMessages(count);
            }
            if (m.getUserId().equals(userId)) {
                m.setUserIdMessages(count);
            }
        });

        matchRepository.saveAll(matches);
        return true;
    }

    public List<Match> getUsersByMatchCreatedAt(String userId) {
        return matchRepository.findByUserChatContainingOrderByCreatedAtDesc(userId);
    }

}
