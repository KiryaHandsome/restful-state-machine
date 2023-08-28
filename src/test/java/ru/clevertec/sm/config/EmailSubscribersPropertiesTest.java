package ru.clevertec.sm.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.sm.util.TestData;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class EmailSubscribersPropertiesTest {

    @Autowired
    private EmailSubscribersProperties properties;

    @Test
    void checkGetEmailsShouldContainsExpectedEmails() {
        var expected = TestData.getEmailsFromProps();

        var actual = properties.getEmails();

        assertThat(actual).containsAll(expected);
    }
}