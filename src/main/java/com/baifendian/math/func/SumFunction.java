//package com.baifendian.math.func;
//
//import org.roaringbitmap.RoaringBitmap;
//
///**
//The sum function.
//*/
//public class SumFunction implements Function {
//
//	public SumFunction() {}
//
//	/**
//	Returns the sum of the values in the array from [0, numParam).
//	*/
//	public RoaringBitmap of(RoaringBitmap[] d, int numParam) {
//		RoaringBitmap sum = 0;
//
//		for (int i = 0; i < numParam; i++)
//			sum += d[i];
//
//		return sum;
//	}
//
//	/**
//	Returns true for 1 or more parameters, false otherwise.
//	*/
//	public boolean acceptNumParam(int numParam) {
//		return numParam > 0;
//	}
//
//	public String toString() {
//		return "sum(x1, x2, ..., xn)";
//	}
//}