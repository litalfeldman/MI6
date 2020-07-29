package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    Future<Event> f;

    @BeforeEach
    public void setUp() {
        f = new Future<>();
    }

    @Test
    public void getTest() {
        assertTrue(f.get() != null);
        assertFalse(f.get() instanceof Future);
    }

    @Test
    public void resolveTest() {
        assertFalse(f.isDone());
        f.resolve(f.get());
        assertTrue(f.isDone());
    }

    @Test
    public void isDoneTest() {
        assertFalse(f.isDone());
        f.resolve(f.get());
        assertTrue(f.isDone());
    }
}