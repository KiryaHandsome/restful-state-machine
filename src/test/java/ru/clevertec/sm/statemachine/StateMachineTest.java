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
    private StateMachine<States, Events> stateMachine;

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
        States state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(States.STARTED);
    }

    @Test
    void checkStateAfterStartWithCategoryShouldBeCategoryProcessing() {
        StateMachineUtil.sendEventToSM(stateMachine, Events.STARTED_WITH_CATEGORY);

        States state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(States.CATEGORY_PROCESSING);
    }

    @Test
    void checkStateAfterFetchCategoriesShouldBeCategoryProcessing() {
        StateMachineUtil.sendEventToSM(stateMachine, Events.FETCH_CATEGORIES);

        States state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(States.CATEGORY_PROCESSING);
    }

    @Test
    void checkStateAfterFetchProductsShouldBeMakingCSVFiles() {
        StateMachineUtil.sendEventToSM(stateMachine, Events.FETCH_CATEGORIES);
        StateMachineUtil.sendEventToSM(stateMachine, Events.PRODUCTS_FETCHED);

        States state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(States.MAKING_CSV_FILES);
    }

    @Test
    void checkStateAfterFinishCSVFilesShouldBeMakingZipArchives() {
        StateMachineUtil.sendEventToSM(stateMachine, Events.FETCH_CATEGORIES);
        StateMachineUtil.sendEventToSM(stateMachine, Events.PRODUCTS_FETCHED);
        StateMachineUtil.sendEventToSM(stateMachine, Events.FINISH_CSV_FILES);

        States state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(States.MAKING_ZIP_ARCHIVES);
    }

    @Test
    void checkStateAfterFinishShouldBeIdle() {
        StateMachineUtil.sendEventToSM(stateMachine, Events.FETCH_CATEGORIES);
        StateMachineUtil.sendEventToSM(stateMachine, Events.PRODUCTS_FETCHED);
        StateMachineUtil.sendEventToSM(stateMachine, Events.FINISH_CSV_FILES);
        StateMachineUtil.sendEventToSM(stateMachine, Events.FINISH_ZIP_ARCHIVES);

        States state = stateMachine.getState().getId();

        assertThat(state).isEqualTo(States.IDLE);
    }
}
