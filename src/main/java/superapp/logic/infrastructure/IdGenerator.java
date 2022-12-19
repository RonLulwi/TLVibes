package superapp.logic.infrastructure;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public final class IdGenerator {

	private final AtomicInteger counter = new AtomicInteger();

	public UUID GenerateUUID() {
		//TODO: make this method thread safe
		return UUID.randomUUID();
	}
	
	public int GenerateIntID() {
		//TODO: make this method thread safe
		return counter.getAndIncrement();
	}
	
	
}
