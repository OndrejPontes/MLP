import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author opontes
 */
public class Layer {
    private List<Neuron> neurons = new ArrayList<>();

    public Layer(int numberOfNeurons) {
        for (int i = 0; i < numberOfNeurons; ++i)
            neurons.add(new Neuron());
    }

    public List<Double> evaluate(ArrayList<Double> inputs) {
        if(inputs.size() < 1){
            throw new IllegalArgumentException();
        }

        List<Double> result = new ArrayList<>();
        neurons.forEach(neuron -> result.add(neuron.getResult(inputs)));
        return result;
    }

    public int size() {
        return neurons.size();
    }

    public List<Double> getWeights(int i) {
        return neurons.get(i).getWeights();
    }

    public Double getWeight(int i, int j) {
        return neurons.get(i).getWeight(j);
    }

    public void setWeight(int i, int j, double v) {
        neurons.get(i).setWeight(j, v);
    }
}
