package zoo.simulation.Jproject;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Monkey implements AnimalFeedInterface<Monkey>, HealthFallInterface<Monkey>, Cloneable {

	private String animalType;
	private String animalName;
	private int animalCount;
	private String healthStatus;
	private BigDecimal healthRate;
	private BigDecimal maxWithhold;
	private BigDecimal maxHealthCap;

	public String reportHealthStatus(BigDecimal currentHealth, BigDecimal maxHealthCap, BigDecimal maxWithhold) {
	    return (currentHealth.compareTo(maxHealthCap.multiply(getMaxWithhold()).divide(BigDecimal.valueOf(100))) < 0) ? "Dead" : "Healthy";
	}

	public String getAnimalType() {
		return "Monkey";
	}

	public List<Monkey> generateAnimalList(String healthStatus, int animalCount, BigDecimal healthRate,
			BigDecimal maxWithhold, int initialHealth) {
		return IntStream.range(1, animalCount + 1).mapToObj(i -> {
			Monkey animal = new Monkey();
			animal.setAnimalName(getAnimalType() + i);
			animal.setHealthStatus(healthStatus);
			animal.setHealthRate(healthRate);
			animal.setMaxWithhold(maxWithhold);
			animal.setMaxHealthCap(BigDecimal.valueOf(initialHealth));
			return animal;
		}).collect(Collectors.toList());
	}

	@Override
	public BigDecimal deriveAnimalFeed(BigDecimal healthFeedValue, Monkey e) {

		// derive feed health
		BigDecimal currentHealth = e.getHealthRate().add(healthFeedValue);

		return currentHealth;
	}

	@Override
	public BigDecimal deriveHealthReduction(BigDecimal randomHealthFallValue, Monkey e) {
		return e.getHealthRate().subtract(randomHealthFallValue);
	}
	
	@Override
    public Object clone() throws CloneNotSupportedException {
        Monkey clonedMonkey = (Monkey) super.clone();
        // Since BigInteger is immutable, it can be directly assigned
        clonedMonkey.healthRate = new BigDecimal(this.healthRate.toString());
        clonedMonkey.maxWithhold = new BigDecimal(this.maxWithhold.toString());
        clonedMonkey.maxHealthCap = new BigDecimal(this.maxHealthCap.toString());
        return clonedMonkey;
    }

	public String getAnimalName() {
		return animalName;
	}

	public void setAnimalName(String animalName) {
		this.animalName = animalName;
	}

	public int getAnimalCount() {
		return animalCount;
	}

	public void setAnimalCount(int animalCount) {
		this.animalCount = animalCount;
	}

	public String getHealthStatus() {
		return healthStatus;
	}

	public void setHealthStatus(String healthStatus) {
		this.healthStatus = healthStatus;
	}

	public BigDecimal getHealthRate() {
		return healthRate;
	}

	public void setHealthRate(BigDecimal healthRate) {
		this.healthRate = healthRate;
	}

	public BigDecimal getMaxWithhold() {
		return maxWithhold;
	}

	public void setMaxWithhold(BigDecimal maxWithhold) {
		this.maxWithhold = maxWithhold;
	}

	public void setAnimalType(String animalType) {
		this.animalType = animalType;
	}

	public BigDecimal getMaxHealthCap() {
		return maxHealthCap;
	}

	public void setMaxHealthCap(BigDecimal maxHealthCap) {
		this.maxHealthCap = maxHealthCap;
	}

	
}
