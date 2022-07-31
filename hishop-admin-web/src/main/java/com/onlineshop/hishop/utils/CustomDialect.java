package com.onlineshop.hishop.utils;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

public class CustomDialect extends AbstractDialect implements IExpressionObjectDialect {
    private final IExpressionObjectFactory EXPRESSION_OBJECTS_FACTORY = new WorkFocusExpressionFactory();

    protected CustomDialect(String name) {
        super(name);
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return this.EXPRESSION_OBJECTS_FACTORY;
    }

}
