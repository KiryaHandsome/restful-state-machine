package ru.clevertec.sm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.sm.service.StateMachineService;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class StateMachineController {

    private final StateMachine<State, Event> stateMachine;
    private final StateMachineService stateMachineService;
    private final StateMachinePersister<State, Event, UUID> persister;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @PostMapping("/start")
    public ResponseEntity<String> startSM(
            @RequestParam(defaultValue = "true") boolean sendEmail,
            @RequestParam Optional<String> category
    ) {
        log.info("Starting State Machine. sendEmail={}, hasCategory={}", sendEmail, category.isPresent());
        executorService.submit(() -> stateMachineService.launch(sendEmail, category));
        return ResponseEntity.ok("State Machine started");
    }

    @GetMapping("/info")
    public ResponseEntity<String> getInfoAboutFilesCreation() {
        String report = stateMachineService.buildReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/state")
    public ResponseEntity<State> getCurrentState() {
        State state = stateMachineService.getCurrentState();
        return ResponseEntity.ok(state);
    }

    @PostMapping("/state")
    public ResponseEntity<UUID> persistStateMachine() throws Exception {
        UUID uuid = stateMachine.getUuid();
        log.info(String.valueOf(uuid));
        persister.persist(stateMachine, uuid);
        return ResponseEntity.ok(uuid);
    }

    @GetMapping("/state/{uuid}")
    public ResponseEntity<String> getStateFromDb(@PathVariable UUID uuid) throws Exception {
        persister.restore(stateMachine, uuid);
        return ResponseEntity.ok("State machine restored");
    }
}
