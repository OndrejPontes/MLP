package another.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Neuron {
    private double valueOfNeuron;
    private List<Double> weights;
    private static final double lambda = 1.5f;

    public Neuron(int numberOfPrevNeurons) {
        // each neuron know the weights of each connection
        // with neurons of the previous layer
        weights = new ArrayList<>();

        // set default weights
        for (int i = 0; i < numberOfPrevNeurons; ++i)
            weights.add( -1.0d + (1.0d - (-1.0d)) * new Random().nextDouble());
    }

    // activate the neuron with given inputs, return the output
    public Double activate(List<Double> inputs) {
        valueOfNeuron = 0.0d;
        if (inputs.size() != weights.size()) throw new IllegalArgumentException();

        for (int i = 0; i < inputs.size(); ++i) // dot product (produit scalaire)
            valueOfNeuron += inputs.get(i) * weights.get(i);

        // phi(valueOfNeuron), our activation function (tanh(x))
        return 2.0d / (1.0d + Math.exp((-valueOfNeuron) * lambda)) - 1.0d;
//		return 1.0d / (1.0d + Math.exp(-valueOfNeuron));
    }

    public Double getActivationDerivative() // dphi(valueOfNeuron)
    {
        double expmlx = Math.exp(lambda * valueOfNeuron);
        return 2 * lambda * expmlx / ((1 + expmlx) * (1 + expmlx));
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
