package model.dto;

public class ProductComboComponentDTO {
	private long productComboComponentNumber;
	private long productComboComponentOne;
	private long productComboComponentTwo;
	private long productComboComponentThree;
	private long productComboNumber;
	private long totalCountNumber;
	private long productSingleNumber;
	private String productSingleImage;
	private String productSingleName;
	private long productSinglePrice;
	private int productComboComponentIndex;
	private int productComboComponentContentCount;
	private String condition;
	private String searchKeyword;
	public long getProductComboComponentNumber() {
		return productComboComponentNumber;
	}
	public void setProductComboComponentNumber(long productComboComponentNumber) {
		this.productComboComponentNumber = productComboComponentNumber;
	}
	public long getProductComboComponentOne() {
		return productComboComponentOne;
	}
	public void setProductComboComponentOne(long productComboComponentOne) {
		this.productComboComponentOne = productComboComponentOne;
	}
	public long getProductComboComponentTwo() {
		return productComboComponentTwo;
	}
	public void setProductComboComponentTwo(long productComboComponentTwo) {
		this.productComboComponentTwo = productComboComponentTwo;
	}
	public long getProductComboComponentThree() {
		return productComboComponentThree;
	}
	public void setProductComboComponentThree(long productComboComponentThree) {
		this.productComboComponentThree = productComboComponentThree;
	}
	public long getProductComboNumber() {
		return productComboNumber;
	}
	public void setProductComboNumber(long productComboNumber) {
		this.productComboNumber = productComboNumber;
	}
	public long getTotalCountNumber() {
		return totalCountNumber;
	}
	public void setTotalCountNumber(long totalCountNumber) {
		this.totalCountNumber = totalCountNumber;
	}
	public long getProductSingleNumber() {
		return productSingleNumber;
	}
	public void setProductSingleNumber(long productSingleNumber) {
		this.productSingleNumber = productSingleNumber;
	}
	public String getProductSingleImage() {
		return productSingleImage;
	}
	public void setProductSingleImage(String productSingleImage) {
		this.productSingleImage = productSingleImage;
	}
	public String getProductSingleName() {
		return productSingleName;
	}
	public void setProductSingleName(String productSingleName) {
		this.productSingleName = productSingleName;
	}
	public long getProductSinglePrice() {
		return productSinglePrice;
	}
	public void setProductSinglePrice(long productSinglePrice) {
		this.productSinglePrice = productSinglePrice;
	}
	public int getProductComboComponentIndex() {
		return productComboComponentIndex;
	}
	public void setProductComboComponentIndex(int productComboComponentIndex) {
		this.productComboComponentIndex = productComboComponentIndex;
	}
	public int getProductComboComponentContentCount() {
		return productComboComponentContentCount;
	}
	public void setProductComboComponentContentCount(int productComboComponentContentCount) {
		this.productComboComponentContentCount = productComboComponentContentCount;
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
		return "ProductComboComponentDTO [productComboComponentNumber=" + productComboComponentNumber
				+ ", productComboComponentOne=" + productComboComponentOne + ", productComboComponentTwo="
				+ productComboComponentTwo + ", productComboComponentThree=" + productComboComponentThree
				+ ", productComboNumber=" + productComboNumber + ", totalCountNumber=" + totalCountNumber
				+ ", productSingleNumber=" + productSingleNumber + ", productSingleImage=" + productSingleImage
				+ ", productSingleName=" + productSingleName + ", productSinglePrice=" + productSinglePrice
				+ ", productComboComponentIndex=" + productComboComponentIndex + ", productComboComponentContentCount="
				+ productComboComponentContentCount + ", condition=" + condition + ", searchKeyword=" + searchKeyword
				+ "]";
	}
}
