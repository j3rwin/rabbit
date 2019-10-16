package com.stackroute.service;

import com.stackroute.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {
    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(User message) {
        System.out.println("Received <" + message + ">");

        Logger logger = LoggerFactory.getLogger(Receiver.class);
        logger.error(message.getFirstName());
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
