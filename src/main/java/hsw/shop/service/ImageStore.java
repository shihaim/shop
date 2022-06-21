package hsw.shop.service;

import hsw.shop.domain.ProductImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
public class ImageStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public ProductImage storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();

        String storeFileName = createStoreFileName(originalFilename);

        File folder = new File(fileDir);

        if (!folder.exists()) {
            try {
                log.info("folder mkdir");
                folder.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return ProductImage.createProductImage(originalFilename, storeFileName);
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
