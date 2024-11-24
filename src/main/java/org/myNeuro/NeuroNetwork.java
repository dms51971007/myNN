package org.myNeuro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NeuroNetwork {
    final float LEARNING_RATE = 0.5F;
    List<List<Neuron>> nn = new ArrayList();

    public NeuroNetwork(int numLevels, int... sizes) {
        for(int i = 0; i < numLevels; ++i) {
            List<Neuron> level = new ArrayList();

            for(int j = 0; j < sizes[i]; ++j) {
                level.add(new Neuron(this, i));
            }

            this.nn.add(level);
        }

    }

    public List<Neuron> getLevel(int level) {
        return (List)this.nn.get(level);
    }

    public Neuron getNeuron(int level, int index) {
        return (Neuron)((List)this.nn.get(level)).get(index);
    }

    public void setValueFirst(int[] values) {
        List<Neuron> level = this.getLevel(0);

        for(int i = 0; i < level.size(); ++i) {
            ((Neuron)level.get(i)).setValue((float)values[i]);
        }

        this.reCalcAll();
    }

    void reCalcAll() {
        Iterator var1 = this.nn.iterator();

        while(var1.hasNext()) {
            List<Neuron> ln = (List)var1.next();
            Iterator var3 = ln.iterator();

            while(var3.hasNext()) {
                Neuron n = (Neuron)var3.next();
                n.calcVaule();
            }
        }

    }
}
