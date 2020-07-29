package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.Q;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    private MessageBroker mB;
    private Future<ExampleEvent> vI;
    Q q;
    private Subscriber s1;
    private Subscriber s2;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void subscribeEventTest() {
            mB.subscribeEvent(ExampleEvent.class, s1 );
            Future<String> future1 = mB.sendEvent(new ExampleEvent("sender"));
            future1.resolve("res");
            try{ExampleEvent msg =(ExampleEvent) mB.awaitMessage(s1);
                assertEquals(msg.getSenderName(), "sender");
            }
            catch (Exception InterruptedException){}
    }

    @Test
    public void subscribeBroadcastTest() {
        mB.subscribeBroadcast(ExampleBroadcast.class, s2);
        mB.sendBroadcast(new ExampleBroadcast("sender"));
        try{ExampleBroadcast msg = (ExampleBroadcast)mB.awaitMessage(s2);
            assertEquals(msg.getSenderId(), "sender");
        }
        catch (Exception InterruptedException){}
    }


    @Test
    public void completeTest() {
        Event e = new ExampleEvent("abcdefg");
        ExampleEvent r = vI.get();
        mB.complete(e, r);
        assertTrue(vI.isDone());
    }

    public void awaitMessageTest() throws InterruptedException {
        Message m = mB.awaitMessage(s1);
        assertNotNull(m);
    }


}