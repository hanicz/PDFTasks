package pdf.tasks.exception;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class FileExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String message = "Test FileException message";
        FileException fileException = new FileException(message);
        assertEquals(message, fileException.getMessage());
        assertNull(fileException.getCause());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String message = "Test FileException message";
        Throwable cause = new Throwable("Test cause");
        FileException fileException = new FileException(message, cause);
        assertEquals(message, fileException.getMessage());
        assertEquals(cause, fileException.getCause());
    }
}