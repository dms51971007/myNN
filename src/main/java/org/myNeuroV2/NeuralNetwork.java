package org.myNeuroV2;

import java.util.function.UnaryOperator;

public class NeuralNetwork {

    private double learningRate;
    public Layer[] layers;
    private UnaryOperator<Double> activation;
    private UnaryOperator<Double> derivative;


    public NeuralNetwork(double learningRate, UnaryOperator<Double> activation, UnaryOperator<Double> derivative, int... sizes) {
        this.learningRate = learningRate;
        this.activation = activation;
        this.derivative = derivative;
        sizes = new int[]{2, 3, 3};
        layers = new Layer[sizes.length];
        layers[0] = new Layer(sizes[0], 0, false);
        layers[1] = new Layer(sizes[1], sizes[0], false);
        layers[2] = new Layer(sizes[2], sizes[1], true);


//        layers[0].neurons = new double[sizes[0]];
//
//        layers[1].weights = new double[sizes[0]][sizes[1]];

        layers[1].weights[0][0] = 0.1;
        layers[1].weights[0][1] = -0.3;

        layers[1].weights[1][0] = 0.4;
        layers[1].weights[1][1] = 0.5;

        layers[1].weights[2][0] = -0.2;
        layers[1].weights[2][1] = 0.6;

//        layers[1].biases = new double[sizes[1]];
        layers[1].biases[0] = 0.1;
        layers[1].biases[1] = -0.2;
        layers[1].biases[2] = 0.3;

//        layers[2].weights = new double[sizes[1]][sizes[2]];

        layers[2].weights[0][0] = 0.2;
        layers[2].weights[0][1] = 0.4;
        layers[2].weights[0][2] = -0.6;

        layers[2].weights[1][0] = -0.1;
        layers[2].weights[1][1] = 0.7;
        layers[2].weights[1][2] = 0.8;

        layers[2].weights[2][0] = 0.3;
        layers[2].weights[2][1] = -0.5;
        layers[2].weights[2][2] = 0.1;

//        layers[2].biases = new double[sizes[2]];

        layers[2].biases[0] = -0.1;
        layers[2].biases[1] = 0.2;
        layers[2].biases[2] = 0.0;


    }

    public void calc(double[] input) {
        System.arraycopy(input, 0, layers[0].neurons, 0, input.length);

        for (int i = 1; i < layers.length; i++) {
            System.out.format("layer %d%n", i);
            for (int j = 0; j < layers[i].neurons.length; j++) {
                double neuron = 0;
//                System.out.println("----------------");
                for (int k = 0; k < layers[i - 1].neurons.length; k++) {

                    System.out.format("%.6f * %.6f + ", layers[i].weights[j][k], layers[i - 1].neurons[k]);

                    neuron += layers[i - 1].neurons[k] * layers[i].weights[j][k];
                }
                System.out.format("%.6f = ", layers[i].biases[j]);
                neuron += layers[i].biases[j];
                layers[i].neurons[j] = activation.apply(neuron);
                System.out.format("%.6f activation: %.6f%n", neuron, activation.apply(neuron));
            }
        }
    }


    public void train(double[] input) {

        double[] res = layers[layers.length - 1].neurons.clone();
        double error = 0;
        for (int i = 0; i < res.length; i++) {
            error += Math.pow(input[i] - res[i], 2);
        }
        error = error / 2;
        System.out.format("Error:  %.6f%n", error);
        System.out.println("-------------------");

        for (int i = layers.length - 1; i > 0; i--) {
            layers[i].oldWeights = Utils.cloneArray(layers[i].weights.clone());
            layers[i].oldBiases = layers[i].biases.clone();

            if (layers[i].output)
                System.out.println("Output layer: " + i);
            else
                System.out.println("layer" + i);
            if (layers[i].output) {
                for (int j = 0; j < layers[i].size; j++) {
                    double o = layers[i].neurons[j];
                    layers[i].grad[j] = (o - input[j]) * (o) * (1 - o);
                    System.out.format("error d[%d]: (%.6f - %.6f) * %.6f * (1 - %.6f) = %.6f%n", j, o, input[j], o, o, layers[i].grad[j]);
                    System.out.format("bias[%d]: %.6f + (-%.6f*%.6f) = %.6f%n",  j, layers[i].biases[j], learningRate, layers[i].grad[j], layers[i].biases[j] + (-learningRate * layers[i].grad[j]));
                    layers[i].biases[j] = layers[i].biases[j] + (-learningRate * layers[i].grad[j]);
                }
                calcWeights(i);
            } else {
                for (int k = 0; k < layers[i+1].size; k++) {
                    double dw = 0;
                    for (int j = 0; j < layers[i+1].size; j++) {
                        dw += layers[i+1].oldWeights[j][k] * layers[i+1].grad[j];
                        System.out.format("%.6f * %.6f", layers[i+1].grad[j] ,layers[i+1].oldWeights[j][k]);
                        if (j != layers[i+1].size-1)
                            System.out.format(" + ");
                    }
                    System.out.format(" = %.6f%n", dw);
                    layers[i].grad[k] = derivative.apply(layers[i].neurons[k]) * dw;
                    System.out.format("d[%d,%d] = %.6f * (1 - %.6f) * %.6f = %.6f%n", i, k, layers[i].neurons[k], layers[i].neurons[k], dw, layers[i].grad[k]);
                }
                for (int j = 0; j < layers[i].size; j++) {

                    layers[i].biases[j] = layers[i].biases[j] +  (-learningRate*layers[i].grad[j]);
                    System.out.format("%.6f + ( -%.6f * %.6f) = %.6f%n", layers[i].biases[j], learningRate, layers[i].grad[j], layers[i].biases[j]);
                }
                calcWeights(i);
            }


    }

}

    private void calcWeights(int i) {
        for (int k = 0; k < layers[i].weights[0].length; k++) {
            for (int j = 0; j < layers[i].size; j++) {
                double dw = -learningRate *  layers[i].grad[j] * layers[i-1].neurons[k];
                layers[i].weights[j][k] = layers[i].weights[j][k] + dw;
                System.out.format("delta w d[%d,%d]: %.6f * %.6f * %.6f = %.6f new w = %.6f%n", j, k, -learningRate, layers[i].grad[j], layers[i-1].neurons[k], dw, layers[i].weights[j][k]);
            }
    }
    }
}


