package ru.clevertec.sm.statemachine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import ru.clevertec.sm.util.StateMachineUtil;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
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
    void checkStateAfterStartWithCategoryShouldBeCategoryProcessing() {
        StateMachineUtil.sendEventToSM(stateMachine, Event.STARTED_WITH_CATEGORY);

        State state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(State.CATEGORY_PROCESSING);
    }

    @Test
    void checkStateAfterFetchCategoriesShouldBeCategoryProcessing() {
        StateMachineUtil.sendEventToSM(stateMachine, Event.FETCH_CATEGORIES);

        State state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(State.CATEGORY_PROCESSING);
    }

    @Test
    void checkStateAfterFetchProductsShouldBeMakingCSVFiles() {
        StateMachineUtil.sendEventToSM(stateMachine, Event.FETCH_CATEGORIES);
        StateMachineUtil.sendEventToSM(stateMachine, Event.PRODUCTS_FETCHED);

        State state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(State.MAKING_CSV_FILES);
    }

    @Test
    void checkStateAfterFinishCSVFilesShouldBeMakingZipArchives() {
        StateMachineUtil.sendEventToSM(stateMachine, Event.FETCH_CATEGORIES);
        StateMachineUtil.sendEventToSM(stateMachine, Event.PRODUCTS_FETCHED);
        StateMachineUtil.sendEventToSM(stateMachine, Event.FINISH_CSV_FILES);

        State state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(State.MAKING_ZIP_ARCHIVES);
    }

    @Test
    void checkStateAfterFinishShouldBeIdle() {
        StateMachineUtil.sendEventToSM(stateMachine, Event.FETCH_CATEGORIES);
        StateMachineUtil.sendEventToSM(stateMachine, Event.PRODUCTS_FETCHED);
        StateMachineUtil.sendEventToSM(stateMachine, Event.FINISH_CSV_FILES);
        StateMachineUtil.sendEventToSM(stateMachine, Event.FINISH_ZIP_ARCHIVES);

        State state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(State.IDLE);
    }
}
