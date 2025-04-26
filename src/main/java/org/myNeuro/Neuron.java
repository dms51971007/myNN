package org.myNeuro;

import java.util.List;

import static java.lang.Math.abs;

public class Neuron {
    NeuronNetwork nn;
    int level;
    double[] weights;
    double bias;

    public double value;


    public Neuron(NeuronNetwork nn, int level) {
        int sizeWeight = level == 0 ? 0 : nn.getLevel(level - 1).size();
        this.weights = new double[sizeWeight];
        this.nn = nn;
        this.level = level;
        for(int i = 0; i < sizeWeight; ++i) {
            this.bias = (float)Math.random();
            this.weights[i] = (float)Math.random();
        }

    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setWeights(double... weights) {
        this.weights = weights;
        //this.calcValue();
    }

    public void teach(double correctValue) {
        if (this.level != 0) {
            List<Neuron> preLevel = this.nn.getLevel(this.level - 1);

            double error = this.value * (1.0F - this.value) * (correctValue - this.value);
//            if (this.level == nn.nn.size()-1)
//                System.out.println("level " + this.level + " error " + error);
            double[] newWeight = new double[this.weights.length];


            for(int i = 0; i < preLevel.size(); i++) {
                Neuron n = preLevel.get(i);
                double correct = n.value + weights[i] * error;
                n.teach(correct);
            }

            for(int i = 0; i < this.weights.length; i++) {
                newWeight[i] = weights[i] + nn.LEARNING_RATE * preLevel.get(i).value * error;
            }

            bias = bias + nn.LEARNING_RATE * error;
            this.setWeights(newWeight);

        }
    }

    void   calcValue() {
        if (level > 0) {
            float res = 0.0F;
            List<Neuron> preLevel = nn.getLevel(this.level - 1);

            for(int i = 0; i < preLevel.size(); ++i) {
                res += this.weights[i] * preLevel.get(i).value;
            }

            this.value = this.sigmoid(res + this.bias);
            //System.out.println(" level " + this.level + " value " + this.value);
        }

    }

    double sigmoid(double x) {
        return (1.0d / (1.0d + Math.exp((-x))));
    }
    float sigmoid1(float x) {
        // Return early for large or small x values to avoid overflow issues and enhance stability
        if (x > 20) {
            return 1.0f; // e^x is very large and effectively reaches 1
        } else if (x < -20) {
            return 0.0f; // e^x is effectively 0 for large negative values
        } else {
            float expNegX = (float) Math.exp(-x);
            return 1.0f / (1.0f + expNegX);
        }
    }
    float sigmoid4(float x) {
        // Handle extreme values to prevent overflow and improve numerical stability
        if (x >= 0) {
            float expNegX = (float) Math.exp(-x);
            return 1.0f / (1.0f + expNegX);
        } else {
            float expX = (float) Math.exp(x);
            return expX / (1.0f + expX);
        }
    }

    float sigmoid5(float x) {
        return 0.5f * (x / (1 + abs(x))) + 0.5f;
    }

}
