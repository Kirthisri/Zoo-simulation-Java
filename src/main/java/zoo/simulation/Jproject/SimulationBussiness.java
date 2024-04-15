package zoo.simulation.Jproject;

import java.math.BigDecimal;

public class SimulationBussiness {

	public static boolean getTimeToFeed(String animalName, String healthStatus, int minFeedValue, BigDecimal remainingFeedRate) {
		if (remainingFeedRate.compareTo(BigDecimal.valueOf(minFeedValue)) < 0) {
			System.out.println("cannot feed " + animalName + ", attained its max Health");
			return false;
		} 
		return true;
	}

	public static BigDecimal generateRandomFeedValue(int minFeedValue, int maxFeedValue, BigDecimal remainingFeedRate) {
		
		BigDecimal randomFeedOfElephant;
		
		if (remainingFeedRate.compareTo(BigDecimal.valueOf(minFeedValue).min(BigDecimal.valueOf(maxFeedValue))) >= 0 
        		&& remainingFeedRate.compareTo(BigDecimal.valueOf(minFeedValue).max(BigDecimal.valueOf(maxFeedValue))) <= 0) {
        	randomFeedOfElephant = SimulationUtilities.getRandomBigDecimal(BigDecimal.valueOf(minFeedValue), remainingFeedRate);
        } else {
        	randomFeedOfElephant = SimulationUtilities.getRandomBigDecimal(BigDecimal.valueOf(minFeedValue), BigDecimal.valueOf(maxFeedValue));
        }
		return randomFeedOfElephant;
	}

}
