package pdf.tasks.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    public boolean isValid(MultipartFile file) {
        return "pdf".equals(FilenameUtils.getExtension(file.getOriginalFilename()));
    }
}
