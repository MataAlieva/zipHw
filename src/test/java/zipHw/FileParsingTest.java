package zipHw;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileParsingTest {
    private ClassLoader cl = FileParsingTest.class.getClassLoader();

    @Test
    void zipFileParsingTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("files.zip")
        )) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());
            }
        }
    }

    @Test
    void zipFileParsingTest2() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("files.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".csv")) {
                    break;
                }
                throw new FileNotFoundException("ERROR:CSV File - not found in zip file");
            }
            try (Reader reader = new InputStreamReader(zis); CSVReader csvReader = new CSVReader(reader)) {
                List<String[]> list = csvReader.readAll();
                Assertions.assertEquals("name", list.get(0)[0]);

            }
        }
    }

    @Test
    void zipFileParsingTest3() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("zipExample.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".pdf")) {
                    break;
                }
                throw new FileNotFoundException("ERROR:PDF File - not found in zip file");
            }
            PDF pdf = new PDF(zis);
            Assertions.assertTrue(pdf.text.contains("Инструкция"));
        }
    }

    @Test
    void zipFileParsingTest4() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("zipExample.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".xlsx")) {
                    break;
                }
                throw new FileNotFoundException("ERROR:XLSL File - not found in zip file");
            }
            XLS xls = new XLS(zis);
            Assertions.assertEquals("2092", xls.excel.getSheetAt(0).getRow(2).getCell(0).getStringCellValue());
        }
    }
}
