package model.dto;

public class ProductComboDTO {
	private long productComboNumber;
	private String productComboName;
	private String productComboImage;
	private int productComboStore; // 1. 씨유 2. gs
	private String productComboCategory; // 1. md 2. 인플루언서 3. 갓성비
	private String productComboInformation;
	private int productComboPrice;
	private int productComboStock;
	private int ProductComboCount; // 장바구니 상품 개수
	private int ProductComboIndex; // 시작할 인덱스 번호
	private int ProductComboContentCount; // 한 페이지에 출력할 데이터 개수
	private int ProductComboADNumber; // 광고 번호
	private long totalCountNumber; // 총 데이터 개수
	private String condition;
	private String searchKeyword;
	public long getProductComboNumber() {
		return productComboNumber;
	}
	public void setProductComboNumber(long productComboNumber) {
		this.productComboNumber = productComboNumber;
	}
	public String getProductComboName() {
		return productComboName;
	}
	public void setProductComboName(String productComboName) {
		this.productComboName = productComboName;
	}
	public String getProductComboImage() {
		return productComboImage;
	}
	public void setProductComboImage(String productComboImage) {
		this.productComboImage = productComboImage;
	}
	public int getProductComboStore() {
		return productComboStore;
	}
	public void setProductComboStore(int productComboStore) {
		this.productComboStore = productComboStore;
	}
	public String getProductComboCategory() {
		return productComboCategory;
	}
	public void setProductComboCategory(String productComboCategory) {
		this.productComboCategory = productComboCategory;
	}
	public String getProductComboInformation() {
		return productComboInformation;
	}
	public void setProductComboInformation(String productComboInformation) {
		this.productComboInformation = productComboInformation;
	}
	public int getProductComboPrice() {
		return productComboPrice;
	}
	public void setProductComboPrice(int productComboPrice) {
		this.productComboPrice = productComboPrice;
	}
	public int getProductComboStock() {
		return productComboStock;
	}
	public void setProductComboStock(int productComboStock) {
		this.productComboStock = productComboStock;
	}
	public int getProductComboCount() {
		return ProductComboCount;
	}
	public void setProductComboCount(int productComboCount) {
		ProductComboCount = productComboCount;
	}
	public int getProductComboIndex() {
		return ProductComboIndex;
	}
	public void setProductComboIndex(int productComboIndex) {
		ProductComboIndex = productComboIndex;
	}
	public int getProductComboContentCount() {
		return ProductComboContentCount;
	}
	public void setProductComboContentCount(int productComboContentCount) {
		ProductComboContentCount = productComboContentCount;
	}
	public int getProductComboADNumber() {
		return ProductComboADNumber;
	}
	public void setProductComboADNumber(int productComboADNumber) {
		ProductComboADNumber = productComboADNumber;
	}
	public long getTotalCountNumber() {
		return totalCountNumber;
	}
	public void setTotalCountNumber(long totalCountNumber) {
		this.totalCountNumber = totalCountNumber;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	@Override
	public String toString() {
		return "ProductComboDTO [productComboNumber=" + productComboNumber + ", productComboName=" + productComboName
				+ ", productComboImage=" + productComboImage + ", productComboStore=" + productComboStore
				+ ", productComboCategory=" + productComboCategory + ", productComboInformation="
				+ productComboInformation + ", productComboPrice=" + productComboPrice + ", productComboStock="
				+ productComboStock + ", ProductComboCount=" + ProductComboCount + ", ProductComboIndex="
				+ ProductComboIndex + ", ProductComboContentCount=" + ProductComboContentCount
				+ ", ProductComboADNumber=" + ProductComboADNumber + ", totalCountNumber=" + totalCountNumber
				+ ", condition=" + condition + ", searchKeyword=" + searchKeyword + "]";
	}
}








