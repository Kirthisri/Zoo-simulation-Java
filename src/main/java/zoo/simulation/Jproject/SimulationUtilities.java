package zoo.simulation.Jproject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class SimulationUtilities {
	
    public static BigDecimal getRandomBigDecimal(BigDecimal min, BigDecimal max) {
        Random random = new Random();
        int scale = Math.max(min.scale(), max.scale());
        BigDecimal randomBigDecimal = min.add(new BigDecimal(random.nextDouble())
                .multiply(max.subtract(min)));
        return randomBigDecimal.setScale(scale, RoundingMode.HALF_UP);
    }

}
