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

@Component
class StudentCsvReader implements CsvReader<Student> {

    @Override
    public List<Student> readFromCsv(MultipartFile file) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(file.getInputStream())) {
            // ERRORS LIST
            List<String> validationErrors = new ArrayList<>();

            // CONFIG CSVTOBEAN WITH VALIDATIONS
            final int[] index = {1};
            CsvToBean<Student> csvToBean = new CsvToBeanBuilder<Student>(reader)
                    .withType(Student.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .withThrowExceptions(false) // DONT THROWS THE EXCEPTION IMMEDIATELY
                    .withFilter(line -> {
                        // VALIDATION
                        boolean isValid = true;
                        if (line[0] == null || line[0].trim().isEmpty()) {
                            validationErrors.add("Linha " + index[0] + ": RM é obrigatório");
                            isValid = false;
                        }
                        if (line[1] == null || line[1].trim().isEmpty()) {
                            validationErrors.add("Linha " + index[0] + ": Nome é obrigatório");
                            isValid = false;
                        }
                        index[0]++;
                        return isValid;
                    })
                    .build();

            // PERFORM THE READING OF THE .csv
            List<Student> students = csvToBean.parse();

            // VERIFY IF THERE WAS ANY ERROR WHEN PARSED
            List<CsvException> exceptions = csvToBean.getCapturedExceptions();
            if (exceptions != null) {
                for (CsvException exception : exceptions) {
                    validationErrors.add("Erro na linha " + exception.getLineNumber() + ": " + exception.getMessage());
                }
            }

            // IF THERE IS AN ERROR, IT THROWS THE EXCEPTION
            if (!validationErrors.isEmpty()) {
                throw new StudentCsvValidationException(validationErrors);
            }

            return students;
        }
    }
}
