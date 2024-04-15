package zoo.simulation.Jproject;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Giraffe implements AnimalFeedInterface<Giraffe>, HealthFallInterface<Giraffe>, Cloneable
{
	

	public String animalType;
	private String animalName;
	public int animalCount;
	private String healthStatus;
	private BigDecimal healthRate;
	private BigDecimal maxWithhold;
	private BigDecimal maxHealthCap;

	public String reportHealthStatus(BigDecimal currentHealth, BigDecimal maxHealthCap, BigDecimal maxWithhold) {
	    return (currentHealth.compareTo(maxHealthCap.multiply(getMaxWithhold()).divide(BigDecimal.valueOf(100))) < 0) ? "Dead" : "Healthy";
	}
	

	public String getAnimalType() {
		return "Giraffe";
	}

	public List<Giraffe> generateAnimalList(String healthStatus, int animalCount, BigDecimal healthRate,
			BigDecimal maxWithhold, int initialHealth) {
		return IntStream.range(1, animalCount+1)
                .mapToObj(i -> {
                	Giraffe animal = new Giraffe();
                	animal.setAnimalName(getAnimalType()+i);
                    animal.setHealthStatus(healthStatus);
                    animal.setHealthRate(healthRate);
                    animal.setMaxWithhold(maxWithhold);
                    animal.setMaxHealthCap(BigDecimal.valueOf(initialHealth));
                    animal.setAnimalCount(animalCount);
                    return animal;
                })
                .collect(Collectors.toList());
	}
	

	
	@Override
	public BigDecimal deriveAnimalFeed(BigDecimal healthFeedValue, Giraffe e) {

		//derive feed health
		BigDecimal currentHealth = e.getHealthRate().add(healthFeedValue);
		
		return currentHealth;	
	}

	@Override
	public BigDecimal deriveHealthReduction(BigDecimal randomHealthFallValue, Giraffe e) {
		
		return e.getHealthRate().subtract(randomHealthFallValue);
		
	}
	
	@Override
    public Object clone() throws CloneNotSupportedException {
        Giraffe clonedGiraffe = (Giraffe) super.clone();
        // Since BigInteger is immutable, it can be directly assigned
        clonedGiraffe.healthRate = new BigDecimal(this.healthRate.toString());
        clonedGiraffe.maxWithhold = new BigDecimal(this.maxWithhold.toString());
        clonedGiraffe.maxHealthCap = new BigDecimal(this.maxHealthCap.toString());
        return clonedGiraffe;
    }


	public String getAnimalName() {
		return animalName;
	}


	public void setAnimalName(String animalName) {
		this.animalName = animalName;
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


	public BigDecimal getMaxHealthCap() {
		return maxHealthCap;
	}


	public void setMaxHealthCap(BigDecimal maxHealthCap) {
		this.maxHealthCap = maxHealthCap;
	}
	
	

}
