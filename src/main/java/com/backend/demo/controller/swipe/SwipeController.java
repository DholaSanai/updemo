package com.backend.demo.controller.swipe;

import com.backend.demo.components.QueueSender;
import com.backend.demo.dto.swipe.GetAllSwipes;
import com.backend.demo.exceptions.IncorrectFlagException;
import com.backend.demo.model.SwipeRequestBody;
import com.backend.demo.model.SwipeResponseBody;
import com.backend.demo.service.match.MatchService;
import com.backend.demo.service.swipe.SwipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("chillow-swipe/api/v1")
public class SwipeController {

    @Autowired
    private SwipeService swipeService;

    @Autowired
    private MatchService matchService;
    @Autowired
    private QueueSender queueSender;

    @PostMapping(value = "/swipe")
    public SwipeResponseBody swipe(@Valid @RequestBody SwipeRequestBody body) throws JsonProcessingException {
        if ((body.getIsSwipedLeft() && body.getIsSwipedRight()) ||
                (!body.getIsSwipedLeft() && !body.getIsSwipedRight())) {
            throw new IncorrectFlagException("Swipe flags are incorrect, both can not be true or false at the same time");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(body);
        queueSender.send(json); //send to queue
        return new SwipeResponseBody("success");
    }

    @GetMapping("/get-all")
    public List<GetAllSwipes> getAllSwipesOfUser(@RequestParam String userId, @RequestParam Double longitude,
                                                 @RequestParam Double latitude,
                                                 @RequestParam(required = false) List<String> pronouns,
                                                 @RequestParam(defaultValue = "0", required = false) Integer minAge,
                                                 @RequestParam(defaultValue = "200", required = false) Integer maxAge) {
        return swipeService.getAllSwipes(userId, longitude, latitude, minAge, maxAge, pronouns);
    }

    @DeleteMapping("/swipe/clear-all")
    public Boolean clearAllLeftSwipesOfUsers(@RequestParam String userId) {
            return swipeService.clearAllLeftSwipesOfUser(userId);

    }

    @PostMapping("/swipe/reset")
    public String resetUserSwipes(@RequestParam String userId) {
        if (swipeService.resetSwipeOfUser(userId)) {
            return "swipes reset";
        }
        return "swipes could not be reset";
    }

    @DeleteMapping("/swipe/delete")
    public Boolean deleteUserSwipes(@RequestParam String userId) {
        return swipeService.deleteSwipesOfUser(userId);
    }

}
