package com.backend.demo.controller.message;

import com.backend.demo.dto.message.MessageRequestBody;
import com.backend.demo.dto.message.MessageResponseBody;
import com.backend.demo.service.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/message/")
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("get-all-user-received-message")
    private ResponseEntity<?> getAllUserReceivedMessage(@RequestParam String userId) {
        return new ResponseEntity<>(messageService.getAllUserReceivedMessage(userId), HttpStatus.OK);
    }

    @GetMapping("message-check")
    private ResponseEntity<?> messageCheck(@RequestParam String requestedById, @RequestParam String requestedToId) {
        return new ResponseEntity<>(messageService.messageCheck(requestedById, requestedToId), HttpStatus.OK);
    }

    @PostMapping("request-message")
    private ResponseEntity<?> requestMessage(@RequestBody MessageRequestBody messageRequestBody) {
        MessageResponseBody messageResponseBody = messageService.requestMessage(messageRequestBody);
        if (messageResponseBody != null)
            return new ResponseEntity<>(messageResponseBody, HttpStatus.OK);
        else return new ResponseEntity<>("message already exists", HttpStatus.CONFLICT);
    }

    @PutMapping("accept-request")
    private ResponseEntity<?> acceptRequest(@RequestBody MessageRequestBody messageRequestBody) {
        Boolean messageResponseBody = messageService.acceptRequest(messageRequestBody);
        if (messageResponseBody) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else return new ResponseEntity<>(false, HttpStatus.CONFLICT);
    }
}
