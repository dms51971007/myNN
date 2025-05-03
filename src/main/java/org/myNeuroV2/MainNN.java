package org.myNeuroV2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.logging.LogManager;

public class MainNN {

    public static void main(String[] args) throws IOException {
        digits();
    }


    private static void digits() throws IOException {
        UnaryOperator<Double> sigmoid = x -> 1 / (1 + Math.exp(-x));
        UnaryOperator<Double> dsigmoid = y -> y * (1 - y);

        NeuralNetwork nn = new NeuralNetwork(0.1, sigmoid, dsigmoid, 2, 3, 3);

        for (int i = 0; i < 1; i++) {
            nn.calc(new double[]{0.5, 0.8});
            nn.train(new double[]{0.1, 0.9, 0.3});
        }
    }

}

