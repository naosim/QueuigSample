package com.example.queuingsample.taskexecutor;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Handler;
import android.os.HandlerThread;

public class TaskExecutor {
	private TaskDataManager taskManager = new TaskDataManager();
	private Handler handler;

	public TaskExecutor() {
		setupHandler();
	}

	private void setupHandler() {
		HandlerThread handlerThread = new HandlerThread("TaskExecuter");
		handlerThread.start();
		handler = new Handler(handlerThread.getLooper());
		handler.getLooper().getThread().setPriority(Thread.MIN_PRIORITY);
	}

	public void addTask(Task<?> task) {
		taskManager.addTask(task);
		run();
	}

	public void addTaskToFirst(Task<?> task) {
		taskManager.addTask(0, task);
		run();
	}

	public void cancelAll() {
		Iterable<Task<?>> tasks = taskManager.getTasks();
		for (Task<?> task : tasks) task.cancel();
	}

	private void run() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				while (taskManager.size() != 0) taskManager.remove(0).run();
			}
		});
	}

	private static class TaskDataManager {
		private ArrayList<Task<?>> tasks = new ArrayList<Task<?>>();
		private HashMap<Task<?>, Task<?>> map = new HashMap<Task<?>, Task<?>>();

		public void addTask(Task<?> task) {
			tasks.add(task);
			map.put(task, task);
		}

		public void addTask(int index, Task<?> task) {
			tasks.add(index, task);
			map.put(task, task);
		}

		public Task<?> remove(int index) {
			Task<?> task = tasks.remove(index);
			map.remove(task);
			return task;
		}

		public Iterable<Task<?>> getTasks() {
			return tasks;
		}

		public int size() {
			return tasks.size();
		}
	}
}