package ru.clevertec.sm.statemachine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.sm.util.StateMachineUtil;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class StateMachineTest {

    @Autowired
    private StateMachine<State, Event> stateMachine;

    @BeforeEach
    void setUp() {
        stateMachine.startReactively().subscribe();
    }

    @AfterEach
    void tearDown() {
        stateMachine.stopReactively().subscribe();
    }

    @Test
    void checkInitialStateShouldBeStarted() {
        State state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(State.STARTED);
    }

    @Test
    void checkStateAfterFetchCategoriesShouldBeCategoryProcessing() {
        StateMachineUtil.sendEvent(stateMachine, Event.FETCH_CATEGORIES);

        State state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(State.CATEGORY_PROCESSING);
    }

    @Test
    void checkStateAfterFetchProductsShouldBeMakingCSVFiles() {
        StateMachineUtil.sendEvent(stateMachine, Event.FETCH_CATEGORIES);
        StateMachineUtil.sendEvent(stateMachine, Event.MAKE_CSV_FILES);

        State state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(State.MAKING_CSV_FILES);
    }

    @Test
    void checkStateAfterFinishCSVFilesShouldBeMakingZipArchives() {
        StateMachineUtil.sendEvent(stateMachine, Event.FETCH_CATEGORIES);
        StateMachineUtil.sendEvent(stateMachine, Event.MAKE_CSV_FILES);
        StateMachineUtil.sendEvent(stateMachine, Event.MAKE_ZIP_ARCHIVES);

        State state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(State.MAKING_ZIP_ARCHIVES);
    }

    @Test
    void checkStateAfterFinishShouldBeIdle() {
        StateMachineUtil.sendEvent(stateMachine, Event.FETCH_CATEGORIES);
        StateMachineUtil.sendEvent(stateMachine, Event.MAKE_CSV_FILES);
        StateMachineUtil.sendEvent(stateMachine, Event.MAKE_ZIP_ARCHIVES);
        StateMachineUtil.sendEvent(stateMachine, Event.FINISH_ZIP_ARCHIVES);

        State state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(State.IDLE);
    }

    @Test
    void checkStateSendEmailShouldBeIdle() {
        StateMachineUtil.sendEvent(stateMachine, Event.FETCH_CATEGORIES);
        StateMachineUtil.sendEvent(stateMachine, Event.MAKE_CSV_FILES);
        StateMachineUtil.sendEvent(stateMachine, Event.MAKE_ZIP_ARCHIVES);
        StateMachineUtil.sendEvent(stateMachine, Event.FINISH_ZIP_ARCHIVES);
        StateMachineUtil.sendEvent(stateMachine, Event.SEND_EMAIL);

        State state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(State.IDLE);
    }
}


