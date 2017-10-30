package com.hq.java.util.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.junit.Test;

public class ConcurrentLinkedQueueTest {
@Test
public void test2(){
	final AtomicLong in = new AtomicLong(0);
	in.addAndGet(5l);
	System.out.println(in.get());
}


	class Me{
		public long num;
		public long time;
	}
	
	@Test
	public void test() throws InterruptedException, BrokenBarrierException, ExecutionException{
		final ConcurrentLinkedQueue<Integer> q = new ConcurrentLinkedQueue<Integer>();
		for (int i = 0; i < 10000; i++) {
			q.add(i);
		}
		int num = 10;
		long count = 0;
//		final CyclicBarrier c = new CyclicBarrier(num+1);
		ExecutorService es = Executors.newFixedThreadPool(num);
		CompletionService<Me> cs = new ExecutorCompletionService<Me>((Executor) es);
		long s = System.currentTimeMillis();
		for (int i = 0; i < num; i++) {
			cs.submit(new Callable<Me>(){
				final AtomicLong ato = new AtomicLong();
				@Override
				public Me call() throws Exception {

//					long s = System.currentTimeMillis();
					while(q.poll() != null){
						ato.getAndIncrement();
						//如果poll这后的业务逻辑运行时间小的话,多线程序没有任何意义,
						//反之如果poll之后的业务逻辑运行时间相当于Thread.sleep(1);
						//哈哈,多线程确实起作用!
						Thread.sleep(1);
					}
//					long end = System.currentTimeMillis();
//					System.out.println(ato.get() + "===="+(end-s));
					Me m = new Me();
					m.num = ato.get();
//					m.time = end-s;
//					try {
//						c.await();
//					} catch (InterruptedException e) {
//					} catch (BrokenBarrierException e) {
//					}
				
					return m;
				}
				
			});
//			Long result = f.get();
//			count +=result;
		}
		long time = 0;
		Me m = new Me();
		
		for (int i = 0; i < num; i++) {
			m = cs.take().get();
			count += m.num;
//			time += m.time;
		}
		long e = System.currentTimeMillis();
		System.out.println("count="+count);
		System.out.println("time="+(e-s));
//		c.await();
	}
}
