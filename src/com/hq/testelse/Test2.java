package com.hq.testelse;

import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;

public class Test2 {
	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		Double d = -5000000000123D;
		DecimalFormat df = new DecimalFormat("########.00");
		Double d2 = Math.abs(d);

		System.out.println(d2);
		System.out.println(df.format(d2));

		int i = 305 / 5;
		System.out.println(i);

		ConcurrentLinkedQueue c = new ConcurrentLinkedQueue();
		ArrayDeque l = new ArrayDeque();
	}
}
