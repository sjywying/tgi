//package com.baifendian.math;
//
//import org.roaringbitmap.RoaringBitmap;
//
///**
//A node of an expression tree that represents a value.  A ValNode cannot have any children.
//*/
//public class ValNode extends Expression {
//
//	protected RoaringBitmap val = new RoaringBitmap();
//
//	public ValNode(RoaringBitmap d) {
//		val = d;
//	}
//
//	/**
//	Returns the value.
//	*/
//	public RoaringBitmap eval(VarMap v, FuncMap f) {
//		return val;
//	}
//
//	public RoaringBitmap getValue() {
//		return val;
//	}
//
//	public void setValue(RoaringBitmap d) {
//		val = d;
//	}
//}
