package model.crawling;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import model.dao.ProductSingleDAO;
import model.dto.ProductSingleDTO;

public class StoreCU {
	public void makeSampleCU() {
		System.out.println("[CU] : 크롤링 시작");
		// System.setProperty("webdriver.chrome.driver", "googleDriver/chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("user-agent=Mozilla/5.0");
		WebDriver driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try {
			crawlNewCU(driver, wait);        // 핫이슈
			crawlEventCU(driver, wait);      // 1+1 증정상품
			crawlCategoryCU(driver, wait);   //식품/음료/생활용품
		} catch (Exception e) {
			System.out.println("[CU]: 전체 크롤링 중 예외 발생");
			e.printStackTrace();
		} finally {
			driver.quit();
			System.out.println("[CU]: 크롬 드라이버 종료");
		}
	}

	// NEW 태그 상품 (HOTISSUE)
	private void crawlNewCU(WebDriver driver, WebDriverWait wait) throws Exception {
		System.out.println("[CU/HOT]: 핫이슈 크롤링 시작");
		driver.get("https://cu.bgfretail.com/product/product.do?category=product&depth2=4&sf=N");
		Thread.sleep(2000);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.prod_list")));

		List<WebElement> items = driver.findElements(By.cssSelector("li.prod_list"));
		ProductSingleDAO dao = new ProductSingleDAO();

		for (WebElement item : items) {
			try {
				if (item.findElements(By.cssSelector(".tag span.new")).isEmpty()) continue;

				String name = item.findElement(By.className("name")).getText().trim();
				if (name == null || name.isEmpty()) continue;
				String priceText = item.findElement(By.cssSelector(".price strong")).getText().replaceAll("[^0-9]", "");
				int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText);
				String img = item.findElement(By.tagName("img")).getAttribute("src");

				ProductSingleDTO dto = new ProductSingleDTO();
				dto.setProductSingleStore("CU"); // 번호생성
				dto.setProductSingleName(name);
				dto.setProductSinglePrice(price);
				dto.setProductSingleStock(100);
				dto.setProductSingleImage(img);
				dto.setProductSingleStore("CU");
				dto.setProductSingleCategory("HOTISSUE");
				dto.setCondition("CU_HOT");//"핫이슈"
				dto.setProductSingleInformation(null);
				dto.setSearchKeyword(null);

				boolean insert= dao.insert(dto);
				if (insert) {
					System.out.println("[HOTISSUE][INSERT] " + name + " / " + price + "원");
				} else {
					System.out.println("[HOTISSUE][SKIP] 중복 상품: " + name);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 1+1 상품 크롤링
	private void crawlEventCU(WebDriver driver, WebDriverWait wait) throws Exception {
		driver.get("https://cu.bgfretail.com/event/plus.do?category=event&depth2=1&sf=N");
		Thread.sleep(2000);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.prod_list")));

		List<WebElement> items = driver.findElements(By.cssSelector("li.prod_list"));
		System.out.println("[CU/HOT] 전체 상품 수: " + items.size());
		ProductSingleDAO dao = new ProductSingleDAO();

		for (WebElement item : items) {
			try {
				String badgeText = item.findElement(By.className("badge")).getText().trim();
				if (!badgeText.contains("1+1")) continue;


				String name = item.findElement(By.className("name")).getText().trim();
				String priceText = item.findElement(By.cssSelector(".price strong")).getText().replaceAll("[^0-9]", "");
				int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText);
				String img = item.findElement(By.tagName("img")).getAttribute("src");

				ProductSingleDTO dto = new ProductSingleDTO();
				dto.setProductSingleStore("CU"); // 번호생성
				dto.setProductSingleName(name);
				dto.setProductSinglePrice(price);
				dto.setProductSingleStock(100);
				dto.setProductSingleImage(img);
				dto.setProductSingleStore("CU");
				dto.setProductSingleCategory("PLUSPRODUCT");
				dto.setCondition("CU_1+1");
				dto.setProductSingleInformation(null);
				dto.setSearchKeyword(null);

				boolean insert= dao.insert(dto);
				if (insert) {
					System.out.println("[PLUSPRODUCT][INSERT] " + name + " / " + price + "원");
				} else {
					System.out.println("[PLUSPRODUCT][SKIP] 중복 상품: " + name);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private void crawlCategoryCU(WebDriver driver, WebDriverWait wait) throws Exception {
		String[] urls = {
				"https://cu.bgfretail.com/product/product.do?category=product&depth2=4&depth3=1", // 식품
				"https://cu.bgfretail.com/product/product.do?category=product&depth2=4&depth3=6", // 음료
				"https://cu.bgfretail.com/product/product.do?category=product&depth2=4&depth3=7"  // 생활용품
		};
		String[] categories = { "FOODPRODUCT", "BEVERAGEPRODUCT","DAILYSUPPLIESPRODUCT" };
		String[] conditions = { "food", "drink", "goods" };

		ProductSingleDAO dao = new ProductSingleDAO();

		for (int i = 0; i < urls.length; i++) {
			System.out.println("[CU/" + categories[i] + "]: 카테고리 크롤링 시작");
			driver.get(urls[i]);
			Thread.sleep(2000);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.prod_list")));

			List<WebElement> items = driver.findElements(By.cssSelector("li.prod_list"));
			System.out.println("[CU" + categories[i] + "] 상품 수: " + items.size());

			for (WebElement item : items) {
				try {
					String name = item.findElement(By.className("name")).getText().trim();
					if (name.isEmpty()) continue;

					String priceText = item.findElement(By.cssSelector(".price strong")).getText().replaceAll("[^0-9]", "");
					int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText);
					String img = item.findElement(By.tagName("img")).getAttribute("src");

					ProductSingleDTO dto = new ProductSingleDTO();
					dto.setProductSingleStore("CU"); 
					dto.setProductSingleName(name);
					dto.setProductSinglePrice(price);
					dto.setProductSingleStock(100);
					dto.setProductSingleImage(img);
					dto.setProductSingleStore("CU");
					dto.setProductSingleCategory(categories[i]);
					dto.setCondition("CU_" + conditions[i]);
					dto.setProductSingleInformation(null);
					dto.setSearchKeyword(null);

					boolean inserted = dao.insert(dto);
					if (inserted) {
						System.out.println("[categories][INSERT] " + name + " / " + price + "원");
					} else {
						System.out.println("[categories][SKIP] 중복 상품: " + name);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new StoreCU().makeSampleCU();
	}
}
