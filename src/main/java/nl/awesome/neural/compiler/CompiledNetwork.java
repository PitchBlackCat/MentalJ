package nl.awesome.neural.compiler;

import nl.awesome.neural.neuron.Neuron;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;

public class CompiledNetwork {
    private ArrayList<Tuple> tuples;

    public CompiledNetwork(ArrayList<Tuple> tuples) {
        this.tuples = tuples;
    }

    public double[] feedforward(double[] inputs) {
        tuples.get(0).l = Matrix.from(inputs);

        for (int i = 1; i < tuples.size(); i++) {
            Tuple curr = tuples.get(i);
            Tuple prev = tuples.get(i - 1);

            curr.l = Matrix.map(curr.w.mult(prev.l), Neuron::Sigmoid);
        }

        return unpackLayer(tuples.get(tuples.size() - 1).l);
    }

    public void print() {
        for (int i = 0; i < tuples.size(); i++) {
            Tuple t = tuples.get(i);
            if (t.w != null) Matrix.print("Weights " + (i-1) + "-" + i, t.w);
            Matrix.print("Layer " + i, t.l);
        }
    }

    private double[] unpackLayer(SimpleMatrix l) {
        double[] d = new double[l.numRows()];
        for (int i = 0; i < l.numRows(); i++) {
            d[i] = l.get(i, 0);
        }
        return d;
    }
}
