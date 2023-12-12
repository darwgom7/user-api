package com.darwgom.userapi.utilities;

import com.darwgom.userapi.domain.exceptions.InvalidPasswordException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PasswordValidatorTest {

    @Test
    void isValidReturnsTrueForValidPassword() {
        String validPassword = "Hunter2+";
        assertTrue(PasswordValidator.isValid(validPassword));
    }

    @Test
    void isValidReturnsFalseForInvalidPassword() {
        String invalidPassword = "invalid";
        assertFalse(PasswordValidator.isValid(invalidPassword));
    }


    @Test
    void validatePasswordDoesNotThrowForValidPassword() {
        String validPassword = "ValidPassword1+";
        assertDoesNotThrow(() -> PasswordValidator.validatePassword(validPassword));
    }

    @Test
    void validatePasswordThrowsForInvalidPassword() {
        String invalidPassword = "invalid";
        assertThrows(InvalidPasswordException.class, () -> PasswordValidator.validatePassword(invalidPassword));
    }
}
