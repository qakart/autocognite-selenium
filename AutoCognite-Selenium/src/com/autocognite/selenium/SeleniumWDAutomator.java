package com.autocognite.selenium;

import com.autocognite.selenium.pvt.lib.config.SeleniumWDAutomatorSingleton;

public class SeleniumWDAutomator {

	public static void init(){
		SeleniumWDAutomatorSingleton.INSTANCE.init();
	}
}
