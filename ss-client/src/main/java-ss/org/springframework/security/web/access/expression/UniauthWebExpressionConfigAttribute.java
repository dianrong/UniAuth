package org.springframework.security.web.access.expression;

import org.springframework.expression.Expression;
import org.springframework.security.access.ConfigAttribute;

public class UniauthWebExpressionConfigAttribute implements ConfigAttribute{
    private static final long serialVersionUID = 1451901931314373762L;
    private final Expression authorizeExpression;

    public UniauthWebExpressionConfigAttribute(Expression authorizeExpression) {
        this.authorizeExpression = authorizeExpression;
    }

    Expression getAuthorizeExpression() {
        return authorizeExpression;
    }

    public String getAttribute() {
        return null;
    }

    @Override
    public String toString() {
        return authorizeExpression.getExpressionString();
    }
}
