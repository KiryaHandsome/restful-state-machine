package ru.clevertec.sm.service;

public interface EmailService {

    void sendEmailToSubscribers(String[] consumersEmails,
                                String subject,
                                String text);
}
