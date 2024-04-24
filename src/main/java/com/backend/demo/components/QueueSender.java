package com.backend.demo.components;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueueSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private Queue roomQueue;

    public void send(String order){
        rabbitTemplate.convertAndSend(this.queue.getName(),order);
    }


    public void sendToRoomQueue(String order){
        rabbitTemplate.convertAndSend(this.roomQueue.getName(),order);
    }
}
