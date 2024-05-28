package application.imageeditor;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class ImageEditorController {
    @FXML
    private ImageView imageView;
    @FXML
    private Slider slider;
    static boolean fileIsSaved = false;
    static String path;
    static String extension;
    static BufferedImage mainImage;

    @FXML
    private void initialize() {
        path = "C:\\Users\\zaids\\Downloads\\Pictures\\gigachad emoji.jpg";
        extension = "jpg";
        File file = new File(path);
        try {
            mainImage = ImageIO.read(file);
            imageView.setImage(getImage(mainImage));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        slider.valueProperty().addListener((observableValue, number, t1) -> {
            mainImage = changeBrightness(mainImage, (t1.intValue() - number.intValue()));
            imageView.setImage(getImage(mainImage));
            fileIsSaved = false;
        });
    }

    private static BufferedImage changeBrightness(BufferedImage inputImage, int value) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color pixel = new Color(inputImage.getRGB(j, i));
                int[] rgb = {pixel.getRed(), pixel.getBlue(), pixel.getGreen()};
                for (int k=0; k<rgb.length; k++) {
                    rgb[k] += value;
                    if (rgb[k] < 0) rgb[k] = 0;
                    if (rgb[k] > 255) rgb[k] = 255;

                }
                Color newPixel = new Color(rgb[0], rgb[1], rgb[2]);
                outputImage.setRGB(j, i, newPixel.getRGB());
            }
        }

        return outputImage;
    }

    private Image getImage(BufferedImage img){
        //converting to a good type, read about types here: https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        newImg.createGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);

        //converting the BufferedImage to an IntBuffer
        int[] type_int_argb = ((DataBufferInt) newImg.getRaster().getDataBuffer()).getData();
        IntBuffer buffer = IntBuffer.wrap(type_int_argb);

        //converting the IntBuffer to an Image, read more about it here: https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
        PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
        PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(newImg.getWidth(), newImg.getHeight(), buffer, pixelFormat);
        return new WritableImage(pixelBuffer);
    }

    @FXML
    protected void grayScale() {
        mainImage = convertToGrayScale(mainImage);
        imageView.setImage(getImage(mainImage));
        fileIsSaved = false;
    }

    private static BufferedImage convertToGrayScale(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(j, i));
            }
        }
        return outputImage;
    }

    @FXML
    protected void rotateLeft() {
        mainImage = rotateImage(mainImage, "left");
        imageView.setImage(getImage(mainImage));
        fileIsSaved = false;
    }

    @FXML
    protected void rotateRight() {
        mainImage = rotateImage(mainImage, "right");
        imageView.setImage(getImage(mainImage));
        fileIsSaved = false;
    }

    private static BufferedImage rotateImage(BufferedImage inputImage, String direction) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);

        int y;
        if (direction.equals("right")) {
            int x = height - 1;
            for (int i = 0; i < height; i++) {
                y = 0;
                for (int j = 0; j < width; j++) {
                    outputImage.setRGB(x, y, inputImage.getRGB(j, i));
                    y++;
                }
                x--;
            }
        } else {
            int x = 0;
            for (int i = 0; i < height; i++) {
                y = width - 1;
                for (int j = 0; j < width; j++) {
                    outputImage.setRGB(x, y, inputImage.getRGB(j, i));
                    y--;
                }
                x++;
            }
        }
        return outputImage;
    }

    public void flipHorizontal() {
        mainImage = flipImage(mainImage, "horizontal");
        imageView.setImage(getImage(mainImage));
        fileIsSaved = false;
    }

    public void flipVertical() {
        mainImage = flipImage(mainImage, "vertical");
        imageView.setImage((getImage(mainImage)));
        fileIsSaved = false;
    }

    private static BufferedImage flipImage(BufferedImage inputImage, String direction) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        if (direction.equals("horizontal")) {
            int x;
            for (int i = 0; i < height; i++) {
                x = width - 1;
                for (int j = 0; j < width; j++) {
                    outputImage.setRGB(x, i, inputImage.getRGB(j, i));
                    x--;
                }
            }
        } else {
            int y = height - 1;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    outputImage.setRGB(j, y, inputImage.getRGB(j, i));
                }
                y--;
            }
        }

        return outputImage;
    }

    public void blur() {
        mainImage = blurImage(mainImage);
        imageView.setImage((getImage(mainImage)));
        fileIsSaved = false;
    }

    private static BufferedImage blurImage(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int value = inputImage.getWidth()/32;
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        for (int i = 0; i < height/value; i++) {
            for (int j = 0; j < width/value; j++) {
                ArrayList<Color> pixels = new ArrayList<>();
                for (int k = i*value; k < (i*value)+value; k++) {
                    for (int l = j*value; l < (j*value)+value; l++) {
                        pixels.add(new Color(inputImage.getRGB(l, k)));
                    }
                }
                int[] rgb = {0, 0, 0};
                for (Color pixel : pixels) {
                    rgb[0] += pixel.getRed();
                    rgb[1] += pixel.getGreen();
                    rgb[2] += pixel.getBlue();
                }
                rgb[0] /= pixels.size();
                rgb[1] /= pixels.size();
                rgb[2] /= pixels.size();
                Color newPixel = new Color(rgb[0], rgb[1], rgb[2]);
                for (int k = i*value; k < (i*value)+value; k++) {
                    for (int l = j*value; l < (j*value)+value; l++) {
                        outputImage.setRGB(l, k, newPixel.getRGB());
                    }
                }
            }
        }

        return outputImage;
    }

    public void menuOpenClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.gif", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            slider.setValue(50);
            String fileName = file.getName();
            extension = fileName.substring(fileName.lastIndexOf(".") + 1, file.getName().length());
            path = file.getAbsolutePath();
            fileIsSaved = true;
            File file2 = new File(path);
            try {
                mainImage = ImageIO.read(file2);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageView.setImage(getImage(mainImage));
        }
    }

    public void saveFile() {
        File file = new File(path);
        try {
            ImageIO.write(mainImage, extension, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void menuSaveClicked() {
        saveFile();
    }

    public void menuSaveAsClicked() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(extension.toUpperCase(), "*." + extension));
        Path newPath = Paths.get(path);
        fileChooser.setInitialFileName(newPath.getFileName().toString());
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            file.createNewFile();
            path = file.getAbsolutePath();
            saveFile();
            fileIsSaved = true;
        }
    }

    public void menuExitClicked() {
        if (fileIsSaved) {
            ImageEditor.closeWindow();
        } else {
            ExitDialog exitDialog = new ExitDialog();
            String action = exitDialog.showDialog("Save changes before exiting?");
            if (action.equals("Yes")) {
                saveFile();
                fileIsSaved = true;
                ImageEditor.closeWindow();
            } else if (action.equals("No")) {
                ImageEditor.closeWindow();
            }
        }
    }
}