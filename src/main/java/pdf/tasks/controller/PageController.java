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
import pdf.tasks.service.PageService;

@RestController
@Slf4j
@RequestMapping("api/v1/page")
public class PageController {

    @Autowired
    private PageService pageService;

    @PutMapping("remove")
    public ResponseEntity<byte[]> removePages(@RequestParam("pdfFile") MultipartFile pdfFile, @RequestParam("pages") Integer[] pages) {
        byte[] removedPdf = this.pageService.removePages(pdfFile, pages);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=removed.pdf");
        return new ResponseEntity<>(removedPdf, headers, HttpStatus.OK);
    }

    @PutMapping("keep")
    public ResponseEntity<byte[]> keepOnePage(@RequestParam("pdfFile") MultipartFile pdfFile, @RequestParam("page") int page) {
        byte[] onePagedPdf = this.pageService.keepOnePage(pdfFile, page);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=one_page.pdf");
        return new ResponseEntity<>(onePagedPdf, headers, HttpStatus.OK);
    }
}
