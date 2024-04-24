package com.backend.demo.controller.contest;

import com.backend.demo.dto.contest.ContestRequestBody;
import com.backend.demo.service.contest.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chillow-contest/api/v1/")
public class ContestController {
    @Autowired
    ContestService contestService;
    @PostMapping("submit")
    private Boolean submitContestEntry(@RequestBody ContestRequestBody contestRequestBody){
        return contestService.submitContestEntry(contestRequestBody);
    }
}
