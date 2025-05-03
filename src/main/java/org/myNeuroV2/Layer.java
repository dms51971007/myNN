package org.myNeuroV2;

public class Layer {

    public int size;
    public double[] neurons;
    public double[] biases;
    public double[][] weights;

    public double[] oldBiases;
    public double[][] oldWeights;


    public double[] grad;
    public boolean output;

    public Layer(int size, int prevSize, boolean output) {
        this.size = size;
        neurons = new double[size];
        biases = new double[size];
        weights = new double[size][prevSize];
        grad = new double[size];
        this.output = output;
    }

}