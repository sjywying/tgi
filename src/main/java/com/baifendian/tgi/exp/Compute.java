package com.baifendian.tgi.exp;

import com.baifendian.math.Expression;
import com.baifendian.math.ExpressionTree;
import com.baifendian.math.VarMap;
import org.roaringbitmap.RoaringBitmap;

import java.util.Arrays;

/**
 * Created by crazyy on 16/9/29.
 */
public class Compute {
//
//    private static RoaringBitmap result = new RoaringBitmap();
//    private static Stack<Object> exp = new Stack();
//
//
//    public RoaringBitmap compute(RoaringBitmap result, RoaringBitmap result2) {
//        if(result == null) {
//            if(exp.pop() instanceof String);
//        }
//
//        if(exp.pop() instanceof String) {
//
//        }
//
//
//    }

    public static char SYMBOL_AND = '∪';
    public static char SYMBOL_OR = '∩';

    public static void main(String[] args) {

        String s = "(aa ∩ bbb ∪ abc)";
        Expression x = ExpressionTree.parse(s);
        System.out.println(Arrays.toString(x.getVariableNames()));
        VarMap vm = new VarMap(false);
        vm.setValue("aa", RoaringBitmap.bitmapOf(1,2,3,4));
        vm.setValue("bbb", RoaringBitmap.bitmapOf(4,5,6,7,8));
        vm.setValue("abc", RoaringBitmap.bitmapOf(2,3,4,9));

        RoaringBitmap bitmap = x.eval(vm, null);
        System.out.println(Arrays.toString(bitmap.toArray())); // (pi*(r^2))

    }
}
