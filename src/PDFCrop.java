import java.io.File;

public class PDFCrop {
    public static void main(String[] args) throws Exception{


        IllustrationChanger Illu = new IllustrationChanger();
        Illu.IllustrationChange(new File("src/IllustrationChanger/PDF_Files"), new File("src/IllustrationChanger/Cropped_Files"));

        PdfToTxt PTT = new PdfToTxt();
        PTT.ToTxt(new File("src/PdfToTxt/Pdf"), new File("src/PdfToTxt/Txt"));



        System.out.println("\nFinished! :)");
    }//EOF
}//EOC