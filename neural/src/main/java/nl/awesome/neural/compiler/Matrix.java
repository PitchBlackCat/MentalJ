package nl.awesome.neural.compiler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ejml.simple.SimpleMatrix;

import java.util.Arrays;
import java.util.function.Function;

public class Matrix {
    public static final Logger logger = LogManager.getLogger(Matrix.class);

    public static SimpleMatrix from(double[] arr) {
        return new SimpleMatrix(toPrimitive(Arrays.stream(arr)
                .boxed()
                .map(d -> new Double[]{d})
                .toArray(Double[][]::new)
        ));
    }

    public static SimpleMatrix from(double[][] arr) {
        return new SimpleMatrix(arr);
    }

    public static SimpleMatrix from(int rows) {
        return from(rows, 1);
    }

    public static SimpleMatrix from(int rows, int columns) {
        return new SimpleMatrix(new double[rows][columns]);
    }

    public static SimpleMatrix from(Double[] arr) {
        return new SimpleMatrix(toPrimitive(Arrays.stream(arr)
                .map(d -> new Double[]{d})
                .toArray(Double[][]::new)
        ));
    }

    public static SimpleMatrix from(Double[][] arr) {
        return from(toPrimitive(arr));
    }

    private static double[] toPrimitive(Double[] array) {
        if (array == null) return null;
        else if (array.length == 0) return new double[0];

        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    private static double[][] toPrimitive(Double[][] array) {
        if (array == null) return null;
        else if (array.length == 0) return new double[0][];

        final double[][] result = new double[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = toPrimitive(array[i]);
        }
        return result;
    }

    public static void print(String label, SimpleMatrix m) {
        logger.info("|= {}x{} - {}", m.numRows(), m.numCols(), label);
        for (int y = 0; y < m.numRows(); y++) {
            StringBuilder b = new StringBuilder();
            for (int x = 0; x < m.numCols(); x++) {
                b.append(String.format("|%5.5s", m.get(y, x)));
            }
            b.append("|");
            logger.info(b);
        }
    }

    public static SimpleMatrix map(SimpleMatrix m, Function<Double, Double> p) {
        for (int c = 0; c < m.numCols(); c++) {
            for (int r = 0; r < m.numRows(); r++) {
                m.set(r, c, p.apply(m.get(r, c)));
            }
        }
        return m;
    }
}
