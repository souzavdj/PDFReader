import model.Subject;
import utils.TimesheetPDFReader;

import java.io.IOException;
import java.util.List;

public class main {

    public static void main(String[] args) throws IOException {
        TimesheetPDFReader reader = new TimesheetPDFReader();

        String pdfReaderContent = reader.pdfReader("src/main/resources/pdf/Quadro de Horários (2021_2).pdf");
        List<Subject> subjects = reader.getSubject(pdfReaderContent);
        System.out.println("Finished");


    }

}
