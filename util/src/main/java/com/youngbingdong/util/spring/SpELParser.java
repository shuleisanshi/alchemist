package com.youngbingdong.util.spring;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

import static java.util.Objects.requireNonNull;

/**
 * @author ybd
 * @date 18-8-3
 * @contact yangbingdong1994@gmail.com
 */
public final class SpELParser {
	private static final ExpressionParser PARSER = new SpelExpressionParser();
	private static final LocalVariableTableParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

	public static <T> T parseSpEL(Method method, Object[] args, String spEL, Class<T> clazz) {
		String[] parameterNames = DISCOVERER.getParameterNames(method);
		requireNonNull(parameterNames);
		EvaluationContext context = buildSpELContext(parameterNames, args);
		Expression expression = PARSER.parseExpression(spEL);
		return expression.getValue(context, clazz);
	}

	private static EvaluationContext buildSpELContext(String[] parameterNames, Object[] args) {
		EvaluationContext context = new StandardEvaluationContext();
		for (int len = 0; len < parameterNames.length; len++) {
			context.setVariable(parameterNames[len], args[len]);
		}
		context.setVariable("args", args);
		return context;
	}
}
