package app.confectionery.exception;

public class FileAlreadyExistsException extends RuntimeException {
    public FileAlreadyExistsException(String filename) {
        super("File with name '" + filename + "' already exists.");
    }
}
