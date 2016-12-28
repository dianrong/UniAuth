package org.springframework.security.web.access.sessionmanage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.session.InvalidSessionStrategy;

/**
 * 组合InvalidSessionStrategy
 * @author wanglin
 */
public final class CompositeInvalidSessionStrategy implements InvalidSessionStrategy {
    
    private List<InvalidSessionStrategy> invalidSessionStrategies;
    
    public List<InvalidSessionStrategy> getInvalidSessionStrategies() {
        if (invalidSessionStrategies == null) {
            return null;
        }
        return Collections.unmodifiableList(invalidSessionStrategies);
    }

    public void setInvalidSessionStrategies(List<InvalidSessionStrategy> invalidSessionStrategies) {
        this.invalidSessionStrategies = new ArrayList<InvalidSessionStrategy>(invalidSessionStrategies);
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (this.invalidSessionStrategies != null && !this.invalidSessionStrategies .isEmpty()) {
            for (InvalidSessionStrategy invalidSessionStrategy: this.invalidSessionStrategies) {
                invalidSessionStrategy.onInvalidSessionDetected(request, response);
            }
        }
    }
}
