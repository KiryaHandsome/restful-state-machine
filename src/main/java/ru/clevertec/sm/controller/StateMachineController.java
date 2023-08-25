package ru.clevertec.sm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.sm.service.StateMachineService;
import ru.clevertec.sm.statemachine.Events;
import ru.clevertec.sm.statemachine.States;

import javax.swing.plaf.nimbus.State;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class StateMachineController {

    private final StateMachineService stateMachineService;

    @PostMapping("/start")
    public ResponseEntity<String> startSM(
            @RequestParam(defaultValue = "true") boolean sendEmail,
            @RequestParam Optional<String> category
    ) {
        log.info("Starting State Machine. sendEmail={}", sendEmail);
        stateMachineService.launch(sendEmail, category);
        return ResponseEntity.ok("State Machine started");
    }

    @GetMapping("/state")
    public ResponseEntity<States> getCurrentState() {
        return ResponseEntity.ok(stateMachineService.getCurrentState());
    }

    @GetMapping("/variables")
    public ResponseEntity<Map<?, ?>> getVariables() {
        return ResponseEntity.ok(stateMachineService.getVariables());
    }

    @PostMapping("/fetch_categories")
    public ResponseEntity<String> fetchCategories() {
        stateMachineService.sendEvent(Events.FETCH_CATEGORIES);
        return ResponseEntity.ok("sent");
    }
}
