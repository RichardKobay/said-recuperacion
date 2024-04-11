package org.example;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Objects;

public class PDF {
    public void generatePDF(Professor[] professors) throws FileNotFoundException {

        URL resourceUrl;
        String absolutePath;

        resourceUrl = getClass().getResource("/nominas.pdf");
        absolutePath = Objects.requireNonNull(resourceUrl).getPath();

        PdfDocument pdf = new PdfDocument(new PdfWriter(absolutePath));
        Document document = new Document(pdf);

        Table table = new Table(9);

        table.addHeaderCell("Name");
        table.addHeaderCell("ITI Hours");
        table.addHeaderCell("IM Hours");
        table.addHeaderCell("ITM Hours");
        table.addHeaderCell("ISA Hours");
        table.addHeaderCell("LAyGE Hours");
        table.addHeaderCell("Comercio Hours");
        table.addHeaderCell("Total Hours");
        table.addHeaderCell("Total Payment");

        for (Professor professor : professors) {
            table.addCell(new Cell().add(new Paragraph(professor.getName())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(professor.getITIHours()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(professor.getIMHours()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(professor.getITMHours()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(professor.getISAHours()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(professor.getLAyGEHours()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(professor.getComercioHours()))));

            int totalHours = professor.getITIHours() + professor.getIMHours() +
                    professor.getITMHours() + professor.getISAHours() +
                    professor.getLAyGEHours() + professor.getComercioHours();
            table.addCell(new Cell().add(new Paragraph(String.valueOf(totalHours))));

            BigDecimal totalPayment = BigDecimal.valueOf(totalHours).multiply(BigDecimal.valueOf(100));
            table.addCell(new Cell().add(new Paragraph(totalPayment.setScale(2, RoundingMode.HALF_UP).toString())));
        }

        document.add(table);
        document.close();
    }
}
