import java.util.ArrayList;
import java.util.List;

/**
 * @author opontes
 */
public class NeuronNetworkBuilder {
    private List<Layer> layers = new ArrayList<>();

    public NeuronNetworkBuilder() {
    }

    public NeuronNetworkBuilder addLayerWith(int numberOfNeurons){
        layers.add(new Layer(numberOfNeurons));
        return this;
    }

    public NeuronNetworkBuilder neurons(){
        return this;
    }
}
