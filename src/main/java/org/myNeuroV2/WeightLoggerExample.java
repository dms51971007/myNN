package org.myNeuroV2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeightLoggerExample {
    private static final Logger logger = LoggerFactory.getLogger(WeightLoggerExample.class);

    public static void logWeightUpdate(int fromLayer, int fromNeuron, int toLayer, int toNeuron, double delta, double newWeight) {
        // ✅ SLF4J 2.x поддерживает форматирование прямо в строке
        logger.trace(
            "Weight {}-{} to {}-{}: delta={:.6f}, new weight={:.6f}",
            fromLayer, fromNeuron, toLayer, toNeuron, delta, newWeight
        );
    }

    public static void main(String[] args) {
        logWeightUpdate(1, 5, 2, 3, 0.000123456789, 0.987654321);
    }
}
