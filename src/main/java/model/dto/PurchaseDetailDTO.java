package model.dto;

public class PurchaseDetailDTO {
	private long purchaseDetailNumber;
	private long purchaseProductCount;
	private long productSingleNumber;
	private long productComboNumber;
	private long purchaseNumber;
	private String productSingleName;
	private String productComboName;
	private long productSinglePrice;
	private long productComboPrice;
	private long purchaseTotalPrice;
	private String condition;
	private long memberNumber;
	public long getPurchaseDetailNumber() {
		return purchaseDetailNumber;
	}
	public void setPurchaseDetailNumber(long purchaseDetailNumber) {
		this.purchaseDetailNumber = purchaseDetailNumber;
	}
	public long getPurchaseProductCount() {
		return purchaseProductCount;
	}
	public void setPurchaseProductCount(long purchaseProductCount) {
		this.purchaseProductCount = purchaseProductCount;
	}
	public long getProductSingleNumber() {
		return productSingleNumber;
	}
	public void setProductSingleNumber(long productSingleNumber) {
		this.productSingleNumber = productSingleNumber;
	}
	public long getProductComboNumber() {
		return productComboNumber;
	}
	public void setProductComboNumber(long productComboNumber) {
		this.productComboNumber = productComboNumber;
	}
	public long getPurchaseNumber() {
		return purchaseNumber;
	}
	public void setPurchaseNumber(long purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}
	public String getProductSingleName() {
		return productSingleName;
	}
	public void setProductSingleName(String productSingleName) {
		this.productSingleName = productSingleName;
	}
	public String getProductComboName() {
		return productComboName;
	}
	public void setProductComboName(String productComboName) {
		this.productComboName = productComboName;
	}
	public long getProductSinglePrice() {
		return productSinglePrice;
	}
	public void setProductSinglePrice(long productSinglePrice) {
		this.productSinglePrice = productSinglePrice;
	}
	public long getProductComboPrice() {
		return productComboPrice;
	}
	public void setProductComboPrice(long productComboPrice) {
		this.productComboPrice = productComboPrice;
	}
	public long getPurchaseTotalPrice() {
		return purchaseTotalPrice;
	}
	public void setPurchaseTotalPrice(long purchaseTotalPrice) {
		this.purchaseTotalPrice = purchaseTotalPrice;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public long getMemberNumber() {
		return memberNumber;
	}
	public void setMemberNumber(long memberNumber) {
		this.memberNumber = memberNumber;
	}
	@Override
	public String toString() {
		return "PurchaseDetailDTO [purchaseDetailNumber=" + purchaseDetailNumber + ", purchaseProductCount="
				+ purchaseProductCount + ", productSingleNumber=" + productSingleNumber + ", productComboNumber="
				+ productComboNumber + ", purchaseNumber=" + purchaseNumber + ", productSingleName=" + productSingleName
				+ ", productComboName=" + productComboName + ", productSinglePrice=" + productSinglePrice
				+ ", productComboPrice=" + productComboPrice + ", purchaseTotalPrice=" + purchaseTotalPrice
				+ ", condition=" + condition + ", memberNumber=" + memberNumber + "]";
	}
}
