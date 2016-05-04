package com.autocognite.selenium.pvt.lib.strings;

import java.util.ArrayList;

import com.autocognite.configurator.lib.strings.Name;
import com.autocognite.configurator.lib.strings.NamesContainer;

public class SeleniumAutomatorNames {
	
	public static ArrayList<NamesContainer> getAllNames(){
		ArrayList<NamesContainer> containers = new ArrayList<NamesContainer>();
		NamesContainer objectNames = new NamesContainer("COMPONENT_NAMES");			
		objectNames.add(new Name("WEBDRIVER_AUTOMATOR", "WebDriver Automator"));
		containers.add(objectNames);
		return containers;
	}
}
