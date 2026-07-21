package com.app.authorization.server.domain.model.valueobject;

import com.app.authorization.server.domain.exception.InvalidPasswordException;
import com.app.authorization.server.domain.repository.PasswordHasher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PasswordTest {

    @Mock
    private PasswordHasher hasher;

    @Test
    void fromHash_null_throws() {
        assertThatThrownBy(() -> Password.fromHash(null))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void fromHash_blank_throws() {
        assertThatThrownBy(() -> Password.fromHash("  "))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void fromHash_valid_stores_value() {
        Password p = Password.fromHash("$2a$10$hash");
        assertThat(p.getValue()).isEqualTo("$2a$10$hash");
    }

    @Test
    void fromRaw_too_short_throws() {
        assertThatThrownBy(() -> Password.fromRaw("abc", hasher))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void fromRaw_valid_delegates_to_hasher() {
        given(hasher.hash("secret123")).willReturn("hashed");
        Password p = Password.fromRaw("secret123", hasher);
        assertThat(p.getValue()).isEqualTo("hashed");
    }

    @Test
    void matches_delegates_to_hasher() {
        Password p = Password.fromHash("hashed");
        given(hasher.verify("raw", "hashed")).willReturn(true);
        assertThat(p.matches("raw", hasher)).isTrue();
    }
}
