package pl.lab;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final String ORIGINAL_CONTENT_PATH = "C:\\Users\\bmroz\\Desktop\\JS";
    private static final String COPIED_CONTENT_PATH = "C:\\Users\\bmroz\\Desktop\\JS2";

    public static void main(String[] args) {

        List<File> tururu = new ArrayList<>();
        listf(ORIGINAL_CONTENT_PATH, tururu);
        tururu.forEach(x -> System.out.println(x.getAbsolutePath().replace(ORIGINAL_CONTENT_PATH+"\\", "")));

        Runnable sync = () -> {
            try (Stream<Path> directory = Files.walk(Paths.get(ORIGINAL_CONTENT_PATH));
                 Stream<Path> anotherDirectory = Files.walk(Paths.get(COPIED_CONTENT_PATH))) {
                List<File> original = directory
                        .filter(Files::isRegularFile)
                        .map(Path::toFile)
                        .collect(Collectors.toList());

                List<File> copy = anotherDirectory
                        .filter(Files::isRegularFile)
                        .map(Path::toFile)
                        .collect(Collectors.toList());

                List<File> filesToAdd = original
                        .stream()
                        .filter(file -> copy.stream().map(File::getName).noneMatch(f -> f.equals(file.getName())))
                        .collect(Collectors.toList());

                List<File> filesToRemove = copy
                        .stream()
                        .filter(file -> original.stream().map(File::getName).noneMatch(f -> f.equals(file.getName())))
                        .collect(Collectors.toList());

                //AddFiles(filesToAdd, COPIED_CONTENT_PATH+"\\");
                //RemoveRedundantFiles(filesToRemove);

            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(sync, 0, 1, TimeUnit.SECONDS);
    }

    private static void AddFiles(List<File> files, String path) throws IOException {
        for (File file : files) {
            Files.copy(file.toPath(),
                    (new File(path + file.getName())).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void RemoveRedundantFiles(List<File> files) {
        files.forEach(File::delete);
    }

    private static void listf(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        if (fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    listf(file.getAbsolutePath(), files);
                }
            }
    }
}