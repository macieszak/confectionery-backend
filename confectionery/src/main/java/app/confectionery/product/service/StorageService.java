package app.confectionery.product.service;

import app.confectionery.product.model.FileData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    String uploadImageToFileSystem(MultipartFile file) throws IOException;

    FileData uploadImageToFileSystemAndReturnFileData(MultipartFile file) throws IOException;

    byte[] downloadImageFromFileSystem(String fileName) throws IOException;

    FileData uploadImageToFileSystemAndReturnFileData(MultipartFile file, Long productId) throws IOException;

}
