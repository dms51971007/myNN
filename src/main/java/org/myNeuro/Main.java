package org.myNeuro;


import java.util.List;
import java.util.Scanner;

public class Main {
    static int[][] numData = new int[][]{
            {1, 1, 1, 1, -1, 1, 1, -1, 1, 1, -1, 1, 1, 1, 1},
            {-1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1},
            {1, 1, 1, -1, -1, 1, 1, 1, 1, 1, -1, -1, 1, 1, 1},
            {1, 1, 1, -1, -1, 1, 1, 1, 1, -1, -1, 1, 1, 1, 1},
            {1, -1, 1, 1, -1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1},
            {1, 1, 1, 1, -1, -1, 1, 1, 1, -1, -1, 1, 1, 1, 1},
            {1, 1, 1, 1, -1, -1, 1, 1, 1, 1, -1, 1, 1, 1, 1},
                    {1, 1, 1,
                    -1, -1, 1,
                    -1, -1, 1,
                    -1, -1, 1,
                    -1, -1, 1},
            {1, 1, 1, 1, -1, 1, 1, 1, 1, 1, -1, 1, 1, 1, 1},
            {1, 1, 1, 1, -1, 1, 1, 1, 1, -1, -1, 1, 1, 1, 1}};


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        NeuroNetwork neuro = new NeuroNetwork(4, new int[]{15, 12, 12, 10});

        for(int i = 0; i <= 20000; ++i) {
            for(int j = 0; j < numData.length; ++j) {
                neuro.setValueFirst(numData[j]);

                for(int k = 0; k < numData.length; ++k) {
                    neuro.getNeuron(3, k).teach(k == j ? 1.0F : 0.0F);
                }
            }
        }

        System.out.println("1. Learn the network");
        System.out.println("2. Guess a number");
        System.out.println("Your choice:");
        String input = scanner.nextLine();
        if (input.equals("2")) {
            while(true) {
                System.out.println("Input grid:");
                int[] num = new int[15];

                for(int i = 0; i < 5; ++i) {
                    char[] chars = scanner.nextLine().toCharArray();

                    for(int j = 0; j < 3; ++j) {
                        if (chars[j] == 'X') {
                            num[i * 3 + j] = 1;
                        } else {
                            num[i * 3 + j] = -1;
                        }
                    }
                }

                float res = 0.0F;
                int intRes = -1;
                neuro.setValueFirst(num);
                List<Neuron> neurons = neuro.getLevel(3);

                for(int j = 0; j < 10; ++j) {
                    float newRes = ((Neuron)neurons.get(j)).value;
                    System.out.println(" value " + newRes);
                    if (res < newRes) {
                        res = newRes;
                        intRes = j;
                    }
                }

                System.out.println("This number is " + intRes);
                System.out.println("This number is " + res);
            }
        }

    }
}
