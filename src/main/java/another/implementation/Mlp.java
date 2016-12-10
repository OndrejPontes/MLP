package another.implementation;

import java.util.ArrayList;
import java.util.List;
import java.io.*;


public class Mlp {
    private List<Layer> layers;
    private List<List<List<Double>>> deltas;
    private List<List<Double>> gradients;

    public Mlp(List<Integer> layerInfo) {
        // create the required layers
        layers = new ArrayList<>();
        for (int i = 0; i < layerInfo.size(); ++i)
            layers.add(            // pridavam vrstvy, kazdy layer berie ako prvy argument pocet predchadzajucich neuronov a potom pocet neuronov vo vrstve
                    new Layer(        // v kazdej vrstve si nainivializujem pocet neuronov +1 kvoli biasu
                            i == 0 ?
                                    layerInfo.get(i) : layerInfo.get(i - 1),
                            layerInfo.get(i))
            );

        deltas = new ArrayList<>();
        for (int i = 0; i < layerInfo.size(); ++i)
            deltas.add(new ArrayList<List<Double>>(){{add(new ArrayList<>());}});

        gradients = new ArrayList<>();
        for (int i = 0; i < layerInfo.size(); ++i)
            gradients.add(new ArrayList<>());
    }

    public List<Double> evaluate(List<Double> inputs) {
        // propagate the inputs through all neural network
        // and return the outputs
//		assert(false);

//        double outputs[] = new double[inputs.length];
        List<Double> outputs = new ArrayList<>();

        for (int i = 0; i < layers.size(); ++i) {
            outputs = layers.get(i).evaluate(inputs);
            inputs = outputs;
        }

        return outputs;
    }

    private double evaluateError(List<Double> nn_output, List<Double> desired_output) {
        List<Double> d;

        // add bias to input if necessary
        if (desired_output.size() != nn_output.size())
            d = Layer.add_bias(desired_output);
        else
            d = desired_output;

        assert (nn_output.size() == d.size());

        float e = 0;
        for (int i = 0; i < nn_output.size(); ++i)
            e += (nn_output.get(i) - d.get(i)) * (nn_output.get(i) - d.get(i));

        return e;
    }

    public double evaluateQuadraticError(ArrayList<List<Double>> examples,
                                         ArrayList<List<Double>> results) {
        // this function calculate the quadratic error for the given
        // examples/results sets
//		assert(false);

        double e = 0;

        for (int i = 0; i < examples.size(); ++i) {
            e += evaluateError(evaluate(examples.get(i)), results.get(i));
        }

        return e;
    }

    private void evaluateGradients(List<Double> results) {
        // for each neuron in each layer
        for (int c = layers.size() - 1; c >= 0; --c) {
            gradients.get(c).clear();
            for (int i = 0; i < layers.get(c).size(); ++i) {
                // if it's output layer neuron
                if (c == layers.size() - 1) {
                    gradients.get(c).add(2 * (layers.get(c).getOutput(i) - results.get(0))
                                    * layers.get(c).getActivationDerivative(i));
                } else { // if it's neuron of the previous layers
                    float sum = 0;
                    for (int k = 1; k < layers.get(c + 1).size(); ++k)
                        sum += layers.get(c + 1).getWeight(k, i) * gradients.get(c + 1).get(k);
                    gradients.get(c).add(layers.get(c).getActivationDerivative(i) * sum);
                }
            }
        }
    }

    private void resetWeightsDelta() {
        // reset delta values for each weight
        deltas.clear();
        for (int c = 0; c < layers.size(); ++c) {
            deltas.add(new ArrayList<>());
            for (int i = 0; i < layers.get(c).size(); ++i) {
                deltas.get(c).add(new ArrayList<>());
                List<Double> weights = layers.get(c).getWeights(i);
                for (int j = 0; j < weights.size(); ++j)
                    deltas.get(c).get(i).add(0d);
            }
        }
    }

    private void evaluateWeightsDelta() {
        // evaluate delta values for each weight
        for (int c = 1; c < layers.size(); ++c) {
            for (int i = 0; i < layers.get(c).size(); ++i) {
                List<Double> weights = layers.get(c).getWeights(i);
                for (int j = 0; j < weights.size(); ++j)
                    if(deltas.get(c).get(i).size() <= j){
                        deltas.get(c).get(i).add(gradients.get(c).get(i)
                                * layers.get(c - 1).getOutput(j));
                    } else {
                        deltas.get(c).get(i).set(j, gradients.get(c).get(i)
                                * layers.get(c - 1).getOutput(j) + deltas.get(c).get(i).get(j));
                    }
            }
        }
    }

    private void updateWeights(double learning_rate) {
        for (int c = 0; c < layers.size(); ++c) {
            for (int i = 0; i < layers.get(c).size(); ++i) {
                List<Double> weights = layers.get(c).getWeights(i);
                for (int j = 0; j < weights.size(); ++j)
                    layers.get(c).setWeight(i, j, layers.get(c).getWeight(i, j)
                            - (learning_rate * deltas.get(c).get(i).get(j)));
            }
        }
    }

    private void batchBackPropagation(ArrayList<List<Double>> examples,
                                      ArrayList<List<Double>> results,
                                      double learning_rate) {
        resetWeightsDelta();

        for (int l = 0; l < examples.size(); ++l) {
            evaluate(examples.get(l));
            evaluateGradients(results.get(l));
            evaluateWeightsDelta();
        }

        updateWeights(learning_rate);
    }

    public void learn(ArrayList<List<Double>> examples,
                      ArrayList<List<Double>> results,
                      double learning_rate) {
        // this function implements a batched back propagation algorithm
//		assert(false);

        double e = Double.POSITIVE_INFINITY;

        while (e > 0.001d) {

            batchBackPropagation(examples, results, learning_rate);

            e = evaluateQuadraticError(examples, results);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        // initialization
        ArrayList<List<Double>> ex = new ArrayList<>();
        ArrayList<List<Double>> out = new ArrayList<>();
//        for (int i = 0; i < 4; ++i) {
//            ex.add(new ArrayList<>());
//            out.add(new ArrayList<>());
//        }

        // fill the examples database
        ex.add(new ArrayList<Double>(){{ add(-1d); add(1d); }});
//        ex.get(0).set(0, -1d);
//        ex.get(0).set(1, 1d);
        out.add(new ArrayList<Double>(){{ add(1d); }});
//        out.get(0).set(0, 1d);
        ex.add(new ArrayList<Double>(){{ add(1d); add(1d); }});
//        ex.get(1).set(0, 1d);
//        ex.get(1).set(1, 1d);
        out.add(new ArrayList<Double>(){{ add(-1d); }});
//        out.get(1).set(0, -1d);
        ex.add(new ArrayList<Double>(){{ add(1d); add(-1d); }});
//        ex.get(2).set(0, 1d);
//        ex.get(2).set(1, -1d);
        out.add(new ArrayList<Double>(){{ add(1d); }});
//        out.get(2).set(0, 1d);
        ex.add(new ArrayList<Double>(){{ add(-1d); add(-1d); }});
//        ex.get(3).set(0, -1d);
//        ex.get(3).set(1, -1d);
        out.add(new ArrayList<Double>(){{ add(-1d); }});
//        out.get(3).set(0, -1d);

        List<Integer> layerInfo = new ArrayList<Integer>(){{ add(ex.get(0).size()); add(ex.get(0).size() * 3); add(1); }};
//        { // kde kazda hodnota predstavuje vrstvu, prva hodnota je input posledna output a vsetko medzi tym je hidden
//                ex.get(0).size(),    // layer 1: input layer - 2 neurons
//                ex.get(0).size() * 3,    // layer 2: hidden layer - 6 neurons
//                1            // layer 3: output layer - 1 neuron
//        };

        Mlp mlp = new Mlp(layerInfo); // Inicializacia

        try {
            PrintWriter fout = new PrintWriter(new FileWriter("plot.dat"));
            fout.println("#\tX\tY");

            for (int i = 0; i < 40000; ++i) {
                mlp.learn(ex, out, 0.3d);
                double error = mlp.evaluateQuadraticError(ex, out);
                System.out.println(i + " -> error : " + error);
                fout.println("\t" + i + "\t" + error);
            }

            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
