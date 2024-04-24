package com.backend.demo.service.message;

import com.backend.demo.dto.message.CheckMessageResponseBody;
import com.backend.demo.dto.message.MessageRequestBody;
import com.backend.demo.dto.message.MessageResponseBody;
import com.backend.demo.entity.message.Message;
import com.backend.demo.entity.swipe.Match;
import com.backend.demo.entity.swipe.Swipe;
import com.backend.demo.entity.user.ChillowUser;
import com.backend.demo.repository.message.MessageRepository;
import com.backend.demo.repository.swipe.MatchRepository;
import com.backend.demo.repository.swipe.SwipeRepository;
import com.backend.demo.repository.user.UserRepository;
import com.backend.demo.service.match.MatchService;
import com.backend.demo.service.notification.NotificationService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    MatchService matchService;
    @Autowired
    SwipeRepository swipeRepository;
    @Autowired
    MatchRepository matchRepository;

    public Object getAllUserReceivedMessage(String userId) {
        List<Message> messageList = messageRepository.findAllByRequestedToIdAndIsRequestAcceptedFalse(userId);
        if (!messageList.isEmpty()) {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STANDARD);

            List<ChillowUser> chillowUserList = userRepository.findAllByIdIn(
                    messageList.stream().map(each -> each.getRequestedById()).collect(Collectors.toList()));

            if (!chillowUserList.isEmpty()) {
                List<MessageResponseBody> messageResponseBodyList = new ArrayList<>();
                for (Message eachMessage : messageList) {
                    for (ChillowUser eachUser : chillowUserList) {
                        if (eachMessage.getRequestedById().equals(eachUser.getId())) {
                            MessageResponseBody messageResponseBody
                                    = new MessageResponseBody(eachMessage.getId(), eachMessage.getRequestedById(),
                                    eachUser.getChillowUserImages().get(0).getFile(), eachUser.getName(), eachUser.getPronouns(),
                                    eachUser.getBirthDate(), eachMessage.getMessage(), eachMessage.getSendDateTime());
                            messageResponseBodyList.add(messageResponseBody);
                            break;
                        }
                    }
                }
                return messageResponseBodyList;
            }
        }
        return null;
    }

    public MessageResponseBody requestMessage(MessageRequestBody messageRequestBody) {
        Optional<Message> message = messageRepository.
                findByRequestedToIdAndRequestedById(messageRequestBody.getRequestedToId(),
                        messageRequestBody.getRequestedById());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STANDARD);
        if (!message.isPresent()) {
            message =
                    messageRepository.findByRequestedToIdAndRequestedById(messageRequestBody.getRequestedToId(),
                            messageRequestBody.getRequestedById());
            if (!message.isPresent()) {

                Message newMessage = new Message(UUID.randomUUID().toString(),
                        messageRequestBody.getRequestedById(), messageRequestBody.getRequestedToId(), false,
                        messageRequestBody.getMessage(), LocalDateTime.now(), Instant.now(), Instant.now());

                messageRepository.save(newMessage);

                Swipe swipe = new Swipe(UUID.randomUUID().toString(), messageRequestBody.getRequestedById(),
                        messageRequestBody.getRequestedToId(), true, true,
                        false, false);
                swipeRepository.save(swipe);

//                Match match = new Match(UUID.randomUUID().toString(), messageRequestBody.getRequestedById(),
//                        messageRequestBody.getRequestedToId(), 0,0,
//                        matchService.getUserChat(messageRequestBody.getRequestedById(),
//                        messageRequestBody.getRequestedToId()), null,false,LocalDateTime.now(),
//                        LocalDateTime.now());
//                matchRepository.save(match);
                try {
                    notificationService.sendNoMatchNotification(messageRequestBody.getRequestedById(),
                            messageRequestBody.getRequestedToId(), "You have received a new message request!",
                            matchService.getUserChat(messageRequestBody.getRequestedById(),
                                    messageRequestBody.getRequestedToId()));
                } catch (Exception e) {
                    System.out.print("unable to send notification!");
                }
                return modelMapper.map(newMessage, MessageResponseBody.class);
            }
        }
        message.get().setMessage(messageRequestBody.getMessage());
        message.get().setIsRequestAccepted(false);
        message.get().setSendDateTime(LocalDateTime.now());
        message.get().setUpdatedAt(Instant.now());
        messageRepository.save(message.get());
        return modelMapper.map(message, MessageResponseBody.class);
    }

    public Boolean acceptRequest(MessageRequestBody messageRequestBody) {
        Optional<Message> optionalMessage = messageRepository.findById(messageRequestBody.getId());
        if (optionalMessage.isPresent()) {
            if (messageRequestBody.getIsAccepted()) {
                optionalMessage.get().setIsRequestAccepted(true);
                optionalMessage.get().setUpdatedAt(Instant.now());
                messageRepository.save(optionalMessage.get());

                Optional<Swipe> isSwipedAlready =
                        swipeRepository.findByUserIdAndShownUserId(optionalMessage.get().getRequestedToId(),
                                optionalMessage.get().getRequestedById());
                if (!isSwipedAlready.isPresent()) {
                    Swipe swipe = new Swipe(UUID.randomUUID().toString(), optionalMessage.get().getRequestedToId(),
                            optionalMessage.get().getRequestedById(), true, true,
                            false, false);
                    swipeRepository.save(swipe);
                }
                Optional<Match> isMatchExisting = matchRepository.findByUserIdAndMatchedUserIdAndIsDeletedFalse(optionalMessage.get()
                        .getRequestedById(), optionalMessage.get().getRequestedToId());
                if (!isMatchExisting.isPresent()) {
                    Match match = new Match(UUID.randomUUID().toString(), optionalMessage.get().getRequestedById(),
                            optionalMessage.get().getRequestedToId(), 0, 0,
                            matchService.getUserChat(optionalMessage.get().getRequestedById(),
                                    optionalMessage.get().getRequestedToId()), null, false, LocalDateTime.now(),
                            LocalDateTime.now());
                    matchRepository.save(match);
                }
            } else {
                messageRepository.delete(optionalMessage.get());
                Optional<Swipe> isSwipedAlready =
                        swipeRepository.findByUserIdAndShownUserId(optionalMessage.get().getRequestedToId(),
                                optionalMessage.get().getRequestedById());
                if (!isSwipedAlready.isPresent()) {
                    Swipe swipe = new Swipe(UUID.randomUUID().toString(), optionalMessage.get().getRequestedToId(),
                            optionalMessage.get().getRequestedById(), true, false,
                            true, false);
                    swipeRepository.save(swipe);
                }
            }
            return true;
        }
        return false;
    }

    public Object messageCheck(String requestedById, String requestedToId) {
        Optional<Message> isMessageExist = messageRepository.
                findByRequestedByIdAndRequestedToId(requestedById, requestedToId);
        if (isMessageExist.isPresent()) {
            return new CheckMessageResponseBody(isMessageExist.get().getMessage());
        }
        return false;
    }
}
