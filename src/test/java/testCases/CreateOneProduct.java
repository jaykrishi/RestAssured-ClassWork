package testCases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CreateOneProduct {

	
	SoftAssert softAssert;
	HashMap<String, String> payloadMap;
	String expectedProductName;
	String expectedProductPrice;
	String expectedProductDescription;
	String firstProductId;

	public CreateOneProduct() {
		softAssert = new SoftAssert();
	}

	public Map<String, String> createPayloadMap() {
		payloadMap = new HashMap<String, String>();
		payloadMap.put("name", "Comfy Amazing Pillow 2.0");
		payloadMap.put("price", "99");
		payloadMap.put("description", "The best pillow");
		payloadMap.put("category_id", "1");
		return payloadMap;
	}

	@Test(priority = 0)
	public void createNewProduct() {
		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type", "application/json; charset=UTF-8").auth().preemptive()
				.basic("demo@techfios.com", "abc123").body(createPayloadMap()).when().post("/create.php").then()
				.extract().response();

		int actualResponseStatus = response.getStatusCode();
		System.out.println("actual Response Status: " + actualResponseStatus);
		softAssert.assertEquals(actualResponseStatus, 201, "Status codes are not matching!");

		String actualResponseContentType = response.getHeader("Content-Type");
		System.out.println("actual Response ContentType: " + actualResponseContentType);
		softAssert.assertEquals(actualResponseContentType, "application/json; charset=UTF-8",
				"Response Content-Types are not matching!");

		String actualResponseBody = response.getBody().asString();
		System.out.println("actualResponseBody: " + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);
		String productMessage = jp.get("message");
		softAssert.assertEquals(productMessage, "Product was created.", "Product messages are not matching!");

		softAssert.assertAll();
        
	}
	
	// ReadAllproducts to get the productId of the newly created product
		
	@Test(priority=1)
		public void readAllProducts() {  
			  
			  Response response = 
			    given()
			     .baseUri("https://techfios.com/api-prod/api/product")
			     .header("Content-Type", "application/json; charset=UTF-8")
			     .auth().preemptive().basic("demo@techfios.com", "abc123").
			    when()
			     .get("/read.php").
			    then()
			     .extract().response();

			  int actualResponseStatus = response.getStatusCode();
			  System.out.println("actual Response Status: " + actualResponseStatus);
			  softAssert.assertEquals(actualResponseStatus, 200);

			  String actualResponseContentType = response.getHeader("Content-Type");
			  System.out.println("actual Response ContentType: " + actualResponseContentType);
			  softAssert.assertEquals(actualResponseContentType, "application/json; charset=UTF-8");

			  softAssert.assertAll();
			  
			  String actualResponseBody = response.getBody().asString();
			  //System.out.println("actualResponseBody: " + actualResponseBody);

			  JsonPath jp = new JsonPath(actualResponseBody);
			  firstProductId = jp.get("records[0].id");

			  if (firstProductId != null) {
			   System.out.println("Product exist!!.");
			  } else {
			   System.out.println("Product does not exist!");
			  }
			  
					 
			 }
		
		// Read the details of the product created
		
		@Test(priority=2)
		public void readOneProduct() {
	        
			Response response = given().baseUri("https://techfios.com/api-prod/api/product")
					.header("Content-Type", "application/json").auth().preemptive().basic("demo@techfios.com", "abc123")
					.queryParam("id", firstProductId).when().get("/read_one.php").

					then().extract().response();

			int actualResponseStatus = response.getStatusCode();
			System.out.println("actual Response Status:" + actualResponseStatus);
			softAssert.assertEquals(actualResponseStatus, 200, "Status code Not matching");

			String actualResponseContentType = response.getHeader("Content-Type");
			softAssert.assertEquals(actualResponseContentType, "application/json");

			String actualResponseBody = response.getBody().asString();
			System.out.println(actualResponseBody);

			JsonPath jp = new JsonPath(actualResponseBody);
			String productId = jp.get("id");
			softAssert.assertEquals(productId, firstProductId, "Product Id Not matching");

			expectedProductName = payloadMap.get("name");
			expectedProductPrice = payloadMap.get("price");
			expectedProductDescription = payloadMap.get("description");
			
			String productName = jp.getString("name");
			softAssert.assertEquals(productName,expectedProductName);
			String productPrice = jp.getString("price");
			softAssert.assertEquals(productPrice,expectedProductPrice);
			String productDescription = jp.getString("description");
			softAssert.assertEquals(productDescription,expectedProductDescription);

			softAssert.assertAll();

		}
		

	}

