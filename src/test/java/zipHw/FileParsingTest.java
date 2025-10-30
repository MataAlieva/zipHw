package zipHw;

import com.opencsv.CSVReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
            boolean csv = false;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".csv")) {
                    csv = true;
                    try (Reader reader = new InputStreamReader(zis);
                         CSVReader csvReader = new CSVReader(reader)) {
                        List<String[]> list = csvReader.readAll();
                        Assertions.assertEquals("name", list.get(0)[0]);
                    }
                    break;
                }
            }
            if (!csv) {
                throw new FileNotFoundException("ERROR:CSV File - not found in zip file");
            }
        }
    }

    @Test
    void zipFileParsingTest3() throws Exception {
        ClassLoader cl = this.getClass().getClassLoader();
        try (InputStream resourceStream = cl.getResourceAsStream("zipExample.zip")) {
            if (resourceStream == null) {
                throw new FileNotFoundException("ERROR:zipExample File not found in resources");
            }
            try (ZipInputStream zis = new ZipInputStream(resourceStream)) {
                ZipEntry entry;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                boolean pdfFound = false;

            while ((entry = zis.getNextEntry()) != null) {
                 if (entry.getName().endsWith(".pdf")) {
                     byte[] buffer = new byte[1024];
                     int len;
                     while ((len = zis.read(buffer)) > 0) {
                         baos.write(buffer, 0, len);
                     }
                     pdfFound = true;
                     break;
                 }
                }
                if (!pdfFound) {
                    throw new FileNotFoundException("ERROR:PDF File - not found in zip file");
                }
                try (PDDocument document = PDDocument.load(new ByteArrayInputStream(baos.toByteArray()))) {
                    PDFTextStripper stripper = new PDFTextStripper();
                    String pdfText = stripper.getText(document);
                    Assertions.assertTrue(pdfText.contains("1"));

                }
            }
        }
    }
    @Test
    void zipFileParsingTest4() throws Exception {
        ClassLoader cl = this.getClass().getClassLoader();
        try (InputStream resourceStream = cl.getResourceAsStream("zipExample.zip")) {
            if (resourceStream == null) {
                throw new FileNotFoundException("ERROR:zipExample File not found in resources");
            }
            try (ZipInputStream zis = new ZipInputStream(resourceStream)) {
                ZipEntry entry;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                boolean xlsxFound = false;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().endsWith(".xlsx")) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            baos.write(buffer, 0,len);
                        }
                        xlsxFound = true;
                        break;
                    }
                }
                if (!xlsxFound){
                    throw new FileNotFoundException("ERROR:xlsx File - not found in zip file");
                }
                try(Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(baos.toByteArray()))) {
                    String cellValue = workbook.getSheetAt(0).getRow(2).getCell(0).getStringCellValue();
                    Assertions.assertEquals("2092", cellValue);
                }
            }
        }
    }
}
