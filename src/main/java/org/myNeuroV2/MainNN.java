package org.myNeuroV2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class MainNN {
    static Logger l =  LoggerFactory.getLogger(MainNN.class);
    static double[][] numData = new double[][]{
            {1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, -1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0},
            {-1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, -1.0},
            {1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0},
            {1.0, -1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0}};

    public static void main(String[] args) throws IOException {
        l.info("Запускаем обучение");
       digits();
    }


    private static void digits() throws IOException {
        UnaryOperator<Double> sigmoid = x -> 1 / (1 + Math.exp(-x));
        UnaryOperator<Double> dsigmoid = y -> y * (1 - y);

        NeuralNetwork nn = new NeuralNetwork(0.2, sigmoid, dsigmoid, 15, 12, 12, 10);

        for (int i = 0; i < 1000; i++) {
            l.info("i: {}", i);
            for (int j = 0; j < 10; j++) {
                nn.calc(numData[j]);
                int res = indexOfTheMaxByStream(nn.getResult());
                if ( res != j)
                    l.info("num {} error: {} - {}", i,  j,  res);

                double[] train = new double[numData.length];
                train[j] = 1.0;
                nn.train(train);
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

