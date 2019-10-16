package com.stackroute.controller;


import com.stackroute.domain.User;
import com.stackroute.exception.UserAlreadyExistException;
import com.stackroute.service.Receiver;
import com.stackroute.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "api/v1/user-service")
public class UserController {
  UserService userService;

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    @Autowired
    private Queue queue;

    @Autowired
    public UserController(UserService userService, RabbitTemplate rabbitTemplate, Receiver receiver) {
        this.userService = userService;
        this.rabbitTemplate = rabbitTemplate;
        this.receiver = receiver;
    }
    @PostMapping("user")
    public ResponseEntity<?> saveUser(@RequestBody User user){
        ResponseEntity responseEntity;
        try {
            userService.saveUser(user);
            responseEntity = new ResponseEntity<String>("Successfully Created", HttpStatus.CREATED);
        } catch (UserAlreadyExistException e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
            }
       return responseEntity;
    }
    @GetMapping("users")
    public ResponseEntity<?>getAllUser() throws InterruptedException {
        Logger logger = LoggerFactory.getLogger(UserController.class);
        User user = new User();
        user.setId(99);
        user.setFirstName("zzzzz");
        //rabbitTemplate.convertAndSend(queue.getName(), "Hello from RabbitMQ!");
        rabbitTemplate.convertAndSend(queue.getName(), user);
        //receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
      return new ResponseEntity<List<User>>(userService.getAllUser() , HttpStatus.OK);
    }




}
