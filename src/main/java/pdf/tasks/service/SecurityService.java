package pdf.tasks.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pdf.tasks.exception.FileException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class SecurityService {

    @Autowired
    private FileService fileService;

    public byte[] removePassword(MultipartFile file, String password) {
        if (!this.fileService.isValid(file)) {
            throw new FileException("File is not valid");
        }

        try (PDDocument pdfDoc = PDDocument.load(file.getInputStream(), password);
             ByteArrayOutputStream decryptedPdf = new ByteArrayOutputStream()) {
            pdfDoc.setAllSecurityToBeRemoved(true);
            pdfDoc.save(decryptedPdf);
            return decryptedPdf.toByteArray();
        } catch (IOException e) {
            log.error("Unable to remove password", e);
            throw new FileException("Unable to remove password", e);
        }
    }

    public byte[] addPassword(MultipartFile file, String password) {
        if (!this.fileService.isValid(file)) {
            throw new FileException("File is not valid");
        }

        try (PDDocument pdfDoc = PDDocument.load(file.getInputStream());
             ByteArrayOutputStream decryptedPdf = new ByteArrayOutputStream()) {
            pdfDoc.protect(this.createProtectionPolicy(password));
            pdfDoc.save(decryptedPdf);
            return decryptedPdf.toByteArray();
        } catch (IOException e) {
            log.error("Unable to add password", e);
            throw new FileException("Unable to add password", e);
        }
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
