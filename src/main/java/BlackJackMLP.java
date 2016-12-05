import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
<<<<<<< 20330b99aed63fafbd95d11806f0b1d882166693
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
=======
import java.io.FileReader;
import java.io.IOException;
>>>>>>> Read input data as csv file
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author opontes
 */
public class BlackJackMLP implements NeuralNetwork {
    private Integer numberOfInputs;
    private Layer outputLayer;
    private List<Layer> hiddenLayers;
    private Integer learning;
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

    @Override
    public NeuralNetwork train(File csvFile) {
        try {
            CSVReader reader = null;
            reader = new CSVReader(new FileReader(csvFile));
            String [] nextLine;
            try {
                while ((nextLine = reader.readNext()) != null) {
                    // nextLine[] is an array of values from the line

                    System.out.println(nextLine[0] + nextLine[1] + "etc...");
                }
            } catch (IOException ex) {
                Logger.getLogger(BlackJackMLP.class.getName()).log(Level.SEVERE, null, ex);
            }         
            return this;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BlackJackMLP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    @Override
    public NeuralNetwork save(String file) {
        List<String> lines = new ArrayList<>();
        lines.add(numberOfInputs.toString() + " " + outputLayer.size() + " " + learning);
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
        setLayers(numberOfInputs, outputLayer.size(), hidden, learning);
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
