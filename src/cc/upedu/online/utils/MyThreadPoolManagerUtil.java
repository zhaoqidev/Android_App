package cc.upedu.online.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyThreadPoolManagerUtil {
	private static ExecutorService newFixedThreadPool;
	public static ExecutorService getInstance() {
		// TODO Auto-generated method stub
		if (newFixedThreadPool == null) {
			newFixedThreadPool = Executors.newFixedThreadPool(5);
		}
		return newFixedThreadPool;
	}
}
