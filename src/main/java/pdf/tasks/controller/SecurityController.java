package pdf.tasks.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pdf.tasks.service.SecurityService;

@RestController
@Slf4j
@RequestMapping("api/v1/security")
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @PutMapping("remove")
    public ResponseEntity<byte[]> removePassword(@RequestParam("pdfFile") MultipartFile pdfFile, @RequestParam("password") String password) {
        byte[] onePagedPdf = this.securityService.removePassword(pdfFile, password);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=no_password.pdf");
        return new ResponseEntity<>(onePagedPdf, headers, HttpStatus.OK);
    }

    @PutMapping("protect")
    public ResponseEntity<byte[]> addPassword(@RequestParam("pdfFile") MultipartFile pdfFile, @RequestParam("password") String password) {
        byte[] onePagedPdf = this.securityService.addPassword(pdfFile, password);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=protected.pdf");
        return new ResponseEntity<>(onePagedPdf, headers, HttpStatus.OK);
    }
}
