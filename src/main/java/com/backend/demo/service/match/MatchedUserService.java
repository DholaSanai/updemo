package com.backend.demo.service.match;

import com.backend.demo.dto.match.MatchedSpecificInfo;
import com.backend.demo.dto.match.MatchedUser;
import com.backend.demo.dto.user.chillowUser.ChillowUserDto;
import com.backend.demo.entity.swipe.Match;
import com.backend.demo.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchedUserService {
    @Autowired
    private MatchService matchService;

    @Autowired
    private UserService userService;

    public List<MatchedUser> getAllUserMatches(String userId) {
        List<Match> allMatches = matchService.getUsersByMatchCreatedAt(userId);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        List<MatchedSpecificInfo> matchedUsers = allMatches.stream().
                map(each -> new MatchedSpecificInfo(each.getUserChat(), getUserId(each, userId)))
                .collect(Collectors.toList());

        List<MatchedUser> matchedUserList = new ArrayList<>();
        for (MatchedSpecificInfo user : matchedUsers) {
            ChillowUserDto users = userService.getAllUserMatchesWithDeleteFalse(user.getMatchedUserId());
            if(users != null) {
                MatchedUser matchedUser = modelMapper.map(users, MatchedUser.class);
                matchedUser.setChatId(user.getChatId());
                matchedUser.setChillowUserImage(users.getChillowUserImages().get(0).getFile());
                matchedUserList.add(matchedUser);
            }
        }
        return matchedUserList;
    }

    private String getUserId(Match each, String userId) {
        if(each.getMatchedUserId().equals(userId)) {
            return each.getUserId();
        }else{
            return each.getMatchedUserId();
        }
    }
}
