import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FontSizeAndPositionAnalyzer {

    public static void analyzeFontSizesAndPositions(File pdfFile) throws IOException {
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            PDFTextStripper pdfStripper = new PDFTextStripper() {
                @Override
                protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                    StringBuilder wordBuilder = new StringBuilder();
                    List<TextPosition> wordPositions = new ArrayList<>();
                    float lastX = -1; // letzte X-Position
                    float threshold = 2.5f; // Abstandsschwelle zwischen Zeichen

                    for (TextPosition textPosition : textPositions) {
                        float currentX = textPosition.getXDirAdj();
                        if (lastX != -1 && (currentX - lastX > threshold)) {
                            // Neues Wort erkannt, daher ausgeben
                            printWord(wordBuilder.toString(), wordPositions);
                            wordBuilder.setLength(0); // Wort zurücksetzen
                            wordPositions.clear();
                        }
                        wordBuilder.append(textPosition.getUnicode());
                        wordPositions.add(textPosition);
                        lastX = currentX + textPosition.getWidthDirAdj();
                    }

                    // Letztes Wort ausgeben
                    if (wordBuilder.length() > 0) {
                        printWord(wordBuilder.toString(), wordPositions);
                    }
                }

                private void printWord(String word, List<TextPosition> positions) {
                    if (positions.isEmpty()) return;
                    float fontSize = positions.get(0).getFontSizeInPt(); // Schriftgröße des ersten Zeichens
                    float yPosition = positions.get(0).getYDirAdj(); // Y-Position des ersten Zeichens
                    System.out.println("Größe: " + fontSize + "pt, " + "Y-Position: " + yPosition + " - Wort: " + word);
                }
            };

            pdfStripper.setSortByPosition(true); // Sortiere nach Position
            pdfStripper.getText(document); // Verarbeite das PDF-Dokument
        }
    }

    public static void main(String[] args) {
        File pdfFile = new File("src\\PdfToTxt\\Pdf\\Classroom of the Elite (Light Novel) Vol. 2.pdf");
        try {
            analyzeFontSizesAndPositions(pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
