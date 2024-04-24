package com.backend.demo.firebase.evidentVerification.firebaseService;

import com.backend.demo.firebase.evidentVerification.firebaseModel.FirebaseEvidentVerification;
import com.backend.demo.firebase.swipe.firebaseServices.FirebaseCrud;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class FirebaseEvidentVerificationService extends FirebaseCrud<FirebaseEvidentVerification> {
    private final String COLLECTION_NAME = "NotificationCenter";

    @Override
    public String save(FirebaseEvidentVerification obj) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> result = firestore.collection(COLLECTION_NAME).document(obj.getSendTo().get(0))
                .set(obj);
        return result.get().getUpdateTime().toString();
    }

    @Override
    public FirebaseEvidentVerification getById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = getSnapShotById(id);

        if (doc.exists()) {
            return doc.toObject(FirebaseEvidentVerification.class);
        }
        return null;
    }

    @Override
    public List<FirebaseEvidentVerification> getAll() {
        return null;
    }
}
