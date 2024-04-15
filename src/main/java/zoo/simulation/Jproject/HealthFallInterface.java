package zoo.simulation.Jproject;

import java.math.BigDecimal;

public interface HealthFallInterface<T> {
	BigDecimal deriveHealthReduction(BigDecimal randomHealthFallValue, T zooAnimals);
}
