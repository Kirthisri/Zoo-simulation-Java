package zoo.simulation.Jproject;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class JprojectApplication {

	static int minHealthFall;
	static int maxHealthFall;
	static int minFeedValue;
	static int maxFeedValue;
	static int initialHealth;
	static int timePassRate;

	private static boolean simulationRunning = false;

	static List<Elephant> elephants = new ArrayList<>();
	static List<Giraffe> Giraffes = new ArrayList<>();
	static List<Monkey> Monkeys = new ArrayList<>();

	private static final Object lock = new Object();
	
	static LocalTime zooTime = LocalTime.now();
	private static int SIMULATION_TIME_HOURS = 0;
	
	static Scanner scanner = new Scanner(System.in);

	/**
	 * -----------------------------------------------------------------------------------------------------------------------------------
	 * The program contains threads to perform
	 * 
	 * 1. inputThread - To get user Input 
	 * 2. startZooSimulation - to start simulation
	 * 
	 * 
	 * The program contains following methods
	 * 
	 * 1. generateElephants - Generates list of elephant objects 
	 * 2. generateGiraffe - Generate list of giraffe objects 
	 * 3. generateMonkeys - Generate list of monkey objects 
	 * 4. updateHealthStatusOfAnimals - prints health status of all animals - called after feed, simulation 
	 * 5. startSimulation - sets simulationRunning boolean true 
	 * 6. simulateHealthFall - simulate the periodical health fall 
	 * 7. feedAnimals - simulate feed for individual animal
	 * type
	 * 
	 * The Program calls Business utility class SimulationBusiness. For
	 * 
	 * 1. Generate random BigInteger value between min/max feed value - based on logics 
	 * 2. Predict the user can feed/not
	 * 
	 * The Program calls Simulation utility class SimulationUtilities. For
	 * 
	 * 1. Generate random BigInteger value between min/max value
	 */

	/**
	 * Main method starting the simulation provide environmental variable in this
	 * format minHealthFall,maxHealthFall,minFeedValue,maxFeedValue,initialHealth
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 6) {
            throw new IllegalArgumentException(
                    "Invalid arguments. Please provide minHealthFall, maxHealthFall, minFeedValue, maxFeedValue, initialHealth.");
    
        }
		
		minHealthFall = Integer.parseInt(args[0].split("=")[1]);
		maxHealthFall = Integer.parseInt(args[1].split("=")[1]);
		minFeedValue = Integer.parseInt(args[2].split("=")[1]);
		maxFeedValue = Integer.parseInt(args[3].split("=")[1]);
		initialHealth = Integer.parseInt(args[4].split("=")[1]);
		timePassRate = Integer.parseInt(args[5].split("=")[1]);

		// generate list of zoo animals in its healthy state
		elephants = generateElephants();
		Giraffes = generateGiraffe();
		Monkeys = generateMonkeys();

		System.out.println(
				"---------------------------------------------------------------------------------------------------------");
		System.out.println("Welcome to the Zoo simulation !");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------");

		// show the current health status of healthy zoo
		updateHealthStatusOfAnimals(elephants, Giraffes, Monkeys);

		System.out.println("Choose an option:");
		System.out.println("1. Start the simulation");
		System.out.println("2. Feed the animals");
		System.out.println("3. Stop the simulation");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------");

		// Thread to run the user input
		Thread inputThread = new Thread(() -> {
			while (true) {
					int choice = scanner.nextInt();

					switch (choice) {
					case 1:
						if (!simulationRunning) {
							startSimulation();
							break;
						} else {
							System.out.println("Simulation is already running.");
						}
					case 2:
						if (simulationRunning) {
							feedAnimals(elephants, Giraffes, Monkeys, initialHealth, minFeedValue, maxFeedValue);
							updateHealthStatusOfAnimals(elephants, Giraffes, Monkeys);
							break;
						} else {
							System.out.println("Cannot feed animals");
							System.out.println("Enter 1 to start the suimulation");
							System.out.println(
									"---------------------------------------------------------------------------------------------------------");
						}
					case 3:
						System.out.println("Simulaion stopped...");
						simulationRunning = false;
						break;
					default:
						System.out.println("Invalid choice. Please try again.");
					}

				}
			
		});
		inputThread.start();

		// Start a thread to run the simulation
		new Thread(JprojectApplication::startZooSimulation).start();
		
		//scanner.close();
	}


	public static void updateHealthStatusOfAnimals(List<Elephant> elephants, List<Giraffe> giraffes,
			List<Monkey> monkeys) {
		System.out.println(
				"---------------------------------------------------------------------------------------------------------");
		
		simulateZooTime();
        // Format the simulation time as HH:mm:ss
        String formattedTime = zooTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println("Zoo time: " + formattedTime);
        
		System.out.println(
				"---------------------------------------------------------------------------------------------------------");

		synchronized (lock) {
			elephants.forEach(animal -> System.out.println(animal.getAnimalName() + " - " 
					+ animal.reportHealthStatus(animal.getHealthRate(), animal.getMaxHealthCap(), animal.getMaxWithhold())
					+ ", Current Health : " + animal.getHealthRate()));
			giraffes.forEach(animal -> System.out.println(animal.getAnimalName() + " - " 
					+ animal.reportHealthStatus(animal.getHealthRate(), animal.getMaxHealthCap(), animal.getMaxWithhold())
					+ ", Current Health : " + animal.getHealthRate()));
			monkeys.forEach(animal -> System.out.println(animal.getAnimalName() + " - " 
					+ animal.reportHealthStatus(animal.getHealthRate(), animal.getMaxHealthCap(), animal.getMaxWithhold())
					+ ", Current Health : " + animal.getHealthRate()));
		}
		System.out.println(
				"---------------------------------------------------------------------------------------------------------");
	}

	/**
	 * 
	 * @param list of zooAnimals with healthy zoo
	 */
	private static void startSimulation() {
		simulationRunning = true;
		System.out.println("Simulation started");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------");
	}

	/**
	 * simulate feeding animals calls SimulationBussiness class methods to perform
	 * Health feed 3 feeds are chosen and provided to individual type
	 * 
	 * @param list of zooAnimals with simulated health
	 */
	public static void feedAnimals(List<Elephant> elephants, List<Giraffe> giraffes, List<Monkey> monkeys,
			int initialHealth, int minFeedValue, int maxFeedValue) {

		System.out.println("Feeding animals");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------");

		synchronized (lock) {

			BigDecimal elephantsRem = BigDecimal.valueOf(initialHealth).subtract(elephants.get(0).getHealthRate());
			BigDecimal randomFeedOfElephant = SimulationBussiness.generateRandomFeedValue(minFeedValue, maxFeedValue,
					elephantsRem);

			elephants.forEach(animal -> {
				// calculate how much remaining can be fed

				if (SimulationBussiness.getTimeToFeed(animal.getAnimalName(), animal.getHealthStatus(), minFeedValue,
						elephantsRem)) {
					BigDecimal increasedHealth = animal.deriveAnimalFeed(randomFeedOfElephant, animal);
					// animal.setHealthRate(currentHealth.max(BigInteger.ZERO));
					// sets smaller between increasedHealth and initialHealth
					animal.setHealthRate(increasedHealth.min(BigDecimal.valueOf(initialHealth)));
				}
			});

			BigDecimal giraffesRem = BigDecimal.valueOf(initialHealth).subtract(giraffes.get(0).getHealthRate());
			BigDecimal randomFeedOfGiraffe = SimulationBussiness.generateRandomFeedValue(minFeedValue, maxFeedValue,
					giraffesRem);

			giraffes.forEach(animal -> {

				if (SimulationBussiness.getTimeToFeed(animal.getAnimalName(), animal.getHealthStatus(), minFeedValue,
						giraffesRem)) {
					BigDecimal increasedHealth = animal.deriveAnimalFeed(randomFeedOfGiraffe, animal);
					animal.setHealthRate(increasedHealth.min(BigDecimal.valueOf(initialHealth)));
				}
			});

			BigDecimal monkeyRem = BigDecimal.valueOf(initialHealth).subtract(monkeys.get(0).getHealthRate());
			BigDecimal randomFeedOfMonk = SimulationBussiness.generateRandomFeedValue(minFeedValue, maxFeedValue,
					monkeyRem);

			monkeys.forEach(animal -> {

				if (SimulationBussiness.getTimeToFeed(animal.getAnimalName(), animal.getHealthStatus(), minFeedValue,
						monkeyRem)) {
					BigDecimal increasedHealth = animal.deriveAnimalFeed(randomFeedOfMonk, animal);
					animal.setHealthRate(increasedHealth.min(BigDecimal.valueOf(initialHealth)));
				}
			});

		}
	}

	/**
	 * thread to run the health fall, running periodically
	 * 
	 * @param list of zooAnimals with healthy zoo
	 */
	private static void startZooSimulation() {

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (simulationRunning) {
					// generate common random health fall value for all animals
					BigDecimal randomHealthFallValue = SimulationUtilities
							.getRandomBigDecimal(BigDecimal.valueOf(minHealthFall), BigDecimal.valueOf(maxHealthFall));

					simulateHealthFall(elephants, Giraffes, Monkeys, randomHealthFallValue);
					updateHealthStatusOfAnimals(elephants, Giraffes, Monkeys);
				}
			}

		}, 0, timePassRate * 1000);// every timePassRate in seconds

	}

	static List<Elephant> generateElephants() {
		List<Elephant> animalList = new ArrayList<>();
		
		// step 1
		System.out.println("Enter count of Elephants in zoo: ");
		int animalCountScInt = scanner.nextInt();

		// step 2
		System.out.println("Enter min health value for an Elephant to survive: ");
		int maxHealthHold = scanner.nextInt();
		
		// for elephant
		animalList.addAll(new Elephant().generateAnimalList("Healthy", animalCountScInt,
				BigDecimal.valueOf(initialHealth), BigDecimal.valueOf(maxHealthHold), initialHealth));

		return animalList;
	}

	static List<Giraffe> generateGiraffe() {
		List<Giraffe> animalList = new ArrayList<>();

		// step 1
		System.out.println("Enter count of Giraffe in zoo: ");
		int animalCountScInt = scanner.nextInt();

		// step 2
		System.out.println("Enter min health value for an Giraffe to survive: : ");
		int maxHealthHold = scanner.nextInt();

		// for elephant
		animalList.addAll(new Giraffe().generateAnimalList("Healthy", animalCountScInt,
				BigDecimal.valueOf(initialHealth), BigDecimal.valueOf(maxHealthHold), initialHealth));
				
		return animalList;
	}

	static List<Monkey> generateMonkeys() {
		List<Monkey> animalList = new ArrayList<>();

		// step 1
		System.out.println("Enter count of Monkeys in zoo: ");
		int animalCountScInt = scanner.nextInt();

		// step 2
		System.out.println("Enter min health value for an Monkey to survive: : ");
		int maxHealthHold = scanner.nextInt();

		// for elephant
		animalList.addAll(new Monkey().generateAnimalList("Healthy", animalCountScInt,
				BigDecimal.valueOf(initialHealth), BigDecimal.valueOf(maxHealthHold), initialHealth));
		
		return animalList;
	}

	/**
	 * simulate the health fall of all animals calls SimulationBussiness class
	 * methods to perform health fall same feed is provided to all animal types
	 * 
	 * @param list of zooAnimals with simulated health
	 */
	public static void simulateHealthFall(List<Elephant> elephants, List<Giraffe> giraffes, List<Monkey> monkeys, BigDecimal randomHealthFallValue) {

		synchronized (lock) {
			
			elephants.forEach(elephant -> {
				BigDecimal currentHealth = elephant.deriveHealthReduction(randomHealthFallValue, elephant);
				elephant.setHealthRate(currentHealth.max(BigDecimal.ZERO));

			});

			giraffes.forEach(giraffe -> {
				BigDecimal currentHealth = giraffe.deriveHealthReduction(randomHealthFallValue, giraffe);
				giraffe.setHealthRate(currentHealth.max(BigDecimal.ZERO));

			});

			monkeys.forEach(monkey -> {
				BigDecimal currentHealth = monkey.deriveHealthReduction(randomHealthFallValue, monkey);
				monkey.setHealthRate(currentHealth.max(BigDecimal.ZERO));

			});
		}
	}
	
    
    public static void simulateZooTime() {
        
    	if(SIMULATION_TIME_HOURS == 0) {
    		SIMULATION_TIME_HOURS = 1;
    	}else {
    		zooTime = zooTime.plusHours(SIMULATION_TIME_HOURS);
    	}

    }

}