package pdf.tasks.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import pdf.tasks.service.SecurityService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class SecurityControllerTest {

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private SecurityController securityController;

    @Test
    public void removePassword() {
        MockMultipartFile pdfFile = new MockMultipartFile("pdfFile", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "mock pdf content".getBytes());
        String password = "password";
        byte[] pdfContent = "PDF content without password".getBytes();

        when(this.securityService.removePassword(any(MultipartFile.class), anyString())).thenReturn(pdfContent);

        ResponseEntity<byte[]> response = this.securityController.removePassword(pdfFile, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("inline; filename=no_password.pdf", response.getHeaders().getFirst("Content-Disposition"));
        assertEquals(pdfContent, response.getBody());
    }

    @Test
    public void protectPassword() {
        MockMultipartFile pdfFile = new MockMultipartFile("pdfFile", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "mock pdf content".getBytes());
        String password = "password";
        byte[] pdfContent = "PDF content with password".getBytes();

        when(this.securityService.addPassword(any(MultipartFile.class), anyString())).thenReturn(pdfContent);

        ResponseEntity<byte[]> response = this.securityController.addPassword(pdfFile, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("inline; filename=protected.pdf", response.getHeaders().getFirst("Content-Disposition"));
        assertEquals(pdfContent, response.getBody());
    }
}