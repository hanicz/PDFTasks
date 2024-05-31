package pdf.tasks.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import pdf.tasks.exception.dto.ErrorResponse;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
class ExceptionAdviceTest {

    ExceptionAdvice exceptionAdvice = new ExceptionAdvice();

    @Test
    public void testHandleFileException() {
        FileException fileException = new FileException("Test File Exception");

        ResponseEntity<ErrorResponse> responseEntity = this.exceptionAdvice.handleFileException(fileException);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), Objects.requireNonNull(responseEntity.getBody()).code());
        assertEquals("Test File Exception", responseEntity.getBody().error());
    }
}