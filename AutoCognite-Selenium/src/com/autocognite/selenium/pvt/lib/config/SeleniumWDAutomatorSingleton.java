package com.autocognite.selenium.pvt.lib.config;

import com.autocognite.configurator.Configurator;
import com.autocognite.selenium.pvt.lib.strings.SeleniumWDNames;

public enum SeleniumWDAutomatorSingleton {
	INSTANCE;
	
	public void init(){
		Configurator.configureNames(SeleniumWDNames.getAllNames());		
	}
}
