package model.dto;

public class ProductSingleDTO {
	private long productSingleNumber;
	private String productSingleName;
	private int productSinglePrice;
	private int productSingleStock;
	private String productSingleImage;
	private String productSingleStore;
	private String productSingleCategory;
	private String productSingleInformation;
	private String condition; // 조건
	private String searchKeyword; // 검색어
	private long totalCountNumber; // 총 데이터 개수
	private long startIndex; // 시작 인덱스
	private int limitNumber; // 데이터 수
	private int productSingleCount; // 장바구니 상품 개수
	public long getProductSingleNumber() {
		return productSingleNumber;
	}
	public void setProductSingleNumber(long productSingleNumber) {
		this.productSingleNumber = productSingleNumber;
	}
	public String getProductSingleName() {
		return productSingleName;
	}
	public void setProductSingleName(String productSingleName) {
		this.productSingleName = productSingleName;
	}
	public int getProductSinglePrice() {
		return productSinglePrice;
	}
	public void setProductSinglePrice(int productSinglePrice) {
		this.productSinglePrice = productSinglePrice;
	}
	public int getProductSingleStock() {
		return productSingleStock;
	}
	public void setProductSingleStock(int productSingleStock) {
		this.productSingleStock = productSingleStock;
	}
	public String getProductSingleImage() {
		return productSingleImage;
	}
	public void setProductSingleImage(String productSingleImage) {
		this.productSingleImage = productSingleImage;
	}
	public String getProductSingleStore() {
		return productSingleStore;
	}
	public void setProductSingleStore(String productSingleStore) {
		this.productSingleStore = productSingleStore;
	}
	public String getProductSingleCategory() {
		return productSingleCategory;
	}
	public void setProductSingleCategory(String productSingleCategory) {
		this.productSingleCategory = productSingleCategory;
	}
	public String getProductSingleInformation() {
		return productSingleInformation;
	}
	public void setProductSingleInformation(String productSingleInformation) {
		this.productSingleInformation = productSingleInformation;
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
	public long getTotalCountNumber() {
		return totalCountNumber;
	}
	public void setTotalCountNumber(long totalCountNumber) {
		this.totalCountNumber = totalCountNumber;
	}
	public long getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(long startIndex) {
		this.startIndex = startIndex;
	}
	public int getLimitNumber() {
		return limitNumber;
	}
	public void setLimitNumber(int limitNumber) {
		this.limitNumber = limitNumber;
	}
	public int getProductSingleCount() {
		return productSingleCount;
	}
	public void setProductSingleCount(int productSingleCount) {
		this.productSingleCount = productSingleCount;
	}
	@Override
	public String toString() {
		return "ProductSingleDTO [productSingleNumber=" + productSingleNumber + ", productSingleName="
				+ productSingleName + ", productSinglePrice=" + productSinglePrice + ", productSingleStock="
				+ productSingleStock + ", productSingleImage=" + productSingleImage + ", productSingleStore="
				+ productSingleStore + ", productSingleCategory=" + productSingleCategory
				+ ", productSingleInformation=" + productSingleInformation + ", condition=" + condition
				+ ", searchKeyword=" + searchKeyword + ", totalCountNumber=" + totalCountNumber + ", startIndex="
				+ startIndex + ", limitNumber=" + limitNumber + ", productSingleCount=" + productSingleCount + "]";
	}
}
