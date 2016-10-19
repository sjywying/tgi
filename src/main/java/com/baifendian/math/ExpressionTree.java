package com.baifendian.math;

import com.baifendian.math.op.*;
import com.baifendian.struc.Stack;
import com.sun.tools.corba.se.idl.constExpr.Xor;

/**
<p>Expression string parser.  The parser returns an Expression object which can be evaluated.

<p>The elements of an expression fall into two categories, operators and terms.  Terms include
variables, functions and values.  Although values do not subclass TermNode, they are processed
in a similar way during the parsing.

<p>The operators are exponent (^), times (*), divide (/), plus (+) and minus (-).  The exponent has
the highest precedence, then times and divide (which have an equal level of precedence) and then
plus and minus (which have an equal level of precedence).  For those operators that have an equal
level of precedence, the operator that comes first (reading from left to right) gets the higher
level of precedence.  For example, a-b+c becomes ((a-b)+c)

<p>During the parsing, the string is scanned from left to right.  Either a term or an operator will
come next.  If it is a term, then it may be preceded by an optional + or - sign.  For example,
For example +a/-b/+c is valid, and becomes ((a/(-b))/c).  If a term is not explicitly signed, then
by default it is positive.

<p>If the first character of a term is a decimal (.) or a digit (0-9) then the term marks the
beginning of a value.  The first character that is found that is <b>not</b> a digit or a decimal marks the
end of the value, except if the character is an 'e' or an 'E'.  In this case, the first digit immediately
following can be a plus sign, minus sign or digit.  E.g. 1.23E+4.  At this point, the first character that
is not a digit marks the end of the value.

<p>If the first character of a term is <b>not</b> a decimal, digit, open bracket '(', a close bracket ')', a comma ',',
a whitespace character ' ', '\t', '\n', or an operator, then the character marks the beginning of a variable or
function name.  The first character found that is an operator, whitespace, open bracket, close bracket or comma
marks the end of the name.  If the first non-whitespace character after a name is an open bracket, then it marks
the beginning of the function parameters, otherwise the term is marked as a variable.

<p>Functions that accept more than one parameter will have the parameters separated by commas.  E.g.
f(x,y).  Since the parameters of a function are also expressions, when a comma is detected that has
exactly one non-balanced open bracket to the left, a recursive call is made passing in the substring.
E.g. f(g(x,y),z), a recursive call will occur passing in the substring "g(x,y)".

<p>Miscellaneous Notes

<ul>
<li>Names cannot begin with a decimal or a digit since those characters mark the beginning of a value.</li>
<li>In general, whitespace is ignored or marks the end of a name or value.</li>
<li>Evaluation of values is done using the Double.parseDouble(...) method.</li>
<li>Constants can be represented as functions that take no parameters.  E.g. pi() or e()</li>
<li>Round brackets '(' and ')' are the only brackets that have special meaning.</li>
<li>The brackets in the expression must balance otherwise a ExpressionParseException is thrown.</li>
<li>An ExpressionParseException is thrown in all cases where the expression string is invalid.</li>
<li>All terms must be separated by an operator.  E.g. 2x is <b>not</b> valid, but 2*x is.</li>
<li>In cases where simplification is possible, simplification is <b>not</b> done.  E.g. 2^4 is <b>not</b>
simplified to 16.</li>
<li>Computerized scientific notation is supported for values.  E.g. 3.125e-4</li>
<li>Scoped negations are not permitted. E.g. -(a) is <b>not</b> valid, but -1*(a) is.</li>
</ul>

@see Expression
*/
public class ExpressionTree {

	private ExpressionTree() {}

	/**
	Returns an expression-tree that represents the expression string.  Returns null if the string is empty.

	@throws ExpressionParseException If the string is invalid.
	*/
	public static Expression parse(String s) {
		if (s == null)
			throw new ExpressionParseException("Expression string cannot be null.", -1);

		return build(s, 0);
	}

	private static Expression build(String s, int indexErrorOffset) {

		// do not remove (required condition for functions with no parameters, e.g. Pi())
		if (s.trim().length() == 0)
			return null;

		Stack s1 = new Stack(); // contains expression nodes
		Stack s2 = new Stack(); // contains open brackets ( and operators ^,*,/,+,-

		boolean term = true; // indicates a term should come next, not an operator
		boolean signed = false; // indicates if the current term has been signed

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == ' ' || c == '\t' || c == '\n')
				continue;

			if (term) {
				if (c == '(') {
					s2.push("(");
				}
				else if (!signed && Op.contain(c)) {
					signed = true;
				}
				else if (c != ',' && c != ')' && !Op.contain(c)) {
					int j = i + 1;
					while (j < s.length()) {
						c = s.charAt(j);
						if (c != ',' && c != ' ' && c != '\t' && c != '\n' && c != '(' && c != ')' && !Op.contain(c))
							j++;
						else break;
					}

					if (j < s.length()) {
						int k = j;
						while (c == ' ' || c == '\t' || c == '\n') {
							k++;
							if (k == s.length()) break;
							c = s.charAt(k);
						}

						if (c == '(') {
							FuncNode fn = new FuncNode(s.substring(i, j));
							int b = 1;
							int kOld = k + 1;
							while (b != 0) {
								k++;

								if (k >= s.length()) {
									throw new ExpressionParseException("Missing function close bracket.", i + indexErrorOffset);
								}

								c = s.charAt(k);

								if (c == ')') {
									b--;
								}
								else if (c == '(') {
									b++;
								}
								else if (c == ',' && b == 1) {
									Expression o = build(s.substring(kOld, k), kOld);
									if (o == null) {
										throw new ExpressionParseException("Incomplete function.", kOld + indexErrorOffset);
									}
									fn.add(o);
									kOld = k + 1;
								}
							}
							Expression o = build(s.substring(kOld, k), kOld);
							if (o == null) {
								if (fn.numChildren() > 0) {
									throw new ExpressionParseException("Incomplete function.", kOld + indexErrorOffset);
								}
							}
							else {
								fn.add(o);
							}
							s1.push(fn);
							i = k;
						}
						else {
							s1.push(new VarNode(s.substring(i, j)));
							i = k - 1;
						}
					}
					else {
						s1.push(new VarNode(s.substring(i, j)));
						i = j - 1;
					}

					term = false;
					signed = false;
				}
				else {
					throw new ExpressionParseException("Unexpected character: " + String.valueOf(c), i + indexErrorOffset);
				}
			}
			else {
				if (c == ')') {
					Stack s3 = new Stack();
					Stack s4 = new Stack();
					while (true) {
						if (s2.isEmpty()) {
							throw new ExpressionParseException("Missing open bracket.", i + indexErrorOffset);
						}
						Object o = s2.pop();
						if (o.equals("(")) break;
						s3.addToTail(s1.pop());
						s4.addToTail(o);
					}
					s3.addToTail(s1.pop());

					s1.push(build(s3, s4));
				}
				else if (Op.contain(c)) {
					term = true;
					s2.push(String.valueOf(c));
				}
				else {
					throw new ExpressionParseException("Expected operator or close bracket but found: " + String.valueOf(c), i + indexErrorOffset);
				}
			}
		}

		if (s1.size() != s2.size() + 1) {
			throw new ExpressionParseException("Incomplete expression.", indexErrorOffset + s.length());
		}

		return build(s1, s2);
	}

	private static Expression build(Stack s1, Stack s2) {
		Stack s3 = new Stack();
		Stack s4 = new Stack();

		while (!s2.isEmpty()) {
			Object o = s2.removeTail();
			Object o1 = s1.removeTail();
			Object o2 = s1.removeTail();

//			if (o.equals("^")) {
//				s1.addToTail(new XorNode((Expression) o1, (Expression) o2));
//			}
//			else {
				s1.addToTail(o2);
				s4.push(o);
				s3.push(o1);
//			}
		}

		s3.push(s1.pop());

		while (!s4.isEmpty()) {
			Object o = s4.removeTail();
			Object o1 = s3.removeTail();
			Object o2 = s3.removeTail();

//			if (o.equals("*")) {
//				s3.addToTail(new AndnotNode((Expression) o1, (Expression) o2));
//			}
//			else if (o.equals("/")) {
//				s3.addToTail(new OrNode((Expression) o1, (Expression) o2));
//			}
//			else {
				s3.addToTail(o2);
				s2.push(o);
				s1.push(o1);
//			}
		}

		s1.push(s3.pop());

		while (!s2.isEmpty()) {
			Object o = s2.removeTail();
			Object o1 = s1.removeTail();
			Object o2 = s1.removeTail();

			if (o.equals(String.valueOf(Op.SYMBOL_AND))) {
				s1.addToTail(new AndNode((Expression) o1, (Expression) o2));
			}
			else if (o.equals(String.valueOf(Op.SYMBOL_OR))) {
				s1.addToTail(new OrNode((Expression) o1, (Expression) o2));
			}
//			else if (o.equals(OpNode.SYMBOL_ANDNOT)) {
//				s1.addToTail(new AndnotNode((Expression) o1, (Expression) o2));
//			}
//			else if (o.equals(OpNode.SYMBOL_XOR)) {
//				s1.addToTail(new XorNode((Expression) o1, (Expression) o2));
//			}
			else {
				// should never happen
				throw new ExpressionParseException("Unknown operator: " + o, -1);
			}
		}

		return (Expression) s1.pop();
	}
}
