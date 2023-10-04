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

        StringBuilder binaryData = new StringBuilder();
        // Binary data
        if (encrypt) {
            Scanner bitscan = new Scanner(System.in);
            System.out.print("What do you want to encrypt (ASCII)?");
            String unBinary = bitscan.nextLine();

            for (char c : unBinary.toCharArray()) {
                int val = c;
                for (int i = 0; i < 8; i++) {
                    binaryData.append((val & 128) == 0 ? 0 : 1);
                    val <<= 1;
                }
                binaryData.append(' ');
            }
        }

// Set color for encoding
        Color encodingColor;
        Color color00 = Color.BLACK;
        Color color11 = Color.WHITE;
        /*Color color01 = Color.decode("#34dc33");
        Color color10 = Color.decode("#2eb4eb");*/
        Color color01 = Color.decode("#c334eb");
        Color color10 = Color.decode("#34d2eb");

// Encode or decode
        int index = 0;
        for (int x = 0; x < width; x += gridSize) {
            for (int y = 0; y < height; y += gridSize) {
                if (index < binaryData.length()) {
                    char bit1 = binaryData.charAt(index);
                    char bit2 = '0'; // Default value if there are no more bits
                    if (index + 1 < binaryData.length()) {
                        bit2 = binaryData.charAt(index + 1);
                    }
                    String bitGroup = String.valueOf(bit1) + String.valueOf(bit2);
                    Color color;
                    if (bitGroup.equals("00")) {
                        color = color00;
                    } else if (bitGroup.equals("11")) {
                        color = color11;
                    } else if (bitGroup.equals("01")) {
                        color = color01;
                    } else if (bitGroup.equals("10")) {
                        color = color10;
                    } else {
                        color = Color.BLACK; // Default color if unexpected bit group
                    }
                    graphics.setColor(color);
                    graphics.fillRect(x, y, gridSize, gridSize);
                    index += 2;
                }
            }
        }

        StringBuilder binaryValue = new StringBuilder();
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
            try {
                // Load the black and white image
                BufferedImage image1 = ImageIO.read(new File("binary.png"));

                // Traverse each pixel of the image
                StringBuilder binaryInfo = new StringBuilder();
                for (int y = 0; y < image1.getHeight(); y++) {
                    for (int x = 0; x < image1.getWidth(); x++) {
                        Color color = new Color(image1.getRGB(x, y));
                        if (color.equals(color00)) {
                            binaryInfo.append("00");
                        } else if (color.equals(color11)) {
                            binaryInfo.append("11");
                        } else if (color.equals(color01)) {
                            binaryInfo.append("01");
                        } else if (color.equals(color10)) {
                            binaryInfo.append("10");
                        }
                    }
                }

                // Split the binary data into groups of 8
                String binaryString = binaryInfo.toString();
                binaryString = binaryString.replaceAll("(.{8})", "$1 ");

                // Print the original binary data
                System.out.println("Decoded binary data: " + binaryString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        scanner.close();
    }

}