package com.use3w.grade.util.pdf;

public interface PdfWriter<T> {
    byte[] write(T data);
}
