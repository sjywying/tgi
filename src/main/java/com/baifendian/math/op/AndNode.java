package com.baifendian.math.op;

import com.baifendian.math.*;
import org.roaringbitmap.RoaringBitmap;

/**
A node of an expression tree, represented by the symbol "+".
*/
public class AndNode extends OpNode {

	public AndNode(Expression leftChild, Expression rightChild) {
		super(leftChild, rightChild);
	}

	/**
	Adds the evaluation of the left side to the evaluation of the right side and returns the result.
	*/
	public RoaringBitmap eval(VarMap v, FuncMap f) {
		RoaringBitmap a = leftChild.eval(v, f);
		RoaringBitmap b = rightChild.eval(v, f);
		return RoaringBitmap.and(a, b);
	}

	public String getSymbol() {
		return String.valueOf(Op.SYMBOL_AND);
	}
}
