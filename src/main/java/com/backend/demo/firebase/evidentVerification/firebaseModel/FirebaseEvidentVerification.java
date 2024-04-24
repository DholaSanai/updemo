package com.backend.demo.firebase.evidentVerification.firebaseModel;

import com.google.cloud.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FirebaseEvidentVerification {
    private String title;
    private String message;
    private String type;
    private List<String> sendTo;
    private List<String> seenBy;
    private Map<String, String> data;
    private Timestamp createdAt;

}
