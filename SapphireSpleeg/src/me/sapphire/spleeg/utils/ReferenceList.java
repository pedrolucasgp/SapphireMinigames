package me.sapphire.spleeg.utils;

import java.util.List;


public class ReferenceList extends ReferenceBase {

	private List<Integer> list;

	public ReferenceList(List<Integer> list,Object result) {
		super(null, result);
		setList(list);
	}

	@Override
	public void update() { 
		@SuppressWarnings("unchecked")
		List<Object> newList = (List<Object>) getInstance();
		for (Integer item : list) {
			newList.add(StorageAPI.getObjectById(item));
		}
	}

	public List<Integer> getList() {
		return list;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	}

}
