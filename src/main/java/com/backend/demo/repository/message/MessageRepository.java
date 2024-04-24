package com.backend.demo.repository.message;

import com.backend.demo.entity.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    List<Message> findAllByRequestedToIdAndIsRequestAcceptedFalse(String userId);

    Optional<Message> findByRequestedToIdAndRequestedById(String requestedToId, String requestedById);

    Optional<Message> findByRequestedByIdAndRequestedToId(String requestedById, String requestedToId);
}
