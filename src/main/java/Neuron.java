import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author opontes
 */

public class Neuron {

    private List<Double> weights = new ArrayList<>();
    private Double bias = 1d;
    private Double value = 0d;

    public Double getResult(List<Double> inputs)
    {
        value = 0d;
        if (weights.isEmpty()){
            weights.addAll(inputs.stream().map(ignored -> -1.0d + (1.0d - (-1.0d)) * new Random().nextDouble()).collect(Collectors.toList()));
//            bias = -1.0d + (1.0d - (-1.0d)) * new Random().nextDouble();  // bias in range -1 to 1
            weights.add(1d);
        }

        for (int i = 0; i < inputs.size(); i++)
            value += inputs.get(i) * weights.get(i);

        value += bias * weights.get(weights.size() - 1);

        return 1.0d / (1.0d + Math.exp(-value));
    }

    public List<Double> getWeights() {
        return weights;
    }

    public double getWeight(int i) {
        return weights.get(i);
    }

    public void setWeight(int i, double weight){
        weights.set(i, weight);
    }

    public void print() {
        System.out.print("|");
    }

    public Double getValue() {
        return value;
    }

    public Double getBias() {
        return bias;
    }

    public Neuron setBias(Double bias) {
        this.bias = bias;
        return this;
    }

    public Neuron setWeights(List<Double> weights) {
        this.weights = weights;
        return this;
    }
}

