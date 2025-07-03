package com.use3w.grade.util.csv;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.use3w.grade.infra.exception.StudentCsvValidationException;
import com.use3w.grade.model.Student;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
class StudentCsvReader implements CsvReader<Student> {

    @Override
    public List<Student> readFromCsv(MultipartFile file) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(file.getInputStream())) {
            // ERRORS LIST
            List<String> validationErrors = new ArrayList<>();
            AtomicInteger lineNumber = new AtomicInteger(1);

            // CONFIG CSVTOBEAN WITH VALIDATIONS
            CsvToBean<Student> csvToBean = new CsvToBeanBuilder<Student>(reader)
                    .withType(Student.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .withThrowExceptions(false) // DONT THROWS THE EXCEPTION IMMEDIATELY
                    .withFilter(line -> {
                        // VALIDATION
                        boolean isValid = true;
                        if (line[0] == null || line[0].trim().isEmpty()) {
                            validationErrors.add(String.format("Linha %d: RM é obrigatório", lineNumber.get()));
                            isValid = false;
                        }
                        if (line[1] == null || line[1].trim().isEmpty()) {
                            validationErrors.add(String.format("Linha %d: Nome é obrigatório", lineNumber.get()));
                            isValid = false;
                        }
                        lineNumber.incrementAndGet();
                        return isValid;
                    })
                    .build();

            // PERFORM THE READING OF THE .csv
            List<Student> students = csvToBean.parse();

            // VERIFY IF THERE WAS ANY ERROR WHEN PARSED
            List<CsvException> exceptions = csvToBean.getCapturedExceptions();
            if (exceptions != null && !exceptions.isEmpty()) {
                exceptions.forEach(e ->
                        validationErrors.add(String.format("Erro na linha %d: %s",
                                e.getLineNumber(), e.getMessage()))
                );
            }

            // IF THERE IS AN ERROR, IT THROWS THE EXCEPTION
            if (!validationErrors.isEmpty()) {
                throw new StudentCsvValidationException(validationErrors);
            }

            return students;
        }
    }
}
