package com.hq.test;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class AutoAttendance {
	public static void main(String[] args)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		String LOGIN_URL = "http://kq.neusoft.com/index.jsp";
		// 模拟一个浏览器
		WebClient webClient = new WebClient(BrowserVersion.CHROME);

		// 设置webClient的相关参数
		// webClient.setJavaScriptEngine(true);

		// webClient.setCssEnabled(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.setJavaScriptTimeout(35000);
		// htmlunit 对css和javascript的支持不好，所以请关闭之
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);

		// 模拟浏览器打开目标网址
		HtmlPage loginPage = webClient.getPage(LOGIN_URL);

		// System.out.println("loginPage.asText()="+loginPage.asText());
		// System.out.println(loginPage.getTitleText());
		// System.out.println(loginPage.getTextContent());
		HtmlElement focusedElement = loginPage.getFocusedElement();
		// focusedElement.setNodeValue("mengqiang");
		focusedElement.setAttribute("value", "mengqiang");
		HtmlInput object = (HtmlInput) loginPage.getByXPath("//input").get(5);
		// object.setNodeValue("1355578MQ2338dj");
		object.setAttribute("value", "1355578MQ2338dj");
		final HtmlSubmitInput subBtn = (HtmlSubmitInput) loginPage.getByXPath(
				"//input").get(6);

		//
		//
		// 根据form的名字获取页面表单，也可以通过索引来获取：page.getForms().get(0)
		// final HtmlForm form = loginPage.getFormByName("LoginForm");
		//
		// // 1.登录：用户名/密码
		// HtmlTextInput userName = form.getInputByName("username");
		// userName.setValueAttribute("YourName");
		// HtmlPasswordInput userPwd = form.getInputByName("password");
		// userPwd.setValueAttribute("YourPassword");
		//
		// final HtmlSubmitInput subBtn =
		// (HtmlSubmitInput)loginPage.getByXPath("//input").get(2);
		// System.out.println(subBtn.asText());

		final HtmlPage targetPage = subBtn.click();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 2.打卡：找到打卡button打卡
		// HtmlAnchor anchor = (HtmlAnchor)
		// targetPage.getByXPath("//*[@id=\"clockIn\"]").get(0);
		HtmlAnchor htmlAnchor = targetPage.getAnchors().get(2);// //获取超链接
		HtmlPage finalPage = htmlAnchor.click();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(finalPage.asText());
		
		webClient.closeAllWindows();
		// 5、模拟用户登录
		// URL url = new URL("some_url");
		// WebRequestSettings reqSet = new WebRequestSettings(url,
		// SubmitMethod.POST);
		// List reqParam = new ArrayList();
		// reqParam.add(new NameValuePair("entered_login", username));
		// reqParam.add(new NameValuePair("entered_password", password));
		// reqParam.add(new NameValuePair("entered_imagecode", verifycode));
		// reqSet.setRequestParameters(reqParam);
		// HtmlPage mypage = （Htmlpage)client.getPage(reqSet);
	}
}