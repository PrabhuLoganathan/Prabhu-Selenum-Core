package com.ggasoftware.jdi_ui_tests.core.elements.interfaces.common;

import com.ggasoftware.jdi_ui_tests.core.elements.interfaces.base.IClickable;
import com.ggasoftware.jdi_ui_tests.core.elements.interfaces.base.ISetValue;
import com.ggasoftware.jdi_ui_tests.core.elements.page_objects.annotations.JDIAction;

/**
 * Created by Roman_Iovlev on 7/6/2015.
 */
public interface ICheckBox extends IClickable, ISetValue {
    /** Set checkbox checked */
    @JDIAction
    void check();
    /** Set checkbox unchecked */
    @JDIAction
    void uncheck();
    /** Verify is checkbox checked */
    @JDIAction
    boolean isChecked();
}