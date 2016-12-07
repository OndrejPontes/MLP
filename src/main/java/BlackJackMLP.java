import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    private Integer learningRate;   // nie je Integer celociselny ?
    private String help;

    public BlackJackMLP() {
        setLayers(10, 1, Collections.emptyList(), 1);
    }

    @Override
    public NeuralNetwork initialize(String file) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("TestFile.csv"));

            List<String> help = Arrays.asList(lines.get(0).split(" "));
            Integer inputs = Integer.getInteger(help.get(0));
            Integer outputs = Integer.getInteger(help.get(1));
            Integer speed = Integer.getInteger(help.get(2));

            List<List<Double>> weights = new ArrayList<>();

            for (int i = 1; i < lines.size(); i++) {

            }
        } catch (IOException e) {
            System.out.println("Can not find a file.");
        }
        throw new NotImplementedException();
    }

    // compute error for one sample from dataset
    private Double computeSquaredErrorFunction(List<Double> results, List<Double> targetValues) {
        if (results.size() != targetValues.size()) {
            throw new IllegalArgumentException("Can not compute error function - size of result does not equal to size of target values.");
        }
        Double errorOfSample = 0d;
        for(int i = 0; i < results.size(); i++) {
            errorOfSample += Math.sqrt(results.get(i) - targetValues.get(i));
        }
        
        return 0.5 * errorOfSample;
    }
      
    private List<Double> computeOutputError(List<Double> results, List<Double> targetValues) {
        if (results.size() != targetValues.size()) throw new IllegalArgumentException("Can not compute output error !");
      
        List<Double> outputError = new ArrayList<>();
        for(int i = 0; i < results.size(); i++) {
            Double error = targetValues.get(i) - results.get(i);
            outputError.add(i, error);
        }
        
        return outputError;
    }
    
    private void backpropagation(List<Double> outputError) {
        // compute delta for output layer
        for(int j = 0; j < outputLayer.getNeurons().size(); j++) {
            List<Double> weights = outputLayer.getNeurons().get(j).getWeights();
            Double delta = outputLayer.getResults().get(j) * (1 - outputLayer.getResults().get(j)) * outputError.get(j);
            outputLayer.setDelta(j, delta);
        }
            
        for(int i = hiddenLayers.size()-1; i >= 0; i--) {
            if (i == hiddenLayers.size()-1) {    // hidden layer bellow output layer
                for(int j = 0; j < hiddenLayers.get(i).getNeurons().size(); j++) {  // iterate throught all neurons in layer i
                    Double neuronResult = hiddenLayers.get(i).getResults().get(j);    
                    Double sum = 0d;
                    for(int k = 0; k < outputLayer.getNeurons().size(); k++) {  // neurons above actual layer
                        sum += outputLayer.getDelta(k) * outputLayer.getWeight(k, j);
                    }
                    Double delta = neuronResult * (1 - neuronResult) * sum;
                    hiddenLayers.get(i).setDelta(j, delta);
                }
            } else {
                // toto je este stara ZLA verzia !!
                // other hidden layers
                for(int j = 0; j < hiddenLayers.get(i).getNeurons().size(); j++) {
                    List<Double> weights = hiddenLayers.get(i).getWeights(j);
                    Double neuronResult = hiddenLayers.get(i).getResults().get(j);
                    Double sum = 0d;
                    for(int k = 0; k < weights.size(); k++) {
                        Double weightChange = - learningRate * neuronResult * hiddenLayers.get(i+1).getDelta(k);
                        hiddenLayers.get(i).setWeight(j, k, weights.get(k) + weightChange);
                        
                        sum += weights.get(k) * hiddenLayers.get(i+1).getDelta(k);
                    }
                    Double delta = neuronResult * (1 - neuronResult) * sum;
                    hiddenLayers.get(i).setDelta(j, delta);
                } 
            }
        }
    }
    
    @Override
    public NeuralNetwork train(File csvFile) {
        double globalError = Double.POSITIVE_INFINITY;
        while (globalError > 0.001f) {
            // iterate throught all samples 
            try {
            CSVReader reader = null;
            reader = new CSVReader(new FileReader(csvFile));
            String[] nextLine;
                try {
                    while ((nextLine = reader.readNext()) != null) {
                        if (nextLine.length - 1 != numberOfInputs) {
                            throw new IllegalArgumentException("Number of inputs does not equal to number of input neurons!");
                        }
                        List<Double> input = Arrays.stream(nextLine).map(Double::parseDouble).collect(Collectors.toList());
                        List<Double> results = getResult(input.subList(0, numberOfInputs - 1));  //forward propagation
                        List<Double> targetValues = input.subList(numberOfInputs, input.size()-1);
                        
                        List<Double> outputError = new ArrayList<>();
                        outputError = computeOutputError(results, targetValues);
                        backpropagation(outputError);
                        
                        // compute global error throught all samples from dataset
                        globalError += computeSquaredErrorFunction(results, targetValues);            
                    }
                } catch (IOException ex) {
                    Logger.getLogger(BlackJackMLP.class.getName()).log(Level.SEVERE, null, ex);
                }         
            } catch (FileNotFoundException ex) {
            Logger.getLogger(BlackJackMLP.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            Files.write(Paths.get(file), lines);
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
                                   List<Integer> numberOfNeuronsInHiddenLayers, Integer learningSpeed) {
        numberOfInputs = numberOfInputNeurons;
        outputLayer = new Layer(numberOfOutputNeurons);
        hiddenLayers = new ArrayList<Layer>() {{
            numberOfNeuronsInHiddenLayers.forEach(numberOfNeurons -> add(new Layer(numberOfNeurons)));
        }};
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
            help = help.substring(0, help.length()-1);
            help += ", ";
        });
    }
}
