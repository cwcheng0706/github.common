package com.zy.multithread.timeout;



public class TimeoutController {

	public static void main(String[] args) throws Exception{
		TimeoutRunner runner = new TimeoutRunner(3000);
		
		TimeoutController.execute(runner, 3000);
	}
	

	public static void execute(Thread task, long timeout) throws TimeoutException {
		task.start();
		try {
			task.join(timeout);
		} catch (InterruptedException e) {
			/* if somebody interrupts us he knows what he is doing */
		}
		if (task.isAlive()) {
			task.interrupt();
			throw new TimeoutException();
		}
	}

	public static void execute(Runnable task, long timeout) throws TimeoutException {
		Thread t = new Thread(task, "Timeout guard");
		t.setDaemon(true);
		execute(t, timeout);
	}

	
}


