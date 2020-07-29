package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    Inventory A = null;
    String[] gadjets = new String[3];

    @BeforeEach
    public void setUp() {
        gadjets[0] = "Sky Hook";
        gadjets[1] = "Dagger shoe";
        gadjets[2] = null;
        A.load(gadjets);
    }

    @Test
    public void getItemTest() {
        assertFalse(A.getItem("X-ray Glasses"));
        assertTrue(A.getItem("Sky Hook"));
        assertTrue(A.getItem(null));
    }

    @Test
    public void loadTest() {
        Inventory newInventory = null;
        String[] newGadjets = new String[2];
        newGadjets[0] = "rope";
        newGadjets[1] = "future watch";
        newInventory.load(newGadjets);
        assertTrue(newInventory.getItem("rope"));
        assertFalse(newInventory.getItem("magic board"));
    }
}