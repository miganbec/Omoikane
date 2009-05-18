/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;


//Requires Fest library:
import org.fest.swing.fixture.FrameFixture;  

//Requires JUnit 4.1 library:
import org.junit.Test;  
import org.junit.After;  
import org.junit.Before;  

import omoikane.formularios.*;
import omoikane.sistema.*;
/**
 *
 * @author SYSTEM
 */

public class TestSplashTest {

    private FrameFixture window;

    @Before
    public void setUp() {
        //Assumes the main class is named "Anagrams" and extends JFrame:
        window = new FrameFixture(Dialogos.lanzarAlerta("jijiji"));
        window.show();

    }

    @Test
    public void shouldEnterAnagramAndReturnTrue() {
        //Assumes a JTextField named "anagramField":
        window.textBox("anagramField").enterText("abstraction");
        //Assumes a JButton named "guessButton":
        window.button("guessButton").click();
        //Assumes a JLabel named "resultLabel":
        window.label("resultLabel").requireText("Correct! Try a new word!");
    }

    @After
    public void tearDown() {
        window.cleanUp();
    }

}