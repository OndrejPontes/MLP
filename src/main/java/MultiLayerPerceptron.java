import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.List;

/**
 * @author opontes
 */
public class MultiLayerPerceptron implements NeuralNetwork {
    private Layer inputLayer;
    private Layer outputLayer;
    private List<Layer> hiddenLayers;

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
    public double getResult(List<Double> data) {
        throw new NotImplementedException();
    }

    @Override
    public NeuralNetwork setLayers(Integer numberOfInputNeurons, Integer numberOfOutputNeurons, Integer numberOfHiddenLayers, List<Integer> numberOfNeuronsInLayers) {
        throw new NotImplementedException();
    }


}
