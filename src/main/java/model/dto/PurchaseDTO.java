package model.dto;

public class PurchaseDTO {
	private long purchaseNumber;
	private String purchaseTerminalId;
	private long purchaseTotalPrice;
	private long memberNumber;
	private long totalCountNumber;
	private int purchaseIndex;
	private int purchaseContentCount;
	private String productName;
	private int productPrice;
	private int purchaseProductCount;
	private String condition;
	private String searchKeyword;
	
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}
	
	public int getPurchaseProductCount() {
		return purchaseProductCount;
	}
	public void setPurchaseProductCount(int purchaseProductCount) {
		this.purchaseProductCount = purchaseProductCount;
	}
	public long getPurchaseNumber() {
		return purchaseNumber;
	}
	public void setPurchaseNumber(long purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}
	public String getPurchaseTerminalId() {
		return purchaseTerminalId;
	}
	public void setPurchaseTerminalId(String purchaseTerminalId) {
		this.purchaseTerminalId = purchaseTerminalId;
	}
	public long getPurchaseTotalPrice() {
		return purchaseTotalPrice;
	}
	public void setPurchaseTotalPrice(long purchaseTotalPrice) {
		this.purchaseTotalPrice = purchaseTotalPrice;
	}
	public long getMemberNumber() {
		return memberNumber;
	}
	public void setMemberNumber(long memberNumber) {
		this.memberNumber = memberNumber;
	}
	public long getTotalCountNumber() {
		return totalCountNumber;
	}
	public void setTotalCountNumber(long totalCountNumber) {
		this.totalCountNumber = totalCountNumber;
	}
	public int getPurchaseIndex() {
		return purchaseIndex;
	}
	public void setPurchaseIndex(int purchaseIndex) {
		this.purchaseIndex = purchaseIndex;
	}
	public int getPurchaseContentCount() {
		return purchaseContentCount;
	}
	public void setPurchaseContentCount(int purchaseContentCount) {
		this.purchaseContentCount = purchaseContentCount;
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
		return "PurchaseDTO [purchaseNumber=" + purchaseNumber + ", purchaseTerminalId=" + purchaseTerminalId
				+ ", purchaseTotalPrice=" + purchaseTotalPrice + ", memberNumber=" + memberNumber
				+ ", totalCountNumber=" + totalCountNumber + ", purchaseIndex=" + purchaseIndex
				+ ", purchaseContentCount=" + purchaseContentCount + ", productName=" + productName + ", productPrice="
				+ productPrice + ", purchaseProductCount=" + purchaseProductCount + ", condition=" + condition
				+ ", searchKeyword=" + searchKeyword + "]";
	}
	
	
}
