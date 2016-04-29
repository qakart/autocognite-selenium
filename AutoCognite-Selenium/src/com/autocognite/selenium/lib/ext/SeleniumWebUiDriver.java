package com.autocognite.selenium.lib.ext;

import com.autocognite.configurator.api.config.RunConfiguration;
import com.autocognite.selenium.api.WDMediator;
import com.autocognite.selenium.lib.BaseSeleniumUiDriver;
import com.autocognite.uiautomator.api.ElementMetaData;
import com.autocognite.uiautomator.api.UiElement;
import com.autocognite.uiautomator.api.enums.ElementLoaderType;
import com.autocognite.uiautomator.lib.ext.DefaultUiElement;

public class SeleniumWebUiDriver extends BaseSeleniumUiDriver {
	
	public SeleniumWebUiDriver(RunConfiguration runConfig) {
		super(runConfig);
	}
	
	public SeleniumWebUiDriver(RunConfiguration runConfig, ElementLoaderType loaderType){
		super(runConfig, loaderType);
	}

	public WDMediator createMediatorSkeleton(UiElement element) throws Exception {
		return new DefaultSeleniumWDMediator(this, element);
	}
	
	public UiElement createDefaultElementSkeleton(ElementMetaData elementMetaData) throws Exception {
		return new DefaultUiElement(elementMetaData);
	}
	
}
