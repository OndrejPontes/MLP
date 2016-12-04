import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author opontes
 */
public class BlackJackMLP implements NeuralNetwork {
    private InputLayer inputLayer;
    private Layer outputLayer;
    private List<Layer> hiddenLayers;

    public BlackJackMLP() {
        setLayers(5, 1, Collections.emptyList());
    }

    @Override
    public NeuralNetwork initialize(File file) {
        throw new NotImplementedException();
    }

    @Override
    public NeuralNetwork train(File csvFile) {
        throw new NotImplementedException();
    }

    @Override
    public NeuralNetwork save(String nameOfFile) {
        throw new NotImplementedException();
    }

    @Override
    public List<Double> getResult(List<List<Double>> data) {
        throw new NotImplementedException();
    }

    @Override
    public NeuralNetwork setLayers(Integer numberOfInputNeurons, Integer numberOfOutputNeurons, List<Integer> numberOfNeuronsInHiddenLayers) {
        inputLayer = new InputLayer(numberOfInputNeurons);
        outputLayer = new Layer(numberOfOutputNeurons);
        hiddenLayers = new ArrayList<Layer>() {{
            numberOfNeuronsInHiddenLayers.forEach(numberOfNeurons -> add(new Layer(numberOfNeurons)));
        }};
        return this;
    }

    @Override
    public NeuralNetwork print() {
        inputLayer.print();
        throw new NotImplementedException();
    }
}
