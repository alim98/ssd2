package com.alim.ssn.MyJunitTest;

public class MyMath {

    public int division(int a , int b)
    {
        if (b==0)
            throw new ArithmeticException("Divided by zero");

        return a/b;
    }

    public int multiply(int a , int b)
    {
        return a*b;
    }
}
