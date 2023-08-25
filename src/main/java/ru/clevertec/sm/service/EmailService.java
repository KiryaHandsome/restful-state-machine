package ru.clevertec.sm.service;

import java.util.List;

public interface EmailService {

    void sendEmailToSubscribers(List<String> subscribers);
}
