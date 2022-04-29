package backend;

import java.io.Serializable;
import java.util.function.Function;

public interface SerializableFunction extends Function<Double, Double>, Serializable {
}
