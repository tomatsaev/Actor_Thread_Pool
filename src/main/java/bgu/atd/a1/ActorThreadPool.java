package bgu.atd.a1;

import bgu.atd.a1.sim.Warehouse;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 *
 * SUBMITTED: Tom Matsaev 318864550 & Or Saar 205476369
 *
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {

	private final Integer nthreads;
	private final AtomicInteger currentActions = new AtomicInteger(0);
	private final AtomicBoolean terminate = new AtomicBoolean(false);
	private final List<Thread> activeThreads = new ArrayList<>();
	private final List<Thread> sleepingThreads = new ArrayList<>();
	private final Lock threadsLock  = new ReentrantLock();
	private final Map<String, PrivateState> actors = new ConcurrentHashMap<>();
	private final Map<String, Lock> locksByID = new ConcurrentHashMap<>();
	private final Map<String, Queue<Action<?>>> actionsByActorID = new ConcurrentHashMap<>();
	public Warehouse warehouse;

	/**
	 * creates a {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 * <p>
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads the number of threads that should be started by this thread
	 *                 pool
	 */
	public ActorThreadPool(int nthreads) {
		this.nthreads = nthreads;
	}

	/**
	 * getter for actors
	 *
	 * @return actors
	 */
	public Map<String, PrivateState> getActors() {
		return actors;
	}

	/**
	 * getter for actor's private state
	 *
	 * @param actorId actor's id
	 * @return actor's private state
	 */
	public PrivateState getPrivateState(String actorId) {
		return actors.get(actorId);
	}

	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action     the action to execute
	 * @param actorId    corresponding actor's id
	 * @param actorState actor's private state (actor's information)
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		actors.putIfAbsent(actorId, actorState);
		actionsByActorID.putIfAbsent(actorId, new ConcurrentLinkedQueue<>());
		locksByID.putIfAbsent(actorId, new ReentrantLock());

//		currentActions is decremented last (added again), so the program shuts down gracefully
		action.getResult().subscribe(() ->
				action.getResult().subscribe(() ->
						action.getResult().subscribe(currentActions::decrementAndGet)));

		currentActions.incrementAndGet();
		actionsByActorID.get(actorId).add(action);

		synchronized (threadsLock) {
			if (activeThreads.size() < nthreads && activeThreads.size() < playingNowCount()) {
				Thread worker = sleepingThreads.remove(0);
				threadsLock.notify();
				activeThreads.add(worker);
			}
		}
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 * <p>
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException if the thread that shut down the threads is interrupted
	 */
	public void shutdown() throws InterruptedException {
		List<Thread> allThreads = new ArrayList<>();
		System.out.println("shutdown: Number of actions after shutdown: " + currentActions.get());
		System.out.println("shutdown: Number of active threads after shutdown: " + activeThreads.size());
		System.out.println("shutdown: Number of sleeping threads after shutdown: " + sleepingThreads.size());
		while (currentActions.get() != 0) {
			Thread.sleep(1000);
		}
		terminate.set(true);
		synchronized (threadsLock) {
			for (Thread thread: sleepingThreads) {
				allThreads.add(thread);
				threadsLock.notifyAll();
				System.out.println("shutdown: found a sleeping thread: " + thread.getName());
			}
				allThreads.addAll(activeThreads);
		}
			for (Thread thread : allThreads) {
				thread.join();
		}
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		for (int i = 0; i < nthreads; i++) {
			Thread worker = new Thread(this::task);
			worker.setName("Worker " + (i));
			activeThreads.add(worker);
			worker.start();
		}
	}

//		must be holding threadsLock
	private int playingNowCount() {
		if (!Thread.holdsLock(threadsLock))
			throw new RuntimeException("threadLock not held in playingNowCount");
		int counter = 0;
		for (Queue<Action<?>> actions : actionsByActorID.values())
				if (!actions.isEmpty())
					counter++;
		return counter;
	}


	private void task() {
		Action<?> currAction;
        while (true) {
			synchronized (threadsLock) {
				if (activeThreads.size() > playingNowCount()) {
					System.out.println("task: found self " + activeThreads.remove(Thread.currentThread()));
					sleepingThreads.add(Thread.currentThread());
					try {
						if (!terminate.get())
							threadsLock.wait();
						if (terminate.get()){
							System.out.println("thread: " + Thread.currentThread().getName()+ " is saying bye bye");
							return;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
            for (String actor : locksByID.keySet()) {
                try {
                    if (locksByID.get(actor).tryLock()) {
                        currAction = actionsByActorID.get(actor).poll();
                        if (currAction != null) {
                            currAction.handle(this, actor, actors.get(actor));
                        }
                        locksByID.get(actor).unlock();
                    }
                } catch (Exception e) {
					e.printStackTrace();
				}
            }
        }
    }
}
