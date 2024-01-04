package me.sapphire.bowspleef.utils;

import java.sql.Timestamp;

@StorageAttributes(inline=true)
public class TimeStampStorable implements Storable {

	@Override
	public Object store(Object object) {
		if (object instanceof Timestamp) {
			Timestamp timestamp = (Timestamp) object;
			return timestamp.getTime();

		}
		return null;
	}

	@Override
	public Object restore(Object object) {
		if (object instanceof Long) {
			Long long1 = (Long) object;
			return new Timestamp(long1);
		}
		if (object instanceof String) {
			String string = (String) object;
			return new Timestamp(Extra.toLong(string));

		}
		return null;
	}

}
