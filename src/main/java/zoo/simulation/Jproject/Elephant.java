package zoo.simulation.Jproject;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Elephant implements AnimalFeedInterface<Elephant>, HealthFallInterface<Elephant>, Cloneable{
		
	public String animalType;
	private String animalName;
	public int animalCount;
	private String healthStatus;
	private BigDecimal healthRate;
	private BigDecimal maxWithhold;	
	private BigDecimal maxHealthCap;
	
	private boolean initialCall;
	private boolean recovered;
	

	@Override
	public BigDecimal deriveHealthReduction(BigDecimal randomHealthFallValue, Elephant e) {	
		return e.getHealthRate().subtract(randomHealthFallValue);
	}

	@Override
	public BigDecimal deriveAnimalFeed(BigDecimal healthDecreasePtg, Elephant e) {	
		BigDecimal currentHealth = e.getHealthRate().add(healthDecreasePtg);		
		return currentHealth;	
	}

	@Override
    public Object clone() throws CloneNotSupportedException {
        Elephant clonedElephant = (Elephant) super.clone();
        // Since BigDecimal is immutable, it can be directly assigned
        clonedElephant.healthRate = new BigDecimal(this.healthRate.toString());
        clonedElephant.maxWithhold = new BigDecimal(this.maxWithhold.toString());
        clonedElephant.maxHealthCap = new BigDecimal(this.maxHealthCap.toString());
        return clonedElephant;
    }

	/*
	 * protected String reportHealthStatus() { if(this.getHealthRate() ==
	 * this.getMaxWithhold()) { return alertPoorHealth(); }
	 * 
	 * return (this.getHealthRate().compareTo(this.getMaxWithhold()) < 0) ? "Dead" :
	 * "Healthy"; }
	 */
	
	public String reportHealthStatus(BigDecimal currentHealth, BigDecimal maxHealthCap, BigDecimal maxWithhold) {
	    if (currentHealth.compareTo(maxHealthCap.multiply(maxWithhold).divide(BigDecimal.valueOf(100))) < 0) {
	    		  
	    	if(this.initialCall) {
	    		this.initialCall = false;
	    		this.recovered = true;
	    		return "Cannot walk";
	    	}
	    	else if(recovered){
	    		this.recovered = false;
	    		return "Dead - Health did not recover";
	    	}
	    	else {
	    		return "Dead";
	    	}
	    }
	    else {
	    	this.initialCall = true;
	    }
	    return (currentHealth.compareTo(maxWithhold) < 0) ? "cannot walk" : "Healthy";
	}


	public List<Elephant> generateAnimalList(String healthStatus, int animalCount, BigDecimal healthRate,
			BigDecimal maxWithhold, int initialHealth) {
		return IntStream.range(1, animalCount+1)
                .mapToObj(i -> {
                    Elephant animal = new Elephant();
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
	

	public String getAnimalType() {
		return "Elephant";
	}

	public BigDecimal getMaxHealthCap() {
		return maxHealthCap;
	}

	public void setMaxHealthCap(BigDecimal maxHealthCap) {
		this.maxHealthCap = maxHealthCap;
	}
	
	

}
