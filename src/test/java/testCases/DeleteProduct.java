package testCases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DeleteProduct {

	
	SoftAssert softAssert;
	HashMap<String, String> payloadMap;
	HashMap<String, String> deletePayloadMap;
	String expectedProductName;
	String expectedProductPrice;
	String expectedProductDescription;
	String firstProductId;

	public DeleteProduct() {
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
	
	public Map<String, String> DeleteMap() {
		deletePayloadMap = new HashMap<String, String>();
		deletePayloadMap.put("id",firstProductId);
		
		return deletePayloadMap;
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
	@Test(priority=2)	
	public void deleteProduct() {
		
		Response response = 
			    given()
			     .baseUri("https://techfios.com/api-prod/api/product")
			     .header("Content-Type", "application/json")
			     .auth().preemptive().basic("demo@techfios.com", "abc123")
			     .body(DeleteMap()).
			    when()
			     .delete("/delete.php").
			    then()
			     .extract().response();

		int actualResponseStatus = response.getStatusCode();
		System.out.println("actual Response Status:" + actualResponseStatus);
		softAssert.assertEquals(actualResponseStatus, 200, "Status code Not matching");
		
		String actualResponseBody = response.getBody().asString();
		System.out.println("actualResponseBody: " + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);
		String productMessage = jp.get("message");
		softAssert.assertEquals(productMessage, "Product was deleted.", "Product messages are not matching!");

		softAssert.assertAll();
		
	}
	
		// Read the details of the product deleted
		
		@Test(priority=3)
		public void readOneProduct() {
	        
			Response response = given().baseUri("https://techfios.com/api-prod/api/product")
					.header("Content-Type", "application/json; charset=UTF-8").auth().preemptive().basic("demo@techfios.com", "abc123")
					.queryParam("id", firstProductId).when().get("/read_one.php").

					then().extract().response();

			int actualResponseStatus = response.getStatusCode();
			System.out.println("actual Response Status:" + actualResponseStatus);
			softAssert.assertEquals(actualResponseStatus, 404, "Status code Not matching");

			
			String actualResponseBody = response.getBody().asString();
			System.out.println("actualResponseBody: " + actualResponseBody);

			JsonPath jp = new JsonPath(actualResponseBody);
			String productMessage = jp.get("message");
			softAssert.assertEquals(productMessage, "Product does not exist.", "Product messages are not matching!");

			softAssert.assertAll();

		}
}
		
