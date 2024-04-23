package app.confectionery.product.service;

import app.confectionery.product.model.FileData;
import app.confectionery.product.repository.FileDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

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
        String filePath = "";
        if (fileData.isPresent()) {
            filePath = fileData.get().getFilePath();
        }

        return Files.readAllBytes(
                new File(filePath).toPath());
    }

}
