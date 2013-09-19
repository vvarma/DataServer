package com.nvr.data.loader;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;

/**
 * A {@link CompletionService} based on the Spring {@link TaskExecutor}
 * abstraction. Since a {@link TaskExecutor} can be single threaded this also
 * allows for synchronous calling of tasks (this is the default). A throttle
 * limit can also be provided to limit the number of pending requests. In this
 * case the submit method blocks until there are results available to retrieve.
 * 
 * @author Dave Syer
 * 
 * @param <V> the type of object returned by the service
 */
public class TaskExecutorCompletionService<V> implements CompletionService<V> {

	private final BlockingQueue<Future<V>> completionQueue;

	private final Semaphore semaphore;

	private volatile int count = 0;

	private TaskExecutor taskExecutor = new SyncTaskExecutor();

	/**
	 * Create a {@link TaskExecutorCompletionService} with infinite
	 * (Integer.MAX_VALUE) throttle limit. A task can always be submitted.
	 */
	public TaskExecutorCompletionService() {
		this(null, Integer.MAX_VALUE);
	}

	/**
	 * Create a {@link TaskExecutorCompletionService} with infinite
	 * (Integer.MAX_VALUE) throttle limit. A task can always be submitted.
	 * 
	 * @param taskExecutor the {@link TaskExecutor} to use
	 */
	public TaskExecutorCompletionService(TaskExecutor taskExecutor) {
		this(taskExecutor, Integer.MAX_VALUE);
	}

	/**
	 * Create a {@link TaskExecutorCompletionService} with finite throttle
	 * limit. The submit method will block when this limit is reached until one
	 * of the tasks has finished.
	 * 
	 * @param taskExecutor the {@link TaskExecutor} to use
	 * @param throttleLimit the throttle limit
	 */
	public TaskExecutorCompletionService(TaskExecutor taskExecutor, int throttleLimit) {
		super();
		if (taskExecutor != null) {
			this.taskExecutor = taskExecutor;
		}
		this.completionQueue = new LinkedBlockingQueue<Future<V>>(throttleLimit);
		this.semaphore = new Semaphore(throttleLimit);
	}

	/**
	 * Public setter for the {@link TaskExecutor} to be used to execute the
	 * tasks submitted. The default is synchronous, executing tasks on the
	 * calling thread. In this case the throttle limit is irrelevant as there
	 * will always be at most one task pending.
	 * 
	 * @param taskExecutor
	 */
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public Future<V> poll() {
		return completionQueue.poll();
	}

	public Future<V> poll(long timeout, TimeUnit unit) throws InterruptedException {
		return completionQueue.poll(timeout, unit);
	}

	public Future<V> submit(Callable<V> task) {
		if (task == null) {
			throw new NullPointerException();
		}
		return doSubmit(new FutureTask<V>(task));
	}

	public Future<V> submit(Runnable task, V result) {
		if (task == null) {
			throw new NullPointerException();
		}
		return doSubmit(new FutureTask<V>(task, result));
	}

	public Future<V> take() throws InterruptedException {
		return completionQueue.take();
	}

	/**
	 * Get an estimate of the number of pending requests.
	 * 
	 * @return the estimate
	 */
	public int size() {
		return count;
	}

	private Future<V> doSubmit(final FutureTask<V> task) {

		try {
			semaphore.acquire();
			count++;
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new TaskRejectedException("Task could not be submitted because of a thread interruption.");
		}

		taskExecutor.execute(new FutureTask<V>(task, null) {
			@Override
			protected void done() {
				try {
					completionQueue.put(task);
					semaphore.release();
					count--;
				}
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		});

		return task;
	}

}
