package zoo.simulation.Jproject;

import java.math.BigDecimal;

public interface AnimalFeedInterface<T> {
	BigDecimal deriveAnimalFeed(BigDecimal healthFeedValue, T zooAnimals);
}
