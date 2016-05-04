package com.autocognite.selenium.pvt.lib.config;

import com.autocognite.configurator.Configurator;
import com.autocognite.selenium.pvt.lib.strings.SeleniumAutomatorNames;

public enum SeleniumAutomatorSingleton {
	INSTANCE;
	
	public void init(){
		Configurator.configureNames(SeleniumAutomatorNames.getAllNames());		
	}
}
