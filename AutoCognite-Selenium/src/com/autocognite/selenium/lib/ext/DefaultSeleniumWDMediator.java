package com.autocognite.selenium.lib.ext;

import com.autocognite.selenium.api.SeleniumUiDriver;
import com.autocognite.selenium.lib.BaseSeleniumMediator;
import com.autocognite.uiautomator.api.UiElement;

public class DefaultSeleniumWDMediator extends BaseSeleniumMediator{
	public DefaultSeleniumWDMediator(SeleniumUiDriver uiDriver, UiElement uiElement){
		super(uiElement, uiDriver);
	}

}
