package com.testinium.step;

import com.testinium.base.BaseTest;
import com.testinium.model.ElementInfo;
import com.thoughtworks.gauge.Step;
import org.apache.log4j.PropertyConfigurator;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseSteps extends BaseTest {


    public static int DEFAULT_MAX_ITERATION_COUNT = 150;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;

    private static String SAVED_ATTRIBUTE;

    private String compareText;

    public BaseSteps() {
        initMap(getFileList());
    }

    WebElement findElement(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    List<WebElement> findElements(String key) {
        return driver.findElements(getElementInfoToBy(findElementInfoByKey(key)));
    }

    public By getElementInfoToBy(ElementInfo elementInfo) {
        By by = null;
        if (elementInfo.getType().equals("css")) {
            by = By.cssSelector(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("name"))) {
            by = By.name(elementInfo.getValue());
        } else if (elementInfo.getType().equals("id")) {
            by = By.id(elementInfo.getValue());
        } else if (elementInfo.getType().equals("xpath")) {
            by = By.xpath(elementInfo.getValue());
        } else if (elementInfo.getType().equals("linkText")) {
            by = By.linkText(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("partialLinkText"))) {
            by = By.partialLinkText(elementInfo.getValue());
        }
        return by;
    }

    private void clickElement(WebElement element) {
        element.click();
    }

    private void clickElementBy(String key) {
        findElement(key).click();
    }

    private void hoverElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }

    private void hoverElementBy(String key) {
        WebElement webElement = findElement(key);
        actions.moveToElement(webElement).build().perform();
    }

    private void sendKeyESC(String key) {
        findElement(key).sendKeys(Keys.ESCAPE);

    }

    private boolean isDisplayed(WebElement element) {
        return element.isDisplayed();
    }

    private boolean isDisplayedBy(By by) {
        return driver.findElement(by).isDisplayed();
    }

    private String getPageSource() {
        return driver.switchTo().alert().getText();
    }

    public static String getSavedAttribute() {
        return SAVED_ATTRIBUTE;
    }

    public String randomString(int stringLength) {

        Random random = new Random();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz0123456789".toCharArray();
        String stringRandom = "";
        for (int i = 0; i < stringLength; i++) {

            stringRandom = stringRandom + chars[random.nextInt(chars.length)];
        }

        return stringRandom;
    }

    public WebElement findElementWithKey(String key) {
        return findElement(key);
    }

    public String getElementText(String key) {
        return findElement(key).getText();
    }

    public String getElementAttributeValue(String key, String attribute) {
        return findElement(key).getAttribute(attribute);
    }

    @Step("Print page source")
    public void printPageSource() {
        System.out.println(getPageSource());
    }

    public void javaScriptClicker(WebDriver driver, WebElement element) {

        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);", element);
    }

    public void javascriptclicker(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    @Step({"Wait <value> seconds",
            "<int> saniye bekle"})
    public void waitBySeconds(int seconds) {
        try {
            logger.info(seconds + " saniye bekleniyor.");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step({"Wait <value> milliseconds",
            "<long> milisaniye bekle"})
    public void waitByMilliSeconds(long milliseconds) {
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Step({"Click to element <key>",
            "Elementine t??kla <key>"})
    public void clickElement(String key) {
        if (!key.isEmpty()) {
            hoverElement(findElement(key));
            clickElement(findElement(key));
            logger.info(key + " elementine t??kland??.");
        }
    }


    @Step("<key> elementin ??st??nde bekle")
    public void hover(String key) {
        hoverElement(findElement(key));
    }

    @Step({"Click to element <key> with focus",
            "<key> elementine focus ile t??kla"})
    public void clickElementWithFocus(String key) {
        actions.moveToElement(findElement(key));
        actions.click();
        actions.build().perform();
        logger.info(key + " elementine focus ile t??kland??.");
    }


    @Step({"<key> elementini kontrol et", "check <key> element is exist"})
    public void checkElement(String key) {
        assertTrue(findElement(key).isDisplayed(), "Aranan element bulunamad??");
    }

    @Step("<key> elementini kontrol et yoksa <key2> devam et")
    public void checkElementAndContinue(String key, String key2) {
        logger.info("checkElementAndContinue");
        try {
            findElement(key).click();
        } catch (Exception e) {
            logger.info("Catch");
            assertTrue(findElement(key2).isDisplayed(), "Aranan element bulunamad??");
        }

    }

    @Step({"Go to <url> address",
            "<url> adresine git"})
    public void goToUrl(String url) {
        driver.get(url);
        logger.info(url + " adresine gidiliyor.");
    }

    @Step({"Wait for element to load with css <css>",
            "Elementin y??klenmesini bekle css <css>"})
    public void waitElementLoadWithCss(String css) {
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(By.cssSelector(css)).size() > 0) {
                logger.info(css + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element: '" + css + "' doesn't exist.");
    }

    @Step({"Wait for element to load with xpath <xpath>",
            "Elementinin y??klenmesini bekle xpath <xpath>"})
    public void waitElementLoadWithXpath(String xpath) {
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(By.xpath(xpath)).size() > 0) {
                logger.info(xpath + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element: '" + xpath + "' doesn't exist.");
    }

    @Step({"Check if element <key> exists else print message <message>",
            "Element <key> var m?? kontrol et yoksa hata mesaj?? ver <message>"})
    public void getElementWithKeyIfExistsWithMessage(String key, String message) {
        ElementInfo elementInfo = findElementInfoByKey(key);
        By by = getElementInfoToBy(elementInfo);

        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(by).size() > 0) {
                logger.info(key + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail(message);
    }

    @Step({"Check if element <key> not exists",
            "Element yok mu kontrol et <key>"})
    public void checkElementNotExists(String key) {
        ElementInfo elementInfo = findElementInfoByKey(key);
        By by = getElementInfoToBy(elementInfo);

        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(by).size() == 0) {
                logger.info(key + " elementinin olmad?????? kontrol edildi.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element '" + key + "' still exist.");
    }

    @Step({"Upload file in project <path> to element <key>",
            "Proje i??indeki <path> dosyay?? <key> elemente upload et"})
    public void uploadFile(String path, String key) {
        String pathString = System.getProperty("user.dir") + "/";
        pathString = pathString + path;
        findElement(key).sendKeys(pathString);
        logger.info(path + " dosyas?? " + key + " elementine y??klendi.");
    }

    @Step({"Write value <text> to element <key>",
            "<text> textini <key> elemente yaz"})
    public void ssendKeys(String text, String key) {
        if (!key.equals("")) {
            findElement(key).sendKeys(text);
            logger.info(key + " elementine " + text + " texti yaz??ld??.");
        }
    }

    @Step({"Click with javascript to css <css>",
            "Javascript ile css t??kla <css>"})
    public void javascriptClickerWithCss(String css) {
        assertTrue(isDisplayedBy(By.cssSelector(css)), "Element bulunamad??");
        javaScriptClicker(driver, driver.findElement(By.cssSelector(css)));
        logger.info("Javascript ile " + css + " t??kland??.");
    }

    @Step({"Click with javascript to xpath <xpath>",
            "Javascript ile xpath t??kla <xpath>"})
    public void javascriptClickerWithXpath(String xpath) {
        assertTrue(isDisplayedBy(By.xpath(xpath)), "Element bulunamad??");
        javaScriptClicker(driver, driver.findElement(By.xpath(xpath)));
        logger.info("Javascript ile " + xpath + " t??kland??.");
    }

    @Step({"Check if current URL contains the value <expectedURL>",
            "??uanki URL <url> de??erini i??eriyor mu kontrol et"})
    public void checkURLContainsRepeat(String expectedURL) {
        int loopCount = 0;
        String actualURL = "";
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualURL = driver.getCurrentUrl();

            if (actualURL != null && actualURL.contains(expectedURL)) {
                logger.info("??uanki URL" + expectedURL + " de??erini i??eriyor.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail(
                "Actual URL doesn't match the expected." + "Expected: " + expectedURL + ", Actual: "
                        + actualURL);
    }

    @Step({"Send TAB key to element <key>",
            "Elemente TAB keyi yolla <key>"})
    public void sendKeyToElementTAB(String key) {
        findElement(key).sendKeys(Keys.TAB);
        logger.info(key + " elementine TAB keyi yolland??.");
    }

    @Step({"Send BACKSPACE key to element <key>",
            "Elemente BACKSPACE keyi yolla <key>"})
    public void sendKeyToElementBACKSPACE(String key) {
        findElement(key).sendKeys(Keys.BACK_SPACE);
        logger.info(key + " elementine BACKSPACE keyi yolland??.");
    }

    @Step({"Send ESCAPE key to element <key>",
            "Elemente ESCAPE keyi yolla <key>"})
    public void sendKeyToElementESCAPE(String key) {
        findElement(key).sendKeys(Keys.ESCAPE);
        logger.info(key + " elementine ESCAPE keyi yolland??.");
    }

    @Step({"Check if element <key> has attribute <attribute>",
            "<key> elementi <attribute> niteli??ine sahip mi"})
    public void checkElementAttributeExists(String key, String attribute) {
        WebElement element = findElement(key);
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) != null) {
                logger.info(key + " elementi " + attribute + " niteli??ine sahip.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element DOESN't have the attribute: '" + attribute + "'");
    }

    @Step({"Check if element <key> not have attribute <attribute>",
            "<key> elementi <attribute> niteli??ine sahip de??il mi"})
    public void checkElementAttributeNotExists(String key, String attribute) {
        WebElement element = findElement(key);

        int loopCount = 0;

        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) == null) {
                logger.info(key + " elementi " + attribute + " niteli??ine sahip olmad?????? kontrol edildi.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element STILL have the attribute: '" + attribute + "'");
    }

    @Step({"Check if <key> element's attribute <attribute> equals to the value <expectedValue>",
            "<key> elementinin <attribute> niteli??i <value> de??erine sahip mi"})
    public void checkElementAttributeEquals(String key, String attribute, String expectedValue) {
        WebElement element = findElement(key);

        String actualValue;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.equals(expectedValue)) {
                logger.info(
                        key + " elementinin " + attribute + " niteli??i " + expectedValue + " de??erine sahip.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element's attribute value doesn't match expected value");
    }

    @Step({"Check if <key> element's attribute <attribute> contains the value <expectedValue>",
            "<key> elementinin <attribute> niteli??i <value> de??erini i??eriyor mu"})
    public void checkElementAttributeContains(String key, String attribute, String expectedValue) {
        WebElement element = findElement(key);

        String actualValue;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.contains(expectedValue)) {
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element's attribute value doesn't contain expected value");
    }

    @Step({"Write <value> to <attributeName> of element <key>",
            "<value> de??erini <attribute> niteli??ine <key> elementi i??in yaz"})
    public void setElementAttribute(String value, String attributeName, String key) {
        String attributeValue = findElement(key).getAttribute(attributeName);
        findElement(key).sendKeys(attributeValue, value);
    }

    @Step({"Write <value> to <attributeName> of element <key> with Js",
            "<value> de??erini <attribute> niteli??ine <key> elementi i??in JS ile yaz"})
    public void setElementAttributeWithJs(String value, String attributeName, String key) {
        WebElement webElement = findElement(key);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('" + attributeName + "', '" + value + "')",
                webElement);
    }

    @Step({"Clear text of element <key>",
            "<key> elementinin text alan??n?? temizle"})
    public void clearInputArea(String key) {
        findElement(key).clear();
    }

    @Step("<text> pathi haf??zada tut")
    public void keepPathInMemory(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    @Step("Yap????t??r ve g??nder")
    public void pasteAndSendEnter() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    @Step({"Clear text of element <key> with BACKSPACE",
            "<key> elementinin text alan??n?? BACKSPACE ile temizle"})
    public void clearInputAreaWithBackspace(String key) {
        WebElement element = findElement(key);
        element.clear();
        element.sendKeys("a");
        actions.sendKeys(Keys.BACK_SPACE).build().perform();
    }

    @Step({"Save attribute <attribute> value of element <key>",
            "<attribute> niteli??ini sakla <key> elementi i??in"})
    public void saveAttributeValueOfElement(String attribute, String key) {
        SAVED_ATTRIBUTE = findElement(key).getAttribute(attribute);
        System.out.println("Saved attribute value is: " + SAVED_ATTRIBUTE);
    }

    @Step({"Write saved attribute value to element <key>",
            "Kaydedilmi?? niteli??i <key> elementine yaz"})
    public void writeSavedAttributeToElement(String key) {
        findElement(key).sendKeys(SAVED_ATTRIBUTE);
    }

    @Step({"Check if element <key> contains text <expectedText>",
            "<key> elementi <text> de??erini i??eriyor mu kontrol et"})
    public void checkElementContainsText(String key, String expectedText) {

        Boolean containsText = findElement(key).getText().contains(expectedText);
        assertTrue(containsText, "Expected text is not contained");
        logger.info(key + " elementi" + expectedText + "de??erini i??eriyor.");
    }

    @Step({"Write random value to element <key>",
            "<key> elementine random de??er yaz"})
    public void writeRandomValueToElement(String key) {
        findElement(key).sendKeys(randomString(15));
    }

    @Step({"Write random value to element <key> starting with <text>",
            "<key> elementine <text> de??eri ile ba??layan random de??er yaz"})
    public void writeRandomValueToElement(String key, String startingText) {
        String randomText = startingText + randomString(15);
        findElement(key).sendKeys(randomText);
    }

    @Step({"Print element text by css <css>",
            "Elementin text de??erini yazd??r css <css>"})
    public void printElementText(String css) {
        System.out.println(driver.findElement(By.cssSelector(css)).getText());
    }

    @Step({"Write value <string> to element <key> with focus",
            "<string> de??erini <key> elementine focus ile yaz"})
    public void sendKeysWithFocus(String text, String key) {
        actions.moveToElement(findElement(key));
        actions.click();
        actions.sendKeys(text);
        actions.build().perform();
        logger.info(key + " elementine " + text + " de??eri focus ile yaz??ld??.");
    }

    @Step({"Refresh page",
            "Sayfay?? yenile"})
    public void refreshPage() {
        driver.navigate().refresh();
    }


    @Step({"Change page zoom to <value>%",
            "Sayfan??n zoom de??erini de??i??tir <value>%"})
    public void chromeZoomOut(String value) {
        JavascriptExecutor jsExec = (JavascriptExecutor) driver;
        jsExec.executeScript("document.body.style.zoom = '" + value + "%'");
    }

    @Step({"Open new tab",
            "Yeni sekme a??"})
    public void chromeOpenNewTab() {
        ((JavascriptExecutor) driver).executeScript("window.open()");
    }

    @Step({"Focus on tab number <number>",
            "<number> numaral?? sekmeye odaklan"})//Starting from 1
    public void chromeFocusTabWithNumber(int number) {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(number - 1));
    }

    @Step("popupa gec")
    public void switchTo() {
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    @Step({"Focus on last tab",
            "Son sekmeye odaklan"})
    public void chromeFocusLastTab() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
    }

    @Step({"Focus on frame with <key>",
            "Frame'e odaklan <key>"})
    public void chromeFocusFrameWithNumber(String key) {
        WebElement webElement = findElement(key);
        driver.switchTo().frame(webElement);
    }

    @Step({"Accept Chrome alert popup",
            "Chrome uyar?? popup'??n?? kabul et"})
    public void acceptChromeAlertPopup() {
        driver.switchTo().alert().accept();
    }


    //----------------------SONRADAN YAZILANLAR-----------------------------------\\


    // Key de??eri al??nan listeden rasgele element se??me amac??yla yaz??lm????t??r. @Mehmet??nan
    public void randomPick(String key) {
        List<WebElement> elements = findElements(key);
        Random random = new Random();
        int index = random.nextInt(elements.size());
        elements.get(index).click();
    }

    //Javascript driver??n ba??lat??lmas??
    private JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }

    //Javascript scriptlerinin ??al????mas?? i??in gerekli fonksiyon
    private Object executeJS(String script, boolean wait) {
        return wait ? getJSExecutor().executeScript(script, "") : getJSExecutor().executeAsyncScript(script, "");
    }

    //Belirli bir locasyona sayfan??n kayd??r??lmas??
    private void scrollTo(int x, int y) {
        String script = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(script, true);
    }

    //Belirli bir elementin oldu??u locasyona websayfas??n??n kayd??r??lmas??
    public WebElement scrollToElementToBeVisible(String key) {
        ElementInfo elementInfo = findElementInfoByKey(key);
        WebElement webElement = driver.findElement(getElementInfoToBy(elementInfo));
        if (webElement != null) {
            scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY() - 100);
        }
        return webElement;
    }


    @Step({"<key> alan??na kayd??r"})
    public void scrollToElement(String key) {
        scrollToElementToBeVisible(key);
        logger.info(key + " elementinin oldu??u alana kayd??r??ld??");

    }


    @Step({"<key> alan??na js ile kayd??r"})
    public void scrollToElementWithJs(String key) {
        ElementInfo elementInfo = findElementInfoByKey(key);
        WebElement element = driver.findElement(getElementInfoToBy(elementInfo));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }


    @Step({"<length> uzunlugunda random bir kelime ??ret ve <saveKey> olarak sakla"})
    public void createRandomString(int length, String saveKey) {
        saveValue(saveKey, randomString(length));

    }

    @Step({"<key> li elementi bul ve de??erini <saveKey> saklanan degeri yazdir",
            "Find element by <key> and compare saved key <saveKey>"})
    public void equalsSendTextByKey(String key, String saveKey) throws InterruptedException {
        WebElement element;
        int waitVar = 0;
        element = findElementWithKey(key);
        while (true) {
            if (element.isDisplayed()) {
                logger.info("WebElement is found at: " + waitVar + " second.");
                element.clear();
                getValue(saveKey);
                element.sendKeys(getValue(saveKey));

                break;
            } else {
                waitVar = waitVar + 1;
                Thread.sleep(1000);
                if (waitVar == 20) {
                    throw new NullPointerException(String.format("by = %s Web element list not found"));
                } else {
                }
            }
        }
    }

    //Zaman bilgisinin al??nmas??
    private Long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return (timestamp.getTime());
    }

    @Step({"<key> li elementi bul, temizle ve rasgele  email de??erini yaz",
            "Find element by <key> clear and send keys  random email"})
    public void RandomMail(String key) {
        Long timestamp = getTimestamp();
        WebElement webElement = findElementWithKey(key);
        webElement.clear();
        webElement.sendKeys("testotomasyon" + timestamp + "@sahabt.com");

    }

    @Step("<key> olarak <text> se??ersem")
    public void choosingTextFromList(String key, String text) throws InterruptedException {
        List<WebElement> comboBoxElement = findElements(key);
        for (int i = 0; i < comboBoxElement.size(); i++) {
            String texts = comboBoxElement.get(i).getText();
            String textim = text;
            if (texts.contains(textim)) {
                comboBoxElement.get(i).click();
                break;
            }
        }
        logger.info(key + " combobox??ndan " + text + " de??eri se??ildi");


    }

    @Step("<key> olarak comboboxdan bir de??er se??ilir")
    public void comboBoxRandom(String key) throws InterruptedException {

        List<WebElement> comboBoxElement = findElements(key);
        int randomIndex = new Random().nextInt(comboBoxElement.size());
        Thread.sleep(2000);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", comboBoxElement.get(randomIndex));
        logger.info(key + " combobox??ndan herhangi bir de??er se??ildi");

    }


    @Step("<key> elementine javascript ile t??kla")
    public void clickToElementWithJavaScript(String key) {
        WebElement element = findElement(key);
        javascriptclicker(element);
        logger.info(key + " elementine javascript ile t??kland??");
    }


    // Belirli bir key de??erinin oldu??u locasyona websayfas??n??n kayd??r??lmas??
    public void scrollToElementToBeVisiblest(WebElement webElement) {
        if (webElement != null) {
            scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY() - 100);
        }
    }


    //??ift t??klama fonksiyonu
    public void doubleclick(WebElement elementLocator) {
        Actions actions = new Actions(driver);
        actions.doubleClick(elementLocator).perform();
    }

    @Step("<key> alan??n?? javascript ile temizle")
    public void clearWithJS(String key) {
        WebElement element = findElement(key);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value ='';", element);
    }


    @Step("<key> elementleri aras??ndan <text> kay??tl?? de??i??kene t??kla")
    public void clickParticularElement(String key, String text) {
        List<WebElement> anchors = findElements(key);
        Iterator<WebElement> i = anchors.iterator();
        while (i.hasNext()) {
            WebElement anchor = i.next();
            if (anchor.getText().contains(getValue(text))) {
                scrollToElementToBeVisiblest(anchor);
                doubleclick(anchor);
                break;
            }
        }
    }

    @Step("<key> menu listesinden rasgele se??")
    public void chooseRandomElementFromList(String key) {
        for (int i = 0; i < 3; i++)
            randomPick(key);
    }


    @Step("<key> olarak <index> indexi se??ersem")
    public void choosingIndexFromDemandNo(String key, String index) {

        try {
            TimeUnit.SECONDS.sleep(3);

            List<WebElement> anchors = findElements(key);
            WebElement anchor = anchors.get(Integer.parseInt(index));
            anchor.click();
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Step("Siparis durmununu <kartDurumu> elementinden bul")
    public void findOrderStatus(String kartDurumu) throws InterruptedException {
        WebElement webElement = findElement(kartDurumu);
        logger.info(" webelement bulundu");
        compareText = webElement.getText();
        logger.info(compareText + " texti bulundu");
    }

    @Step("<key> elementiyle karsilastir")
    public void compareOrderStatus(String key) throws InterruptedException {
        WebElement cardDetail = findElement(key);
        String supplyDetailStatus = cardDetail.getText();
        logger.info(supplyDetailStatus + " texti bulundu");
        assertTrue(compareText.equals(supplyDetailStatus));
        logger.info(compareText + " textiyle " + supplyDetailStatus + " texti kar????la??t??r??ld??.");
    }

    @Step("<text> textini <key> elemente tek tek yaz")
    public void sendKeyOneByOne(String text, String key) throws InterruptedException {

        WebElement field = findElement(key);
        field.clear();
        if (!key.equals("")) {
            for (char ch : text.toCharArray())
                findElement(key).sendKeys(Character.toString(ch));
            Thread.sleep(10);
            logger.info(key + " elementine " + text + " texti karakterler tek tek girlilerek yaz??ld??.");
        }
    }

    @Step("<key> elementine <text> de??erini js ile yaz")
    public void writeToKeyWithJavaScript(String key, String text) {
        WebElement element = findElement(key);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value=arguments[1]", element, text);
        logger.info(key + " elementine " + text + " de??eri js ile yaz??ld??.");
    }


    //Bug??n??n Tarihinin se??ilmesi
    public String chooseDate() {
        Calendar now = Calendar.getInstance();
        int tarih = now.get(Calendar.DATE) + 2;
        return String.valueOf(tarih);
    }

    @Step("<key> tarihinden 2 g??n sonraya al")
    public void chooseTwoDaysFromNow(String key) {
        List<WebElement> elements = findElements(key);
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getText().equals(chooseDate())) {
                elements.get(i).click();
            }
        }
    }

    @Step("<variable> de??i??kenini <key> elementine yaz")
    public void sendKeysVariable(String variable, String key) {
        if (!key.equals("")) {
            clearInputArea(key);
            findElement(key).sendKeys(getValue(variable));
            logger.info(key + " elementine " + getValue(variable) + " texti yaz??ld??.");
        }
    }


    @Step("<key> olarak comboboxtan <text> se??imini yap")
    public void selectDropDown(String key, String text) {
        Select drpCountry = new Select(findElement(key));
        drpCountry.selectByVisibleText(text);
    }

    @Step("<key> olarak se??imini yap")
    public void randomPick2(String key) {
        List<WebElement> elements = findElements(key);
        Random random = new Random();
        int index = random.nextInt(elements.size());
        elements.get(index).click();
    }

}










