package me.sapphire.bowspleef.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class StorageMap extends StorageBase {

	public StorageMap(StorageInfo info) {
		super(info);
		this.keyType = Extra.getTypeKey(getField().getGenericType());
		this.valueType = Extra.getTypeValue(getField().getGenericType());
		// TODO Auto-generated constructor stub
	}

	private Class<?> keyType;
	private Class<?> valueType;

	public Class<?> getKeyType() {
		return keyType;
	}

	public void setKeyType(Class<?> keyType) {
		this.keyType = keyType;
	}

	public Class<?> getValueType() {
		return valueType;
	}

	public void setValueType(Class<?> valueType) {
		this.valueType = valueType;
	}

	@Override
	public Object restore(Object data) {
		StorageObject storeKey = new StorageObject(getInfo().clone());
		storeKey.setType(keyType);
		storeKey.update();
		StorageObject storeValue = new StorageObject(getInfo().clone());
		storeValue.setType(valueType);
		storeValue.update();

		if (isReference()) {
			if (data instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> oldMap = (Map<String, Object>) data;
				Map<Object, Integer> newMap = new HashMap<>();
				for (Entry<String, Object> entry : oldMap.entrySet()) {

					newMap.put(storeKey.restore(entry.getKey()), (Integer) storeValue.restore(entry.getValue()));
				}
				StorageAPI.newReference(new ReferenceMap(newMap, newMap));
				debug("Restoring refereced map");
			}
			return null;
		}
		Map<Object, Object> newMap = new HashMap<>();
		if (data instanceof Map) {

			Map<?, ?> map = (Map<?, ?>) data;
			for (Entry<?, ?> entry : map.entrySet()) {

				Object key = storeKey.restore(entry.getKey());
				Object value = storeValue.restore(entry.getValue());
				debug("^^ " + key + " " + value);
				newMap.put(key, value);
			}
		}
		return newMap;
	}

	@Override
	public Object store(Object data) {
		StorageObject storeKey = new StorageObject(getInfo().clone());
		storeKey.setType(keyType);
		storeKey.update();
		StorageObject storeValue = new StorageObject(getInfo().clone());
		storeValue.setType(valueType);
		storeValue.update();
		Map<String, Object> newMap = new HashMap<>();
		Map<?, ?> map = (Map<?, ?>) data;
		for (Entry<?, ?> entry : map.entrySet()) {
			String key = storeKey.store(entry.getKey()).toString();
			Object value = storeValue.store(entry.getValue());
			newMap.put(key, value);
		}
		return newMap;
	}

}
