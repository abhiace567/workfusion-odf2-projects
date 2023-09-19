package training.pega.place_order.automation;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;

import com.google.common.base.Throwables;
import com.workfusion.bot.service.SecureEntryDTO;
import com.workfusion.odf2.core.settings.Configuration;
import com.workfusion.odf2.service.vault.SecretsVaultService;
import com.workfusion.odf2.transaction.model.Transaction;
import com.workfusion.rpa.driver.Driver;

import training.pega.place_order.automation.pages.CartPage;
import training.pega.place_order.automation.pages.HomePage;
import training.pega.place_order.automation.pages.MenuNavigationBar;
import training.pega.place_order.automation.pages.OrderGeneratedPage;
import training.pega.place_order.automation.pages.OrderedProductsPage;
import training.pega.place_order.automation.pages.ProductPage;
import training.pega.place_order.automation.pages.SigninPage;
import training.pega.place_order.exceptions.InvalidItemRecordException;
import training.pega.place_order.model.BillingDetails;
import training.pega.place_order.model.CardDetails;
import training.pega.place_order.model.Order;

public class PegaRobot {

//	public static final String HTTP_OPENSPAN_LOGIN = "https://training.openspan.com/login";
	private static final String PEGA_ALIAS = "pega_alias";
	private static final String SCREENSHOT_PATH = "C:\\Users\\abhij\\Desktop\\";
	private static final String S3_SCREENSHOT_PATH = "screenshots/";
	
	private final Driver driver;
    private final SecretsVaultService secretsVault;
    private final Configuration configuration;
    private final Logger logger;
    
    private HomePage homePage;
    private MenuNavigationBar menuNavigationBar;
    
	public PegaRobot(Driver driver, SecretsVaultService secretsVault, Configuration configuration, Logger logger) {
		this.driver = Objects.requireNonNull(driver);
		this.secretsVault = Objects.requireNonNull(secretsVault);
		this.configuration = configuration;
		this.logger = logger;
		initDriver();
		signIn();
	}

	private void initDriver() {
		driver.switchDriver("chrome");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS).pageLoadTimeout(90, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
	}
	
	private void signIn() {
		driver.navigate().to(configuration.getRequiredProperty("HTTP_OPENSPAN_LOGIN"));
		SigninPage signinPage = new SigninPage(driver, logger);
		SecureEntryDTO secureEntryDTO = secretsVault.getEntry(PEGA_ALIAS);
		homePage = signinPage.signIn(secureEntryDTO);
		menuNavigationBar = new MenuNavigationBar(driver, logger);
	}
	
	public Order placeOrder(Order order) {
		logger.info("Processing -------- {}", order);
		homePage = menuNavigationBar.getHomePage();
		Transaction transaction = order.getTransaction();
		String itemType = order.getType();
		String itemName = order.getItem();
		String quantity = order.getQuantity();

		try {
			if (itemName == null || itemName.equals(""))
				throw new InvalidItemRecordException("Invalid Item");
			if (itemType == null || itemType.equals(""))
				throw new InvalidItemRecordException("Invalid Item Type");
			if (quantity == null || quantity.equals("") || quantity == "0")
				throw new InvalidItemRecordException("Invalid Quantity");
			
		ProductPage productPage = homePage.viewProduct(itemType, itemName);
		OrderedProductsPage orderedProductsPage = productPage.makeOrder(quantity);
		
		CartPage cartPage = orderedProductsPage.getCart();
		String totalPrice = cartPage.getTotalPrice();

		OrderGeneratedPage orderGeneratedPage = cartPage.completeOrder(new BillingDetails(), new CardDetails());
		String orderId = orderGeneratedPage.getOrderId();
		
		order.setTotalPrice(totalPrice);
		order.setOrderId(orderId);
		order.setStatus("Ordered");
		transaction.setStatus("COMPLETED");
		
		order.setTransaction(transaction);
		
		menuNavigationBar.logout();
		
		logger.info(
				"Completed processing transaction: id - {}, item - {}, quantity - {}, total price- {}, order id - {}",
				transaction.getUuid(), itemName, quantity, totalPrice, orderId);
		} catch (NoSuchElementException nse) {
			order.setStatus("Failed");
			transaction.setStatus("Failed");
			order.setTransaction(transaction);

			String fileName = "Screenshot_" + transaction.getUuid() + ".png";
			byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
//			s3PluginAdapter.put(screenshot, S3_SCREENSHOT_PATH + fileName);

			logger.error(
					"Ecxeption occurred while processing Transaction id - {} \n" + "Saving Screenshoot to s3 as - {}\n"
							+ "{} \n {} \n",
					transaction.getUuid(), fileName, nse.getMessage(), Throwables.getStackTraceAsString(nse));
			try {				
				FileUtils.writeByteArrayToFile(new File(SCREENSHOT_PATH + fileName), screenshot);
			} catch (IOException e) {
				logger.error("Error saving the Screenshoot", e);
			}
		} catch (InvalidItemRecordException e) {
			transaction.setStatus("Invalid");
			order.setStatus("Invalid");
			order.setTransaction(transaction);
			
			logger.error("Invalid record for Transaction id - {} \n error message: {}", transaction.getUuid(), e.getMessage());
		}
		
		return order;
	}
}
