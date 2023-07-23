package thread.imageprocessing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./out/many-flowers.jpg";

    public static void main(String[] args) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        int maxNumberOfThreads = 12;
        int numberOfTest = 20;
        for (int numberOfThread = 0; numberOfThread <= maxNumberOfThreads; numberOfThread++) {
            ArrayList<Long> durations = new ArrayList<>();
            for (int i = 0; i < numberOfTest; i++) {
                long startTime = System.currentTimeMillis();
                if (numberOfThread == 0) {
                    recolorSingleThreaded(originalImage, resultImage);
                } else {
                    recolorMultiThreaded(originalImage, resultImage, numberOfThread);
                }
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                durations.add(duration);
            }
            double durationAverage = durations.stream()
                .mapToLong(d -> d)
                .average()
                .orElse(0);
            System.out.println(">>> durationAverage #" + numberOfThread + " = " + durationAverage);
        }
        ImageIO.write(resultImage, "jpg", new File(DESTINATION_FILE));
    }

    public static void recolorMultiThreaded(BufferedImage originImage, BufferedImage resultImage, int numberOfThreads) {
        ArrayList<Thread> threads = new ArrayList<>();
        int width = originImage.getWidth();
        int height = originImage.getHeight() / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            int threadMultiplier = i;
            Thread thread = new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * threadMultiplier;
                recolorImage(originImage, resultImage, leftCorner, topCorner, width, height);
            });
            threads.add(thread);
        }

        for (int i = 0; i < numberOfThreads; i++) {

        }

        threads.forEach(t -> {
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner, int width, int height) {
        for (int x = leftCorner; x < leftCorner + width /*&& x < originalImage.getWidth()*/; x++) {
            for (int y = topCorner; y < topCorner + height /*&& y < originalImage.getHeight()*/; y++) {
                recolorPixcel(originalImage, resultImage, x, y);
            }
        }
    }

    public static void recolorPixcel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);
        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed;
        int newGreen;
        int newBlue;

        if (isShadeOfGray(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        int newRgb = createRgbFromColors(newRed, newGreen, newBlue);
        setRgb(resultImage, x, y, newRgb);
    }

    public static void setRgb(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    public static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
    }

    public static int createRgbFromColors(int red, int green, int blue) {
        int rgb = 0;
        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;
        rgb |= 0xFF000000;
        return rgb;
    }

    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }
}
