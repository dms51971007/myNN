package org.myNeuroV2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class MainNN {
    static Logger l =  LoggerFactory.getLogger(MainNN.class);
    static int[] numDataDigits = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

    static double[][] numData = new double[][]{
            {-1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, -1.0},
            {1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0},
            {1.0, -1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, -1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0},
    };

    public static void main(String[] args) throws IOException {
        l.info("Запускаем обучение");
       digits();
    }


    private static void digits() throws IOException {
        UnaryOperator<Double> sigmoid = x -> 1 / (1 + Math.exp(-x));
        UnaryOperator<Double> dsigmoid = y -> y * (1 - y);
        fillNumData(60000);
        NeuralNetwork nn = new NeuralNetwork(0.01, sigmoid, dsigmoid, numData[0].length, 512, 128, 32, 10);

        for (int i = 0; i < 100; i++) {
            double error = 0;
            int epoch = 1000;
            int good = 0;
            for (int j = 0; j < epoch; j++) {
                int imgIndex = (int)(Math.random() * numData.length);
                nn.feedForward(numData[imgIndex]);
                int res = indexOfTheMaxByStream(nn.getResult());
                if ( res != numDataDigits[imgIndex])
                    l.debug("num  error: {} - {} - {}",  imgIndex , numDataDigits[imgIndex],  res);
                else
                    good++;
                double[] train = new double[numData.length];
                train[ numDataDigits[imgIndex] ] = 1.0;
                error += nn.train(train);
            }
            l.info("good: {} error: {}", good, error);

        }
        FormDigits f = new FormDigits(nn);
        new Thread(f).start();

    }

    private static void fillNumData(int samples) {
        numDataDigits = new int[samples];
        BufferedImage[] images = new BufferedImage[samples];
        File[] imagesFiles = new File("c:/proj/train").listFiles();
        for (int i = 0; i < samples; i++) {
            try {
                images[i] = ImageIO.read(imagesFiles[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            numDataDigits[i] = Integer.parseInt(imagesFiles[i].getName().charAt(10) + "");
        }
        numData = new double[samples][784];
        for (int i = 0; i < samples; i++) {
            for (int x = 0; x < 28; x++) {
                for (int y = 0; y < 28; y++) {
                    numData[i][x + y * 28] = (images[i].getRGB(x, y) & 0xff) / 255.0;
                }
            }
        }
    }

    static int indexOfTheMaxByStream(double[] array) {
        return IntStream.range(0, array.length)
                .boxed()
                .max(Comparator.comparingDouble(i -> array[i]))
                .orElse(-1);
    }

}

