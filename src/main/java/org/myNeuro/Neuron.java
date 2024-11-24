package org.myNeuro;

import java.util.List;
import java.util.Objects;

public class Neuron {
    NeuroNetwork nn;
    int level;
    float[] weights;
    public float value;

    public Neuron(NeuroNetwork nn, int level) {
        int sizeWeight = level == 0 ? 0 : nn.getLevel(level - 1).size();
        this.weights = new float[sizeWeight];
        this.nn = nn;
        this.level = level;

        for(int i = 0; i < sizeWeight; ++i) {
            this.weights[i] = (float)Math.random();
        }

    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setWeights(float... weights) {
        this.weights = weights;
        this.calcVaule();
    }

    public void teach(float correctValue) {
        if (this.level != 0) {
            List<Neuron> preLevel = this.nn.getLevel(this.level - 1);
            float error = this.value - correctValue;
            float weightDelta = error * this.value * (1.0F - this.value);
            float[] newWeight = new float[this.weights.length];

            int i;
            for(i = 0; i < this.weights.length; ++i) {
                float var10002 = this.weights[i];
                float var10003 = ((Neuron)preLevel.get(i)).value * weightDelta;
                Objects.requireNonNull(this.nn);
                newWeight[i] = var10002 - var10003 * 0.5F;
            }

            this.setWeights(newWeight);

            for(i = 0; i < preLevel.size(); ++i) {
                Neuron n = (Neuron)preLevel.get(i);
                n.teach(n.value - newWeight[i] * weightDelta);
            }

        }
    }

    void calcVaule() {
        if (this.level > 0) {
            float res = 0.0F;
            List<Neuron> preLevel = this.nn.getLevel(this.level - 1);

            for(int i = 0; i < preLevel.size(); ++i) {
                res += this.weights[i] * ((Neuron)preLevel.get(i)).value;
            }

            this.value = this.sigmoid(res);
        }

    }

    float sigmoid(float x) {
        return (float)(1.0 / (1.0 + Math.exp((double)(-x))));
    }
}
