import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author opontes
 */
public class NeuronNetworkBuilder {
    public static void main(String[] args) {
        File f = new File("data/xor.csv");
        NeuralNetwork neuralNetwork = new BlackJackMLP()
//                .setLayers(2, 1, Collections.emptyList(), 1d)
                .setLayers(2, 1, new ArrayList<Integer>(){{add(5);}}, 1d)
                .train(f);
        System.out.println(neuralNetwork.getResult(Arrays.asList(1d, 1d)));
        System.out.println(neuralNetwork.getResult(Arrays.asList(1d, 0d)));
        System.out.println(neuralNetwork.getResult(Arrays.asList(0d, 1d)));
        System.out.println(neuralNetwork.getResult(Arrays.asList(0d, 0d)));

        neuralNetwork.save("TestFile1.csv");
    }
}
