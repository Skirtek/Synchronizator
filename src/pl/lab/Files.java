package pl.lab;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Files {

    private static List<File> originalDirectoryFiles = new ArrayList<>();
    private static List<File> backupDirectoryFiles = new ArrayList<>();

    static void Synchronize() throws IOException {
        ListFiles();
        RemoveRedundantFiles();
        AddFiles(Constants.COPIED_CONTENT_PATH);
    }

    private static void ListFiles() {
        Utils.listFiles(Constants.ORIGINAL_CONTENT_PATH, originalDirectoryFiles);
        Utils.listFiles(Constants.COPIED_CONTENT_PATH, backupDirectoryFiles);
    }

    private static void AddFiles(String path) throws IOException {
        List<File> filesToAdd = originalDirectoryFiles
                .stream()
                .filter(file -> backupDirectoryFiles.stream().map(f -> f.getAbsolutePath().replace(Constants.COPIED_CONTENT_PATH, ""))
                        .noneMatch(f -> f.equals(file.getAbsolutePath().replace(Constants.ORIGINAL_CONTENT_PATH, ""))))
                .collect(Collectors.toList());

        for (File file : filesToAdd) {
            File newFile = new File(path + file.getAbsolutePath().replace(Constants.ORIGINAL_CONTENT_PATH, ""));
            java.nio.file.Files.copy(file.toPath(),
                    (newFile).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void RemoveRedundantFiles() {
        List<File> filesToRemove = backupDirectoryFiles
                .stream()
                .filter(file -> originalDirectoryFiles.stream().map(f -> f.getAbsolutePath().replace(Constants.ORIGINAL_CONTENT_PATH, ""))
                        .noneMatch(f -> f.equals(file.getAbsolutePath().replace(Constants.COPIED_CONTENT_PATH, ""))))
                .collect(Collectors.toList());
        filesToRemove.forEach(File::delete);
    }
}
