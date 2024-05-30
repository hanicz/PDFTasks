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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class MergeServiceTest {

    @InjectMocks
    private MergeService mergeService;

    @Mock
    private FileService fileService;

    @Test
    public void testMergePDFS() throws IOException {
        List<MultipartFile> mockFiles = new ArrayList<>();

        mockFiles.add(this.createBlankPdf());
        mockFiles.add(this.createBlankPdf());
        when(this.fileService.isValid(any(MultipartFile.class))).thenReturn(true);

        byte[] result = this.mergeService.mergePDFS(mockFiles);

        assertNotNull(result);
    }

    @Test
    public void testMergePDFSInvalidFile() {
        List<MultipartFile> mockFiles = new ArrayList<>();
        mockFiles.add(new MockMultipartFile("files", "input1.pdf", "application/pdf", "mock pdf content".getBytes()));
        mockFiles.add(new MockMultipartFile("files", "input2.pdf", "application/pdf", "mock pdf content".getBytes()));
        when(this.fileService.isValid(any(MultipartFile.class))).thenReturn(false);

        assertThrows(FileException.class, () -> this.mergeService.mergePDFS(mockFiles));
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