package com.ggasoftware.uitest.asserter;

import com.ggasoftware.uitest.utils.linqInterfaces.JAction;
import com.ggasoftware.uitest.utils.linqInterfaces.JFuncT;
import org.testng.Assert;

import static com.ggasoftware.uitest.logger.enums.LogInfoTypes.FRAMEWORK;
import static com.ggasoftware.uitest.utils.settings.FrameworkSettings.logger;

/**
 * Created by Roman_Iovlev on 6/9/2015.
 */
public class TestNGAsserter extends Assert implements IAsserter {
    public Exception exception(String message) {
        logger.error(FRAMEWORK, message);
        assertTrue(false, message);
        return new Exception(message);
    }
    public void silentException(JAction action) {
        try { action.invoke();
        } catch (Exception ex) { exception(ex.getMessage()); }
    }
    public <TResult> TResult silentException(JFuncT<TResult> func) {
        try { return func.invoke();
        } catch (Exception ex) { exception(ex.getMessage()); }
        return null;
    }


}
