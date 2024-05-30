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
import pdf.tasks.service.MergeService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class MergeControllerTest {

    @InjectMocks
    private MergeController mergeController;

    @Mock
    private MergeService mergeService;

    @Test
    public void testMergePdfs() {
        List<MultipartFile> mockPdfFiles = new ArrayList<>();
        mockPdfFiles.add(new MockMultipartFile("pdfs", "input1.pdf", "application/pdf", "mock pdf content".getBytes()));
        mockPdfFiles.add(new MockMultipartFile("pdfs", "input2.pdf", "application/pdf", "mock pdf content".getBytes()));
        byte[] mockMergedPDF = "mock merged pdf content".getBytes();
        when(mergeService.mergePDFS(anyList())).thenReturn(mockMergedPDF);

        ResponseEntity<byte[]> response = mergeController.mergePdfs(mockPdfFiles);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("inline; filename=merged.pdf", response.getHeaders().getFirst("Content-Disposition"));
        assertEquals(mockMergedPDF, response.getBody());
    }
}