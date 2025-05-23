package org.myNeuroV2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.UnaryOperator;

public class NeuralNetwork {

    public double learningRate = 0.0;
    public Layer[] layers;

    public double getLearningRate() {
        return learningRate;
    }

    public Layer[] getLayers() {
        return layers;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setLayers(Layer[] layers) {
        this.layers = layers;
    }

    public void setL(Logger l) {
        this.l = l;
    }

    private UnaryOperator<Double> activation = x -> 1 / (1 + Math.exp(-x));
    private UnaryOperator<Double> derivative = y -> y * (1 - y);
    Logger l =  LoggerFactory.getLogger(NeuralNetwork.class);
    boolean isTrace = l.isTraceEnabled();

    public NeuralNetwork() {
    }

    public void setActivation(UnaryOperator<Double> activation) {
        this.activation = activation;
    }

    public void setDerivative(UnaryOperator<Double> derivative) {
        this.derivative = derivative;
    }

    public NeuralNetwork(double learningRate, int... sizes) {
        this.learningRate = learningRate;
        this.activation = activation;
        this.derivative = derivative;
        layers = new Layer[sizes.length];

        for (int i = 0; i < layers.length; i++) {
            if (i == 0)
                layers[i] = new Layer(sizes[i], 0);
            else
                layers[i] = new Layer(sizes[i], sizes[i - 1]);

            for (int j = 0; j < layers[i].size; j++) {
                layers[i].biases[j] = 2*Math.random()-1;
                for (int k = 0; k < layers[i].weights[j].length; k++) {
                    layers[i].weights[j][k] = 2*Math.random()-1;
                }
            }
        }
    }

    public void feedForward(double[] input) {
        System.arraycopy(input, 0, layers[0].neurons, 0, input.length);
        boolean isTrace = l.isTraceEnabled();
        l.trace("Forward");
        for (int i = 1; i < layers.length; i++) {
            l.trace("Layer {}", i);
            for (int j = 0; j < layers[i].neurons.length; j++) {
                double neuron = 0;
                StringBuilder log = new StringBuilder();
                for (int k = 0; k < layers[i - 1].neurons.length; k++) {
                    if (isTrace)
                        log.append(String.format("%.6f * %.6f + ", layers[i].weights[j][k], layers[i - 1].neurons[k]));
                    neuron += layers[i - 1].neurons[k] * layers[i].weights[j][k];
                }
                if (isTrace) log.append(String.format("%.6f = ", layers[i].biases[j]));
                neuron += layers[i].biases[j];
                layers[i].neurons[j] = activation.apply(neuron);
                if (isTrace) log.append(String.format("%.6f activation: %.6f", neuron, activation.apply(neuron)));
                l.trace(log.toString());
            }
        }
    }

    public double train(double[] input) {
        l.trace("Backward");
        double[] res = layers[layers.length - 1].neurons.clone();
        double error = 0;
        for (int i = 0; i < res.length; i++) {
            error += Math.pow(input[i] - res[i], 2);
        }
        error = error / 2;
        l.debug("Error:  {}", error);
        StringBuilder log = new StringBuilder();

        for (int i = layers.length - 1; i > 0; i--) {
            layers[i].oldWeights = Utils.cloneArray(layers[i].weights.clone());
            layers[i].oldBiases = layers[i].biases.clone();

            if (i == layers.length - 1) {
                l.trace("Output layer: {}", i);
                for (int j = 0; j < layers[i].size; j++) {
                    double o = layers[i].neurons[j];
                    layers[i].grad[j] = (o - input[j]) * derivative.apply(o);
                    log = new StringBuilder();
                    if (isTrace)
                        log.append(String.format("error d[%d]: (%.6f - %.6f) * %.6f * (1 - %.6f) = %.6f", j, o, input[j], o, o, layers[i].grad[j]));
                    l.trace(log.toString());
                    log = new StringBuilder();
                    if (isTrace)
                        log.append(String.format("bias[%d]: %.6f + (-%.6f*%.6f) = %.6f", j, layers[i].biases[j], learningRate, layers[i].grad[j], layers[i].biases[j] + (-learningRate * layers[i].grad[j])));
                    l.trace(log.toString());
                    layers[i].biases[j] = layers[i].biases[j] + (-learningRate * layers[i].grad[j]);
                }
                calcWeights(i);
            } else {
                l.trace("layer {}", i);
                for (int k = 0; k < layers[i + 1].size; k++) {
                    double dw = 0;
                    for (int j = 0; j < layers[i + 1].size; j++) {
                        dw += layers[i + 1].oldWeights[j][k] * layers[i + 1].grad[j];
                        if (isTrace)
                            log.append(String.format("%.6f * %.6f", layers[i + 1].grad[j], layers[i + 1].oldWeights[j][k]));
                        if (j != layers[i + 1].size - 1)
                            if (isTrace) log.append(" + ");
                    }
                    if (isTrace) log.append(String.format(" = %.6f", dw));
                    l.trace(log.toString());

                    layers[i].grad[k] = derivative.apply(layers[i].neurons[k]) * dw;
                    log = new StringBuilder(String.format("d[%d,%d] = %.6f * (1 - %.6f) * %.6f = %.6f", i, k, layers[i].neurons[k], layers[i].neurons[k], dw, layers[i].grad[k]));
                    if (isTrace) l.trace("{}", log);
                }
                for (int j = 0; j < layers[i].size; j++) {

                    layers[i].biases[j] = layers[i].biases[j] + (-learningRate * layers[i].grad[j]);
                    log = new StringBuilder(String.format("%.6f + ( -%.6f * %.6f) = %.6f", layers[i].biases[j], learningRate, layers[i].grad[j], layers[i].biases[j]));
                    if (isTrace) l.trace("{}", log);

                }
                calcWeights(i);
            }
        }
        return error;
    }

    public double[] result() {
        return layers[layers.length - 1].neurons;
    }

    private void  calcWeights(int i) {
        for (int k = 0; k < layers[i].weights[0].length; k++) {
            for (int j = 0; j < layers[i].size; j++) {
                double dw = -learningRate *  layers[i].grad[j] * layers[i-1].neurons[k];
                layers[i].weights[j][k] = layers[i].weights[j][k] + dw;
                if (isTrace) l.trace("{}", String.format("delta w d[%d,%d]: %.6f * %.6f * %.6f = %.6f new w = %.6f", j, k, -learningRate, layers[i].grad[j], layers[i-1].neurons[k], dw, layers[i].weights[j][k]));
            }
    }
    }
}


