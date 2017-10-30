package com.hq.java.util.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class AtomicIntegerTest {
	
	@Test
	public void test() throws InterruptedException{
		final AtomicInteger value = new AtomicInteger();
		 final int threadSize = 1000;
	        Thread[] ts = new Thread[threadSize];
	        for (int i = 0; i < threadSize; i++) {
	            ts[i] = new Thread("线程"+i) {
	                public void run() {
	                    int incrementAndGet = value.incrementAndGet();
	                    System.out.println(Thread.currentThread().getName()+"===="+incrementAndGet);
	                }
	            };
	        }
	        //
	        for(Thread t:ts) {
	            t.start();
	        }
	        for(Thread t:ts) {
	            t.join();
	        }
	        //
	        assertEquals(value.get(), 10+threadSize);
	}
	
	@Test
	public void test2(){
		final AtomicInteger value = new AtomicInteger(10);
        assertEquals(value.compareAndSet(1, 2), false);
        assertEquals(value.get(), 10);
        assertTrue(value.compareAndSet(10, 3));
        assertEquals(value.get(), 3);
        value.set(0);
        //
        assertEquals(value.incrementAndGet(), 1);
        assertEquals(value.getAndAdd(2),1);
        assertEquals(value.getAndSet(5),3);
        assertEquals(value.get(),5);
	}
}
