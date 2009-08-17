/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author SYSTEM
 */
public class n2tTest {

    public n2tTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    @Test
    public void convertirLetrasTest() {
        n2t n = new n2t();
        System.out.println(n.convertirLetras(1500));
        System.out.println(n.convertirLetras(11753));
        System.out.println(n.convertirLetras(100));
        System.out.println(n.convertirLetras(13296));

        System.out.println(n.aCifra(212.4));
    }

}