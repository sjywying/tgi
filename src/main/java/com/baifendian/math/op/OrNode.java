package com.baifendian.math.op;

import com.baifendian.math.*;
import org.roaringbitmap.RoaringBitmap;

/**
A node of an expression tree, represented by the symbol "/".
*/
public class OrNode extends OpNode {

	public OrNode(Expression leftChild, Expression rightChild) {
		super(leftChild, rightChild);
	}

	/**
	Divides the evaluation of the left side by the evaluation of the right side and returns the result.
	*/
	public RoaringBitmap eval(VarMap v, FuncMap f) {
		RoaringBitmap a = leftChild.eval(v, f);
		RoaringBitmap b = rightChild.eval(v, f);
		return RoaringBitmap.or(a, b);
	}

	public String getSymbol() {
		return String.valueOf(Op.SYMBOL_OR);
	}
}
