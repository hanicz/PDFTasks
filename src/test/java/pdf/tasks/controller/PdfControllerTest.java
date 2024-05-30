package pdf.tasks.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import pdf.tasks.service.PdfService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class PdfControllerTest {

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private PdfController pdfController;

    @Test
    public void removePages() {
        MultipartFile mockPdfFile = new MockMultipartFile("pdfFile", "input.pdf", "application/pdf", "mock pdf content".getBytes());
        Integer[] mockPages = {1, 3};
        byte[] mockRemovedPdf = "mock removed pdf content".getBytes();
        when(pdfService.removePages(any(MultipartFile.class), any(Integer[].class))).thenReturn(mockRemovedPdf);

        ResponseEntity<byte[]> response = pdfController.removePages(mockPdfFile, mockPages);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("inline; filename=removed.pdf", response.getHeaders().getFirst("Content-Disposition"));
        assertEquals(mockRemovedPdf, response.getBody());
    }

    @Test
    public void keepOnePage() {
        MultipartFile mockPdfFile = new MockMultipartFile("pdfFile", "input.pdf", "application/pdf", "mock pdf content".getBytes());
        int mockPage = 2;
        byte[] mockOnePagedPdf = "mock one paged pdf content".getBytes();
        when(pdfService.keepOnePage(any(MultipartFile.class), any(Integer.class))).thenReturn(mockOnePagedPdf);

        ResponseEntity<byte[]> response = pdfController.keepOnePage(mockPdfFile, mockPage);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("inline; filename=one_page.pdf", response.getHeaders().getFirst("Content-Disposition"));
        assertEquals(mockOnePagedPdf, response.getBody());
    }
}