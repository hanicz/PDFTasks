package pdf.tasks.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pdf.tasks.service.MergeService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/merge")
public class MergeController {

    @Autowired
    private MergeService mergeService;

    @PostMapping
    public ResponseEntity<byte[]> mergePdfs(@RequestParam("pdfs") List<MultipartFile> pdfFiles) {
        byte[] mergedPDF = this.mergeService.mergePDFS(pdfFiles);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=merged.pdf");
        return new ResponseEntity<>(mergedPDF, headers, HttpStatus.OK);
    }
}
