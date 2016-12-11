package another.implementation;

import java.util.ArrayList;
import java.util.List;


public class Layer {
    private int numberOfNeurons;
    private ArrayList<Neuron> neurons;
    private List<Double> outputs;

    public Layer(int thisNeurons, int prevNeurons) {
        numberOfNeurons = thisNeurons + 1;    // + bias
        neurons = new ArrayList<>();
        outputs = new ArrayList<>();
        for (int i = 0; i < numberOfNeurons; ++i)
            neurons.add(new Neuron(prevNeurons + 1));  // initialize neurons +1 bias
    }

    public List<Double> evaluate(List<Double> inputs) {
        List<Double> tmp;

        if (inputs.size() != getWeights(0).size()){         // check if bias is missing
            tmp = new ArrayList<>();
            tmp.add(1.0d);                                  // add bias
            inputs.forEach(tmp::add);                       // copy others value
        }
        else
            tmp = inputs;

        if(getWeights(0).size() != tmp.size()) throw new ArithmeticException();

        outputs.clear();
        outputs.add(1.0d);
//        neurons.forEach(x -> outputs.add(x.evaluate(inputs)));

//        for (Neuron neuron :
//                neurons) {
//            outputs.add(neuron.evaluate(tmp));
//        }

        for (int i = 1; i < numberOfNeurons; ++i)
            outputs.add(neurons.get(i).evaluate(tmp));

        return outputs;
    }

    public int getNeuronsNumber() {
        return numberOfNeurons;
    }

    public Double getOutput(int i) {
        return outputs.get(i);
    }

    public double getDerivation(int i) {
        return neurons.get(i).getDerivation();
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
