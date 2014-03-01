package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;

/**
 *
 * @author Matthias Bromisch
 */
public interface Conditionable {
    public void setCondition(Condition condition);
    public Condition getCondition();
}
