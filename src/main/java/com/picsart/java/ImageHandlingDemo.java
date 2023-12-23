package com.picsart.java;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ImageHandlingDemo {
    public static void main(String[] args) throws IOException {
        int width = 30;
        int height = 50;


        BufferedImage image = null;
        image= readFromFile(width,height,image);
        addText(image);
        BufferedImage image2 = writeToFile(image);
        addStampToPdf(image2);


    }
    public static BufferedImage readFromFile(int width, int height, BufferedImage image){

        try {
            InputStream resourceAsStream = ImageHandlingDemo.class.getClassLoader().
                    getResourceAsStream("stampImg.png");
            image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(resourceAsStream);
            System.out.println("Reading complete" + image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;

    }
    public static BufferedImage writeToFile(BufferedImage image) {

        try {
            File outputDir = new File("C:\\Users\\DELL\\IdeaProjects\\Img_PDF_Processing\\src\\main\\resources");

            File outputFile = new File(outputDir,"outputImg.png");
            if(!outputFile.exists()){
                outputFile.createNewFile();
            }
            ImageIO.write(image,"png",outputFile);
            System.out.println("Writing complete");
        } catch (IOException e) {
            System.out.println("Error:" +e.getMessage());
        }
        return image;

    }

    public static void addText(BufferedImage image){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your signature: ");
        String signature = scanner.next();
        String modifiedSignature = signature.substring(0,2);
        int fontSize = 24;
        Font font = new Font("Arial", Font.BOLD,fontSize);
        Color textColor = Color.BLACK;

        Graphics2D graphics = image.createGraphics();
        graphics.setFont(font);
        graphics.setColor(textColor);

        int textX = image.getWidth() / 2;
        int textY = image.getHeight() / 2;

        graphics.drawString(modifiedSignature, textX,textY);
        graphics.dispose();
        System.out.println("Text added to the image");

    }

    public  static void addStampToPdf(BufferedImage stampedImage) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the source pdf path:");
        String inputPdfPath = scanner.next();
        System.out.println("Enter the destination pdf path:");
        String outputPdfPath = scanner.next();

        try(PDDocument document = PDDocument.load(new File(inputPdfPath))){
            int pageNum = 0;
            for(PDPage page : document.getPages()){
                pageNum++;
                PDImageXObject imageXObject = PDImageXObject.createFromFileByContent(
                       new File("src/main/resources/outputImg.png"), document);

                try(PDPageContentStream contentStream = new PDPageContentStream(document,page, PDPageContentStream.AppendMode.APPEND,true)){
                    float x = 15;
                    float y = 15;
                    contentStream.drawImage(imageXObject, x, y);
                }
            }
            document.save(outputPdfPath);
            System.out.println("Stamped PDF created successfully!");
        }
        }

}






