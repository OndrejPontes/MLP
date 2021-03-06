import java.util.ArrayList;
import java.util.List;

/**
 * @author opontes
 */
public class Layer {
    private List<Neuron> neurons = new ArrayList<>();
    private List<Double> results = new ArrayList<>();
    private List<Double> deltas = new ArrayList<>();

    public Layer(int numberOfNeurons) {
        for (int i = 0; i < numberOfNeurons; ++i){
            neurons.add(new Neuron());
            deltas.add(100d);
        }
    }

    public List<Double> evaluate(List<Double> inputs) {
        if(inputs.size() < 1){
            throw new IllegalArgumentException();
        }

        List<Double> result = new ArrayList<>();
        neurons.forEach(neuron -> result.add(neuron.getResult(inputs)));

        results = result;

        return result;
    }

    public Integer size() {
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

    public List<Double> getResults() {
        return results;
    }

    public List<Neuron> getNeurons() {
        return neurons;
    }

    public List<Double> getDeltas() {
        return deltas;
    }

    public void setDeltas(List<Double> deltas) {
        this.deltas = deltas;
    }

    public void setDelta(int i, Double delta) {
        deltas.set(i, delta);
    }

    public Double getDelta(int i) {
        return deltas.get(i);
    }
}
