package demo.infrastructure;

import java.util.UUID;

public final class IdGenerator {

	public static UUID GenerateID() {
		//TODO: make this method thread safe
		return UUID.randomUUID();
	}
}
