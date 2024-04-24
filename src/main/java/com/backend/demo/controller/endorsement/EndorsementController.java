package com.backend.demo.controller.endorsement;

import com.backend.demo.dto.endorsement.EndorsementRequestBody;
import com.backend.demo.service.endorsement.EndorsementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/roommate-endorsements")
public class EndorsementController {
    @Autowired
    EndorsementService endorsementService;

    @PostMapping("post-endorsements")
    private ResponseEntity<?> postEndorsements(@RequestBody EndorsementRequestBody endorsementRequestBody) throws IllegalAccessException {
        Boolean check = endorsementService.addEndorsement(endorsementRequestBody);
        return new ResponseEntity<>(check, HttpStatus.OK);
    }
    @GetMapping("get-endorsements")
    private ResponseEntity<?> getEndorsements(@RequestParam String userId){
        Object ob = endorsementService.getEndorsement(userId);
        return new ResponseEntity<>(ob, HttpStatus.OK);
    }
}
