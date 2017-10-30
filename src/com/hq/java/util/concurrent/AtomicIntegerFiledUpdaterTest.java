package com.hq.java.util.concurrent;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import org.junit.Test;

public class AtomicIntegerFiledUpdaterTest {

	class DemoData{
	       public volatile int value1 = 1;
	       volatile int value2 = 2;
	       protected volatile int value3 = 3;
	       private volatile int value4 = 4;
	   }
	
	AtomicIntegerFieldUpdater<DemoData> getUpdater(String fieldName){
		return AtomicIntegerFieldUpdater.newUpdater(DemoData.class, fieldName);
	}
	@Test
	public void test(){
		DemoData data = new DemoData();
		System.out.println("value1="+getUpdater("value1").getAndSet(data, 11));
		System.out.println("value2="+getUpdater("value2").incrementAndGet(data));
		System.out.println("value3="+getUpdater("value3").decrementAndGet(data));
		System.out.println("value4="+getUpdater("value4").compareAndSet(data, 4, 44));
	}
}
