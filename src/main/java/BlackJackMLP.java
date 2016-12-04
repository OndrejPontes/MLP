import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author opontes
 */
public class BlackJackMLP implements NeuralNetwork {
    private Integer inputLayer;
    private Layer outputLayer;
    private List<Layer> hiddenLayers;

    public BlackJackMLP() {
        setLayers(10, 1, Collections.emptyList());
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
    public List<Double> getResult(List<Double> data) {
        if(data.size() != inputLayer) throw new IllegalArgumentException();

        for (Layer layer : hiddenLayers) {
            data = layer.evaluate(data);
        }

        return outputLayer.evaluate(data);
    }

    @Override
    public NeuralNetwork setLayers(Integer numberOfInputNeurons, Integer numberOfOutputNeurons, List<Integer> numberOfNeuronsInHiddenLayers) {
        inputLayer = numberOfInputNeurons;
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
}
