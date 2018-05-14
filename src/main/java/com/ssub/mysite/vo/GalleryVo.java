package com.ssub.mysite.vo;

public class GalleryVo {
	private Long no;
	private String comments;
	private String imageURL;
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	@Override
	public String toString() {
		return "GalleryVo [no=" + no + ", comments=" + comments + ", imageURL=" + imageURL + "]";
	}
}
