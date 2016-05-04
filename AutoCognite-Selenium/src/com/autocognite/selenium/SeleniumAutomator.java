package com.autocognite.selenium;

import com.autocognite.selenium.pvt.lib.config.SeleniumAutomatorSingleton;

public class SeleniumAutomator {

	public static void init(){
		SeleniumAutomatorSingleton.INSTANCE.init();
	}
}
