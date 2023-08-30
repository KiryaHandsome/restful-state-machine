package ru.clevertec.sm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.data.mongodb.MongoDbPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.mongodb.MongoDbStateMachineRepository;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;

import java.util.UUID;


@Configuration
public class MongoPersisterConfig {

    @Bean
    public StateMachinePersister<State, Event, UUID> persister(
            StateMachinePersist<State, Event, UUID> defaultPersist
    ) {
        return new DefaultStateMachinePersister<>(defaultPersist);
    }

    @Bean
    public StateMachineRuntimePersister<State, Event, UUID> stateMachineRuntimePersister(
            MongoDbStateMachineRepository jpaStateMachineRepository
    ) {
        return new MongoDbPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }
}
