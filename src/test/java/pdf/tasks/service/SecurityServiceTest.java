package pdf.tasks.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import pdf.tasks.exception.FileException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class SecurityServiceTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private SecurityService securityService;


    @Test
    void removePassword() throws IOException {
        MultipartFile multipartFile = this.createBlankPdf(true);
        when(this.fileService.isValid(multipartFile)).thenReturn(true);
        byte[] result = this.securityService.removePassword(multipartFile, "password");

        assertNotNull(result);
        PDDocument resultDoc = PDDocument.load(new ByteArrayInputStream(result));
        assertEquals(1, resultDoc.getNumberOfPages());
        resultDoc.close();
    }

    @Test
    void removePasswordInvalidFile() throws IOException {
        MultipartFile multipartFile = this.createBlankPdf(false);
        when(this.fileService.isValid(multipartFile)).thenReturn(false);

        assertThrows(FileException.class, () -> {
            this.securityService.removePassword(multipartFile, "password");
        });
    }

    @Test
    void removePasswordInvalidPassword() throws IOException {
        MultipartFile multipartFile = this.createBlankPdf(true);
        when(this.fileService.isValid(multipartFile)).thenReturn(true);
        assertThrows(FileException.class, () -> {
            this.securityService.removePassword(multipartFile, "invalid");
        });
    }


    @Test
    void addPassword() throws IOException {
        MultipartFile multipartFile = this.createBlankPdf(false);
        when(this.fileService.isValid(multipartFile)).thenReturn(true);
        byte[] result = this.securityService.addPassword(multipartFile, "password");

        assertNotNull(result);
        assertThrows(InvalidPasswordException.class, () -> {
            PDDocument.load(new ByteArrayInputStream(result));
        });
        PDDocument resultDoc = PDDocument.load(new ByteArrayInputStream(result), "password");
        assertEquals(1, resultDoc.getNumberOfPages());
        resultDoc.close();
    }

    @Test
    void addPasswordInvalidFile() throws IOException {
        MultipartFile multipartFile = this.createBlankPdf(false);
        when(this.fileService.isValid(multipartFile)).thenReturn(false);

        assertThrows(FileException.class, () -> {
            this.securityService.addPassword(multipartFile, "password");
        });
    }

    private MultipartFile createBlankPdf(boolean addPassword) throws IOException {
        PDDocument document = new PDDocument();
        PDPage blankPage = new PDPage();
        document.addPage(blankPage);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (addPassword)
            document.protect(this.createProtectionPolicy("password"));
        document.save(byteArrayOutputStream);
        document.close();
        byte[] pdfAsBytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        return new MockMultipartFile("test.pdf", "test.pdf", "application/pdf", pdfAsBytes);
    }

    private StandardProtectionPolicy createProtectionPolicy(String password) {
        AccessPermission accessPermission = new AccessPermission();
        accessPermission.setCanPrint(false);
        accessPermission.setCanExtractContent(false);
        accessPermission.setCanModify(false);
        accessPermission.setCanModifyAnnotations(false);

        StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy(password, password, accessPermission);
        protectionPolicy.setEncryptionKeyLength(128);
        return protectionPolicy;
    }
}