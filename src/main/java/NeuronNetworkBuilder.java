import java.util.Arrays;

/**
 * @author opontes
 */
public class NeuronNetworkBuilder {
    public static void main(String[] args) {
        NeuralNetwork neuralNetwork = new BlackJackMLP()
                .setLayers(2, 4, Arrays.asList(1, 2, 3, 4, 5, 6), 5);

        neuralNetwork.getResult(Arrays.asList(2d, 3d));

        neuralNetwork.save("TestFile.csv");

    }
}
