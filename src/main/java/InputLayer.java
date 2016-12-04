import java.util.ArrayList;
import java.util.List;

/**
 * @author opontes
 */
public class InputLayer {
    private List<Neuron> neurons = new ArrayList<Neuron>();

    public InputLayer(int numberOfNeurons) {
        for (int i = 0; i < numberOfNeurons; ++i)
            neurons.add(new Neuron());
    }
    
    public List<Double> evaluate(List<List<Double>> inputs){
        if(inputs.size() != neurons.size()){
            throw new IllegalArgumentException();
        }

        List<Double> result = new ArrayList<Double>();

        for (int i = 0; i < inputs.size(); i++) {
            result.add(neurons.get(i).getResult(inputs.get(i)));
        }

        return result;
    }

    public void print() {
        neurons.forEach(neuron -> neuron.print());
    }
}
