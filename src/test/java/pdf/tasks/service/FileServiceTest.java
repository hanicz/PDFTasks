package pdf.tasks.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class FileServiceTest {

    @Mock
    private MultipartFile file;

    @InjectMocks
    private FileService fileService;

    @Test
    public void isValid() {
        when(this.file.getOriginalFilename()).thenReturn("test.pdf");
        Assertions.assertTrue(this.fileService.isValid(this.file));
    }

    @Test
    public void isValidNull() {
        when(this.file.getOriginalFilename()).thenReturn(null);
        Assertions.assertFalse(this.fileService.isValid(this.file));
    }

    @Test
    public void isValidNoExtension() {
        when(this.file.getOriginalFilename()).thenReturn("pdf");
        Assertions.assertFalse(this.fileService.isValid(this.file));
    }

    @Test
    public void isValidMultiple() {
        when(this.file.getOriginalFilename()).thenReturn("pdf.jpg.pdf");
        Assertions.assertTrue(this.fileService.isValid(this.file));
    }

    @Test
    public void isValidMultiple2() {
        when(this.file.getOriginalFilename()).thenReturn("pdf.pdf.jpg");
        Assertions.assertFalse(this.fileService.isValid(this.file));
    }
}