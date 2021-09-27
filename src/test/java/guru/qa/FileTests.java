package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import net.lingala.zip4j.core.ZipFile;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


import static org.assertj.core.api.Assertions.assertThat;

public class FileTests {
    @Test
    void checkTxt() throws Exception {
        String result;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("AvrilLavigne.txt")) {
            result = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        assertThat(result).contains("I'm with you");
    }

    void checkPdf() throws Exception {
        PDF parsed = new PDF(getClass().getClassLoader().getResourceAsStream("BillieEilish.pdf"));
        assertThat(parsed.text).contains("Ocean Eyes");
    }

    @Test
    void checkXls() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("JaredLeto.xlsx")) {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(4).getCell(0).getStringCellValue())
                    .isEqualTo("Hurricane");
        }
    }

    @Test
    void checkZipWithPassword() throws Exception {
        try {
            ZipFile zip = new ZipFile("FileTests.zip");
            if (zip.isEncrypted()) {
                zip.setPassword("qaguru");
            }
            zip.extractAll("src/test/resources/UnzippedFileTests");
            assertThat(zip.getFileHeaders().get(0).toString()).contains("AvrilLavigne.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void checkDocx() throws Exception {
        try (InputStream file = getClass().getClassLoader().getResourceAsStream("Madonna.docx")) {
            XWPFDocument docfile = new XWPFDocument(file);
            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(docfile);
            String docText = xwpfWordExtractor.getText();
            assertThat(docText.contains("Hey Mister DJ"));
        }
    }
}