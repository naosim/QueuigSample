package com.example.queuingsample.taskexecutor;

public abstract class Task<E> implements Runnable {
	protected E userData;
	public Task(E userData) {
		this.userData = userData;
	}
	
	@Override
	public void run() {
		run(userData);
	}
	
	abstract public void run(E userData);
	abstract public void cancel();
}