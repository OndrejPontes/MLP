import another.implementation.Mlp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author opontes
 */
public class NeuronNetworkBuilder {
    public static void main(String[] args) {
        anotherImplementation();
//        firstImplementation();
    }

    private static void anotherImplementation(){

        // initialization
        ArrayList<List<Double>> ex = new ArrayList<>();
        ArrayList<List<Double>> out = new ArrayList<>();
//        for (int i = 0; i < 4; ++i) {
//            ex.add(new ArrayList<>());
//            out.add(new ArrayList<>());
//        }

        // fill the examples database
        ex.add(new ArrayList<Double>(){{ add(-1d); add(1d); }});
//        ex.get(0).set(0, -1d);
//        ex.get(0).set(1, 1d);
        out.add(new ArrayList<Double>(){{ add(1d); }});
//        out.get(0).set(0, 1d);
        ex.add(new ArrayList<Double>(){{ add(1d); add(1d); }});
//        ex.get(1).set(0, 1d);
//        ex.get(1).set(1, 1d);
        out.add(new ArrayList<Double>(){{ add(-1d); }});
//        out.get(1).set(0, -1d);
        ex.add(new ArrayList<Double>(){{ add(1d); add(-1d); }});
//        ex.get(2).set(0, 1d);
//        ex.get(2).set(1, -1d);
        out.add(new ArrayList<Double>(){{ add(1d); }});
//        out.get(2).set(0, 1d);
        ex.add(new ArrayList<Double>(){{ add(-1d); add(-1d); }});
//        ex.get(3).set(0, -1d);
//        ex.get(3).set(1, -1d);
        out.add(new ArrayList<Double>(){{ add(-1d); }});
//        out.get(3).set(0, -1d);

        List<Integer> layerInfo = new ArrayList<Integer>(){{ add(ex.get(0).size()); add(ex.get(0).size() * 3); add(1); }};
//        { // kde kazda hodnota predstavuje vrstvu, prva hodnota je input posledna output a vsetko medzi tym je hidden
//                ex.get(0).getNeuronsNumber(),    // layer 1: input layer - 2 neurons
//                ex.get(0).getNeuronsNumber() * 3,    // layer 2: hidden layer - 6 neurons
//                1            // layer 3: output layer - 1 neuron
//        };

        Mlp mlp = new Mlp(layerInfo); // Inicializacia

        try {
            PrintWriter fout = new PrintWriter(new FileWriter("plot.dat"));
            fout.println("#\tX\tY");

            for (int i = 0; i < 40000; ++i) {
                mlp.learn(out, ex, 0.3d);
                double error = mlp.evaluateError(ex, out);
                System.out.println(i + " -> error : " + error);
                fout.println("\t" + i + "\t" + error);
            }

            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void firstImplementation(){
        File f = new File("data/xor.csv");
        NeuralNetwork neuralNetwork = new BlackJackMLP()
//                .initialize("");
                .setLayers(2, 1, Collections.emptyList(), 1d)
//                .setLayers(2, 1, new ArrayList<Integer>(){{add(5);}}, 1d)
                .train(f);
        System.out.println(neuralNetwork.getResult(Arrays.asList(1d, 1d)));
        System.out.println(neuralNetwork.getResult(Arrays.asList(1d, 0d)));
        System.out.println(neuralNetwork.getResult(Arrays.asList(0d, 1d)));
        System.out.println(neuralNetwork.getResult(Arrays.asList(0d, 0d)));

        neuralNetwork.save("TestFile1.csv");
    }
}
