package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import net.lingala.zip4j.core.ZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class SelenideFilesTest {

    @Test
    void txtFileTest() throws Exception {
        String result;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("test.txt")) {
            result = new String(stream.readAllBytes(), "UTF-8");
        }
        assertThat(result).contains("Взволнованно ходили вы по комнате");
    }

    @Test
    void pdfFileTest() throws Exception {
        PDF parsed = new PDF(getClass().getClassLoader().getResourceAsStream("test.pdf"));
        assertThat(parsed.text).contains("Шевченко Виталий Юрьевич");
    }

    @Test
    void xlsxFileTest() throws Exception {
        XLS parsed = new XLS(getClass().getClassLoader().getResourceAsStream("test.xlsx"));
        assertThat(parsed.excel.getSheetAt(0).getRow(17).getCell(5).getStringCellValue())
                .isEqualTo("Агро-Авто");
    }

    @Test
    void zipWithPasswordFileTest() throws Exception {
        ZipFile zipFile = new ZipFile("./src/test/resources/test.zip");
        if (zipFile.isEncrypted()) {
            zipFile.setPassword("qwerty");
        }
        zipFile.extractAll("./src/test/resources/extractzip/");
        String result;
        try (FileInputStream stream = new FileInputStream("./src/test/resources/extractzip/test.txt")) {
            result = new String(stream.readAllBytes(), "UTF-8");
        }
        assertThat(result).contains("hello");
    }

    @Test
    void docxFileTest() throws Exception {
        String text;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("test.docx")) {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(stream);
            text = wordMLPackage.getMainDocumentPart().getContent().toString();
        }
        assertThat(text).contains("Взволнованно ходили вы по комнате");
    }
}