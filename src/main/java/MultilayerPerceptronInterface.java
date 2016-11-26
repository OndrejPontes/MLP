import java.io.File;
import java.util.List;

/**
 * @author opontes
 */
public interface MultilayerPerceptronInterface {
    /**
     * Takes data from csv file and set layers with weights
     * @param file csv file with information about weights and layers
     * @return itself and you can give another command
     */
    MultilayerPerceptronInterface initialize(File file);

    /**
     * Trains Neural Network with data saved into csv file
     * @param csvFile file with saved data
     * @return itself and you can give another command
     */
    MultilayerPerceptronInterface train(File csvFile);

    /**
     * Saves information about weights and layers into file with given name
     * @param nameOfFile name of file
     * @return itself and you can give another command
     */
    MultilayerPerceptronInterface save(String nameOfFile);

    /**
     * Puts data into Neural Network and return some result
     * @param data to put into Neural Network
     * @return result of computation
     */
    double getResult(List<Double> data);

    /**
     * Set layers and weights, this method is primary intend for comparison different structures of layers and neurons
     * @param numberOfInputs number of inputs for each neuron in input layer
     * @param numberOfHiddenLayers number of hidden layers
     * @param numberOfNeuronsInLayers number of neurons in each hidden layer, order must be from input to output layer
     * @return itself and you can give another command
     */
    MultilayerPerceptronInterface setLayers(Integer numberOfInputs, Integer numberOfHiddenLayers, List<Integer> numberOfNeuronsInLayers);
}
