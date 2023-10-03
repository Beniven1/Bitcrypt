import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Scanner;

public class ImageProcessor {
    public static void main(String[] args) {
        int width = 500;
        int height = 500;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Set grid color
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.BLACK);

        // Draw grid
        int gridSize = 10;
        for (int x = 0; x < width; x += gridSize) {
            graphics.drawLine(x, 0, x, height);
        }
        for (int y = 0; y < height; y += gridSize) {
            graphics.drawLine(0, y, width, y);
        }

        // Ask for encryption or decryption
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to encrypt or decrypt(not working)? (e/d): ");
        String choice = scanner.nextLine();

        // Encrypt or decrypt
        boolean encrypt = choice.equalsIgnoreCase("e");

        String binaryData = "";
        // Binary data
        if (encrypt) {
            Scanner bitscan = new Scanner(System.in);
            System.out.print("What do you want to encrypt?");
            binaryData = bitscan.nextLine();
        }

        // Set color for encoding
        Color encodingColor = encrypt ? Color.WHITE : Color.BLACK;

        // Encode or decode
        int index = 0;
        for (int x = 0; x < width; x += gridSize) {
            for (int y = 0; y < height; y += gridSize) {
                if (index < binaryData.length()) {
                    char bit = binaryData.charAt(index);
                    Color color = bit == '0' ? Color.BLACK : encodingColor;
                    graphics.setColor(color);
                    graphics.fillRect(x, y, gridSize, gridSize);
                    index++;
                }
            }
        }

        // Save or print
        if (encrypt) {
            try {
                File output = new File("binary.png");
                ImageIO.write(image, "png", output);
                System.out.println("Image saved as binary.png");
            } catch (IOException e) {
                System.out.println("Error saving image: " + e.getMessage());
            }
        } else {
            // Decrypt and print the message
            StringBuilder decryptedMessage = new StringBuilder();
            for (int x = 0; x < width; x += gridSize) {
                for (int y = 0; y < height; y += gridSize) {
                    Color color = new Color(image.getRGB(x, y));
                    if (color.equals(encodingColor)) {
                        decryptedMessage.append('1');
                    } else {
                        decryptedMessage.append('0');
                    }
                }
            }
            System.out.println("(NOT TRUE)Decrypted message: " + decryptedMessage);
        }

        scanner.close();
    }
}