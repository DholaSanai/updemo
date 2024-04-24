package com.backend.demo.firebase.swipe.firebaseServices;

import com.backend.demo.firebase.FirebaseChatRoom;
import com.backend.demo.firebase.swipe.firebaseModels.FirebaseSwipeMatchModel;
import com.backend.demo.service.match.MatchService;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class FirebaseSwipe extends FirebaseCrud<FirebaseSwipeMatchModel> {
    private static final String COLLECTION_NAME = "Swipes";
    private static final String SUB_COLLECTION_NAME = "MySwipes";

    @Autowired
    MatchService matchService;
    private static final String CHAT_COLLECTION_NAME = "ChatRooms";


    @Override
    public String save(FirebaseSwipeMatchModel obj) throws ExecutionException, InterruptedException {
        return null;
    }

    public String save(FirebaseSwipeMatchModel user, FirebaseSwipeMatchModel matchedUser) throws ExecutionException, InterruptedException {
        if (user.getMatchedId().isEmpty()) {
            return "Not updated";
        }

        ApiFuture<WriteResult> result = firestore.collection(COLLECTION_NAME).document(user.getUserId()).
                collection(SUB_COLLECTION_NAME).document(user.getMatchedUserId()).set(user);

        ApiFuture<WriteResult> result2 = firestore.collection(COLLECTION_NAME).document(matchedUser.getUserId()).
                collection(SUB_COLLECTION_NAME).document(matchedUser.getMatchedUserId()).set(matchedUser);
        log.info("Match posted on firebase!");
        String chatId = matchService.getUserChat(matchedUser.getUserId(), matchedUser.getMatchedUserId());
        FirebaseChatRoom newChatRoom = new FirebaseChatRoom(chatId, null, null, Timestamp.now(), Arrays.asList(user.getUserId(),
                user.getMatchedUserId()), 0,null,false);
//        ApiFuture<WriteResult> result3 = firestore.collection(CHAT_COLLECTION_NAME).document(newChatRoom.getChatId()).set(newChatRoom);
        ApiFuture<WriteResult> result3 = firestore.collection(CHAT_COLLECTION_NAME).document(chatId).set(newChatRoom);

        log.info("Chat room created!");
        return result.get().getUpdateTime().toString().concat(result2.get().getUpdateTime().toString()).
                concat(result3.get().getUpdateTime().toString());
    }

    @Override
    public FirebaseSwipeMatchModel getById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = getSnapShotById(id);
        FirebaseSwipeMatchModel swipe = null;
        if (doc.exists()) {
            return doc.toObject(FirebaseSwipeMatchModel.class);
        }
        return null;
    }

    @Override
    public List<FirebaseSwipeMatchModel> getAll() {
        Iterable<DocumentReference> allUserIds = firestore.collection(COLLECTION_NAME).listDocuments();
        List<FirebaseSwipeMatchModel> orders = new ArrayList<>();
        allUserIds.forEach(id -> {
            ApiFuture<DocumentSnapshot> future = id.get();
            try {
                DocumentSnapshot documentSnapshot = future.get();
                orders.add(documentSnapshot.toObject(FirebaseSwipeMatchModel.class));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        return orders;
    }
}
