package model.dto;

public class BoardComboLikedDTO {
	private long boardComboLikedNumber;
	private long memberNumber;
	private long boardComboNumber;
	private String memberName;
	private long totalCountNumber;
	private int boardComboLikedIndex;
	private int boardComboLikedContentCount;
	public long getBoardComboLikedNumber() {
		return boardComboLikedNumber;
	}
	public long getMemberNumber() {
		return memberNumber;
	}
	public void setMemberNumber(long memberNumber) {
		this.memberNumber = memberNumber;
	}
	public long getBoardComboNumber() {
		return boardComboNumber;
	}
	public void setBoardComboNumber(long boardComboNumber) {
		this.boardComboNumber = boardComboNumber;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public long getTotalCountNumber() {
		return totalCountNumber;
	}
	public void setTotalCountNumber(long totalCountNumber) {
		this.totalCountNumber = totalCountNumber;
	}
	public int getBoardComboLikedIndex() {
		return boardComboLikedIndex;
	}
	public void setBoardComboLikedIndex(int boardComboLikedIndex) {
		this.boardComboLikedIndex = boardComboLikedIndex;
	}
	public int getBoardComboLikedContentCount() {
		return boardComboLikedContentCount;
	}
	public void setBoardComboLikedContentCount(int boardComboLikedContentCount) {
		this.boardComboLikedContentCount = boardComboLikedContentCount;
	}
	public void setBoardComboLikedNumber(long boardComboLikedNumber) {
		this.boardComboLikedNumber = boardComboLikedNumber;
	}
	@Override
	public String toString() {
		return "BoardComboLikedDTO [boardComboLikedNumber=" + boardComboLikedNumber + ", memberNumber=" + memberNumber
				+ ", boardComboNumber=" + boardComboNumber + ", memberName=" + memberName + ", totalCountNumber="
				+ totalCountNumber + ", boardComboLikedIndex=" + boardComboLikedIndex + ", boardComboLikedContentCount="
				+ boardComboLikedContentCount + "]";
	}
}
