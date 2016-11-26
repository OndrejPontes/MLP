import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author opontes
 */

public class Neuron {

    private List<Double> weights = new ArrayList<>();

    // parameter of the sigmoid
    private static final double lambda = 1.5d;

    // activate the neuron with given inputs, return the output
    public Double getResult(List<Double> inputs)
    {
        if (weights.isEmpty()){
            for (Double d : inputs) {
                weights.add(new Random().nextDouble());
            }
        }

        double value = 0.0d;

        for (int i = 0; i < inputs.size(); i++)
            value += inputs.get(i) * weights.get(i);

        return 1.0d / (1.0d + Math.exp(-value));
    }

//    public float getActivationDerivative() // dphi(value)
//    {
//        float expmlx = (float) Math.exp(lambda * value);
//        return 2 * lambda * expmlx / ((1 + expmlx) * (1 + expmlx));
//    }

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

