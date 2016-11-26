import enums.ActivationFunction;

import java.util.ArrayList;
import java.util.List;

import static enums.ActivationFunction.SIGMOID;

/**
 * @author opontes
 */
public class NeuronNetworkBuilder {
    private List<Layer> layers = new ArrayList<>();
    private ActivationFunction activationFunction = SIGMOID;

    public NeuronNetworkBuilder() {
    }

    public NeuronNetworkBuilder addLayerWith(int numberOfNeurons) {
        layers.add(new Layer(numberOfNeurons));
        return this;
    }

    public NeuronNetworkBuilder neurons() {
        return this;
    }

    public List<Layer> build() {
        return layers;
    }
}
