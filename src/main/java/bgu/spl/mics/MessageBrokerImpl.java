package bgu.spl.mics;

import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
    private static class SingletonHolder {
        private static MessageBroker instance = new MessageBrokerImpl();
    }

    private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> map_msgCls_subs;//for every message hold subscriber
    private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> map_sub_todo;//for every sub hold his todo list
    private ConcurrentHashMap<Event, Future> map_ev_fu;

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBroker getInstance() {
        return SingletonHolder.instance;
    }

    private MessageBrokerImpl() {
        map_msgCls_subs = new ConcurrentHashMap<>();
        map_sub_todo = new ConcurrentHashMap<>();
        map_ev_fu = new ConcurrentHashMap<>();
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
        subscribeMessage(type, m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        subscribeMessage(type, m);
    }

    private void subscribeMessage(Class<? extends Message> type, Subscriber m) {
        map_msgCls_subs.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        ConcurrentLinkedQueue<Subscriber> q = map_msgCls_subs.get(type);
        q.add(m);
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        @SuppressWarnings("unchecked")
        Future<T> future = map_ev_fu.get(e);
        future.resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        ConcurrentLinkedQueue<Subscriber> Q = map_msgCls_subs.get(b.getClass());
        if (Q == null)
            return;
        //adding b to the queue of every s who is registered to this b
        Q.forEach(s -> {
            if (!(b instanceof TickBroadcast))
                System.out.println("MB: sent " + b.getClass().getSimpleName() + " to " + s.getName());
            LinkedBlockingQueue<Message> todo = map_sub_todo.get(s);
            if (todo != null)
                todo.add(b);
        });
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        Future<T> f = new Future<>();
        ConcurrentLinkedQueue<Subscriber> subs = map_msgCls_subs.get(e.getClass());
        if (subs == null)
            return null;
        Subscriber s;
        //block all threads (except of one) with e of the same class
        synchronized (e.getClass()) {
            if (subs.isEmpty())
                return null;
            s = subs.poll();
            subs.add(s);
        }
        LinkedBlockingQueue<Message> messages;
        map_ev_fu.put(e, f);
        synchronized (s) {
            messages = map_sub_todo.get(s);
            if (messages == null)
                return null;
            messages.add(e);
        }
        return f;
    }

    @Override
    public void register(Subscriber m) {
        map_sub_todo.put(m, new LinkedBlockingQueue<>());
    }

    @Override
    public void unregister(Subscriber m) {
        // for each pair removing m from the queue
        map_msgCls_subs.forEach((msgCls, subscribers) -> {
            synchronized (msgCls) {
                subscribers.remove(m);
            }
        });
        // sending null to the futures of all the events of m
        LinkedBlockingQueue<Message> todo;
        synchronized (m) {
            todo = map_sub_todo.remove(m);
        }
        while (!todo.isEmpty()) {
            Message message = todo.poll();
            if (message == null)
                return;
            Future<?> future = map_ev_fu.get(message);
            if (future != null && !future.isDone()) {
                future.resolve(null);
            }
        }
    }

    @Override
    public Message awaitMessage(Subscriber m) throws InterruptedException {
        return map_sub_todo.get(m).take();
    }


}