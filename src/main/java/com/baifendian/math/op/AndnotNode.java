//package com.baifendian.math.op;
//
//import com.baifendian.math.Expression;
//import com.baifendian.math.FuncMap;
//import com.baifendian.math.OpNode;
//import com.baifendian.math.VarMap;
//import org.roaringbitmap.RoaringBitmap;
//
///**
//A node of an expression tree, represented by the symbol "*".
//*/
//public class AndnotNode extends OpNode {
//
//	public AndnotNode(Expression leftChild, Expression rightChild) {
//		super(leftChild, rightChild);
//	}
//
//	/**
//	Multiples the evaluation of the left side and the evaluation of the right side and returns the result.
//	*/
//	public RoaringBitmap eval(VarMap v, FuncMap f) {
//		RoaringBitmap a = leftChild.eval(v, f);
//		RoaringBitmap b = rightChild.eval(v, f);
//		return RoaringBitmap.andNot(a, b);
//	}
//
//	public String getSymbol() {
//		return SYMBOL_ANDNOT;
//	}
//}
