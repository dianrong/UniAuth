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
    
    private List<InvalidSessionStrategy> invalidSessionStrategys;
    
    public List<InvalidSessionStrategy> getInvalidSessionStrategys() {
        if (invalidSessionStrategys == null) {
            return null;
        }
        return Collections.unmodifiableList(invalidSessionStrategys);
    }

    public void setInvalidSessionStrategys(List<InvalidSessionStrategy> invalidSessionStrategys) {
        this.invalidSessionStrategys = new ArrayList<InvalidSessionStrategy>(invalidSessionStrategys);
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (this.invalidSessionStrategys != null && !this.invalidSessionStrategys .isEmpty()) {
            for (InvalidSessionStrategy invalidSessionStrategy: this.invalidSessionStrategys) {
                invalidSessionStrategy.onInvalidSessionDetected(request, response);
            }
        }
    }
}
