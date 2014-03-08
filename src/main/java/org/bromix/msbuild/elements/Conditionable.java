package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;

/**
 * Interface to describe an element conditionable.
 * <p>
 * If an element supports conditions the implementation of an element should
 * implement this interface.
 * @see <a href="http://msdn.microsoft.com/en-us/library/7szfhaft.aspx">MSBuild Conditions</a>
 * @author Matthias Bromisch
 */
public interface Conditionable {
    /**
     * Returns the condition of the element.
     * @return condition of the element.
     * @see Condition
     */
    public Condition getCondition();
}
