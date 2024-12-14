package org.myNeuro;


import java.util.List;
//https://medium.com/@karna.sujan52/back-propagation-algorithm-numerical-solved-f60c6986b643

public class Main {
    static int[][] numData = new int[][]{
            {1, 1, 1, 1, -1, 1, 1, -1, 1, 1, -1, 1, 1, 1, 1},
            {-1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1},
            {1, 1, 1, -1, -1, 1, 1, 1, 1, 1, -1, -1, 1, 1, 1},
            {1, 1, 1, -1, -1, 1, 1, 1, 1, -1, -1, 1, 1, 1, 1},
            {1, -1, 1, 1, -1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1},
            {1, 1, 1, 1, -1, -1, 1, 1, 1, -1, -1, 1, 1, 1, 1},
            {1, 1, 1, 1, -1, -1, 1, 1, 1, 1, -1, 1, 1, 1, 1},
            {1, 1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1},
            {1, 1, 1, 1, -1, 1, 1, 1, 1, 1, -1, 1, 1, 1, 1},
            {1, 1, 1, 1, -1, 1, 1, 1, 1, -1, -1, 1, 1, 1, 1}};


    public static void main(String[] args) {
        int NUM_LEVELS = 4;
        NeuronNetwork neuro = new NeuronNetwork(NUM_LEVELS, 15, 13, 12, 10);


//        for (int i = 0; i < 100000; i++) {
//            neuro.setValueFirst(new int[]{1, 1});
//            for (int k = 0; k < neuro.getLevel(NUM_LEVELS - 1).size(); ++k) {
//                neuro.getNeuron(NUM_LEVELS - 1, k).teach(1);
//            }
//
//            neuro.setValueFirst(new int[]{0, 0});
//            for (int k = 0; k < neuro.getLevel(NUM_LEVELS - 1).size(); ++k) {
//                neuro.getNeuron(NUM_LEVELS - 1, k).teach(0);
//            }
//
//            neuro.setValueFirst(new int[]{0, 1});
//            for (int k = 0; k < neuro.getLevel(NUM_LEVELS - 1).size(); ++k) {
//                neuro.getNeuron(NUM_LEVELS - 1, k).teach(1);
//            }
//
//            neuro.setValueFirst(new int[]{1, 0});
//            for (int k = 0; k < neuro.getLevel(NUM_LEVELS - 1).size(); ++k) {
//                neuro.getNeuron(NUM_LEVELS - 1, k).teach(1);
//            }


        for(int i = 0; i <= 5000; ++i) {
            for(int j = 0; j < numData.length; ++j) {
                neuro.setValueFirst(numData[j]);
                List<Neuron> list = neuro.getLevel(NUM_LEVELS - 1);
                System.out.print("===" + j + "=== " + i + " ");

                float maxVal = 0;
                int intVal = 0;
                for (int k = 0; k < list.size(); k++) {
                    float val = list.get(k).value;
                    if (maxVal < val) {
                        maxVal = val;
                        intVal = k;
                    }
                }
                if ((intVal - j)!=0) System.out.print(" ERROR");
                System.out.println(" intVal " + intVal + " maxVal " + maxVal);


                for(int k = 0; k < numData.length; ++k) {
                    neuro.getNeuron(NUM_LEVELS - 1, k).teach(k == j ? 1.0F : 0.0F);
                }


            }
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
