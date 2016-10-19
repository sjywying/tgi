package com.baifendian.math;

import org.roaringbitmap.RoaringBitmap;

/**
A node of an expression tree that represents a variable.  A VarNode cannot have any children.
*/
public class VarNode extends TermNode {

	public VarNode(String name) {
		super(name);
	}

	/**
	Returns the value associated with the variable name in the VarMap.
	*/
	public RoaringBitmap eval(VarMap v, FuncMap f) {
		RoaringBitmap val = v.getValue(name);

		return val;
	}
}
