package pdf.tasks.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pdf.tasks.exception.FileException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class MergeService {

    @Autowired
    private FileService fileService;

    public byte[] mergePDFS(List<MultipartFile> files) {
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();

        try (ByteArrayOutputStream mergedPDF = new ByteArrayOutputStream();) {
            for (MultipartFile file : files) {
                if(!fileService.isValid(file)) {
                    throw new FileException("Unable to merge files. One of them is not a pdf.");
                }
                pdfMergerUtility.addSource(file.getInputStream());
            }
            pdfMergerUtility.setDestinationStream(mergedPDF);
            pdfMergerUtility.mergeDocuments(null);

            return mergedPDF.toByteArray();
        } catch (IOException e) {
            log.error("Unable to merge files", e);
            throw new FileException("Unable to merge files" ,e);
        }
    }
}
