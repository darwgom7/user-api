package com.darwgom.userapi.utilities;

import com.darwgom.userapi.domain.exceptions.InvalidPasswordException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmailFormatValidatorTest {
    @Test
    void isValidEmailFormatReturnsTrueForValidEmail() {
        String validEmail = "example@example.com";
        assertTrue(EmailFormatValidator.isValidEmailFormat(validEmail));
    }

    @Test
    void isValidEmailFormatReturnsFalseForInvalidEmail() {
        String invalidEmail = "invalidemail";
        assertFalse(EmailFormatValidator.isValidEmailFormat(invalidEmail));
    }


    @Test
    void validEmailFormatDoesNotThrowForValidEmail() {
        String validEmail = "example@example.com";
        assertDoesNotThrow(() -> EmailFormatValidator.validEmailFormat(validEmail));
    }

    @Test
    void validEmailFormatThrowsForInvalidEmail() {
        String invalidEmail = "invalidemail";
        assertThrows(InvalidPasswordException.class, () -> EmailFormatValidator.validEmailFormat(invalidEmail));
    }
}
