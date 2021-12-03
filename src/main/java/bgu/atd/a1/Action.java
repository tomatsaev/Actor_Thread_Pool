package bgu.atd.a1;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * an abstract class that represents an action that may be executed using the
 * {@link ActorThreadPool}
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!
 *
 * @param <R> the action result type
 */
public abstract class Action<R> {

    private Promise<R> promise = new Promise<>();
    private ActorThreadPool pool;
    private AtomicInteger dependenciesCounter = new AtomicInteger(0);
    private String actorID;
    private Boolean started = false;
    private PrivateState actorState;
    private callback callback;

    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected abstract void start();


    /**
     *
     * start/continue handling the action
     *
     * this method should be called in order to start this action
     * or continue its execution in the case where it has been already started.
     *
     * IMPORTANT: this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     *
     */
    /*package*/ final void handle(ActorThreadPool pool, String actorId, PrivateState actorState) {
        if(!started && !this.promise.isResolved()) {
            this.started = true;
            this.pool = pool;
            this.actorID = actorId;
            this.actorState = actorState;
            start();
        }
        else{
            callback.call();
        }

    }


    /**
     * add a callback to be executed once *all* the given actions results are
     * resolved
     *
     * Implementors note: make sure that the callback is running only once when
     * all the given actions completed.
     *
     * @param actions
     * @param callback the callback to execute once all the results are resolved
     */
    protected final void then(Collection<? extends Action<?>> actions, callback callback) {
        this.callback = callback;
        if (actions.isEmpty()) {
            return;
        }
        dependenciesCounter.addAndGet(actions.size());
        for (Action<?> action: actions) {
            action.getResult().subscribe(() ->
            {
                if (dependenciesCounter.decrementAndGet() == 0)
                    pool.submit(this, this.actorID, this.actorState);
            });
        }
    }

    /**
     * resolve the internal result - should be called by the action derivative
     * once it is done.
     *
     * @param result - the action calculated result
     */
    protected final void complete(R result) {
        this.promise.resolve(result);
    }

    /**
     * @return action's promise (result)
     */
    public final Promise<R> getResult() {
        return this.promise;
    }

    /**
     * send an action to an other actor
     *
     * @param action
     * 				the action
     * @param actorId
     * 				actor's id
     * @param actorState
     * 				actor's private state (actor's information)
     */
    public void sendMessage(Action<?> action, String actorId, PrivateState actorState){
        pool.submit(action, actorId, actorState);
    }

    /**
     * set action's name
     * @param actionName
     */
    public void setActionName(String actionName){
        this.promise.setActionName(actionName);
    }

    /**
     * @return action's name
     */
    public String getActionName(){
        return this.promise.getActionName();
    }
}
