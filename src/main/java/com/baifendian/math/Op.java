package com.baifendian.math;

/**
 * Created by crazyy on 16/9/29.
 */
public class Op {
    public static char SYMBOL_AND = '*';//'∩'; //'*';
    public static char SYMBOL_OR = '/';//'∪'; //'/';

    public static boolean contain(char op) {
        if(op == SYMBOL_AND || op == SYMBOL_OR) {
            return true;
        } else {
            return false;
        }
    }
}
