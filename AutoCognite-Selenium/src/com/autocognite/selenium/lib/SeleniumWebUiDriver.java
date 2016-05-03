package com.autocognite.selenium.lib;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.autocognite.batteries.FileSystemBatteries;
import com.autocognite.batteries.SystemBatteries;
import com.autocognite.batteries.api.enums.Browser;
import com.autocognite.configurator.Configurator;
import com.autocognite.configurator.api.RunConfiguration;
import com.autocognite.selenium.api.SeleniumUiDriver;
import com.autocognite.selenium.api.WDMediator;
import com.autocognite.selenium.lib.base.DefaultSeleniumMediator;
import com.autocognite.uiautomator.UiAutomator;
import com.autocognite.uiautomator.api.ElementMetaData;
import com.autocognite.uiautomator.api.Identifier;
import com.autocognite.uiautomator.api.UiElement;
import com.autocognite.uiautomator.api.enums.AutomationContext;
import com.autocognite.uiautomator.api.enums.ElementLoaderType;
import com.autocognite.uiautomator.api.enums.UiDriverEngine;
import com.autocognite.uiautomator.api.enums.UiElementType;
import com.autocognite.uiautomator.api.identify.enums.WebIdentifyBy;
import com.autocognite.uiautomator.lib.DefaultUiDriver;
import com.autocognite.uiautomator.lib.DefaultUiElement;

public class SeleniumWebUiDriver extends DefaultUiDriver implements SeleniumUiDriver {
	
	private WebDriver driver = null;
	private WebDriverWait waiter = null;
	private Browser browser = null;
	private int waitTime = -1;
	DesiredCapabilities capabilities = null;
	
	public SeleniumWebUiDriver(RunConfiguration runConfig, ElementLoaderType loaderType){
		super(runConfig, AutomationContext.PC_WEB, loaderType);
		initDriver();
		switch (this.getBrowser()){
		case FIREFOX:
			setDriver(getFirefoxDriver());
			break;
		case CHROME:
			setDriver(getChromeDriver());
			break;
		case SAFARI:
			setDriver(getSafariDriver());
			break;
		default:; 
		}
		initWait();
		maximizeWindow();
	}
	
	public SeleniumWebUiDriver(RunConfiguration runConfig){
		this(runConfig, ElementLoaderType.AUTOMATOR);
	}

	public void maximizeWindow(){
		// Check for some property here. To override this default.
		getDriver().manage().window().maximize();	
	}

	public DesiredCapabilities getFireFoxCapabilitiesSkeleton() { 
		return DesiredCapabilities.firefox();
	}

	public DesiredCapabilities getChromeCapabilitiesSkeleton() {
		return DesiredCapabilities.chrome();
	}

	public DesiredCapabilities getSafariCapabilitiesSkeleton() {
		return DesiredCapabilities.safari();
	}

	public WebDriver getFirefoxDriver() {
		this.setAppTitle(runConfig.get(UiAutomator.WINDOW_NAME_FIREFOX));
		capabilities = getFireFoxCapabilitiesSkeleton();
		//driver = new FirefoxDriver(capabilities);
		FirefoxProfile profile = new FirefoxProfile();
		profile.setEnableNativeEvents(true);
		capabilities.setCapability(FirefoxDriver.PROFILE, profile);
		setCapabilities(capabilities);
		return new FirefoxDriver(capabilities);
	}

	public WebDriver getChromeDriver() {
		this.setAppTitle(runConfig.get(UiAutomator.WINDOW_NAME_CHROME));
		String os = SystemBatteries.getOSName();
		String chromeDriverBinaryName = null;
		if (os.startsWith("Window")){
			chromeDriverBinaryName = "chromedriver.exe";
		} else if (os.startsWith("Mac")) {
			chromeDriverBinaryName = "chromedriver";
		}
		System.setProperty("webdriver.chrome.driver", runConfig.get(UiAutomator.DEPENDS_BROWSER_DIR) + "/" + chromeDriverBinaryName);
		capabilities = getChromeCapabilitiesSkeleton();
		setCapabilities(capabilities);
		return new ChromeDriver(capabilities);
	}

	public WebDriver getSafariDriver() {
		this.setAppTitle(runConfig.get(UiAutomator.WINDOW_NAME_SAFARI));
		capabilities = getSafariCapabilitiesSkeleton();
		setCapabilities(capabilities);
		return new SafariDriver(capabilities);
	}

	public void initDriver() {
		this.setBrowser(Browser.valueOf(getRunConfiguration().get(UiAutomator.PC_BROWSER).toUpperCase()));
		this.setWaitTime(Integer.parseInt(runConfig.get(UiAutomator.PC_BROWSER_WAIT_TIME)));
		this.setUiTestEngineName(UiDriverEngine.WEBDRIVER);		
	}

	public void initWait() {
		this.setWaiter(new WebDriverWait(this.getDriver(), getWaitTime()));
		if(this.getBrowser() != Browser.SAFARI){
			getDriver().manage().timeouts().pageLoadTimeout(getWaitTime(), TimeUnit.SECONDS);
		}	
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public WebDriverWait getWaiter() {
		return this.waiter;
	}

	public void setWaiter(WebDriverWait waiter) {
		this.waiter = waiter;
	}

	public int getWaitTime() {
		return this.waitTime;
	}

	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public Browser getBrowser() {
		return this.browser;
	}

	@Override
	public Object getUiDriverEngine() {
		return this.getDriver();
	}

	public Object getUnderlyingEngine() {
		return getDriver();
	}

	public void setCapabilities(DesiredCapabilities capabilities) {
		RunConfiguration config = getRunConfiguration();
		if (config.get(UiAutomator.PC_BROWSER_PROXY).toLowerCase().equals("true")){
			Proxy proxy = new Proxy();
			String p = config.get(UiAutomator.PC_BROWSER_PROXY_HOST) + ":" + config.get(UiAutomator.PC_BROWSER_PROXY_PORT);
			setHttpProxy(proxy, p);
			setSslProxy(proxy, p);
			capabilities.setCapability("proxy", proxy);
		}
	}

	public void setHttpProxy(Proxy proxy, String proxyString) {
		proxy.setHttpProxy(proxyString);
	}

	public void setSslProxy(Proxy proxy, String proxyString) {
		proxy.setSslProxy(proxyString);
	}

	/**********************************************************************************/

	@Override
	public UiElement declareElement(ElementMetaData elementMetaData) throws Exception {
		UiElement uiElement = createDefaultElementSkeleton(elementMetaData);
		ArrayList<By> finderQueue = new ArrayList<By>();
		for (Identifier id: elementMetaData.getIdentifiers()){
				finderQueue.add(getFinderType(id.NAME, id.VALUE));
		}
		
		WDMediator mediator = createMediatorSkeleton(uiElement);
		uiElement.setMediator(mediator);
		uiElement.setLoaderType(this.getElementLoaderType());
		mediator.setFindersQueue(finderQueue);
		mediator.setAutomatorName(Configurator.getComponentName("WEBDRIVER_AUTOMATOR"));
		return uiElement;
	}

	@SuppressWarnings("incomplete-switch")
	public By getFinderType(String identifier, String idValue) throws Exception {
		By findBy = null;
		WebIdentifyBy idType = null;
		try{
			idType = WebIdentifyBy.valueOf(identifier.toUpperCase());
		} catch (Throwable e){
			throwUnsupportedIndentifierException(
					Configurator.getComponentName("WEBDRIVER_AUTOMATOR"),
					"getFinderType",
					identifier);
		}
		switch(idType){
		case ID: findBy = By.id(idValue); break;
		case NAME: findBy = By.name(idValue); break;
		case CLASS: findBy = By.className(idValue); break;
		case LINK_TEXT: findBy = By.linkText(idValue); break;
		case PARTIAL_LINK_TEXT: findBy = By.partialLinkText(idValue); break;
		case XPATH: findBy = By.xpath(idValue); break;
		case CSS: findBy = By.cssSelector(idValue); break;
		case TAG: findBy = By.tagName(idValue); break;
		}
		return findBy;
	}

	public UiElementType getElementType(WebElement wdElement) {
		String tagName = wdElement.getTagName().toLowerCase();
		if (tagName.equals("select")){
			return UiElementType.DROPDOWN;
		} else if (tagName.equals("input") && wdElement.getAttribute("type").toLowerCase().equals("radio") ){
			return UiElementType.RADIO;
		} else {
			return UiElementType.GENERIC;
		}
	}

	public TakesScreenshot getScreenshotAugmentedDriver() {
		return (TakesScreenshot) (new Augmenter().augment(getDriver()));
	}
	
	/**********************************************************************************/
	/*					AUTOMATOR API												*/
	/**********************************************************************************/
	
	public void goTo(String url) throws Exception {
		getDriver().get(url);
		waitForBody();
	}
	
	public void refresh() throws Exception {
		getDriver().navigate().refresh();
	}
	
	public void back() throws Exception {
		getDriver().navigate().back();
	}
	
	public void forward() throws Exception {
		getDriver().navigate().forward();
	}

	public void close(){
		getDriver().quit();
	}

	public void waitForBody() throws Exception {
		waitForElementPresence(By.tagName("body"));
	}

	public void confirmAlertIfPresent() {
		WebDriver d = getDriver();
		try{
			Alert alert = d.switchTo().alert();
			alert.accept();
			d.switchTo().defaultContent();
		} catch (Exception e){ // ignore
		}
	}
	
	// Windows related
	public String getCurrentWindow() {
		return getDriver().getWindowHandle();
	}
	
	public void switchToWindow(String windowHandle){
		getDriver().switchTo().window(windowHandle); 		
	}
	
	public void switchToNewWindow() {
		WebDriver driver = getDriver();
		String parentHandle = getCurrentWindow();
		for (String winHandle : driver.getWindowHandles()) {
			if (!winHandle.equals(parentHandle)) {
				switchToWindow(winHandle); // switch focus of WebDriver to the next found window handle (that's your newly opened window)
			}
		}
	}
	
	public void closeCurrentWindow(){
		getDriver().close();
	}
	
	public void switchToFrame(int index) throws Exception {
		this.getDriver().switchTo().frame(index);
	}

	public void switchToFrame(String name) throws Exception {
		this.getDriver().switchTo().frame(name);
	}

	@Override
	public void switchToFrame(WebElement wdElement) throws Exception{
		this.getDriver().switchTo().frame(wdElement);
	}
	
	public void switchToDefaultFrame() throws Exception {
		this.getDriver().switchTo().defaultContent();
	}
	
	@Override
	public File takeScreenshot() throws Exception {
		TakesScreenshot augDriver = getScreenshotAugmentedDriver();
        File srcFile = augDriver.getScreenshotAs(OutputType.FILE);
        return FileSystemBatteries.moveFiletoDir(srcFile, this.getRunConfiguration().get(Configurator.TEMP_SCREENSHOTS_DIR));
	}
	
	public void focusOnApp() throws Exception{
		
	}
	
	/**********************************************************************************/
	/*					ELEMENT API													*/
	/**********************************************************************************/
	
	// FINDIND RELATED
	public WebElement findElement(By findBy) throws Exception{
		waitForElementPresence(findBy);
		WebElement element = getDriver().findElement(findBy);
		return element;
	}
	
	public List<WebElement> findElements(By findBy) throws Exception{
		waitForElementPresence(findBy);
		List<WebElement> elements = getDriver().findElements(findBy);
		return elements;
	}
	
	public void waitForElementPresence(By findBy) throws Exception {
		getWaiter().until(ExpectedConditions.presenceOfElementLocated(findBy));
	}
	
	public void waitForElementClickability(By findBy) throws Exception {
		getWaiter().until(ExpectedConditions.elementToBeClickable(findBy));
	}
	
	public void waitForElementClickability(WebElement element) throws Exception {
		getWaiter().until(ExpectedConditions.elementToBeClickable(element));
	}
	
	public boolean isElementPresent(By findBy) {
		try {
			waitForElementPresence(findBy);
			return true;
		} catch ( Exception e){
			return false;
		}
		
	}
	
	/* Element Basic Actions */
	
	public void focus(WebElement wdElement) throws Exception {
		wdElement.sendKeys("");
	}
	
	public boolean isSelected(WebElement wdElement){
		return wdElement.isSelected();
	}
	
	public void click(WebElement wdElement) throws Exception {
		waitForElementClickability(wdElement);
		try{
			wdElement.click();
		} catch (Exception f) {
			focus(wdElement);
			wdElement.sendKeys(Keys.ENTER);
		}	
	}

	public WebElement click(By findBy) throws Exception {
		WebElement wdElement = findElement(findBy);
		click(wdElement);
		return wdElement;
	}
	
	public WebElement clickIfPresent(By findBy) {
		try{
			return click(findBy);
		} catch (Exception e){
			//pass
		}
		return null;
	}
	
	public void clickIfNotSelected(WebElement wdElement) {
		if ((wdElement != null) && (!isSelected(wdElement))){
			wdElement.click();
		}
	}

	public void enterText(WebElement wdElement, String value) throws Exception {
		waitForElementClickability(wdElement);
		wdElement.click();
		wdElement.sendKeys(value);
	}

	public void setText(WebElement wdElement, String text) throws Exception {
		this.waitForElementClickability(wdElement);
		wdElement.click();
		wdElement.clear();
		wdElement.sendKeys(text);
	}

	public void clearText(WebElement wdElement) throws Exception {
		wdElement.clear();
	}
	
	public boolean isChecked(WebElement wdElement){
		return isSelected(wdElement);
	}

	public void check(WebElement wdElement) {
		if (!isChecked(wdElement)){
			wdElement.click();
		}
	}

	public void uncheck(WebElement wdElement) throws Exception {
		if (isChecked(wdElement)){
			wdElement.click();
		}
	}

	public void toggle(WebElement wdElement) throws Exception{
		wdElement.click();
	}

	/*
	 * Drop Down API
	 */
	public void selectDropDownLabel(Select selectElement, String text) {
		selectElement.selectByVisibleText(text);
	}

	public void selectDropDownValue(Select selectElement, String value) {
		selectElement.selectByValue(value);
	}

	public void selectDropDownOptionAtIndex(Select selectElement, int index) {
		selectElement.selectByIndex(index);
	}

	public boolean isDropDownSelectedText(Select selectElement, String text) {
		List<WebElement> selectedOptions = selectElement.getAllSelectedOptions();
		for (WebElement option: selectedOptions){
			if (option.getText().equals(text)) return true;
		}
		return false;
	}

	public boolean isDropDownSelectedValue(Select selectElement, String value) {
		List<WebElement> selectedOptions = selectElement.getAllSelectedOptions();
		for (WebElement option: selectedOptions){
			if (option.getAttribute("value").equals(value)) return true;
		}
		return false;
	}

	public boolean isDropDownSelectedIndex(Select selectElement, int index) {
		List<WebElement> options = selectElement.getOptions();
		return options.get(index).isSelected();
	}

	public ArrayList<String> getDropDownOptionLabels(Select selectControl) {
		ArrayList<String> texts = new ArrayList<String>();
		for (WebElement option: selectControl.getOptions()){
			texts.add(option.getText());
		}
		return texts;
	}
	
	public ArrayList<String> getDropDownOptionValues(Select selectControl) {
		ArrayList<String> texts = new ArrayList<String>();
		for (WebElement option: selectControl.getOptions()){
			texts.add(option.getAttribute("value"));
		}
		return texts;
	}
	
	public int getDropDownOptionCount(Select selectControl) {
		return selectControl.getOptions().size();
	}
	
	public Select convertToSelectElement(WebElement element) throws Exception{
		return new Select(element);
	}
	
	/* 
	 * Radio Button API
	 */
	public WebElement chooseElementBasedOnText(List<WebElement> wdElements, String text) {
		for (WebElement wdElement: wdElements){
			if (getText(wdElement).equals(text)){
				return wdElement;
			}
		}
		return null;
	}
	
	public WebElement chooseElementBasedOnValue(List<WebElement> wdElements, String value) throws Exception {
		for (WebElement wdElement: wdElements){
			if (getValue(wdElement).equals(value)){
				return wdElement;
			}
		}
		return null;
	}
	
	public WebElement getParent(WebElement wdElement){
		return wdElement.findElement(By.xpath("parent::*")); 
	}
	
	public WebElement chooseElementBasedOnParentText(List<WebElement> wdElements, String text) {
		for (WebElement wdElement: wdElements){
			WebElement parent = getParent(wdElement);
			if (getText(parent).equals(text)){
				return wdElement;
			}
		}
		return null;
	}

	public boolean isSelectedText(List<WebElement> wdElements, String text) {
		for (WebElement wdElement: wdElements){
			if (getText(wdElement).equals(text)){
				return true;
			}
		}
		return false;
	}

	public boolean isSelectedValue(List<WebElement> wdElements, String value) throws Exception {
		for (WebElement wdElement: wdElements){
			if (getValue(wdElement).equals(value)){
				return true;
			}
		}
		return false;
	}

	public boolean isSelectedIndex(List<WebElement> elements, int index) {
		return elements.get(index).isSelected();
	}

	public boolean isSelectedElementParentText(List<WebElement> wdElements, String text) {
		for (WebElement wdElement: wdElements){
			WebElement parent = getParent(wdElement); 
			if (getText(parent).equals(text)){
				return true;
			}
		}
		return false;
	}

	public ArrayList<String> getRadioButtonLabels(List<WebElement> wdElements) {
		ArrayList<String> texts = new ArrayList<String>();
		for (WebElement option: wdElements){
			texts.add(getText(option));
		}
		return texts;
	}

	public ArrayList<String> getRadioButtonValues(List<WebElement> wdElements) throws Exception {
		ArrayList<String> texts = new ArrayList<String>();
		for (WebElement option: wdElements){
			texts.add(getValue(option));
		}
		return texts;
	}
	
	// Property API
	public String getText(WebElement wdElement) {
		return wdElement.getText();
	}
	
	public String getValue(WebElement wdElement) throws Exception {
		return wdElement.getAttribute("value");
	}

	public String getAttribute(WebElement wdElement, String attr) throws Exception{
		return wdElement.getAttribute(attr);
	}
	
	// Action chain actions	
	
	public Actions getActionChain(){
		return new Actions(getDriver());
	}
	
	public void moveToElementAndClick(WebElement element) throws Exception{
		Actions builder = getActionChain();
		builder.moveToElement(element).click(element).perform();
	}
	
	public void hover(By findBy) throws Exception {
		Actions builder = getActionChain();
		WebElement element = findElement(findBy);
		builder.moveToElement(element).perform();
	}
	
	@Override
	public void hover(WebElement element) throws Exception {
		Actions builder = getActionChain();
		builder.moveToElement(element).perform();
	}
	
	public void hoverAndClick(WebElement element) throws Exception {
		try {
			Actions builder = getActionChain();
			builder.moveToElement(element).perform();
			getWaiter().until(ExpectedConditions.elementToBeClickable(element));
			element.click();			
		} catch (Exception e){
			moveToElementAndClick(element);
		}
	}
	
	public void hoverAndClick(By finder1, By finder2) throws Exception {
		Actions builder =  null;
		try {
			builder = getActionChain();
			builder.moveToElement(findElement(finder1)).perform();
			getWaiter().until(ExpectedConditions.elementToBeClickable(finder2));
			findElement(finder2).click();
		} catch (Exception e){
			builder = getActionChain();
			builder.moveToElement(findElement(finder1)).click(findElement(finder2)).perform();
		}
	}
	
	@Override
	public void rightClick(WebElement element) throws Exception{
		Actions builder = getActionChain();
		builder.contextClick(element).perform();
	}
	
	public void rightClickAndClick(By finder1, By finder2) throws Exception {
		rightClick(findElement(finder1));
		click(findElement(finder2));
	}

	public WDMediator createMediatorSkeleton(UiElement element) throws Exception {
		return new DefaultSeleniumMediator(this, element);
	}
	
	public UiElement createDefaultElementSkeleton(ElementMetaData elementMetaData) throws Exception {
		return new DefaultUiElement(elementMetaData);
	}
	
}
