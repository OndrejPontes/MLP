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

    // compute the output of the layer
    public List<Double> evaluate(ArrayList<Double> inputs) {

        // add an input (bias) if necessary
//        if (in.length != getWeights(0).length)
//            inputs = add_bias(in);
//        else
//            inputs = in;
//
//        assert (getWeights(0).length == inputs.length);
        List<Double> result = new ArrayList<>();
        for (Neuron n : neurons) {
            result.add(n.getResult(inputs));
        }
        // stimulate each neuron of the layer and get its output
//        for (int i = 1; i < nuberOfNeurons; ++i)
//            _outputs[i] = neurons.get(i).activate(inputs);
//
//         bias treatment
//        _outputs[0] = 1.0f;

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
