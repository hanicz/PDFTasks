package pdf.tasks.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pdf.tasks.exception.FileException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Service
@Slf4j
public class PageService {

    @Autowired
    private FileService fileService;

    public byte[] removePages(MultipartFile file, Integer[] pagesToDelete) {
        if (!this.fileService.isValid(file)) {
            throw new FileException("File is not valid");
        }

        Arrays.sort(pagesToDelete, Collections.reverseOrder());

        try (PDDocument pdfDoc = PDDocument.load(file.getInputStream());
             ByteArrayOutputStream removedPdf = new ByteArrayOutputStream()) {
            for (int pageNumber : pagesToDelete) {
                pdfDoc.removePage(pageNumber - 1);
            }
            pdfDoc.save(removedPdf);

            return removedPdf.toByteArray();
        } catch (IOException | IndexOutOfBoundsException e) {
            log.error("Unable to remove pages", e);
            throw new FileException("Unable to remove pages", e);
        }
    }

    public byte[] keepOnePage(MultipartFile file, int page) {
        if (!this.fileService.isValid(file)) {
            throw new FileException("File is not valid");
        }

        try (PDDocument pdfDoc = PDDocument.load(file.getInputStream());
             ByteArrayOutputStream onePagedPdf = new ByteArrayOutputStream();
             PDDocument newDoc = new PDDocument()) {

            newDoc.addPage(pdfDoc.getPage(page - 1));
            newDoc.save(onePagedPdf);
            return onePagedPdf.toByteArray();
        } catch (IOException | IndexOutOfBoundsException e) {
            log.error("Unable to remove pages", e);
            throw new FileException("Unable to remove pages", e);
        }
    }
}
