package ru.clevertec.sm.repository;

import org.springframework.statemachine.data.mongodb.MongoDbStateMachineRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateMachineRepository extends MongoDbStateMachineRepository {
}
