package com.autocognite.selenium.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.autocognite.uiautomator.api.ElementMetaData;
import com.autocognite.uiautomator.api.UiElement;
import com.autocognite.uiautomator.api.enums.UiElementType;

public interface SeleniumUiDriver {

	void focus(WebElement WebElement) throws Exception;
	void enterText(WebElement WebElement, String text)throws Exception;
	void setText(WebElement WebElement, String text) throws Exception;
	void clearText(WebElement WebElement) throws Exception;
	boolean isElementPresent(By appiumFindBy) throws Exception;
	void click(WebElement WebElement) throws Exception;
	void waitForElementPresence(By appiumFindBy) throws Exception;
	void check(WebElement WebElement) throws Exception;
	void uncheck(WebElement WebElement) throws Exception;
	void toggle(WebElement WebElement) throws Exception;
	String getText(WebElement WebElement) throws Exception;
	String getValue(WebElement WebElement) throws Exception;
	String getAttribute(WebElement WebElement, String attr) throws Exception;
	void selectDropDownLabel(Select selectControl, String text) throws Exception;
	void clickIfNotSelected(WebElement element) throws Exception;
	void selectDropDownValue(Select selectControl, String value) throws Exception;
	WebElement chooseElementBasedOnValue(List<WebElement> elements,String value) throws Exception;
	void selectDropDownOptionAtIndex(Select selectControl, int index) throws Exception;
	boolean isDropDownSelectedText(Select selectControl, String text) throws Exception;
	boolean isSelectedElementParentText(List<WebElement> elements, String text) throws Exception;
	boolean isDropDownSelectedValue(Select selectControl, String value) throws Exception;
	boolean isSelectedValue(List<WebElement> elements, String value) throws Exception;
	boolean isDropDownSelectedIndex(Select selectControl, int index) throws Exception;
	boolean isSelectedIndex(List<WebElement> elements, int index) throws Exception;
	ArrayList<String> getDropDownOptionLabels(Select selectControl) throws Exception;
	ArrayList<String> getRadioButtonLabels(List<WebElement> elements) throws Exception;
	ArrayList<String> getDropDownOptionValues(Select selectControl) throws Exception;
	ArrayList<String> getRadioButtonValues(List<WebElement> elements) throws Exception;
	int getDropDownOptionCount(Select selectControl) throws Exception;
	WebElement chooseElementBasedOnParentText(List<WebElement> elements,String text) throws Exception;
	int getWaitTime() throws Exception;
	File takeScreenshot() throws Exception;
	UiElementType getElementType(WebElement wdElement) throws Exception;
	List<WebElement> findElements(By appiumFindBy) throws Exception;
	void rightClickAndClick(By by1, By by2) throws Exception;
	WebElement findElement(By by) throws Exception;
	void hoverAndClick(By by1, By by2) throws Exception;
	UiElement declareElement(ElementMetaData elementMetaData) throws Exception;
	void rightClick(WebElement wdElement)  throws Exception;
	void hoverAndClick(WebElement wdElement) throws Exception;
	Select convertToSelectElement(WebElement toolElement) throws Exception;
	void hover(WebElement element) throws Exception;
	void switchToFrame(WebElement wdElement) throws Exception;
	WDMediator createMediatorSkeleton(UiElement uiElement) throws Exception;

}
