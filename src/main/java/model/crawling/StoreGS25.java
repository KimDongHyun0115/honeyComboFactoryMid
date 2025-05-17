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

public class StoreGS25 {
	public void makeSampleGS25() {
		//  System.setProperty("webdriver.chrome.driver", "googleDriver/chromedriver.exe");
		System.out.println("[GS25] 크롤링 시작");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("user-agent=Mozilla/5.0");
		WebDriver driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try {
			crawlEvent(driver, wait);      // 1+1 상품
			crawlHotIssue(driver, wait);   // 신상품 (span.new)
			crawlCategory(driver, wait);   // 식품 / 음료 / 생활용품
		} catch (Exception e) {
			System.out.println("[GS25] 전체 크롤링 중 예외 발생:");
			e.printStackTrace();
		} finally {
			driver.quit();
			System.out.println("[GS25] 크롬 드라이버 종료");
		}
	}


	// 1+1 증정상품 크롤링
	private void crawlEvent(WebDriver driver, WebDriverWait wait) throws Exception {
		System.out.println("[GS25/+1] 1+1 크롤링 시작");
		driver.get("https://gs25.gsretail.com/gscvs/ko/products/event-goods");
		WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), '1+1')]")));
		tab.click();
		Thread.sleep(1000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.prod_list li")));

		List<WebElement> items = driver.findElements(By.cssSelector("ul.prod_list li"));
		  System.out.println("[GS25/+1] 상품 수: " + items.size());
		ProductSingleDAO dao = new ProductSingleDAO();

		for (WebElement item : items) {
			try {
				String name = item.findElement(By.className("tit")).getText().trim();
				if (name.isEmpty()) continue;

				String img = item.findElement(By.className("prod_box"))
						.findElement(By.className("img"))
						.findElement(By.tagName("img"))
						.getAttribute("src");

				String priceText = item.findElement(By.className("price")).getText().replaceAll("[^0-9]", "");
				int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText);

				ProductSingleDTO dto = new ProductSingleDTO();
				dto.setProductSingleStore("GS25"); // 번호생성
				dto.setProductSingleName(name);
				dto.setProductSinglePrice(price);
				dto.setProductSingleStock(100);
				dto.setProductSingleImage(img);
				dto.setProductSingleStore("GS25");
				dto.setProductSingleCategory("PLUSPRODUCT");
				dto.setCondition("GS25");
				dto.setProductSingleInformation(null);
				dto.setSearchKeyword(null);

				boolean insert=dao.insert(dto);
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
				

	// 신상품 (HOTISSUE) 크롤링 - 첫 페이지에서 span.new 있는 것만
	private void crawlHotIssue(WebDriver driver, WebDriverWait wait) throws Exception {
		 System.out.println("[GS25/Hot] 핫이슈 크롤링 시작");
		driver.get("http://gs25.gsretail.com/gscvs/ko/products/youus-freshfood");
		Thread.sleep(1000);

		// 상품 리스트 뜰 때까지 대기
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.prod_list li")));

		List<WebElement> items = driver.findElements(By.cssSelector("ul.prod_list li"));
		System.out.println("[GS25/HOTISSUE] 전체 상품 수: " + items.size());
		ProductSingleDAO dao = new ProductSingleDAO();

		for (WebElement item : items) {
			try {
				// NEW 뱃지가 있는 상품만 필터링
				WebElement flag = item.findElement(By.cssSelector("p.flg04 span"));
				if (!flag.getText().contains("NEW")) continue;

				String name = item.findElement(By.className("tit")).getText().trim();
				if (name.isEmpty()) continue;

				String img = item.findElement(By.className("prod_box"))
						.findElement(By.className("img"))
						.findElement(By.tagName("img"))
						.getAttribute("src");

				String priceText = item.findElement(By.className("price")).getText().replaceAll("[^0-9]", "");
				int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText);

				ProductSingleDTO dto = new ProductSingleDTO();
				dto.setProductSingleStore("GS25"); // 또는 "CU"
				dto.setProductSingleName(name);
				dto.setProductSinglePrice(price);
				dto.setProductSingleStock(100);
				dto.setProductSingleImage(img);
				dto.setProductSingleStore("GS25");
				dto.setProductSingleCategory("HOTISSUE");
				dto.setCondition("GS25HOT");
				dto.setProductSingleInformation(null);
				dto.setSearchKeyword(null);

				boolean insert=dao.insert(dto);
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

	// 식품 / 음료 / 생활용품 카테고리 크롤링
	private void crawlCategory(WebDriver driver, WebDriverWait wait) throws Exception {
		driver.get("https://gs25.gsretail.com/gscvs/ko/products/youus-different-service");

		String[] tabIds = { "productRamen", "productDrink", "productGoods" };
		String[] categoryValues = { "FOODPRODUCT", "BEVERAGEPRODUCT","DAILYSUPPLIESPRODUCT" };

		ProductSingleDAO dao = new ProductSingleDAO();

		for (int i = 0; i < tabIds.length; i++) {
			   System.out.println("[GS25" + categoryValues[i] + "] 탭 클릭");
			WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.id(tabIds[i])));
			tab.click();
			Thread.sleep(1000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.prod_list li")));

			List<WebElement> items = driver.findElements(By.cssSelector("ul.prod_list li"));
			   System.out.println("[GS25/" + categoryValues[i] + "] 상품 수: " + items.size());

			for (WebElement item : items) {
				try {
					String name = item.findElement(By.className("tit")).getText().trim();
					if (name.isEmpty()) continue;

					String img = item.findElement(By.className("prod_box"))
							.findElement(By.className("img"))
							.findElement(By.tagName("img"))
							.getAttribute("src");

					String priceText = item.findElement(By.className("price")).getText().replaceAll("[^0-9]", "");
					int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText);

					ProductSingleDTO dto = new ProductSingleDTO();
					dto.setProductSingleStore("GS25"); // 또는 "CU"
					dto.setProductSingleName(name);
					dto.setProductSinglePrice(price);
					dto.setProductSingleStock(100);
					dto.setProductSingleImage(img);
					dto.setProductSingleStore("GS25");
					dto.setProductSingleCategory(categoryValues[i]);
					dto.setCondition("GS25_" + categoryValues[i]);
					dto.setProductSingleInformation(null);
					dto.setSearchKeyword(null);

					boolean insert=dao.insert(dto);
					if (insert) {
		                System.out.println("[categoryValues][INSERT] " + name + " / " + price + "원");
		            } else {
		                System.out.println("[categoryValues][SKIP] 중복 상품: " + name);
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			}
		}
	}

	public static void main(String[] args) {
		new StoreGS25().makeSampleGS25();
	}
}
