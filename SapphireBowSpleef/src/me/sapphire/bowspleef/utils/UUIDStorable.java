package me.sapphire.bowspleef.utils;

import java.util.UUID;


@StorageAttributes(inline = true)
public class UUIDStorable implements Storable {

	@Override
	public Object restore(Object object) {

		return UUID.fromString(object.toString());
	}

	@Override
	public Object store(Object object) {
		return object.toString();
	}

}
