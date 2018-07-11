package BMFC_Payment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.fail;

public class TC_Pay009 {
    private WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();


    @Before
    public void setUp() throws Exception {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test
    public void p_009() throws Exception {
        driver.get("https://www.baeminchan.com");

        //사전조건
        driver.findElement(By.xpath("//*[@id=\"lnb\"]/ul/li[1]/a")).click(); //GNB 로그인 버튼 클릭
        driver.findElement(By.xpath("//*[@id=\"member_id\"]")).sendKeys("kalinzt@gmail.com"); //아이디 입력
        driver.findElement(By.xpath("//*[@id=\"pwd\"]")).sendKeys("worud1029"); //패스워드 입력
        driver.findElement(By.xpath("//*[@id=\"login\"]/div[1]/form/fieldset/button")).click(); //로그인 버튼 클릭
        driver.findElement(By.xpath("//*[@id=\"search_str\"]")).sendKeys("반찬"); //집밥 검색어 입력
        driver.findElement(By.xpath("//*[@id=\"header_wrap\"]/div/form/button")).click(); // 검색버튼 클릭
        driver.findElement(By.xpath("//*[@id=\"products\"]/li[1]/div/a")).click(); // 검색결과 첫번째 상품 클릭
        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div/form/fieldset/div/dl/dd/div/label/input")).clear();
        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div/form/fieldset/div/dl/dd/div/label/input")).sendKeys("10");
        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div/form/fieldset/button")).click(); //장바구니담기
        driver.findElement(By.xpath("//*[@id=\"lnb\"]/ul/li[6]/a")).click(); //GNB장바구니 클릭
        Thread.sleep(1000);
        driver.findElement(By.xpath("//*[@id=\"btn_select_receipt_date\"]")).click(); //희망배송일 선택
        driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div/div[2]/div[2]/button")).click(); //주문하기 클릭
        Thread.sleep(3000);

        //쿠폰선택
        driver.findElement(By.cssSelector("#ordFrm > table.tb_order_style > tbody > tr > td:nth-child(8) > button")).click(); //쿠폰선택
        driver.findElement(By.cssSelector("#coupon-list-tbl > tbody > tr:nth-child(1) > td:nth-child(1) > span > label")).click(); //보유한쿠폰선택
        driver.findElement(By.cssSelector("#coupon_apply")).click();//쿠폰 적용
        Thread.sleep(1000);
        Alert alert0 = driver.switchTo().alert();
        alert0.accept();
        Thread.sleep(1000);

        //보유 포인트 전액 사용하기
        driver.findElement(By.xpath("//*[@id=\"milage_prc\"]")).clear();
        String a = driver.findElement(By.xpath("//*[@id=\"ordFrm\"]/div[1]/div[2]/div/dl[2]/dt[1]/p/span")).getText();
        driver.findElement(By.xpath("//*[@id=\"milage_prc\"]")).sendKeys(a);
        driver.findElement(By.xpath("//*[@id=\"ordFrm\"]/div[1]/div[2]/div/dl[2]/dd[1]/button")).click();
        Thread.sleep(1000);

        //주문필수 스크립트(상품조건별로 노출되는 체크박스 제어)
        //불필요한 물류데이터를 방지하기 위해 무통장 결제만 사용함(라이브기준)
        if(driver.findElement(By.xpath("//*[@id=\"order_delivery_memo\"]/td")).getText().equalsIgnoreCase("새벽배송")) {
            driver.findElement(By.xpath("//*[@id=\"door_number\"]/td/input")).sendKeys("2325*");
            return;
        } //새벽배송 비번입력
        driver.findElement(By.xpath("//*[@id=\"pay_type4\"]")).click(); //무통장 클릭
        if(driver.findElement(By.xpath("//*[@id=\"ordFrm\"]/ul/li[1]")).getText().equalsIgnoreCase("전체동의")) {
            driver.findElement(By.xpath("//*[@id=\"ordFrm\"]/ul/li[1]/span/label/strong")).click(); //구매조건 동의 외 체크박스 존재시 전체동의 체크
        } else {
            driver.findElement(By.xpath("//*[@id=\"ordFrm\"]/ul/li/span/label")).click(); //구매조건 동의
        }
        driver.findElement(By.xpath("//*[@id=\"order1\"]/button")).click(); //결제하기
        Thread.sleep(4000);

        //PG module 제어 (무통장)
        driver.switchTo().frame(1);
        driver.findElement(By.cssSelector("#assentAll")).click(); //PG전체동의
        driver.findElement(By.cssSelector("#main_agreed_info > div > p.progressBtn > a")).click(); //PG다음
        driver.findElement(By.cssSelector("#BANKCODE3")).click(); //은행 클릭
        driver.findElement(By.cssSelector("#LGD_ACCOUNTOWNER")).sendKeys("김대웅"); //입금자명 입력
        driver.findElement(By.cssSelector("#LGD_CASHRECEIPTUSE0")).click(); //현금영수증 미발행
        driver.findElement(By.cssSelector("#chk_payinput")).click(); //구매동의
        driver.findElement(By.cssSelector("#LGD_NEXT > a")).click(); //PG다음1
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("#LGD_NEXT > a")).click(); //PG다음2
        Thread.sleep(2000);

        //주문취소
        driver.findElement(By.xpath("//*[@id=\"orderFinish\"]/div/div/button[1]/span")).click(); //주문배송조회 클릭
        Thread.sleep(1000);
        driver.findElement(By.xpath("//*[@id=\"order_subscription\"]/table/tbody/tr[1]/td[3]/p[1]/a")).click(); //첫주문내역 클릭
        driver.findElement(By.xpath("//*[@id=\"order_detail\"]/table[2]/tbody/tr[4]/td/a[1]")).click(); //결제취소 클릭
        Thread.sleep(2000);
        Alert alert1 = driver.switchTo().alert();
        alert1.accept();
        Thread.sleep(2000);
        Alert alert2 = driver.switchTo().alert();
        alert2.accept();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id=\"header_wrap\"]/div/h1/a/img")).click(); //메인화면가기
        Thread.sleep(1000);
        driver.findElement(By.xpath("//*[@id=\"lnb\"]/ul/li[1]/a")).click(); //로그아웃

        // text check
        //if ("주문이 정상적으로 완료되었습니다!".equals(driver.findElement(By.xpath("//*[@id=\"orderFinish\"]/div/h2")).getText())) {
        //System.out.println("PASS");
        //assertTrue(true);
        //return;
        //} else {
        //System.out.println("FAIL");
        //assertTrue(false);
        //}

    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    public boolean existElement(WebDriver wd, By by, String meaning) throws TimeoutException {
        WebDriverWait wait = new WebDriverWait(wd, 2);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        return true;
    }
}


