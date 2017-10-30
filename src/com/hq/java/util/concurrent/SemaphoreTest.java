package com.hq.java.util.concurrent;

import java.util.Random;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class SemaphoreTest {
	public static void main(String[] args) {
		Runnable task = new Runnable(){
			final Random rand = new Random();
			final Semaphore sem = new Semaphore(3);
			int count = 0;
			public void run(){
				try {
					sem.acquire();
					int num = count++;
					int time = rand.nextInt(15);
					System.out.println("执行任务:"+num+" 执行时间："+time);
					Thread.sleep(time*1000);
					System.out.println("执行结束："+num);
					int a = sem.availablePermits();
					System.out.println("可用许可数:"+a);
					int qwe = sem.getQueueLength();
					System.out.println("正在等待获取的线程的估计数目："+qwe);
					System.out.println("=======================");
					sem.release();
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		};
		
		for (int i = 0; i < 10; i++) {
			new Thread(task).start();
		}
	}
}
