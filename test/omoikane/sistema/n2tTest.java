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
        System.out.println(n.aCifra(212.10));
        System.out.println(n.aCifra(212.11));
        System.out.println(n.aCifra(212.12));
        System.out.println(n.aCifra(212.13));
        System.out.println(n.aCifra(212.14));
        System.out.println(n.aCifra(212.15));
        System.out.println(n.aCifra(212.16));
        System.out.println(n.aCifra(212.17));
        System.out.println(n.aCifra(212.18));
        System.out.println(n.aCifra(212.19));
    }

}