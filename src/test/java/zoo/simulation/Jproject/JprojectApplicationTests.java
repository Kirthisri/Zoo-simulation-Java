package zoo.simulation.Jproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JprojectApplicationTests {
	
	static JprojectApplication application;
	static Elephant elephantModel;
	static Giraffe giraffeModel;
	static Monkey monkeyModel;
	
	static int initialHealth;
	
	@BeforeAll
	static void init() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		application = new JprojectApplication();
		elephantModel = new Elephant();
		giraffeModel = new Giraffe();
		monkeyModel = new Monkey();
		
        Field simulationRunningField = JprojectApplication.class.getDeclaredField("initialHealth");
        simulationRunningField.setAccessible(true);
        initialHealth = (int) simulationRunningField.get(null);
		
	}
	
	@Test
	void testWrongApplicationInput() {
		String[] str = {"0","20","10","25"};
		
		// Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        	// Invoke the method
            JprojectApplication.main(str);
        });
        
        // Assert
        assertEquals("Invalid arguments. Please provide minHealthFall, maxHealthFall, minFeedValue, maxFeedValue, initialHealth.", exception.getMessage());
    
	}
	
	@Test
	void calculateAnimalCount() {
		//parameters - String healthStatus, int animalCount, BigInteger healthRate, BigInteger maxWithhold, int initialHealth
		List<Elephant> elephantList = elephantModel.generateAnimalList("Healthy", 5, BigDecimal.valueOf(100), BigDecimal.valueOf(70), 100);
		//expected vs actual
		assertEquals(5, elephantList.size());
		
		List<Giraffe> giraffeList = giraffeModel.generateAnimalList("Healthy", 5, BigDecimal.valueOf(100), BigDecimal.valueOf(70), 100);
		//expected vs actual
		assertEquals(5, elephantList.size());
		
		List<Monkey> monkeyList = monkeyModel.generateAnimalList("Healthy", 5, BigDecimal.valueOf(100), BigDecimal.valueOf(70), 100);
		//expected vs actual
		assertEquals(5, elephantList.size());
		assertEquals(5, giraffeList.size());
		assertEquals(5, monkeyList.size());
	}
	
	@Test
	void testRandomValuePick() {
		BigDecimal min = BigDecimal.valueOf(10);
		BigDecimal max = BigDecimal.valueOf(25);
		
		BigDecimal randomBigD = SimulationUtilities.getRandomBigDecimal(min, max);
		assertTrue(randomBigD.compareTo(min) >= 0 && randomBigD.compareTo(max) <= 0);
	}
	
	@Test
	void testAnimalsHealthFall() throws CloneNotSupportedException {
		List<Elephant> elephantList = elephantModel.generateAnimalList("Healthy", 5, BigDecimal.valueOf(100), BigDecimal.valueOf(70), 100);
		
		List<Elephant> elephantListInitial = new ArrayList<>();
		for (Elephant elephant : elephantList) {
		    Elephant newElephant = (Elephant) elephant.clone();
		    elephantListInitial.add(newElephant);
		}
		
		List<Giraffe> giraffeList = giraffeModel.generateAnimalList("Healthy", 5, BigDecimal.valueOf(100), BigDecimal.valueOf(70), 100);
		
		List<Giraffe> giraffeListInitial = new ArrayList<>();
		for (Giraffe giraffe : giraffeList) {
			Giraffe newElephant = (Giraffe) giraffe.clone();
			giraffeListInitial.add(newElephant);
		}
		
		List<Monkey> monkeyList = monkeyModel.generateAnimalList("Healthy", 5, BigDecimal.valueOf(100), BigDecimal.valueOf(70), 100);
		
		List<Monkey> monkeyListInitial = new ArrayList<>();
		for (Monkey monkey : monkeyList) {
			Monkey newElephant = (Monkey) monkey.clone();
			monkeyListInitial.add(newElephant);
		}
		
		// Invoke the method
		JprojectApplication.simulateHealthFall(elephantList, giraffeList, monkeyList, BigDecimal.valueOf(15));
				
		//expected vs actual
		assertTrue(elephantList.get(0).getHealthRate().compareTo(elephantListInitial.get(0).getHealthRate()) < 0);
		assertTrue(giraffeList.get(0).getHealthRate().compareTo(giraffeListInitial.get(0).getHealthRate()) < 0);
		assertTrue(monkeyList.get(0).getHealthRate().compareTo(monkeyListInitial.get(0).getHealthRate()) < 0);
	}
	
	
	@Test
	void testElephantsHealthStatus() {
		//parameter - BigInteger currentHealth, BigInteger maxHealthCap, BigInteger maxWithhold
		String healthyReport = elephantModel.reportHealthStatus(BigDecimal.valueOf(80), BigDecimal.valueOf(100), BigDecimal.valueOf(70));
	
		//expected vs actual
		assertEquals("Healthy", healthyReport);
		
		// Invoke the method
		//parameter - BigInteger currentHealth, BigInteger maxHealthCap, BigInteger maxWithhold
		String poorHealthReport = elephantModel.reportHealthStatus(BigDecimal.valueOf(65), BigDecimal.valueOf(100), BigDecimal.valueOf(70));
		
		//expected vs actual
		assertEquals("Cannot walk", poorHealthReport);
		
		// Invoke the method
		//parameter - BigInteger currentHealth, BigInteger maxHealthCap, BigInteger maxWithhold
		String healthToRecoverReport = elephantModel.reportHealthStatus(BigDecimal.valueOf(40), BigDecimal.valueOf(100), BigDecimal.valueOf(70));
		
		//expected vs actual
		assertEquals("Dead - Health did not recover", healthToRecoverReport);
		
		// Invoke the method
		//parameter - BigInteger currentHealth, BigInteger maxHealthCap, BigInteger maxWithhold
		String deathReport = elephantModel.reportHealthStatus(BigDecimal.valueOf(32), BigDecimal.valueOf(100), BigDecimal.valueOf(70));
		
		//expected vs actual
		assertEquals("Dead", deathReport);
		
	}
	
	@Test
	void testHealthIncreaseForFeeds() throws CloneNotSupportedException {
		//parameters - String healthStatus, int animalCount, BigInteger healthRate, BigInteger maxWithhold, int initialHealth
		List<Elephant> elephantList = elephantModel.generateAnimalList("Healthy", 5, BigDecimal.valueOf(50), BigDecimal.valueOf(70), 100);
		
		List<Elephant> elephantListInitial = new ArrayList<>();
		for (Elephant elephant : elephantList) {
		    Elephant newElephant = (Elephant) elephant.clone();
		    elephantListInitial.add(newElephant);
		}
		
		List<Giraffe> giraffeList = giraffeModel.generateAnimalList("Healthy", 5, BigDecimal.valueOf(25), BigDecimal.valueOf(50), 100);
		
		List<Giraffe> giraffeListInitial = new ArrayList<>();
		for (Giraffe giraffe : giraffeList) {
			Giraffe newElephant = (Giraffe) giraffe.clone();
			giraffeListInitial.add(newElephant);
		}
		
		List<Monkey> monkeyList = monkeyModel.generateAnimalList("Healthy", 5, BigDecimal.valueOf(10), BigDecimal.valueOf(30), 100);
		
		List<Monkey> monkeyListInitial = new ArrayList<>();
		for (Monkey monkey : monkeyList) {
			Monkey newElephant = (Monkey) monkey.clone();
			monkeyListInitial.add(newElephant);
		}
		
		// Invoke the method
		//parameters - List<Elephant> elephants, List<Giraffe> giraffes, List<Monkey> monkeys, 
		//int initialHealth, int minFeedValue, int maxFeedValue
		JprojectApplication.feedAnimals(elephantList, giraffeList, monkeyList, 100, 10, 25);
		
		//expected vs actual
		assertTrue(elephantList.get(0).getHealthRate().compareTo(elephantListInitial.get(0).getHealthRate()) >= 0);
		assertTrue(giraffeList.get(0).getHealthRate().compareTo(giraffeListInitial.get(0).getHealthRate()) >= 0);
		assertTrue(monkeyList.get(0).getHealthRate().compareTo(monkeyListInitial.get(0).getHealthRate()) >= 0);
	}
	
	@Test
	void testHealthIncreaseWithinLimit() throws CloneNotSupportedException {
		//parameters - String healthStatus, int animalCount, BigInteger healthRate, BigInteger maxWithhold, int initialHealth
		List<Elephant> elephantList = elephantModel.generateAnimalList("Healthy", 5, BigDecimal.valueOf(88), BigDecimal.valueOf(70), 100);
		
		List<Elephant> elephantListInitial = new ArrayList<>();
		for (Elephant elephant : elephantList) {
		    Elephant newElephant = (Elephant) elephant.clone();
		    elephantListInitial.add(newElephant);
		}
		
		List<Giraffe> giraffeList = giraffeModel.generateAnimalList("Healthy", 5, BigDecimal.valueOf(90), BigDecimal.valueOf(50), 100);
		
		List<Giraffe> giraffeListInitial = new ArrayList<>();
		for (Giraffe giraffe : giraffeList) {
			Giraffe newElephant = (Giraffe) giraffe.clone();
			giraffeListInitial.add(newElephant);
		}
		
		List<Monkey> monkeyList = monkeyModel.generateAnimalList("Healthy", 5, BigDecimal.valueOf(91), BigDecimal.valueOf(30), 100);
		
		List<Monkey> monkeyListInitial = new ArrayList<>();
		for (Monkey monkey : monkeyList) {
			Monkey newElephant = (Monkey) monkey.clone();
			monkeyListInitial.add(newElephant);
		}
		
		// Invoke the method
		//parameters - List<Elephant> elephants, List<Giraffe> giraffes, List<Monkey> monkeys, 
		//int initialHealth, int minFeedValue, int maxFeedValue
		JprojectApplication.feedAnimals(elephantList, giraffeList, monkeyList, 100, 10, 25);
		
		BigDecimal maxHealthCap = BigDecimal.valueOf(100);
		
		System.out.println("Health of elephant: " + elephantList.get(0).getHealthRate());
		System.out.println("Health of giraffe: " + giraffeList.get(0).getHealthRate());
		System.out.println("Health of monkey: " + monkeyList.get(0).getHealthRate());
		
		assertFalse(elephantList.get(0).getHealthRate().compareTo(maxHealthCap) > 0);
		assertFalse(giraffeList.get(0).getHealthRate().compareTo(maxHealthCap) > 0);
		assertFalse(monkeyList.get(0).getHealthRate().compareTo(maxHealthCap) > 0);
	}
	
	@Test
    void testMessageOfAnimalHealth() {
        
		//parameters - String healthStatus, int animalCount, BigInteger healthRate, BigInteger maxWithhold, int initialHealth
		List<Elephant> elephantList = elephantModel.generateAnimalList("Healthy", 1, BigDecimal.valueOf(100), BigDecimal.valueOf(70), 100);
				
		List<Giraffe> giraffeList = giraffeModel.generateAnimalList("Healthy", 1, BigDecimal.valueOf(100), BigDecimal.valueOf(70), 100);
				
		List<Monkey> monkeyList = monkeyModel.generateAnimalList("Healthy", 1, BigDecimal.valueOf(100), BigDecimal.valueOf(70), 100);
		
        // Redirect System.out to capture printed output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Invoke the method
        JprojectApplication.updateHealthStatusOfAnimals(elephantList, giraffeList, monkeyList);

        // Restore original System.out
        System.setOut(originalOut);

        // Capture the printed output
        String printedOutput = outputStream.toString();

        // Assert the output
        // Example assertions, replace with your actual expected output
        assertTrue(printedOutput.contains("Elephant1 - Healthy, Current Health : 100"));
        assertTrue(printedOutput.contains("Giraffe1 - Healthy, Current Health : 100"));
        assertTrue(printedOutput.contains("Monkey1 - Healthy, Current Health : 100"));
    }
	
}
