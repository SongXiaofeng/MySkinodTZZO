package com.skinod.tzzo.skinod.datasave;
/**
 * 
 * @author Administrator
 *
 */
public class MyAchievementData{
	/*object={"data":[{"id":15,"serialNo":"E7:20:6D:AB:CF:12","degree":2,
	"userId":5165,"type":4,"reachDate":0,"createDate":1460516664,
	"updateDate":1460516850,"isReach":0},{"id":16,"serialNo":"E7:20:6D:AB:CF:12",
	"degree":2,"userId":5165,"type":5,"reachDate":0,"createDate":1460517437,
	"updateDate":1460517437,"isReach":0}],"errmsg":"ok","errcode":0}*/
	
	private int id;
	private String serialNo;
	private int degree;
	private int userId;
	private int type;
	private long reachDate;
	private long createDate;
	private long updateDate;
	private int isReach;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getReachDate() {
		return reachDate;
	}
	public void setReachDate(long reachDate) {
		this.reachDate = reachDate;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	public long getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(long updateDate) {
		this.updateDate = updateDate;
	}
	public int getIsReach() {
		return isReach;
	}
	public void setIsReach(int isReach) {
		this.isReach = isReach;
	}
	
	@Override
	public String toString() {
		return "id="+id+";"+"serialNo="+serialNo+";"+"degree="+degree+";"+
				"userId="+userId+";"+"type="+type+";"+"reachDate="+reachDate+";"+"createDate="+createDate+";"
				+"updateDate="+updateDate+";"+"isReach="+isReach;
	}
}
