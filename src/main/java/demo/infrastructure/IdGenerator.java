package demo.infrastructure;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public final class IdGenerator {

	private static final AtomicInteger counter = new AtomicInteger();

	public static UUID GenerateUUID() {
		//TODO: make this method thread safe
		return UUID.randomUUID();
	}
	
	public static int GenerateIntID() {
		//TODO: make this method thread safe
		return counter.getAndIncrement();
	}

}
