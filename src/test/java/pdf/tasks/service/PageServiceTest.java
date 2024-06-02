package pdf.tasks.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class PageServiceTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private PageService pageService;

    @Test
    void removePages() throws IOException {
        MultipartFile multipartFile = this.createBlankPdf();
        when(this.fileService.isValid(multipartFile)).thenReturn(true);
        Integer[] pagesToDelete = {1};

        byte[] result = this.pageService.removePages(multipartFile, pagesToDelete);

        assertNotNull(result);
        PDDocument resultDoc = PDDocument.load(new ByteArrayInputStream(result));
        assertEquals(0, resultDoc.getNumberOfPages());
        resultDoc.close();
    }

    @Test
    void removePagesInvalidFile() throws IOException {
        MultipartFile multipartFile = this.createBlankPdf();
        when(this.fileService.isValid(multipartFile)).thenReturn(false);
        Integer[] pagesToDelete = {1};

        assertThrows(FileException.class, () -> {
            this.pageService.removePages(multipartFile, pagesToDelete);
        });
    }

    @Test
    void removePagesInvalidPages() throws IOException {
        MultipartFile multipartFile = this.createBlankPdf();
        when(this.fileService.isValid(multipartFile)).thenReturn(true);
        Integer[] pagesToDelete = {100};

        assertThrows(FileException.class, () -> {
            this.pageService.removePages(multipartFile, pagesToDelete);
        });
    }

    @Test
    void keepOnePage() throws IOException {
        MultipartFile multipartFile = this.createBlankPdf();
        when(this.fileService.isValid(multipartFile)).thenReturn(true);
        int page = 1;
        byte[] result = pageService.keepOnePage(multipartFile, page);

        assertNotNull(result);
        PDDocument resultDoc = PDDocument.load(new ByteArrayInputStream(result));
        assertEquals(1, resultDoc.getNumberOfPages());
        resultDoc.close();
    }

    @Test
    void keepOnePagesInvalidFile() throws IOException {
        MultipartFile multipartFile = this.createBlankPdf();
        when(this.fileService.isValid(multipartFile)).thenReturn(false);
        int page = 1;

        assertThrows(FileException.class, () -> {
            this.pageService.keepOnePage(multipartFile, page);
        });
    }

    @Test
    void keepOnePageInvalidPage() throws IOException {
        MultipartFile multipartFile = this.createBlankPdf();
        when(this.fileService.isValid(multipartFile)).thenReturn(true);
        int page = 100;

        assertThrows(FileException.class, () -> {
            this.pageService.keepOnePage(multipartFile, page);
        });
    }

    private MultipartFile createBlankPdf() throws IOException {
        PDDocument document = new PDDocument();
        PDPage blankPage = new PDPage();
        document.addPage(blankPage);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();
        byte[] pdfAsBytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        return new MockMultipartFile("test.pdf", "test.pdf", "application/pdf", pdfAsBytes);
    }
}