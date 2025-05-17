package listnerCrawling;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import model.crawling.StoreCU;
import model.crawling.StoreGS25;

/**
 * Application Lifecycle Listener implementation class ListnerCrawling
 */
@WebListener
public class ListnerCrawling implements ServletContextListener {

    // 생성자
    public ListnerCrawling() {
        // 기본 생성자
    }

    /**
     * 서버가 시작될 때 호출되는 메서드
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("[리스너] 서버 시작됨, 크롤링 시작!");

        try {
            // CU 상품 크롤링
            new StoreCU().makeSampleCU();
            System.out.println("[리스너] CU 크롤링 완료!");

            // GS25 상품 크롤링
            new StoreGS25().makeSampleGS25();
            System.out.println("[리스너] GS25 크롤링 완료!");

        } catch (Exception e) {
            System.err.println("[리스너] 크롤링 도중 오류 발생!");
            e.printStackTrace();
        }
    }

    /**
     * 서버가 종료될 때 호출되는 메서드
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[리스너] 서버 종료됨.");
    }
}
