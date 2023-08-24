package ru.clevertec.sm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class StateMachineController {

    @PostMapping("/start")
    public void startSM(@RequestParam(defaultValue = "true") boolean sendEmail) {
        //TODO start state machine
    }
}
