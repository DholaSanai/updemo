package com.backend.demo.controller.swipe;

import com.backend.demo.dto.swipe.MatchData;
import com.backend.demo.service.match.MatchedUserService;
import com.backend.demo.service.match.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("chillow-swipe/api/v1")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchedUserService matchedUserService;

    @GetMapping("/match")
    public List<MatchData> getAllUserMatches(@RequestParam String userId) {
        return matchService.getMatch(userId);
    }

    @DeleteMapping("/match")
    public Boolean deleteMatchesOfUser(@RequestParam String userId) {
        return matchService.deleteUserMatches(userId);
    }

    @PutMapping("/match/update")
    public Boolean updateMatchMessageCount(@RequestParam String userChat, @RequestParam String userId, @RequestParam Integer count) {
        return matchService.updateMatchMessages(userChat, userId, count);
    }

    @GetMapping("/get-user-matches")
    public ResponseEntity<?> getUserMatches(@RequestParam String userId){
        return new ResponseEntity<>(matchedUserService.getAllUserMatches(userId), HttpStatus.OK);
    }


}
