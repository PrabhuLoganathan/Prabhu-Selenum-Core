/****************************************************************************
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
package com.ggasoftware.uitest.control.simple;

import com.ggasoftware.uitest.control.BaseElement;
import com.ggasoftware.uitest.control.interfaces.IElements;
import com.ggasoftware.uitest.utils.*;
import com.ggasoftware.uitest.utils.common.Timer;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

import static com.ggasoftware.uitest.utils.common.LinqUtils.first;
import static com.ggasoftware.uitest.utils.common.LinqUtils.firstIndex;
import static com.ggasoftware.uitest.utils.common.LinqUtils.select;
import static com.ggasoftware.uitest.utils.ReporterNG.logTechnical;
import static com.ggasoftware.uitest.utils.common.Timer.alwaysDoneAction;
import static com.ggasoftware.uitest.utils.WebDriverWrapper.*;
import static com.ggasoftware.uitest.utils.settings.FrameworkSettings.asserter;
import static java.lang.String.format;

/**
 * Element Group control implementation
 *
 * @author Alexeenko Yan
 * @author Belin Yury
 * @author Belousov Andrey
 * @author Shubin Konstantin
 * @author Zharov Alexandr
 */
public class Elements<ParentPanel> extends BaseElement<ParentPanel> implements IElements{
    /**
     * Common constructor without any parameters. Locates own properties of the
     * element by class name and tries to use it to initialize.
     */
    public Elements() { }

    /**
     * Initializes element's with given locator. Locates own properties of the
     * element by class name, takes given locator and tries to initialize.
     *
     * @param name    - Element name
     * @param locator - start it with locator type "id=", "css=", "xpath=" and
     *                etc. Locator without type is assigned to xpath
     * @param panel   - Parent panel instance
     */
    public Elements(String name, String locator, ParentPanel panel) {
        super(name, locator, panel);
    }

    /**
     * Initializes element with given locator. Locates own properties of the element by class name, takes given locator and tries
     * to initialize.
     *
     * @param name    - Element name
     * @param byLocator - Selenium By
     */
    public Elements(String name, By byLocator) {
        super(name, byLocator);
    }

    /**
     * Find webelement from web page. We use locator for this. Where locator -
     * start it with locator type "id=", "css=", "xpath=" and etc. Locator
     * without type is assigned to xpath
     *
     * @return List of WebElements
     */
    public List<WebElement> getWebElements() {
        return getWebElements(TIMEOUT);
    }

    /**
     * Find webelement from web page. We use locator for this. Where locator -
     * start it with locator type "id=", "css=", "xpath=" and etc. Locator
     * without type is assigned to xpath
     *
     * @param seconds to wait until elements found.
     * @return List of WebElements
     */

    public List<WebElement> getWebElements(int seconds) {
        if (logFindElementLocator)
            logTechnical(format("Get Web Elements '%s'", getLocator()));
        SearchContext currentContext = getDriver();
        if (context != null)
            for (By by : context) {
                List<WebElement> elements = currentContext.findElements(by);
                if (elements.size() != 1)
                    asserter.exception(format("Instead of 1 element found '%s' elements", elements.size()));
                currentContext = elements.get(0);
            }
        final SearchContext finalCurrentContext = currentContext;
        setTimeout(seconds);
        List<WebElement> webElementList = new Timer(seconds * 1000)
            .getResult(() -> finalCurrentContext.findElements(getByLocator()));
        setTimeout(TIMEOUT);
        return webElementList;
    }

    /**
     * Is at least one of elements exists (on the web page) or not?
     *
     * @return true if we can find at least one of elements on the web page, otherwise false
     */
    public boolean isExists() {
        return !getWebElements().isEmpty();
    }

    /**
     * Is at least one of elements exists (on the web page) or not?
     *
     * @param seconds to wait until elements become existed.
     * @return true if we can find at least one of elements on the web page, otherwise false
     */
    public boolean isExists(int seconds) {
        return !getWebElements(seconds).isEmpty();
    }

    /**
     * Get Element from Elements by index using last xpath tag for numerate
     *
     * @param elementIndex index of element
     * @return Element
     */
    public Element getElement(int elementIndex) {
        return new Element<>(format("Element #%s", elementIndex), format("%s[%d]", getXPath().replace("//", "/descendant::"), elementIndex + 1), parent);
    }

    /**
     * Get Element from Elements by index using xpath tag for numerate
     *
     * @param elementIndex index of element
     * @param tag - xpath tag for numerate
     * @return Element
     */
    public Element getElement(int elementIndex, String tag) {
        String xpath = getXPath();
        StringBuilder b = new StringBuilder(getXPath());
        b.replace(xpath.lastIndexOf(tag), xpath.lastIndexOf(tag)+1+String.valueOf(elementIndex+1).length(), format("%s[%d]", tag, elementIndex + 1));
        return new Element<>(format("Element #%s", elementIndex), b.toString(), parent);
    }

    /**
     * Get First Visible Element from Elements
     *
     * @return Element
     */
    public Element getVisibleElement() {
        logAction("get first visible element");
        int elementIndex = 0;
        for (WebElement webEl : getWebElements()) {
            if (webEl.isDisplayed()) {
                return new Element<>(format("Element #%s", elementIndex), format("%s[%d]", getXPath().replace("//", "/descendant::"), elementIndex + 1), parent);
            }
            elementIndex++;
        }
        throw new NoSuchElementException("No visible elements available.");
    }

    //  Common functions


    /**
     * Click on the WebElement by index
     *
     * @param elementIndex - index of the element in the List of WebElements
     * @return Parent instance
     */
    public ParentPanel clickBy(int elementIndex) {
        return doJAction(format("click by element with index %d", elementIndex),
                () -> getWebElements().get(elementIndex).click());
    }

    /**
     * Click on the WebElement by text
     *
     * @param elementText - text of the element in the List of WebElements
     * @return Parent instance
     */
    public ParentPanel clickByText(String elementText) {
        logAction(format("click by element with text '%s'", elementText));
        alwaysDoneAction(() -> getWebElements().get(getIndexByText(elementText)).click());
        return parent;
    }

    /**
     * Click on the WebElement by index until expectedElement is NOT DISPLAYED
     *
     * @param expectedElement - expected Element
     * @param elementIndex    - index of WebElement
     * @param tryCount        - number ot click attempts
     * @return Parent instance
     */
    public ParentPanel clickByWhileObjectNotDisplayed(int elementIndex, Element expectedElement, int tryCount) {
        logAction(format("Click by element with index '%d' while expectedElement NOT " +
                                "displayed: element locator '%s', element name '%s'",
                        elementIndex, expectedElement.getLocator(), expectedElement.getName()));
        int i = 0;
        do {
            getWebElements().get(elementIndex).click();
            i++;
            if (i >= tryCount) {
                break;
            }
        }
        while (!(expectedElement.isDisplayed()));
        return parent;
    }

    /**
     * Focus on the WebElement by index
     *
     * @param elementIndex    - index of WebElement
     * @return Parent instance
     */
    public ParentPanel focus(int elementIndex) {
        Dimension size = getWebElements().get(elementIndex).getSize(); //for scroll to object
        logAction("Focus");
        Actions builder = new Actions(WebDriverWrapper.getDriver());
        org.openqa.selenium.interactions.Action focus =
                builder.moveToElement(getWebElements().get(elementIndex), size.width / 2, size.height / 2).build();
        focus.perform();
        return parent;
    }

    /**
     * Mouse Over on the the element by index.
     *
     * @param elementIndex    - index of WebElement
     * @return Parent instance
     */
    public ParentPanel mouseOver(int elementIndex) {
        getWebElements().get(elementIndex).getSize(); //for scroll to object
        logAction("mouseOver");
        Actions builder = new Actions(WebDriverWrapper.getDriver());
        builder.moveToElement(getWebElements().get(elementIndex)).build().perform();
        return parent;
    }

    /**
     * Click on the WebElement by JS
     *
     * @param elementIndex    - index of WebElement
     * @return Parent instance
     */
    public ParentPanel clickByJS(int elementIndex) {
        logAction("clickJS");
        jsExecutor().executeScript("arguments[0].click();", getWebElements().get(elementIndex));
        return parent;
    }

    /**
     * Get WebElement by index
     *
     * @param elementIndex - index of WebElement
     * @return WebElement
     */
    public WebElement getWebElement(int elementIndex) {
        logAction(format("get by element with index '%d'", elementIndex));
        return getWebElements().get(elementIndex);
    }

    /**
     * Get First visible WebElement
     *
     * @return WebElement
     */
    public WebElement getVisibleWebElement() {
        logAction("get first visible element");
        WebElement firstElement = first(
                getWebElements(),
                WebElement::isDisplayed);
        if (firstElement != null)
            return firstElement;
        throw new NoSuchElementException("No visible elements available.");
    }

    /**
     * Is First visible WebElement available
     *
     * @return Whether or not first visible WebElement available(in the Elements)
     */
    public boolean isVisibleWebElementAvailable() {
        logAction("is first visible element available");
        return first(getWebElements(), WebElement::isDisplayed) != null;
    }


    /**
     * Get WebElement by text attribute
     *
     * @param sText - text() attribute of the WebElement in the List of WebElements
     * @return WebElement
     */
    public WebElement getWebElementByText(String sText) {
        logAction(format("get WebElement of element with text '%s'", sText));
        WebElement element = first(getWebElements(),
                el -> el.getText().equals(sText));
        if (element != null)
            return element;
        throw new NoSuchElementException(format("Cannot find element with text '%s'. ", sText));
    }

    /**
     * Get WebElement by text attribute contains
     *
     * @param sText - text() attribute of the WebElement in the List of WebElements
     * @return WebElement
     */
    public WebElement getWebElementByTextContains(String sText) {
        logAction(format("get WebElement with text contains '%s'", sText));
        WebElement element = first(getWebElements(),
                el -> el.getText().contains(sText));
        if (element != null)
            return element;
        throw new NoSuchElementException(format("Cannot find element with text contains '%s'. ", sText));
    }

    /**
     * Get WebElement index by text of current attribute
     *
     * @param sText      - value of attribute of the element in the List of WebElements
     * @param sAttribute - type of attribute of the element in the List of WebElements
     * @return index
     */
    public int getIndexByAttribute(String sText, String sAttribute) {
        logAction(format("get index of element with text '%s' by attribute '%s'", sText, sAttribute));
        int index = firstIndex(getWebElements(),
                el -> el.getAttribute(sAttribute).equals(sText));
        if (index > -1)
            return index;
        throw new NoSuchElementException(
                format("Cannot find element with text '%s' by attribute '%s'. ", sText, sAttribute));
    }

    /**
     * Get WebElement index by text attribute
     *
     * @param sText - text() attribute of the element in the List of WebElements
     * @return index
     */
    public int getIndexByText(String sText) {
        logAction(format("get index of element with text '%s'", sText));
        int index = firstIndex(getWebElements(),
                el -> el.getText().equals(sText));
        if (index > -1)
            return index;
        throw new NoSuchElementException(format("Cannot find element with text '%s'. ", sText));
    }

    /**
     * Get WebElement index by text attribute contains
     *
     * @param sText - text() attribute of the element in the List of WebElements
     * @return index
     */
    public int getIndexByTextContains(String sText) {
        logAction(format("get index of element with text contains '%s'", sText));
        int index = firstIndex(getWebElements(),
                el -> el.getText().contains(sText));
        if (index > -1)
            return index;
        throw new NoSuchElementException(format("Cannot find element with text contains '%s'. ", sText));
    }

    /**
     * Get WebElement index by text attribute starts with
     *
     * @param sText - text() attribute of the element in the List of WebElements
     * @return index of WebElement
     */
    public int getIndexByTextStartsWith(String sText) {
        logAction(format("get index of element with text starts with '%s'", sText));
        int index = firstIndex(getWebElements(),
                el -> el.getText().startsWith(sText));
        if (index > -1)
            return index;
        throw new NoSuchElementException(format("Cannot find element with text starts with '%s'. ", sText));

    }

    /**
     * Get text of WebElement by index
     * <p/>
     * * @param elementIndex - index of the element in the List of WebElements
     *
     * @return text of WebElement
     */
    public String getText(int elementIndex) {
        logAction(format("get text of element with index '%d'", elementIndex));
        return getWebElements().get(elementIndex).getText();
    }

    /**
     * Get List of WebElement text
     *
     * @return ArrayList
     */
    public List<String> getTextList() {
        return (List<String>) select(getWebElements(), WebElement::getText);
    }

    /**
     * Get List of WebElement attributes
     *
     * @param attributeName - name of attribute for getting value
     * @return List of WebElement attributes
     */
    public List<String> getAttribureList(String attributeName) {
        return (List<String>) select(getWebElements(),
                el -> el.getAttribute(attributeName));
    }

    /**
     * Replace each substring of this string "$VALUE" to [value] in [str]
     *
     * @param str   - input string for replacement
     * @param value -The replacement sequence of char values
     * @return The resulting string
     */
    public String insertValue(String str, String value) {
        return str.replace("$VALUE", value);
    }


}
