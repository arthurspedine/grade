package com.use3w.grade.util.pdf;

import com.use3w.grade.dto.AssessmentAnswerDTO;
import com.use3w.grade.dto.CategoryAnswerDTO;
import com.use3w.grade.dto.StudentEvaluationPdfDTO;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class StudentEvaluationPdfWriter implements PdfWriter<StudentEvaluationPdfDTO> {

    private static final float MARGIN = 50;
    private static final float FONT_SIZE_TITLE = 18;
    private static final float FONT_SIZE_SUBTITLE = 14;
    private static final float FONT_SIZE_NORMAL = 11;
    private static final float LINE_HEIGHT = 15;

    @Override
    public byte[] write(StudentEvaluationPdfDTO data) {
        try (PDDocument document = new PDDocument()) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float yPosition = page.getMediaBox().getHeight() - MARGIN;

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                yPosition = drawHeader(cs, data, yPosition);
                yPosition -= 30;

                yPosition = drawStudentInfo(cs, data, yPosition);
                yPosition -= 25;

                yPosition = drawScoresByQuestion(cs, data, yPosition, page, document);

                if (yPosition < 200) {
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    yPosition = page.getMediaBox().getHeight() - MARGIN;
                    cs.close();

                    try (PDPageContentStream newStream = new PDPageContentStream(document, page)) {
                        yPosition = drawFeedback(newStream, data, yPosition);
                    }
                } else {
                    yPosition = drawFeedback(cs, data, yPosition);
                }
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    private float drawHeader(PDPageContentStream stream, StudentEvaluationPdfDTO data, float y) throws IOException {
        stream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_TITLE);
        stream.beginText();
        stream.newLineAtOffset(MARGIN, y);
        stream.showText("Avaliação do Estudante");
        stream.endText();

        y -= LINE_HEIGHT + 5;
        stream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_NORMAL);
        stream.beginText();
        stream.newLineAtOffset(MARGIN, y);
        stream.showText("Avaliação: " + data.student().assessmentName());
        stream.endText();

        y -= LINE_HEIGHT;
        stream.beginText();
        stream.newLineAtOffset(MARGIN, y);
        stream.showText("Turma: " + data.student().className());
        stream.endText();

        return y;
    }

    private float drawStudentInfo(PDPageContentStream stream, StudentEvaluationPdfDTO data, float y) throws IOException {
        stream.setNonStrokingColor(new Color(240, 240, 240));
        stream.addRect(MARGIN, y - 45, 495, 50);
        stream.fill();

        stream.setNonStrokingColor(Color.BLACK);
        stream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_SUBTITLE);
        stream.beginText();
        stream.newLineAtOffset(MARGIN + 10, y - 15);
        stream.showText("Estudante: " + data.student().name());
        stream.endText();

        stream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
        stream.beginText();
        stream.newLineAtOffset(MARGIN + 10, y - 37);
        stream.showText("RM: " + data.student().rm() + " | Nota: " + data.totalScore());
        stream.endText();

        return y - 50;
    }

    private float drawScoresByQuestion(PDPageContentStream stream, StudentEvaluationPdfDTO data, float y, PDPage page, PDDocument document) throws IOException {
        stream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_SUBTITLE);
        stream.beginText();
        stream.newLineAtOffset(MARGIN, y);
        stream.showText("Desempenho por Questão:");
        stream.endText();

        y -= LINE_HEIGHT + 10;

        for (AssessmentAnswerDTO answer : data.answers()) {
            if (y < 150) {
                stream.close();
                PDPage newPage = new PDPage(PDRectangle.A4);
                document.addPage(newPage);
                y = newPage.getMediaBox().getHeight() - MARGIN;

                PDPageContentStream newSteam = new PDPageContentStream(document, newPage);
                return drawScoresByQuestion(newSteam, data, y, newPage, document);
            }
            stream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_NORMAL);
            stream.beginText();
            stream.newLineAtOffset(MARGIN, y);
            stream.showText("Questão " + answer.questionNumber());
            stream.endText();

            y -= LINE_HEIGHT + 5;

            stream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
            for (CategoryAnswerDTO category : answer.categories()) {
                double percentage = (category.answeredScore() / category.score()) * 100;
                stream.beginText();
                stream.newLineAtOffset(MARGIN + 20, y);
                stream.showText(String.format("• %s: %.1f / %.1f (%.0f%%)",
                        category.name(),
                        category.answeredScore(),
                        category.score(),
                        percentage));
                stream.endText();

                float barWidth = 150;
                float barHeight = 8;
                float barX = page.getMediaBox().getWidth() - MARGIN - barWidth - 20;
                float barY = y - 2;

                // background
                stream.setNonStrokingColor(new Color(220, 220, 220));
                stream.addRect(barX, barY, barWidth, barHeight);
                stream.fill();

                // progress
                Color progressColor = percentage >= 70 ? new Color(76, 175, 80) :
                        percentage >= 50 ? new Color(255, 152, 0) :
                                new Color(244, 67, 54);
                stream.setNonStrokingColor(progressColor);
                stream.addRect(barX, barY, (float) (barWidth * (percentage / 100)), barHeight);
                stream.fill();

                stream.setNonStrokingColor(Color.BLACK);

                y -= LINE_HEIGHT;
            }
            y -= 10;
        }
        return y;
    }

    private float drawFeedback(PDPageContentStream stream, StudentEvaluationPdfDTO data, float y) throws IOException {
        if (data.finalFeedback() == null || data.finalFeedback().isEmpty()) {
            return y;
        }

        y -= 15;

        stream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_SUBTITLE);
        stream.beginText();
        stream.newLineAtOffset(MARGIN, y);
        stream.showText("Feedback");
        stream.endText();

        y -= LINE_HEIGHT + 10;

        String[] lines = wrapText(data.finalFeedback(), 495, FONT_SIZE_NORMAL);
        int actualLineCount = lines.length;
        while (actualLineCount > 0 && lines[actualLineCount - 1].trim().isEmpty()) {
            actualLineCount--;
        }
        float boxHeight = (lines.length * LINE_HEIGHT) + 20;

        stream.setNonStrokingColor(new Color(250, 250, 250));
        stream.addRect(MARGIN, y - boxHeight + 10, 495, boxHeight);
        stream.fill();

        stream.setNonStrokingColor(Color.BLACK);
        stream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);

        y -= 10;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                y -= LINE_HEIGHT * 0.5f;
            } else {
                stream.beginText();
                stream.newLineAtOffset(MARGIN + 10, y);
                stream.showText(line);
                stream.endText();
                y -= LINE_HEIGHT;
            }
        }

        return y;
    }

    private String[] wrapText(String text, float maxWidth, float fontSize) throws IOException {
        PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        // Split by double line breaks to preserve paragraphs
        String[] paragraphs = text.split("\n\n");
        List<String> allLines = new ArrayList<>();

        for (int p = 0; p < paragraphs.length; p++) {
            String paragraph = paragraphs[p].trim();

            if (paragraph.isEmpty()) {
                continue;
            }

            // Remove single line breaks within paragraph (join it back)
            paragraph = paragraph.replace("\n", " ");

            String[] words = paragraph.split("\\s+");
            StringBuilder line = new StringBuilder();

            for (String word : words) {
                String testLine = line.isEmpty() ? word : line + " " + word;

                try {
                    float width = font.getStringWidth(testLine) / 1000 * fontSize;

                    if (width > maxWidth - 20) {
                        if (!line.isEmpty()) {
                            allLines.add(line.toString());
                            line = new StringBuilder(word);
                        } else {
                            // Word is too long, add it anyway
                            allLines.add(word);
                        }
                    } else {
                        if (!line.isEmpty()) line.append(" ");
                        line.append(word);
                    }
                } catch (Exception e) {
                    if (!line.isEmpty()) line.append(" ");
                    line.append(word);
                }
            }

            if (!line.isEmpty()) {
                allLines.add(line.toString());
            }

            // Add empty line between paragraphs (except for last paragraph)
            if (p < paragraphs.length - 1) {
                allLines.add("");
            }
        }

        return allLines.toArray(new String[0]);
    }
}
