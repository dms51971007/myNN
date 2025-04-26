package org.myNeuro;

import java.util.ArrayList;
import java.util.List;

public class NeuronNetwork {
    final float LEARNING_RATE = 0.001F;
    List<List<Neuron>> nn = new ArrayList<>();

    public NeuronNetwork(int numLevels, int... sizes) {
        for(int i = 0; i < numLevels; ++i) {
            List<Neuron> level = new ArrayList<>();
            for(int j = 0; j < sizes[i]; ++j) {
                level.add(new Neuron(this, i));
            }
            this.nn.add(level);
        }

    }

    public List<Neuron> getLevel(int level) {
        return nn.get(level);
    }

    public Neuron getNeuron(int level, int index) {
        return nn.get(level).get(index);
    }

    public void setValueFirst(float[] values) {
        List<Neuron> level = this.getLevel(0);

        for(int i = 0; i < level.size(); ++i) {
            (level.get(i)).setValue((float)values[i]);
        }

        this.reCalcAll();

    }

//    void reCalcAll() {
//        Iterator var1 = nn.iterator();
//
//        while(var1.hasNext()) {
//            List<Neuron> ln = (List)var1.next();
//            Iterator var3 = ln.iterator();
//
//            while(var3.hasNext()) {
//                Neuron n = (Neuron)var3.next();
//                n.calcValue();
//            }
//        }

//    }

    void reCalcAll() {
        for (List<Neuron> neurons : nn) {
            for (Neuron neuron : neurons) {
                neuron.calcValue();
            }
        }
    }

}
