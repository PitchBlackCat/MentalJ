package nl.awesome.neural.compiler;

import org.ejml.simple.SimpleMatrix;

public class Tuple {
    SimpleMatrix w;
    SimpleMatrix l;
    SimpleMatrix b;

    public Tuple(SimpleMatrix w, SimpleMatrix l, SimpleMatrix b) {
        this.w = w;
        this.l = l;
        this.b = b;
    }
}
