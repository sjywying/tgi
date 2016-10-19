package com.graphbuilder.math;

public class MathFile {

	public static void main(String[] args) {
		String s = "pi*r^2";
		Expression x = ExpressionTree.parse(s);

		VarMap vm = new VarMap(false /* case sensitive */);
		vm.setValue("pi", Math.PI);
		vm.setValue("r", 5);

		FuncMap fm = null; // no functions in expression

		System.out.println(x); // (pi*(r^2))
		System.out.println(x.eval(vm, fm)); // 78.53981633974483

		vm.setValue("r", 10);
		System.out.println(x.eval(vm, fm)); // 314.1592653589793
	}
}