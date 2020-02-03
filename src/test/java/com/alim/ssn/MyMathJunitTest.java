package com.alim.ssn;

import com.alim.ssn.MyJunitTest.MyMath;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MyMathJunitTest {
    @Test
    public void testDivision(){
        MyMath myMath=new MyMath();
        assertEquals(2, myMath.division(6, 3));
        try
        {
            myMath.division(9, 0);
            fail();
        }catch (Exception e)
        {
            assertTrue(e instanceof ArithmeticException);
        }
    }
}
