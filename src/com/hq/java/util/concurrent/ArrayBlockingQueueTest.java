package com.hq.java.util.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;

public class ArrayBlockingQueueTest<E> {

	private int count;
	final ReentrantLock lock = new ReentrantLock();
	final Condition notFull = lock.newCondition();
	final Condition notEmpty = lock.newCondition();
	@Autowired
	E[] e ;
	public E take() throws InterruptedException{
		
		lock.lockInterruptibly();
		
		E x;
		try {
			try {
				while(count == 0){
					notEmpty.await();
				}
			} catch (InterruptedException ie) {
				notEmpty.signal();
				throw ie;
			}
			x = extract();
		} finally{
			lock.unlock();
		}
		
		return x;
		
	}
	
	public void put(E ee) throws InterruptedException{
		lock.lockInterruptibly();
		try {
			try {
				while(count == e.length){
					notEmpty.await();
				}
			} catch (InterruptedException ie) {
				notEmpty.signal();
				throw ie;
			}
			insert(ee);
		} finally {
			lock.unlock();
		}
		
	}
	
	private void insert(E ee) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		System.out.println(1/2);
	}

	private E extract() {
		// TODO Auto-generated method stub
		return null;
	}
}
