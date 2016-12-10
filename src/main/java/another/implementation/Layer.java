package another.implementation;

import java.util.ArrayList;
import java.util.List;


public class Layer {
    private int numberOfNeurons;
    private ArrayList<Neuron> neurons;
    private List<Double> outputs;

    public Layer(int prevNeurons, int thisNeurons) {
        numberOfNeurons = thisNeurons + 1;

        // allocate everything
        neurons = new ArrayList<>();
        outputs = new ArrayList<>();

        for (int i = 0; i < numberOfNeurons; ++i)
            neurons.add(new Neuron(prevNeurons + 1));
    }

    // add 1 in front of the out vector
    public static List<Double> add_bias(List<Double> input) {
        List<Double> out = new ArrayList<>();
        out.add(1.0d);
        input.forEach(out::add);
        return out;
    }

    // compute the output of the layer
    public List<Double> evaluate(List<Double> in) {
        List<Double> inputs;

        // add an input (bias) if necessary
        if (in.size() != getWeights(0).size())
            inputs = add_bias(in);
        else
            inputs = in;

        if(getWeights(0).size() != inputs.size()) throw new ArithmeticException();

        // stimulate each neuron of the layer and get its output
        outputs.clear();
        outputs.add(1.0d);
//        neurons.forEach(x -> outputs.add(x.activate(inputs)));
        for (int i = 1; i < numberOfNeurons; ++i)
            outputs.add(neurons.get(i).activate(inputs));

        return outputs;
    }

    public int size() {
        return numberOfNeurons;
    }

    public Double getOutput(int i) {
        return outputs.get(i);
    }

    public double getActivationDerivative(int i) {
        return neurons.get(i).getActivationDerivative();
    }

    public List<Double> getWeights(int i) {
        return neurons.get(i).getWeights();
    }

    public double getWeight(int i, int j) {
        return neurons.get(i).getWeight(j);
    }

    public void setWeight(int i, int j, double v) {
        neurons.get(i).setWeight(j, v);
    }
}
