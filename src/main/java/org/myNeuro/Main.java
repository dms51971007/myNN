package org.myNeuro;


import org.simpleNN.NeuralNetwork;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
//https://medium.com/@karna.sujan52/back-propagation-algorithm-numerical-solved-f60c6986b643

public class Main {
//    static int[][] numData = new int[][]{
//            {1, 1, 1, 1, -1, 1, 1, -1, 1, 1, -1, 1, 1, 1, 1},
//            {-1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1},
//            {1, 1, 1, -1, -1, 1, 1, 1, 1, 1, -1, -1, 1, 1, 1},
//            {1, 1, 1, -1, -1, 1, 1, 1, 1, -1, -1, 1, 1, 1, 1},
//            {1, -1, 1, 1, -1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1},
//            {1, 1, 1, 1, -1, -1, 1, 1, 1, -1, -1, 1, 1, 1, 1},
//            {1, 1, 1, 1, -1, -1, 1, 1, 1, 1, -1, 1, 1, 1, 1},
//            {1, 1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1},
//            {1, 1, 1, 1, -1, 1, 1, 1, 1, 1, -1, 1, 1, 1, 1},
//            {1, 1, 1, 1, -1, 1, 1, 1, 1, -1, -1, 1, 1, 1, 1}};


    public static void main(String[] args) throws IOException {
        int NUM_LEVELS = 5;
        NeuronNetwork neuro = new NeuronNetwork(NUM_LEVELS, 784, 512, 128, 32, 10);

        int samples = 600;
        BufferedImage[] images = new BufferedImage[samples];
        int[] digits = new int[samples];
        File[] imagesFiles = new File("c:/projects/train").listFiles();
        for (int i = 0; i < samples; i++) {
            images[i] = ImageIO.read(imagesFiles[i]);
            digits[i] = Integer.parseInt(imagesFiles[i].getName().charAt(10) + "");
        }

        float[][] inputs = new float[samples][784];
        for (int i = 0; i < samples; i++) {
            for (int x = 0; x < 28; x++) {
                for (int y = 0; y < 28; y++) {
                    inputs[i][x + y * 28] = (float)((images[i].getRGB(x, y) & 0xff) / 255.0);
                }
            }
        }

        for(int i = 0; i <= 500; ++i) {
            int correct = 0;
            for(int j = 0; j < samples; j++) {
                neuro.setValueFirst(inputs[j]);
                List<Neuron> list = neuro.getLevel(NUM_LEVELS - 1);

                double maxVal = 0;
                int intVal = 0;
                for (int k = 0; k < list.size(); k++) {
                    double val = list.get(k).value;
                    if (maxVal < val) {
                        maxVal = val;
                        intVal = k;
                    }
                }
                if ((intVal - digits[j])==0) correct++;


                for(int k = 0; k < 10; k++) {
                    neuro.getNeuron(NUM_LEVELS - 1, k).teach(k == digits[j] ? 1.0f : 0.0f);
                }
            }
            System.out.println("i " + i +  "  correct " + correct);


        }


//        System.out.println("1. Learn the network");
//        System.out.println("2. Guess a number");
//        System.out.println("Your choice:");
//        String input = scanner.nextLine();
//        if (input.equals("2")) {
//            while(true) {
//                System.out.println("Input grid:");
//                int[] num = new int[15];
//
//                for(int i = 0; i < 5; ++i) {
//                    char[] chars = scanner.nextLine().toCharArray();
//
//                    for(int j = 0; j < 3; ++j) {
//                        if (chars[j] == 'X') {
//                            num[i * 3 + j] = 1;
//                        } else {
//                            num[i * 3 + j] = -1;
//                        }
//                    }
//                }
//
//                float res = 0.0F;
//                int intRes = -1;
//                neuro.setValueFirst(num);
//                List<Neuron> neurons = neuro.getLevel(3);
//
//                for(int j = 0; j < 10; ++j) {
//                    float newRes = ((Neuron)neurons.get(j)).value;
//                    System.out.println(" value " + newRes);
//                    if (res < newRes) {
//                        res = newRes;
//                        intRes = j;
//                    }
//                }
//
//                System.out.println("This number is " + intRes);
//                System.out.println("This number is " + res);
//            }
//        }

    }
}
