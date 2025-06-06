package model.dto;

import java.sql.Date;
import java.util.Objects;

public class MemberDTO {
	private long memberNumber;
	private String memberId;
	private String memberPassword;
	private String memberName;
	private Date memberBirth;
	private String memberPhoneNumber;
	private String memberAddressMain;
	private String memberAddressDetail;
	private String memberEmailId;
	private String memberEmailDomain;
	private long totalConutNumber;
	private Date memberRegisterDate;
	private boolean memberIsAdmin;
	private boolean memberIsWithdraw;
	private String condition;
	private String searchKeyword;
	private int memberIndex;
	private int memberContentCount;
	private String memberVerificationCode;

	public long getMemberNumber() {
		return memberNumber;
	}

	public void setMemberNumber(long memberNumber) {
		this.memberNumber = memberNumber;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberPassword() {
		return memberPassword;
	}

	public void setMemberPassword(String memberPassword) {
		this.memberPassword = memberPassword;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public Date getMemberBirth() {
		return memberBirth;
	}

	public void setMemberBirth(Date memberBirth) {
		this.memberBirth = memberBirth;
	}

	public String getMemberPhoneNumber() {
		return memberPhoneNumber;
	}

	public void setMemberPhoneNumber(String memberPhoneNumber) {
		this.memberPhoneNumber = memberPhoneNumber;
	}

	public String getMemberAddressMain() {
		return memberAddressMain;
	}

	public void setMemberAddressMain(String memberAddressMain) {
		this.memberAddressMain = memberAddressMain;
	}

	public String getMemberAddressDetail() {
		return memberAddressDetail;
	}

	public void setMemberAddressDetail(String memberAddressDetail) {
		this.memberAddressDetail = memberAddressDetail;
	}

	public String getMemberEmailId() {
		return memberEmailId;
	}

	public void setMemberEmailId(String memberEmailId) {
		this.memberEmailId = memberEmailId;
	}

	public String getMemberEmailDomain() {
		return memberEmailDomain;
	}

	public void setMemberEmailDomain(String memberEmailDomain) {
		this.memberEmailDomain = memberEmailDomain;
	}

	public long getTotalConutNumber() {
		return totalConutNumber;
	}

	public void setTotalConutNumber(long totalConutNumber) {
		this.totalConutNumber = totalConutNumber;
	}

	public Date getMemberRegisterDate() {
		return memberRegisterDate;
	}

	public void setMemberRegisterDate(Date memberRegisterDate) {
		this.memberRegisterDate = memberRegisterDate;
	}

	public boolean isMemberIsAdmin() {
		return memberIsAdmin;
	}

	public void setMemberIsAdmin(boolean memberIsAdmin) {
		this.memberIsAdmin = memberIsAdmin;
	}

	public boolean isMemberIsWithdraw() {
		return memberIsWithdraw;
	}

	public void setMemberIsWithdraw(boolean memberIsWithdraw) {
		this.memberIsWithdraw = memberIsWithdraw;
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

	public int getMemberIndex() {
		return memberIndex;
	}

	public void setMemberIndex(int memberIndex) {
		this.memberIndex = memberIndex;
	}

	public int getMemberContentCount() {
		return memberContentCount;
	}

	public void setMemberContentCount(int memberContentCount) {
		this.memberContentCount = memberContentCount;
	}

	public String getMemberVerificationCode() {
		return memberVerificationCode;
	}

	public void setMemberVerificationCode(String memberVerificationCode) {
		this.memberVerificationCode = memberVerificationCode;
	}

	@Override
	public boolean equals(Object obj) {
		// 객체가 자신과 동일할 경우
		if (this == obj) {
			return true;
		}
		// obj가 null이거나 두 객체가 서로 다른 클래스 타입일 경우
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		// obj가 MemberDTO 타입인 경우 형변환
		MemberDTO other = (MemberDTO) obj;
		// memberNumber (long)은 기본형이므로 == 연산자를 사용하여 값 비교
		// 나머지는 String 타입이므로 equals() 메서드를 사용하여 비교
		return (this.memberNumber == other.memberNumber && Objects.equals(this.memberId, other.memberId)
				&& Objects.equals(this.memberPassword, other.memberPassword)
				&& Objects.equals(this.memberName, other.memberName)
				&& Objects.equals(this.memberBirth, other.memberBirth)
				&& Objects.equals(this.memberPhoneNumber, other.memberPhoneNumber)
				&& Objects.equals(this.memberAddressMain, other.memberAddressMain)
				&& Objects.equals(this.memberAddressDetail, other.memberAddressDetail)
				&& Objects.equals(this.memberEmailId, other.memberEmailId)
				&& Objects.equals(this.memberEmailDomain, other.memberEmailDomain));
	}

	@Override
	public String toString() {
		return "MemberDTO [memberNumber=" + memberNumber + ", memberId=" + memberId + ", memberPassword="
				+ memberPassword + ", memberName=" + memberName + ", memberBirth=" + memberBirth
				+ ", memberPhoneNumber=" + memberPhoneNumber + ", memberAddressMain=" + memberAddressMain
				+ ", memberAddressDetail=" + memberAddressDetail + ", memberEmailId=" + memberEmailId
				+ ", memberEmailDomain=" + memberEmailDomain + ", totalConutNumber=" + totalConutNumber
				+ ", memberRegisterDate=" + memberRegisterDate + ", memberIsAdmin=" + memberIsAdmin
				+ ", memberIsWithdraw=" + memberIsWithdraw + ", condition=" + condition + ", searchKeyword="
				+ searchKeyword + ", memberIndex=" + memberIndex + ", memberContentCount=" + memberContentCount
				+ ", memberVerificationCode=" + memberVerificationCode + "]";
	}
}
