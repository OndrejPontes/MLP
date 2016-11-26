import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author opontes
 */

public class Neuron {

    private List<Double> weights = new ArrayList<>();

    public Double getResult(List<Double> inputs)
    {
        if (weights.isEmpty()){
            weights.addAll(inputs.stream().map(ignored -> new Random().nextDouble()).collect(Collectors.toList()));
        }

        double value = 0.0d;

        for (int i = 0; i < inputs.size(); i++)
            value += inputs.get(i) * weights.get(i);

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

}

