package org.myNeuro;

import java.util.List;

public class Neuron {
    NeuroNetwork nn;
    int level;
    float[] weights;
    float bias;

    public float value;


    public Neuron(NeuroNetwork nn, int level, int j) {
        int sizeWeight = level == 0 ? 0 : nn.getLevel(level - 1).size();
        this.weights = new float[sizeWeight];
        this.nn = nn;
        this.level = level;
        for(int i = 0; i < sizeWeight; ++i) {
            this.bias = (float)Math.random();
//            this.weights[i] = StaticValues.NN[level][j][i];
            this.weights[i] = (float)Math.random();
        }

    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setWeights(float... weights) {
        this.weights = weights;
        //this.calcValue();
    }

    public void teach(float correctValue) {
        if (this.level != 0) {
            List<Neuron> preLevel = this.nn.getLevel(this.level - 1);

            float error = this.value * (1.0F - this.value) * (correctValue - this.value);
//            if (this.level == nn.nn.size()-1)
//                System.out.println("level " + this.level + " error " + error);
            float[] newWeight = new float[this.weights.length];


            bias = bias + nn.LEARNING_RATE * error;

            for(int i = 0; i < preLevel.size(); ++i) {
                Neuron n = preLevel.get(i);
                float correct = n.value + weights[i] * error;
                n.teach(correct);
            }

            for(int i = 0; i < this.weights.length; i++) {
                newWeight[i] = weights[i] + nn.LEARNING_RATE * preLevel.get(i).value * error;
            }

            this.setWeights(newWeight);

        }
    }

    void calcValue() {
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

    float sigmoid(float x) {
        return (float)(1.0 / (1.0 + Math.exp((-x))));
    }
}
