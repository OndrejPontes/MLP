package another.implementation;

import java.util.ArrayList;
import java.util.List;


public class Mlp {
    private List<Layer> layers;
    private List<List<List<Double>>> deltas;
    private List<List<Double>> gradients;

    public Mlp(List<Integer> layerInfo) {
        layers = new ArrayList<>();
        deltas = new ArrayList<>();
        gradients = new ArrayList<>();

        layerInfo.forEach(info -> layers.add(new Layer(info, layers.isEmpty() ? info : layers.get(layers.size() - 1).getNeuronsNumber() - 1)));
    }

    public List<Double> evaluate(List<Double> inputs) {
        for (Layer layer : layers) {
            inputs = layer.evaluate(inputs);
        }
        return inputs;
    }

    private double computeSquaredError(List<Double> result, List<Double> targetResult) {
        List<Double> tmp;
        double error = 0;

        if (targetResult.size() + 1 == result.size()) {     // check bias is missing
            tmp = new ArrayList<>();
            tmp.add(1.0d);                                  // add bias
            targetResult.forEach(tmp::add);                 // copy others value
        } else
            tmp = targetResult;

        if (result.size() != tmp.size()) throw new IllegalArgumentException();

        for (int i = 0; i < result.size(); ++i)
            error += (result.get(i) - tmp.get(i)) * (result.get(i) - tmp.get(i));
        return error;
    }

    public double evaluateError(ArrayList<List<Double>> result, ArrayList<List<Double>> targetResult) {
        double error = 0;
        for (int i = 0; i < result.size(); ++i) {
            error += computeSquaredError(evaluate(result.get(i)), targetResult.get(i));
        }
        return error;
    }

    private void evaluateGradients(List<Double> results) {
        resetGradients();
        for (int i = layers.size() - 1; i >= 0; --i) {
            for (int j = 0; j < layers.get(i).getNeuronsNumber(); ++j) {
                if (i == layers.size() - 1) {       // check if it is output layer
                    gradients.get(i).add(2 * (layers.get(i).getOutput(j) - results.get(0)) * layers.get(i).getDerivation(j));
                } else {
                    float sum = 0;
                    for (int k = 1; k < layers.get(i + 1).getNeuronsNumber(); ++k)
                        sum += layers.get(i + 1).getWeight(k, j) * gradients.get(i + 1).get(k);
                    gradients.get(i).add(layers.get(i).getDerivation(j) * sum);
                }
            }
        }
    }

    private void resetGradients() {
        gradients.clear();
        for (int i = 0; i < layers.size(); i++) {
            gradients.add(new ArrayList<>());
        }
    }

    private void resetDeltas() {
        deltas.clear();
        for (int c = 0; c < layers.size(); ++c) {
            deltas.add(new ArrayList<>());
            for (int i = 0; i < layers.get(c).getNeuronsNumber(); ++i) {
                deltas.get(c).add(new ArrayList<>());
                List<Double> weights = layers.get(c).getWeights(i);
                for (int j = 0; j < weights.size(); ++j)
                    deltas.get(c).get(i).add(0d);
            }
        }
    }

    private void evaluateDeltas() {
        for (int i = 1; i < layers.size(); ++i) {
            for (int j = 0; j < layers.get(i).getNeuronsNumber(); ++j) {
                List<Double> weights = layers.get(i).getWeights(j);
                for (int k = 0; k < weights.size(); ++k)
                    deltas.get(i).get(j).set(k, gradients.get(i).get(j) * layers.get(i - 1).getOutput(k) + deltas.get(i).get(j).get(k));
            }
        }
    }

    private void updateWeights(double learningSpeed) {
        for (int i = 0; i < layers.size(); ++i) {
            for (int j = 0; j < layers.get(i).getNeuronsNumber(); ++j) {
                List<Double> weights = layers.get(i).getWeights(j);
                for (int k = 0; k < weights.size(); ++k)
                    layers.get(i).setWeight(j, k, layers.get(i).getWeight(j, k) - (learningSpeed * deltas.get(i).get(j).get(k)));
            }
        }
    }

    private void backPropagation(ArrayList<List<Double>> results, ArrayList<List<Double>> targetResults, double learningSpeed) {
        resetDeltas();

        for (int i = 0; i < targetResults.size(); ++i) {
            evaluate(targetResults.get(i));
            evaluateGradients(results.get(i));
            evaluateDeltas();
        }
        updateWeights(learningSpeed);
    }

    public void learn(ArrayList<List<Double>> results, ArrayList<List<Double>> targetResult, double learningSpeed) {
        double error = Double.POSITIVE_INFINITY;

        while (error > 0.001d) {
            backPropagation(results, targetResult, learningSpeed);
            error = evaluateError(targetResult, results);
        }
    }
}
