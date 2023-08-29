package ru.clevertec.sm.statemachine.action;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import ru.clevertec.sm.config.EmailSubscribersProperties;
import ru.clevertec.sm.service.EmailService;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;
import ru.clevertec.sm.util.ServiceConstants;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class SendEmailAction implements Action<State, Event> {

    private final static String EMAIL_SUBJECT = "Product archives generated";
    private final EmailService emailService;
    private final EmailSubscribersProperties emailSubscribersProps;

    @Override
    public void execute(StateContext<State, Event> context) {
        String[] subscribersEmails = emailSubscribersProps
                .getEmails()
                .toArray(new String[0]);
        Map<String, List<String>> archivesAndFiles = context.getExtendedState()
                .get(ServiceConstants.GENERATED_ARCHIVES_AND_FILES, Map.class);
        String messageText = buildMessageText(archivesAndFiles);
        emailService.sendEmailToConsumers(
                subscribersEmails,
                EMAIL_SUBJECT,
                messageText
        );
    }

    private String buildMessageText(Map<String, List<String>> archivesAndFiles) {
        StringBuilder messageText = new StringBuilder();
        messageText.append("Generated archives and files:\n");
        for (var entry : archivesAndFiles.entrySet()) {
            messageText
                    .append(entry.getKey())
                    .append("\n");
            for (var fileName : entry.getValue()) {
                messageText
                        .append("\t- ")
                        .append(fileName)
                        .append("\n");
            }
        }

        return messageText.toString();
    }
}
