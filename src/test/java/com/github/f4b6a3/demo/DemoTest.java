package com.github.f4b6a3.demo;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import org.junit.Ignore;

import com.github.f4b6a3.uuid.UuidCreator;
import com.github.f4b6a3.uuid.enums.UuidNamespace;
import com.github.f4b6a3.uuid.factory.DceSecurityUuidCreator;
import com.github.f4b6a3.uuid.nodeid.RandomNodeIdentifierStrategy;
import com.github.f4b6a3.uuid.nodeid.HardwareAddressNodeIdentifierStrategy;
import com.github.f4b6a3.uuid.nodeid.DefaultNodeIdentifierStrategy;
import com.github.f4b6a3.uuid.random.Xoroshiro128PlusRandom;
import com.github.f4b6a3.uuid.random.Xorshift128PlusRandom;
import com.github.f4b6a3.uuid.random.XorshiftRandom;
import com.github.f4b6a3.uuid.random.XorshiftStarRandom;
import com.github.f4b6a3.uuid.timestamp.DefaultTimestampStrategy;
import com.github.f4b6a3.uuid.timestamp.NanosecondTimestampStrategy;
import com.github.f4b6a3.uuid.util.NetworkData;
import com.github.f4b6a3.uuid.util.TimestampUtil;
import com.github.f4b6a3.uuid.util.UuidUtil;

public class DemoTest {

	private static final String HORIZONTAL_LINE = "----------------------------------------";

	@Ignore
	public void testPrintList() {
		int max = 100;
		
		System.out.println();
		System.out.println(HORIZONTAL_LINE);
		System.out.println("Print list of UUIDs");
		System.out.println(HORIZONTAL_LINE);

		System.out.println();
		System.out.println("### Random UUID");

		for (int i = 0; i < max; i++) {
			System.out.println(UuidCreator.getFastRandom());
		}

		System.out.println(HORIZONTAL_LINE);
		System.out.println("### Time-based UUID");

		for (int i = 0; i < max; i++) {
			System.out.println(UuidCreator.getTimeBased());
		}

		System.out.println(HORIZONTAL_LINE);
		System.out.println("### Sequential UUID");

		for (int i = 0; i < max; i++) {
			System.out.println(UuidCreator.getSequential());
		}

		System.out.println(HORIZONTAL_LINE);
		System.out.println("### COMB GUID");

		for (int i = 0; i < max; i++) {
			System.out.println(UuidCreator.getCombGuid());
		}
		
		System.out.println(HORIZONTAL_LINE);
		System.out.println("### Lexical Order Guid");

		for (int i = 0; i < max; i++) {
			System.out.println(UuidCreator.getLexicalOrderGuid());
		}

		System.out.println(HORIZONTAL_LINE);
	}

	/**
	 * This method only prints average running times.
	 */
	@Ignore
	public void testEstimateRunningTimes() {
		long loopMax = 100_000;
		long nano = 1_000_000;
		long randomUUID = (SimpleBenchmark.run(null, UUID.class, "randomUUID", loopMax) * loopMax) / nano;
		long getRandomUUID = (SimpleBenchmark.run(null, UuidCreator.class, "getRandom", loopMax) * loopMax) / nano;
		long getTimeBasedUUID = (SimpleBenchmark.run(null, UuidCreator.class, "getTimeBased", loopMax) * loopMax)
				/ nano;
		long getSequentialUUID = (SimpleBenchmark.run(null, UuidCreator.class, "getSequential", loopMax) * loopMax)
				/ nano;
		long javaNextLong = (SimpleBenchmark.run(new Random(), null, "nextLong", loopMax) * loopMax) / nano;
		long xorshiftNextLong = (SimpleBenchmark.run(new XorshiftRandom(), null, "nextLong", loopMax) * loopMax) / nano;

		System.out.println();
		System.out.println(HORIZONTAL_LINE);
		System.out.println(String.format("Average generation times for %,d UUIDs", loopMax));
		System.out.println(HORIZONTAL_LINE);
		System.out.println(String.format("* java.util.UUID.randomUUID():       %s ms", randomUUID));
		System.out.println(String.format("* UUIDGenerator.getRandomUUID():     %s ms", getRandomUUID));
		System.out.println(String.format("* UUIDGenerator.getTimeBasedUUID():  %s ms", getTimeBasedUUID));
		System.out.println(String.format("* UUIDGenerator.getSequentialUUID(): %s ms", getSequentialUUID));
		System.out.println(String.format("* java.util.Random.nextLong(): %s ms", javaNextLong));
		System.out.println(String.format("* XorshiftRandom.nextLong(): %s ms", xorshiftNextLong));
		System.out.println(HORIZONTAL_LINE);
	}

	/**
	 * Just prints UUIDs generated to a specific instant.
	 */
	@Ignore
	public void testDemoDifferenceBetweenTimeBasedAndSequentialUUID() {

		Instant instant = Instant.now();
		String timeBasedUUID = UuidCreator.getTimeBasedCreator().withInstant(instant).create().toString();
		String sequentialUUID = UuidCreator.getSequentialCreator().withInstant(instant).create().toString();

		Logger.getAnonymousLogger().info("");
		System.out.println();
		System.out.println(HORIZONTAL_LINE);
		System.out.println("Demonstration of time-baed UUIDs");
		System.out.println(HORIZONTAL_LINE);
		System.out.println("- TimeBased UUID:          " + timeBasedUUID);
		System.out.println("- Sequential UUID:         " + sequentialUUID);
		System.out.println("- Original instant:        " + instant.toString());
		System.out.println("- TimeBased UUID instant:  " + UuidUtil.extractInstant(UUID.fromString(timeBasedUUID)));
		System.out.println("- Sequential UUID instant: " + UuidUtil.extractInstant(UUID.fromString(sequentialUUID)));
		System.out.println(HORIZONTAL_LINE);
	}

	/**
	 * Generate images with random pixels.
	 */
	@Ignore
	public void testCreateRandomImage() {

		System.out.println();
		System.out.println(HORIZONTAL_LINE);
		System.out.println("Creating images from random numbers...");
		System.out.println(HORIZONTAL_LINE);

		RandomImage.createRandomImageFile("/tmp/java.util.Random.png", new Random(), 0, 0);
		RandomImage.createRandomImageFile("/tmp/java.security.SecureRandom.png", new SecureRandom(), 0, 0);
		RandomImage.createRandomImageFile("/tmp/XorshiftRandom.png", new XorshiftRandom(), 0, 0);
		RandomImage.createRandomImageFile("/tmp/XorshiftStarRandom.png", new XorshiftStarRandom(), 0, 0);
		RandomImage.createRandomImageFile("/tmp/Xorshift128PlusRandom.png", new Xorshift128PlusRandom(), 0, 0);
		RandomImage.createRandomImageFile("/tmp/Xoroshiro128PlusRandom.png", new Xoroshiro128PlusRandom(), 0, 0);

		System.out.println(HORIZONTAL_LINE);
	}

	@Ignore
	public void testStrategiesUsingFlowInterface() {

		UUID uuid = null;

		System.out.println("\n#### Time-based");

		System.out.println("\n##### Fixed values");

		// @formatter:off
		uuid = UuidCreator.getTimeBasedCreator().withInstant(Instant.now()).create();
		System.out.println(String.format("%s // with fixed instant (now)", uuid));

		uuid = UuidCreator.getTimeBasedCreator().withTimestamp(TimestampUtil.toTimestamp(Instant.now())).create();
		System.out.println(String.format("%s // with fixed timestamp (now as timestamp)", uuid));

		uuid = UuidCreator.getTimeBasedCreator().withClockSequence(0x8888).create();
		System.out.println(String.format("%s // with fixed clock sequence (0x8888)", uuid));

		uuid = UuidCreator.getTimeBasedCreator().withNodeIdentifier(0x111111111111L).create();
		System.out.println(String.format("%s // with fixed node identifier (0x111111111111L)", uuid));

		uuid = UuidCreator.getTimeBasedCreator().withHardwareAddressNodeIdentifier().create();
		System.out.println(String.format("%s // with hardware address (first MAC found)", uuid));

		System.out.println("\n##### Timestamp strategy");

		uuid = UuidCreator.getTimeBasedCreator().withTimestampStrategy(new DefaultTimestampStrategy()).create();
		System.out.println(String.format("%s // with default timestamp strategy (System.currentTimeMillis() + counter)", uuid));

		uuid = UuidCreator.getTimeBasedCreator().withTimestampStrategy(new NanosecondTimestampStrategy()).create();
		System.out.println(String.format("%s // with nanoseconds timestamp strategy (Instant.getNano())", uuid));

		System.out.println("\n##### Node identifier strategy");

		uuid = UuidCreator.getTimeBasedCreator().withNodeIdentifierStrategy(new DefaultNodeIdentifierStrategy()).create();
		System.out.println(String.format("%s // with default node identifier strategy (host name, IP, MAC, OS and JVM)", uuid));

		uuid = UuidCreator.getTimeBasedCreator().withNodeIdentifierStrategy(new RandomNodeIdentifierStrategy()).create();
		System.out.println(String.format("%s // with random node identifier strategy (random number generated once)", uuid));

		uuid = UuidCreator.getTimeBasedCreator().withNodeIdentifierStrategy(new HardwareAddressNodeIdentifierStrategy()).create();
		System.out.println(String.format("%s // with hardware address node identifier strategy (first MAC found)", uuid));

		System.out.println("\n#### Name-based");

		uuid = UuidCreator.getNameBasedMd5Creator().withNamespace("USERS").create("Paul");
		System.out.println(String.format("%s // with fixed namespace as string (USERS)", uuid));

		uuid = UuidCreator.getNameBasedMd5Creator().withNamespace(UuidNamespace.NAMESPACE_DNS.getValue()).create("www.github.com");
		System.out.println(String.format("%s // with fixed namespace as UUID (standard DNS namespace)", uuid));

		System.out.println("\n#### Random");

		uuid = UuidCreator.getRandomCreator().withRandomGenerator(new Random()).create();
		System.out.println(String.format("%s // with java random generator (java.util.Random)", uuid));

		uuid = UuidCreator.getRandomCreator().withFastRandomGenerator().create();
		System.out.println(String.format("%s // with fast random generator (Xorshift128Plus)", uuid));

		System.out.println("\n#### DCE Security");

		uuid = UuidCreator.getDceSecurityCreator().withLocalDomain(DceSecurityUuidCreator.LOCAL_DOMAIN_PERSON).create(1701);
		System.out.println(String.format("%s // with fixed local domain (standard POSIX User ID)", uuid));
		// @formatter:on
	}
	
	@Ignore
	public void testGetNetworkData() {
		
		long startTime = 0;
		long endTime = 0;
		
		startTime = System.currentTimeMillis();
		
		for(int i = 0; i < 100; i++) {
			NetworkData.getNetworkData();
		}
		
		endTime = System.currentTimeMillis();
		
		System.out.println("get network data: " + (endTime - startTime) + " ms");
		
		startTime = System.currentTimeMillis();
		
		for(int i = 0; i < 100; i++) {
			NetworkData.getNetworkDataList();
		}
		
		endTime = System.currentTimeMillis();
		
		System.out.println("get network data list: " + (endTime - startTime) + " ms");
	}
	

}
