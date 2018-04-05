package cc.upedu.online.view;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

public class SearchListData<E> extends AbstractList<E> implements Cloneable, Serializable, RandomAccess {
	private List<E> objestList = new ArrayList<E>();
	@Override
	public E get(int location) {
		// TODO Auto-generated method stub
		return objestList.get(location);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return objestList.size();
	}
	public void addElement(E element){
		List<E> temporaryList = new ArrayList<E>();
		if (objestList.contains(element)) {
			//如果新添加的元素在集合中
			objestList.remove(element);
		}else {
			//如果新添加的元素不在集合中
		}
		//把新添加的元素添加到到首位,并把原集合中的数据添加到后面
		temporaryList.addAll(objestList);
		objestList.clear();
		objestList.add(element);
		objestList.addAll(temporaryList);
		//保留前5个元素
		if (objestList.size() > 5) {
			objestList = objestList.subList(0, 5);
		}
		temporaryList = null;
	}

	public Collection<E> getStringList() {
		// TODO Auto-generated method stub
		return objestList;
	}
	public void addAllObject(Collection<E> object){
		objestList.clear();
		objestList.addAll(object);
	}

	public void clearAll() {
		objestList.clear();
	}
}
