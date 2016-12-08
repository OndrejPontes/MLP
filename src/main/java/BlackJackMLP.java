import com.opencsv.CSVReader;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author opontes
 */
public class BlackJackMLP implements NeuralNetwork {
    private Integer numberOfInputs;
    private Layer outputLayer;
    private List<Layer> hiddenLayers;
    private Double learningRate;
    private String help;

    public BlackJackMLP() {
        setLayers(10, 1, Collections.emptyList(), 1d);
    }

    @Override
    public NeuralNetwork initialize(String file) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("TestFile.csv"));

            List<String> help = Arrays.asList(lines.get(0).split(" "));
            Integer inputs = Integer.getInteger(help.get(0));
            Integer outputs = Integer.getInteger(help.get(1));
            Double speed = Double.parseDouble(help.get(2));

            List<List<Double>> weights = new ArrayList<>();

            for (int i = 1; i < lines.size(); i++) {
                Arrays.asList(lines.get(i).split(", ")).forEach(neuron -> {
                    List<Double> numbers = new ArrayList<>();
                    Arrays.asList(neuron.split(" ")).forEach(x -> {
                        numbers.add(Double.parseDouble(x));
                    });
                    weights.add(numbers);
                });
            }

            List<Integer> setHidden = new ArrayList<>();

            weights.forEach(x -> setHidden.add(x.size()));

            setLayers(inputs, outputs, setHidden, speed);

            //TODO finish setting weights

        } catch (IOException e) {
            System.out.println("Can not find a file.");
        }

        return this;
    }

    // compute error for one sample from dataset
    private Double computeSquaredErrorFunction(List<Double> results, List<Double> targetValues) {
        if (results.size() != targetValues.size()) {
            throw new IllegalArgumentException("Can not compute error function - size of result does not equal to size of target values.");
        }
        Double errorOfSample = 0d;
        for (int i = 0; i < results.size(); i++) {
            errorOfSample += (results.get(i) - targetValues.get(i)) * (results.get(i) - targetValues.get(i));
        }

        return 0.5 * errorOfSample;
    }

    private List<Double> computeOutputError(List<Double> results, List<Double> targetValues) {
        if (results.size() != targetValues.size()) throw new IllegalArgumentException("Can not compute output error !");

        List<Double> outputError = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            Double error = targetValues.get(i) - results.get(i);
            outputError.add(i, error);
        }

        return outputError;
    }

    private void updateWeights(List<Double> inputs) {
        // weights of output layer
        for (int j = 0; j < outputLayer.getNeurons().size(); j++) {
            for (int k = 0; k < outputLayer.getWeights(j).size(); k++) {
                Double res = outputLayer.getResults().get(j);
                Double weightChange = -learningRate * outputLayer.getDelta(j) * res;
                outputLayer.setWeight(j, k, outputLayer.getWeight(j, k) + weightChange);
            }
        }
        // weights of hidden layers
        for (int i = hiddenLayers.size() - 1; i > 0; i--) {
            for (int j = 0; j < hiddenLayers.get(i).getNeurons().size(); j++) {
                for (int k = 0; k < hiddenLayers.get(i).getWeights(j).size(); k++) {
                    Double res = hiddenLayers.get(i - 1).getResults().get(k);
                    Double weightChange = -learningRate * hiddenLayers.get(i).getDelta(j) * res;
                    hiddenLayers.get(i).setWeight(j, k, hiddenLayers.get(i).getWeight(j, k) + weightChange);
                }
            }
        }
        // weights of the first hidden layer (input goes into this layer)
        if (!hiddenLayers.isEmpty()) {
            for (int j = 0; j < hiddenLayers.get(0).getNeurons().size(); j++) {
                for (int k = 0; j < hiddenLayers.get(0).getWeights(j).size(); k++) {
                    // predpoklad, ze do neuronu ide len 1 vstup
                    Double weightChange = -learningRate * hiddenLayers.get(0).getDelta(j) * inputs.get(j);
                    hiddenLayers.get(0).setWeight(j, k, hiddenLayers.get(0).getWeight(j, k) + weightChange);
                }
            }
        }
    }

    private void computeDeltas(List<Double> outputError) {
        // compute delta for output layer
        for (int j = 0; j < outputLayer.getNeurons().size(); j++) {
            Double delta = outputLayer.getResults().get(j) * (1 - outputLayer.getResults().get(j)) * outputError.get(j);
            outputLayer.setDelta(j, delta);
        }

        for (int i = hiddenLayers.size() - 1; i >= 0; i--) {
            if (i == hiddenLayers.size() - 1) {    // hidden layer bellow output layer
                for (int j = 0; j < hiddenLayers.get(i).getNeurons().size(); j++) {  // iterate throught all neurons in layer i
                    Double neuronResult = hiddenLayers.get(i).getResults().get(j);
                    Double sum = 0d;
                    for (int k = 0; k < outputLayer.getNeurons().size(); k++) {  // neurons above actual layer
                        sum += outputLayer.getDelta(k) * outputLayer.getWeight(k, j);
                    }
                    Double delta = neuronResult * (1 - neuronResult) * sum;
                    hiddenLayers.get(i).setDelta(j, delta);
                }
            } else {
                for (int j = 0; j < hiddenLayers.get(i).getNeurons().size(); j++) {
                    Double neuronResult = hiddenLayers.get(i).getResults().get(j);
                    Double sum = 0d;
                    for (int k = 0; k < hiddenLayers.get(i + 1).getNeurons().size(); k++) {  // pocet neuronov vo vrstve nad
                        sum += hiddenLayers.get(i + 1).getDelta(k) * hiddenLayers.get(i + 1).getNeurons().get(k).getWeight(j);
                    }
                    Double delta = neuronResult * (1 - neuronResult) * sum;
                    hiddenLayers.get(i).setDelta(j, delta);
                }
            }
        }
    }

    @Override
    public NeuralNetwork train(File csvFile) {
        Double globalError = Double.POSITIVE_INFINITY;
        PrintWriter fout = null;
        int counter = 0;
        try {
            fout = new PrintWriter(new FileWriter("plot.dat"));

            fout.println("#\tX\tY");
            while (globalError > 0.000f) {
                globalError = 0d;
                // iterate throught all samples
                CSVReader reader = new CSVReader(new FileReader(csvFile));
                String[] nextLine;
                List<Double> input = new ArrayList<>();
                List<Double> results = new ArrayList<>();;
                List<Double> targetValues = new ArrayList<>();;
                while ((nextLine = reader.readNext()) != null) {
                    if (nextLine.length - 1 != numberOfInputs) {
                        throw new IllegalArgumentException("Number of inputs does not equal to number of input neurons!");
                    }
                    input = Arrays.stream(nextLine).map(Double::parseDouble).collect(Collectors.toList());
                    results = getResult(input.subList(0, input.size() - 1));  //forward propagation
                    targetValues = input.subList(input.size() - 1, input.size());

                    // backpropagation
                    computeDeltas(computeOutputError(results, targetValues));
                    updateWeights(input.subList(0, numberOfInputs - 1));

                    // compute global error throught all samples from dataset
                    globalError += computeSquaredErrorFunction(results, targetValues);
                    counter++;
                    fout.println("\t" + counter + "\t" + globalError);
                }
//                List<Double> input = Arrays.stream(nextLine).map(Double::parseDouble).collect(Collectors.toList()); //nextLine is null, it throw nullpointer exception
//                List<Double> results = getResult(input.subList(0, numberOfInputs - 1));  //forward propagation
//                List<Double> targetValues = input.subList(numberOfInputs, input.size() - 1);

                // backpropagation
                List<Double> outputError = computeOutputError(results, targetValues);
                computeDeltas(outputError);
                updateWeights(input.subList(0, numberOfInputs - 1));

                // compute global error throught all samples from dataset
                globalError += computeSquaredErrorFunction(results, targetValues);

                counter++;
                fout.println("\t" + counter + "\t" + globalError);
            }
        } catch (IOException e) {
            Logger.getLogger(BlackJackMLP.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            fout.close();
        }
        return this;
    }

    @Override
    public NeuralNetwork save(String file) {
        List<String> lines = new ArrayList<>();
        lines.add(numberOfInputs.toString() + " " + outputLayer.size() + " " + learningRate);
        hiddenLayers.forEach(layer -> {
            createStringInformation(layer);
            lines.add(help.substring(0, help.length() - 2));
        });

        help = "";
        createStringInformation(outputLayer);
        lines.add(help.substring(0, help.length() - 2));


        try {
            PrintWriter writer = new PrintWriter(file);
            lines.forEach(writer::println);
            writer.close();
        } catch (IOException e) {
            System.out.println("Can not save network.");
        }
        return this;
    }

    @Override
    public List<Double> getResult(List<Double> data) {
        if (data.size() != numberOfInputs) throw new IllegalArgumentException();

        for (Layer layer : hiddenLayers) {
            data = layer.evaluate(data);
        }

        return outputLayer.evaluate(data);
    }

    @Override
    public NeuralNetwork setLayers(Integer numberOfInputNeurons, Integer numberOfOutputNeurons,
                                   List<Integer> numberOfNeuronsInHiddenLayers, Double learningSpeed) {
        numberOfInputs = numberOfInputNeurons;
        outputLayer = new Layer(numberOfOutputNeurons);
        hiddenLayers = new ArrayList<Layer>() {{
            numberOfNeuronsInHiddenLayers.forEach(numberOfNeurons -> add(new Layer(numberOfNeurons)));
        }};
        learningRate = learningSpeed;
        return this;
    }

    @Override
    public NeuralNetwork print() {
        throw new NotImplementedException();
    }

    @Override
    public NeuralNetwork reset() {
        List<Integer> hidden = new ArrayList<>();
        hiddenLayers.forEach(layer -> hidden.add(layer.size()));
        setLayers(numberOfInputs, outputLayer.size(), hidden, learningRate);
        return this;
    }

    private void createStringInformation(Layer layer) {
        help = "";
        layer.getNeurons().forEach(neuron -> {
            help += neuron.getBias().toString() + " ";
            neuron.getWeights().forEach(weight -> {
                help += weight.toString() + " ";
            });
            help = help.substring(0, help.length() - 1);
            help += ", ";
        });
    }
}
