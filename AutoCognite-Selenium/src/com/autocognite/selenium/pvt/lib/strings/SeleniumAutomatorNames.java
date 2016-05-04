/*******************************************************************************
 * Copyright 2016 Rahul Verma (Web: www.AutoCognite.com, Email: RV@autocognite.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
