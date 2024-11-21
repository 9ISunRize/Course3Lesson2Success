package ru.hogwarts.school2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;

public class InfoController {
    @Value("${server.port}")
    private int serverPort;
    @GetMapping("/port")
    public int getServerPortNumber(){return serverPort;}
}
