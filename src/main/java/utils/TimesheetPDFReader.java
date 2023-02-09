package utils;

import model.Subject;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TimesheetPDFReader {

    private static final String SPACE = " ";

    public List<Subject> getSubject(String timesheetContent) {
        List<Subject> subjectList = new ArrayList<>();
        String[] lines = timesheetContent.split("\r\nDisciplina Segunda Terça Quarta Quinta Sexta Professor");
        if (lines.length > 0) {
            for (int i = 1; i < lines.length; i++) {
                String[] subjectsString = lines[i].split("\r\n");

                if (subjectsString.length > 0) {
                    for (int j = 0; j < subjectsString.length; j++) {
                        StringBuilder subjectContentString = new StringBuilder(subjectsString[j]);
                        while (j < subjectsString.length && !Pattern.compile("\\d").matcher(subjectContentString.toString()).find() && !subjectContentString.toString().contains("*")) {
                            subjectContentString.append(subjectsString[j + 1]);
                            j = j+1;
                        }
                        if (!subjectContentString.toString().contains("SAPOS") && !subjectContentString.toString().contains("Horário a combinar")) {
                            if ((subjectContentString.length() > 0) && !subjectContentString.toString().trim().isEmpty()) {
                                String[] subjectContentStringSplited = subjectContentString.toString().split("\\d");
                                Subject subject = new Subject();
                                if (subjectContentStringSplited.length > 1) {
                                    String subjectName = subjectContentStringSplited[0];
                                    subject.setName(subjectName);
                                    String professorName = subjectContentStringSplited[subjectContentStringSplited.length - 1];

                                    String horary = subjectContentString.substring(subjectName.length(), subjectContentString.length() - professorName.length());
                                    subject.setHorary(horary);
                                    if (professorName.startsWith(SPACE)) {
                                        professorName = professorName.substring(1);
                                    }
                                    subject.setProfessorName(professorName);
                                } else {
                                    String[] subjectWithoutHoraryContentStringSplited = subjectContentString.toString().split("\\*");
                                    String subjectName = subjectWithoutHoraryContentStringSplited[0];
                                    subject.setName(subjectName);
                                    String professorName = subjectWithoutHoraryContentStringSplited[subjectWithoutHoraryContentStringSplited.length - 1];

                                    String horary = subjectContentString.substring(subjectName.length(), subjectContentString.length() - professorName.length());
                                    subject.setHorary(horary);
                                    if (professorName.startsWith(SPACE)) {
                                        professorName = professorName.substring(1);
                                    }
                                    subject.setProfessorName(professorName);
                                }
                                subjectList.add(subject);
                                System.out.println(subjectContentString);
                            }
                        }
                    }
                }
            }
        }

        return subjectList;
    }

    public String pdfReader(String path) throws IOException {
        PDFParser parser = new PDFParser(new RandomAccessFile(new File(path), "r"));
        parser.parse();
        COSDocument cosDoc = parser.getDocument();
        PDFTextStripper pdfStripper = new PDFTextStripper();
        PDDocument pdDoc = new PDDocument(cosDoc);
        String text = pdfStripper.getText(pdDoc);
        return text;
    }
}
