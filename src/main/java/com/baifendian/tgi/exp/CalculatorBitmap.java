package com.baifendian.tgi.exp;

import org.roaringbitmap.RoaringBitmap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CalculatorBitmap {

	private String expression;// 表达式原始格式
	private Stack<String> ops = new Stack<String>();// 操作栈
	private Stack<String> vals = new Stack<String>();// 值栈
	private HashMap<String, Method> map = new HashMap<String, Method>();// 运算方法栈
	private ArrayList<String> expressions = new ArrayList<String>();// 存储分析表达式后的元素
	private HashMap<Type, Class<?>> types = new HashMap<Type, Class<?>>();// 存储数据类型

	/**
	 * 自定义运算方法库
	 *
	 * @author so
	 *
	 */
	private static class DIYLIB {
		@SuppressWarnings("unused")
		public static RoaringBitmap and(RoaringBitmap a, RoaringBitmap b) {
			return RoaringBitmap.and(a, b);
		}

		@SuppressWarnings("unused")
		public static RoaringBitmap or(RoaringBitmap a, RoaringBitmap b) {
			return RoaringBitmap.or(a, b);
		}
	}

	/**
	 * 构造器
	 *
	 * @param expression
	 *            计算表达式
	 */
	public CalculatorBitmap(String expression) {
		this.expression = expression;
		this.initHeartMap();
	}

	/**
	 * 设置运算表达式
	 * 
	 * @param expression
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * 加载初始化核心库
	 */
	private void initHeartMap() {
		try {

			types.put(Double.TYPE, Double.class);
			types.put(Integer.TYPE, Integer.class);
			types.put(Long.TYPE, Long.class);
			types.put(Float.TYPE, Float.class);

			Method[] methods = Math.class.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				this.map.put(methods[i].getName(), methods[i]);
			}
			this.map.put("+",
					DIYLIB.class.getMethod("add", Double.TYPE, Double.TYPE));
			this.map.put("/",
					DIYLIB.class.getMethod("divide", Double.TYPE, Double.TYPE));
			this.map.put("-", DIYLIB.class.getMethod("subtract", Double.TYPE,
					Double.TYPE));
			this.map.put("*", DIYLIB.class.getMethod("multiply", Double.TYPE,
					Double.TYPE));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	private Map<String, RoaringBitmap> bitmaps = new HashMap<>();

	/**
	 * 系统准备阶段 拆分表达式
	 */
	private void prepare() {
		Stack<String> vals = new Stack<String>();// 值栈

		String[] expSplit = expression.split(" ");

		for (int i = expSplit.length-1; i > 0; i--) {
			String op = expSplit[i];
//			if(!"".equals(op.trim()) && ) {
//				vals.push(op);
//			}
		}
	}

//	private RoaringBitmap compute() {
//
//	}

	/**
	 * 系统开始计算
	 * 
	 * @return 计算结果
	 */
	public String start() {

		this.prepare();// 运算准备阶段，分析数据元素

		this.init();// 初始化数据环境，解决括号优先级
		this._doit("^[/*/]$");// 乘除优先级
		this._doit("^[/+-]$");// 加减优先级

		String result = this.getResult();// 得到运算结果
		
		return result;

	}

	/**
	 * 得到运算结果
	 * 
	 * @return
	 */
	private String getResult() {
		if (this.vals.size() == 1 && this.ops.isEmpty()) {
			return this.vals.pop();
		}
		return "error";

	}

	/**
	 * 分步计算实现2
	 * 
	 * @param regx
	 *            需要匹配的正则
	 */
	private void _doit(String regx) {
		for (int k = 0; k < this.ops.size(); k++) {
			String op = this.ops.get(k);
			if (op.matches(regx)) {
				Method method = this.map.get(op);
				try {
					if (k + 1 < this.vals.size()) {
						String val = method.invoke(method.getClass(),
								Double.valueOf(this.vals.get(k)),
								Double.valueOf(this.vals.get(k + 1)))
								.toString();
						this.vals.set(k, val);
						this.vals.remove(k + 1);
						this.ops.remove(k);
						k--;
					}
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 分部计算实现
	 * 
	 *            操作符
	 * @return 计算结果
	 */
	private void init() {
		for (int i = 0; i < this.expressions.size(); i++) {
			String item = this.expressions.get(i);
			if (item.matches("^[/+-/*/]$") || item.matches("^[a-z]+[0-9]*$")) {// 处理运算
				this.ops.push(item);
			}
			if (item.matches("^[0-9]*$")) {// 处理数字
				this.vals.push(item);
			}
			if (item.equals(")")) {
				String op = this.ops.pop();
				Method method = this.map.get(op);
				Class<?>[] types = method.getParameterTypes();
				Object[] objects = new Object[types.length];
				for (int j = types.length - 1; j >= 0; j--) {
					Object object = null;
					Class<?> number = this.types.get(types[j]);
					try {
						Method mt = number.getDeclaredMethod("valueOf",
								String.class);
						object = mt.invoke(mt.getClass(), this.vals.pop());
					} catch (NoSuchMethodException | SecurityException
							| IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
					}
					objects[j] = object;

				}
				try {
					this.vals.push(method.invoke(method.getClass(), objects)
							.toString());
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		CalculatorBitmap calculator = new CalculatorBitmap(args[0]);
		System.out.println(calculator.start());
	}
}