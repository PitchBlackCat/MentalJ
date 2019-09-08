package nl.awesome.neural;

import nl.awesome.neural.neuron.Neuron;

public class Gene {

    public static int MaxInnovation = 0;

    public Neuron In;
    public Neuron Out;
    public double Weight;
    public Boolean Enabled;
    public int Innovation;

    public double getValue()
    {
        return In.getValue() * Weight;
    }

}
