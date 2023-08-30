package ru.clevertec.sm.service;

public interface EmailService {

    void sendEmailToConsumers(String[] consumersEmails,
                              String subject,
                              String text);
}
