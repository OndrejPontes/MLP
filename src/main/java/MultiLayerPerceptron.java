import java.io.File;
import java.util.List;

/**
 * @author opontes
 */
public class MultiLayerPerceptron implements Neuralnetwork {
    private Layer inputLayer;
    private Layer outputLayer;
    private List<Layer> hiddenLayers;

    @Override
    public Neuralnetwork initialize(File file) {
        return null;
    }

    @Override
    public Neuralnetwork train(File csvFile) {
        return null;
    }

    @Override
    public Neuralnetwork save(String nameOfFile) {
        return null;
    }

    @Override
    public double getResult(List<Double> data) {
        return 0;
    }

    @Override
    public Neuralnetwork setLayers(Integer numberOfInputs, Integer numberOfHiddenLayers, List<Integer> numberOfNeuronsInLayers) {
        return null;
    }
}
