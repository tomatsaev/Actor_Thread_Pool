package bgu.atd.a1;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * this class represents a deferred result i.e., an object that eventually will
 * be resolved to hold a result of some operation, the class allows for getting
 * the result once it is available and registering a callback that will be
 * called once the result is available. 
 *
 * @param <T>
 *            the result type, <boolean> resolved - initialized ;
 */
public class Promise<T>{

	private final AtomicBoolean isResolved = new AtomicBoolean(false);
	private final AtomicBoolean resolving = new AtomicBoolean(false);
	private final ConcurrentLinkedQueue<callback> callbacks = new ConcurrentLinkedQueue<>();
	private T value;
	private String actionName;

	/**
	 *
	 * @return the resolved value if such exists (i.e., if this object has been
	 *         {@link #resolve(java.lang.Object)}ed 
	 * @throws IllegalStateException
	 *             in the case where this method is called and this object is
	 *             not yet resolved
	 */
	public T get() {
		if(value != null){
			return value;
		}
		else{
			throw new IllegalStateException("Promise did not resolve. No value to return");
		}
	}

    /**
     *
     * @param actionName - action name
     *
     */
	/*package*/ final void setActionName(String actionName){
	    this.actionName = actionName;
    }

    /**
     *
     * @return actionName
     */
    /*package*/ final String getActionName(){
        return this.actionName;
    }

	/**
	 *
	 * @return true if this object has been resolved - i.e., if the method
	 *         {@link #resolve(java.lang.Object)} has been called on this object
	 *         before.
	 */
	public boolean isResolved() { return isResolved.get(); }


	/**
	 * resolve this promise object - from now on, any call to the method
	 * {@link #get()} should return the given value
	 *
	 * Any callbacks that were registered to be notified when this object is
	 * resolved via the {@link #subscribe(callback)} method should
	 * be executed before this method returns
	 *
     * @throws IllegalStateException
     * 			in the case where this object is already resolved
	 * @param value
	 *            - the value to resolve this promise object with
	 */
	public void resolve(T value){
		if (!resolving.compareAndSet(false, true) || isResolved())
			throw new IllegalStateException("Promise already resolved");

		this.value = value;
		isResolved.set(true);

		while(callbacks.peek() != null){
			callbacks.poll().call();
		}

		resolving.set(false);
	}
	/**
	 * add a callback to be called when this object is resolved. If while
	 * calling this method the object is already resolved - the callback should
	 * be called immediately
	 *
	 * @param callback
	 *            the callback to be called when the promise object is resolved
	 */
	public void subscribe(callback callback) {
		if(!resolving.get() && isResolved.get()){
			callback.call();
		}
		else {
			this.callbacks.add(callback);
			if (isResolved.get() && !resolving.get()) {
				if (this.callbacks.remove(callback))
					callback.call();
			}
		}
	}
}
