// Apache PDFBox
// https://pdfbox.apache.org/download.cgi
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Pdftoimg_20180709_Backup {

    private static final String OUTPUT_DIR = "D:\\Dropbox\\PDF\\";

    public static void main(String[] args) throws Exception{

        try (final PDDocument document = PDDocument.load(new File("D:\\Dropbox\\PDF\\ABL생명.pdf"))){
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page)
            {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                String fileName = OUTPUT_DIR + "image-" + page + ".png";
                ImageIOUtil.writeImage(bim, fileName, 300);
            }
            document.close();
        } catch (IOException e){
            System.err.println("Exception while trying to create pdf document - " + e);
        }
        
        
        
        
        // overlay settings
        String text = "한글 입력 테스트";
        File input = new File("D:\\Dropbox\\PDF\\image-0.png");
        File output = new File("D:\\Dropbox\\PDF\\image-0_added.png");

        // adding text as overlay to an image
        addTextWatermark(text, "png", input, output);
        
        
        System.out.println("입력 완료");
    }
    
    
    

    private static void addTextWatermark(String text, String type, File source, File destination) throws IOException {
        BufferedImage image = ImageIO.read(source);

        // determine image type and handle correct transparency
        int imageType = "png".equalsIgnoreCase(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage watermarked = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

        // initializes necessary graphic properties
        Graphics2D w = (Graphics2D) watermarked.getGraphics();
        w.drawImage(image, 0, 0, null);
        //AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        //w.setComposite(alphaChannel);
        w.setColor(Color.BLACK);
        w.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        java.awt.FontMetrics fontMetrics = w.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(text, w);

        // calculate center of the image
        //int centerX = (image.getWidth() - (int) rect.getWidth()) / 2;
        //int centerY = image.getHeight() / 2;

        // add text overlay to the image
        //w.drawString(text, centerX, centerY);
        w.drawString(text, 600, 430);
        ImageIO.write(watermarked, type, destination);
        w.dispose();
    }
}