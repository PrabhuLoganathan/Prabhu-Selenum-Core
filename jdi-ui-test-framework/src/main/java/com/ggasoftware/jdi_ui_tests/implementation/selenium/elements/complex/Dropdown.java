/****************************************************************************
 * Copyright (C) 2014 GGA Software Services LLC
 *
 * This file may be distributed and/or modified under the terms of the
 * GNU General Public License version 3 as published by the Free Software
 * Foundation.
 *
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
 * WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 ***************************************************************************/
package com.ggasoftware.jdi_ui_tests.implementation.selenium.elements.complex;

import com.ggasoftware.jdi_ui_tests.core.utils.linqInterfaces.JFuncTT;
import com.ggasoftware.jdi_ui_tests.implementation.selenium.elements.base.Clickable;
import com.ggasoftware.jdi_ui_tests.implementation.selenium.elements.base.Element;
import com.ggasoftware.jdi_ui_tests.implementation.selenium.elements.interfaces.complex.IDropDown;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * RadioButtons control implementation
 *
 * @author Alexeenko Yan
 */
public class Dropdown<TEnum extends Enum> extends Selector<TEnum> implements IDropDown<TEnum> {
    public Dropdown() { super(); }
    public Dropdown(By selectLocator) { super(selectLocator); }
    public Dropdown(By selectLocator, By optionsNamesLocator) { super(optionsNamesLocator); this.selectLocator = selectLocator;}
    public Dropdown(By selectLocator, By optionsNamesLocator, By allOptionsNamesLocator) {
        super(optionsNamesLocator, allOptionsNamesLocator); this.selectLocator = selectLocator;
    }

    public By selectLocator;
    protected Clickable element() { return new Clickable(selectLocator); }

    protected void expandAction(String name) { if (!isDisplayedAction(name)) element().click(); }
    protected void expandAction(int index) { if (!isDisplayedAction(index)) element().click(); }

    @Override
    protected void selectAction(String name) {
        if (selectLocator != null) {
            expandAction(name);
            super.selectAction(name);
        }
        else
            new Select(getWebElement()).selectByValue(name);
    }
    @Override
    protected void selectAction(int index) {
        if (selectLocator != null) {
            expandAction(index);
            super.selectAction(index);
        }
        else
            new Select(getWebElement()).selectByIndex(index);
    }
    @Override
    protected String getValueAction() { return getTextAction(); }
    @Override
    protected boolean isSelectedAction(String name) { return getTextAction().equals(name); }

    @Override
    public boolean waitDisplayed() {  return element().waitDisplayed(); }
    @Override
    public boolean waitVanished()  { return element().waitVanished(); }

    @Override
    public Boolean wait(JFuncTT<WebElement, Boolean> resultFunc) {
        return element().wait(resultFunc);
    }
    @Override
    public <T> T wait(JFuncTT<WebElement, T> resultFunc, JFuncTT<T, Boolean> condition) {
        return element().wait(resultFunc, condition);
    }
    @Override
    public Boolean wait(JFuncTT<WebElement, Boolean> resultFunc, int timeoutSec) {
        return element().wait(resultFunc, timeoutSec);
    }
    @Override
    public <T> T wait(JFuncTT<WebElement, T> resultFunc, JFuncTT<T, Boolean> condition, int timeoutSec) {
        return element().wait(resultFunc, condition, timeoutSec);
    }
    protected String getTextAction() { return element().getAttribute("value"); }
    public final String getText() { return actions.getText(this::getTextAction); }
    public final String waitText(String text) {
        return actions.waitText(text, this::getTextAction);
    }
    public final String waitMatchText(String regEx) {
        return actions.waitMatchText(regEx, this::getTextAction);
    }

    public void setAttribute(String attributeName, String value) {
        element().setAttribute(attributeName, value);
    }
    public WebElement getWebElement() { return new Element(getLocator()).getWebElement(); }

    public String getAttribute(String name) { return element().getAttribute(name); }

    public boolean waitAttribute(String name, String value) {
        return element().waitAttribute(name, value);
    }

}
