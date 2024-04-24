package com.backend.demo.firebase.swipe.firebaseServices;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class FirebaseCrud<T> {
    protected final Firestore firestore = FirestoreClient.getFirestore();
    protected String collectionName="default";
    public abstract String save(T obj) throws ExecutionException, InterruptedException;

    public DocumentSnapshot getSnapShotById(String id) throws ExecutionException, InterruptedException {
        DocumentReference reference = firestore.collection(collectionName).document(id);
        ApiFuture<DocumentSnapshot> future = reference.get();
        return future.get();
    }

    public abstract T getById(String id) throws ExecutionException, InterruptedException;

    public abstract List<T> getAll();

}
