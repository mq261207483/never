package com.hq.java.util.concurrent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.RandomUtils;
import org.aspectj.weaver.ArrayAnnotationValue;
import org.junit.Test;

public class Test2 {
	public static void main(String[] args) throws InterruptedException,
			ExecutionException {

		Runnable a = new Runnable() {
			@Override
			public void run() {
				System.out.println("runnable");
			}

		};

		Callable<String> c = new Callable<String>() {
			@Override
			public String call() throws Exception {
				System.out.println("callable");
				Thread.sleep(5000);
				return "成功";
			}
		};
		ExecutorService es = Executors.newFixedThreadPool(3);
		Future<?> f = es.submit(a);
		Object object = f.get();
		System.out.println(object);

		Future<String> f2 = es.submit(c);
		System.out.println("=====" + new Date());
		System.out.println(f2.get() + "===" + new Date());
		System.out.println("=====" + new Date());

		es.shutdown();

	}

	@Test
	public void test() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				System.out.println("123123");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		t.start();

		// try {
		// t.join();
		// } catch (InterruptedException e) {
		//
		// }
		// System.out.println("+++");

		System.out.println(RandomUtils.nextInt(1, 10));
	}

	@Test
	public void test2() throws InterruptedException, ExecutionException {
		ExecutorService es = Executors.newFixedThreadPool(10);
		List<Callable<Integer>> list = new ArrayList<Callable<Integer>>();
		final AtomicInteger a = new AtomicInteger(0);
		for (int i = 0; i < 10; i++) {
			list.add(new Callable<Integer>() {

				@Override
				public Integer call() throws Exception {
					Thread.sleep(RandomUtils.nextInt(1, 10) * 2000);
					System.out.println(Thread.currentThread().getName());
					return a.getAndIncrement();
				}

			});
		}
		//1
		solve(es, list);
		
		//2
//		List<Future<Result>> invokeAll = es.invokeAll(list);
//		for(Future<Result> future:invokeAll){  
//			Result result = future.get();
//			System.out.println(result.a.get());
//        }  

	}


	void solve(Executor e, Collection<Callable<Integer>> task)
			throws InterruptedException, ExecutionException {
		CompletionService<Integer> ecs = new ExecutorCompletionService<Integer>(e);
		for (Callable<Integer> s : task) {
			ecs.submit(s);
		}
		int n = task.size();
		for (int i = 0; i < n; ++i) {
			Integer rrr= ecs.take().get();
			if (rrr != null)
				System.out.println(rrr);
		}
	}
}
