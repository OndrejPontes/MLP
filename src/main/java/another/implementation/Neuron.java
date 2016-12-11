package another.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Neuron {
    private double valueOfNeuron;
    private List<Double> weights;
    private static final double lambda = 1.5f;

    public Neuron(int numberOfPrevNeurons) {
        weights = new ArrayList<>();
        for (int i = 0; i < numberOfPrevNeurons; ++i)
            weights.add( -1.0d + (1.0d - (-1.0d)) * new Random().nextDouble());
    }

    public Double evaluate(List<Double> inputs) {
        valueOfNeuron = 0.0d;
        if (inputs.size() != weights.size()) throw new IllegalArgumentException();

        for (int i = 0; i < inputs.size(); ++i)
            valueOfNeuron += inputs.get(i) * weights.get(i);

        return (1d - Math.exp(-2d * valueOfNeuron)) / (1d + Math.exp(-2d * valueOfNeuron));     // Hyperbolic tangent
    }

    public Double getDerivation() // dphi(valueOfNeuron)
    {
        double expmlx = Math.exp(lambda * valueOfNeuron);
        return 2 * lambda * expmlx / ((1 + expmlx) * (1 + expmlx));
//        return valueOfNeuron * (1 - valueOfNeuron);
    }

    public List<Double> getWeights() {
        return weights;
    }

    public Double getWeight(int i) {
        return weights.get(i);
    }

    public void setWeight(int i, Double v) {
        weights.set(i, v);
    }
}
