package com.onlineshop.hishop.utils;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Set;

public class WorkFocusExpressionFactory implements IExpressionObjectFactory {

    @Override
    public Set<String> getAllExpressionObjectNames() {
        return null;
    }

    @Override
    public Object buildObject(IExpressionContext iExpressionContext, String s) {
        return null;
    }

    @Override
    public boolean isCacheable(String s) {
        return false;
    }
}
