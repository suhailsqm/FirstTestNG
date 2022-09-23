package util.com.vilcart.util;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AngularWait {
	WebDriver driver;
	WebDriverWait wait;
	
	public AngularWait(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}
    /*public void waitForAngular() {
	final String ForAngular = "var rootSelector = arguments[0];"+
			"var callback = arguments[1];"+
			"if (window.angular && !(window.angular.version && window.angular.version.major > 1)) {"+
			     "var hooks = getNg1Hooks(rootSelector);"+
			   "if (hooks.$$testability) {"+

			       "hooks.$$testability.whenStable(callback);"+

			   "} else if (hooks.$injector) {"+

			       "hooks.$injector.get('$browser')."+

			       "notifyWhenNoOutstandingRequests(callback);"+

			   "} else if (!!rootSelector) {"+

			       "throw new Error('Could not automatically find injector on page: ' +"+

			           "window.location.toString() + '. Consider setting rootElement');"+

			   "} else {"+

			   "throw new Error('root element (' + rootSelector + ') has no injector.' +"+

			       "' this may mean it is not inside ng-app.');"+

			   "}"+

			"} else if (rootSelector && window.getAngularTestability) {"+

			   "var el = document.querySelector(rootSelector);"+

			   "window.getAngularTestability(el).whenStable(callback);"+

			"} else if (window.getAllAngularTestabilities) {"+

			   "var testabilities = window.getAllAngularTestabilities();"+

			   "var count = testabilities.length;"+

			   "var decrement = function() {"+

			       "count--;"+

			       "if (count === 0) {"+

			           "callback();"+

			       "}"+

			   "};"+

			   "testabilities.forEach(function(testability) {"+

			       "testability.whenStable(decrement);"+

			   "});"+

			"} else if (!window.angular) {"+

			   "throw new Error('window.angular is undefined. This could be either ' +"+

			       "'because this is a non-angular page or because your test involves ' +"+

			       "'client-side navigation, which can interfere with Protractor\'s ' +"+

			       "'bootstrapping. See http://git.io/v4gXM for details');"+

			"} else if (window.angular.version >= 2) {"+

			   "throw new Error('You appear to be using angular, but window.' +"+

			       "'getAngularTestability was never set. This may be due to bad ' +"+

			       "'obfuscation.');"+

			"} else {"+

			   "throw new Error('Cannot get testability API for unknown angular ' +"+

			       "'version \"\"' + window.angular.version + '\"\"');"+

			"}"+
			
"function getNg1Hooks(selector, injectorPlease) {"+

"function tryEl(el) {"+

   "try {"+

       "if (!injectorPlease && angular.getTestability) {"+

           "var $$testability = angular.getTestability(el);"+

           "if ($$testability) {"+

               "return {$$testability: $$testability};"+

           "}"+

       "} else {"+

           "var $injector = angular.element(el).injector();"+

           "if ($injector) {"+

               "return {$injector: $injector};"+

           "}"+

       "}"+

   "} catch(err) {}"+

"}"+

"function trySelector(selector) {"+

   "var els = document.querySelectorAll(selector);"+

   "for (var i = 0; i < els.length; i++) {"+

       "var elHooks = tryEl(els[i]);"+

       "if (elHooks) {"+

           "return elHooks;"+

       "}"+

   "}"+

"}"+

"if (selector) {"+

   "return trySelector(selector);"+

"} else if (window.__TESTABILITY__NG1_APP_ROOT_INJECTOR__) {"+

   "var $injector = window.__TESTABILITY__NG1_APP_ROOT_INJECTOR__;"+

   "var $$testability = null;"+

   "try {"+

       "$$testability = $injector.get('$$testability');"+

   "} catch (e) {}"+

   "return {$injector: $injector, $$testability: $$testability};"+

"} else {"+

   "return tryEl(document.body) ||"+

       "trySelector('[ng-app]') || trySelector('[ng\\:app]') ||"+

       "trySelector('[ng-controller]') || trySelector('[ng\\:controller]');"+

"}"+

"};";		
	
	
	
	((JavascriptExecutor) driver).executeAsyncScript(ForAngular,"\"app-main\"");
}
*/
public void untilAngularFinishHttpCalls() {
    final String javaScriptToLoadAngular =
            "var injector = window.angular.element('body').injector();" + 
            "var $http = injector.get('$http');" + 
            "return ($http.pendingRequests.length === 0)";

    ExpectedCondition<Boolean> pendingHttpCallsCondition = new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver driver) {
            return ((JavascriptExecutor) driver).executeScript(javaScriptToLoadAngular).equals(true);
        }
    };
    //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // timeout = 20 secs
    wait.until(pendingHttpCallsCondition);
}
private void waitUntilJSReady() {
    try {
        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) this.driver)
            .executeScript("return document.readyState").toString().equals("complete");
        boolean jsReady = ((JavascriptExecutor) this.driver).executeScript("return document.readyState").toString().equals("complete");
        if (!jsReady) {
            wait.until(jsLoad);
        }
    } catch (WebDriverException ignored) {
    }
}
private void ajaxComplete() {
	((JavascriptExecutor) this.driver).executeScript("var callback = arguments[arguments.length - 1];"
        + "var xhr = new XMLHttpRequest();" + "xhr.open('GET', '/Ajax_call', true);"
        + "xhr.onreadystatechange = function() {" + "  if (xhr.readyState == 4) {"
        + "    callback(xhr.responseText);" + "  }" + "};" + "xhr.send();");
}
private void waitUntilJQueryReady() {
    Boolean jQueryDefined = (Boolean) ((JavascriptExecutor) this.driver).executeScript("return typeof jQuery != 'undefined'");
    if (jQueryDefined) {
        poll(20);
        waitForJQueryLoad();
        poll(20);
    }
}
private void waitForJQueryLoad() {
    try {
        ExpectedCondition<Boolean> jQueryLoad = driver -> ((Long) ((JavascriptExecutor) this.driver)
            .executeScript("return jQuery.active") == 0);
        boolean jqueryReady = (Boolean) ((JavascriptExecutor) this.driver).executeScript("return jQuery.active==0");
        if (!jqueryReady) {
            wait.until(jQueryLoad);
        }
    } catch (WebDriverException ignored) {
    }
}
public void waitUntilAngular5Ready() {
    try {
        Object angular5Check = ((JavascriptExecutor) this.driver).executeScript("return getAllAngularRootElements()[0].attributes['ng-version']");
        if (angular5Check != null) {
            Boolean angularPageLoaded = (Boolean) ((JavascriptExecutor) this.driver).executeScript("return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1");
            if (!angularPageLoaded) {
                poll(20);
                waitForAngular5Load();
                poll(20);
            }
        }
    } catch (WebDriverException ignored) {
    }
}
private void waitForAngular5Load() {
    String angularReadyScript = "return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1";
    angularLoads(angularReadyScript);
}
private void angularLoads(String angularReadyScript) {
    try {
    	 ExpectedCondition<Boolean> angularLoad = new ExpectedCondition<Boolean>() {
             public Boolean apply(WebDriver driver) {
                 return ((JavascriptExecutor) driver).executeScript(angularReadyScript).equals(true);
             }
         };
    	
        /*ExpectedCondition<Boolean> angularLoad = driver -> Boolean.valueOf(((JavascriptExecutor) driver)
            .executeScript(angularReadyScript).toString());*/
        boolean angularReady = Boolean.valueOf(((JavascriptExecutor) driver).executeScript(angularReadyScript).toString());
        if (!angularReady) {
        	wait.until(angularLoad);
        }
    } catch (WebDriverException ignored) {
    }
}

private void poll(long milis) {
    try {
        Thread.sleep(milis);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
public void waitAllRequest() {
    waitUntilJSReady();
    ajaxComplete();
    waitUntilJQueryReady();
    //waitUntilAngularReady();
    waitUntilAngular5Ready();
}

}
