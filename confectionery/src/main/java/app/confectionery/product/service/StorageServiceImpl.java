package app.confectionery.product.service;

import app.confectionery.exception.FileAlreadyExistsException;
import app.confectionery.product.model.FileData;
import app.confectionery.product.repository.FileDataRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private static final Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

    private final String FOLDER_PATH = "/Users/maciejmaksymiuk/confectionery project/confectionery_web_app_backend/confectionery/src/main/resources/static/product_images/";
    private final FileDataRepository fileDataRepository;

    @Override
    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath = FOLDER_PATH + file.getOriginalFilename();

        FileData fileData = fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath).build());

        file.transferTo(new File(filePath));

        return "file uploaded successfully : " + filePath;
    }

    @Override
    public FileData uploadImageToFileSystemAndReturnFileData(MultipartFile file) throws IOException {

        if (fileDataRepository.existsByName(file.getOriginalFilename())) {
            throw new FileAlreadyExistsException(file.getOriginalFilename());
        }

        String filePath = FOLDER_PATH + file.getOriginalFilename();

        FileData fileData = fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath).build());

        file.transferTo(new File(filePath));

        return fileData;
    }

    @Override
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        if (fileData.isPresent()) {
            File file = new File(fileData.get().getFilePath());
            if (file.exists() && !file.isDirectory()) {
                byte[] imageData = Files.readAllBytes(file.toPath());
                return imageData;
            } else {
                logger.error("File does not exist: " + fileData.get().getFilePath());
                return new byte[0]; // clearer error handling might be necessary
            }
        }
        logger.error("FileData not found for: " + fileName);
        return new byte[0];
    }

}
