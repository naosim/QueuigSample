package com.example.queuingsample.taskexecutor;

public abstract class Task<E> implements Runnable {
	protected E userData;
	private boolean isCanceled = false;
	
	public Task(E userData) {
		if(!isCanceled()) this.userData = userData;
	}
	
	@Override
	public void run() {
		run(userData);
	}
	
	public void cancel() {
		isCanceled = true;
	}
	
	public boolean isCanceled() {
		return isCanceled;
	}
	
	abstract public void run(E userData);
	
}