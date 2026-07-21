package com.app.authorization.server.domain.model.valueobject;

import com.app.authorization.server.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    void valid_email_accepted() {
        Email email = new Email("user@example.com");
        assertThat(email.getValue()).isEqualTo("user@example.com");
    }

    @ParameterizedTest
    @ValueSource(strings = {"notanemail", "@nodomain", "missing@", "spaces @x.com", ""})
    void invalid_format_throws(String bad) {
        assertThatThrownBy(() -> new Email(bad))
                .isInstanceOf(InvalidEmailException.class);
    }
}
