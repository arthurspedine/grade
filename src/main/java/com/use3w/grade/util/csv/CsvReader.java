package com.use3w.grade.util.csv;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CsvReader<T> {
    List<T> readFromCsv(MultipartFile file) throws IOException;
}
