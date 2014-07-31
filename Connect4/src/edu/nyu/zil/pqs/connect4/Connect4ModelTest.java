package edu.nyu.zil.pqs.connect4;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class Connect4ModelTest extends TestCase {
    Connect4Model model = Connect4Model.getModel();

    @Before
    public void setUp() throws Exception {
        Connect4Listener connect4View = mock(Connect4Listener.class);
        model.addListener(connect4View);
        model.play(Connect4Model.MODE.PLAYER);
    }

    @After
    public void tearDown() throws Exception {
        model.reset();
    }

    @Test
    public void testGetModel() throws Exception {

    }

    @Test
    public void testAddListener() throws Exception {

    }

    @Test
    public void testRemoveListener() throws Exception {

    }

    @Test
    public void testPlacePiece() throws Exception {

    }
}