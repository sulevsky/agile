package org.sulevsky;

public class Main {
    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Usage: pathToOriginal pathToCopy pathToResult");
            System.exit(1);
        }

        String pathToOriginal = args[0];
        String pathToCopy = args[1];
        String pathToResult = args[2];
        ImageProcessor imageProcessor = new ImageProcessor();
        imageProcessor.processImages(pathToOriginal, pathToCopy, pathToResult);
    }
}
