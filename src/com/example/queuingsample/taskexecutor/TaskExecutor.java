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
		taskManager.addTaskData(new TaskData(task));
		run();
	}

	public void addTaskToFirst(Task<?> task) {
		taskManager.addTaskData(0, new TaskData(task));
		run();
	}

	public void cancelAll() {
		Iterable<TaskData> tasks = taskManager.getDatas();
		for (TaskData data : tasks)
			cancel(data);
	}

	public void cancel(Runnable task) {
		cancel(taskManager.get(task));
	}

	private void cancel(TaskData data) {
		data.isCanceled = true;
		data.task.cancel();
	}

	private void run() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				while (taskManager.size() != 0) {
					TaskData data = taskManager.remove(0);
					if (!data.isCanceled)
						data.task.run();
				}
			}
		});
	}

	private static class TaskDataManager {
		private ArrayList<TaskData> tasks = new ArrayList<TaskData>();
		private HashMap<Runnable, TaskData> map = new HashMap<Runnable, TaskData>();

		public void addTaskData(TaskData data) {
			tasks.add(data);
			map.put(data.task, data);
		}

		public void addTaskData(int index, TaskData data) {
			tasks.add(index, data);
			map.put(data.task, data);
		}

		public TaskData remove(int index) {
			TaskData data = tasks.remove(index);
			map.remove(data);
			return data;
		}

		public TaskData get(Runnable task) {
			return map.get(task);
		}

		public Iterable<TaskData> getDatas() {
			return tasks;
		}

		public int size() {
			return tasks.size();
		}
	}

	private static class TaskData {
		public Task<?> task;
		public boolean isCanceled;

		public TaskData(Task<?> task) {
			this.task = task;
		}
	}
}