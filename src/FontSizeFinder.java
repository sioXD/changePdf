import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FontSizeFinder {

    public static void findAllFontSizes(File pdfFile) throws IOException {
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            Set<Float> fontSizes = new HashSet<>(); // Einzigartige Schriftgrößen sammeln

            PDFTextStripper pdfStripper = new PDFTextStripper() {
                @Override
                protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                    for (TextPosition textPosition : textPositions) {
                        fontSizes.add(textPosition.getFontSizeInPt()); // Schriftgröße sammeln
                    }
                }
            };

            pdfStripper.setSortByPosition(true); // Text basierend auf der Position sortieren
            pdfStripper.getText(document); // Dokument analysieren

            // Ausgabe der gefundenen Schriftgrößen
            System.out.println("Gefundene Schriftgrößen im Dokument:");
            for (Float fontSize : fontSizes) {
                System.out.println(fontSize + " pt");
            }
        }
    }

    public static void main(String[] args) {
        File pdfFile = new File("src/PdfToTxt/Pdf/Classroom of the Elite (Light Novel) Vol. 1.pdf");
        try {
            findAllFontSizes(pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

