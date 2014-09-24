package com.siemens.ct.its.util.jira;

import java.util.Date;

public class Comment {

	Date created;
	String author;
	String body;
	String image;
	String local;

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	public Comment(){}
	public Comment(String author, Date created, String body , String image ) {
		this.created = created;
		this.author = author;
		this.body = body;
		this.image = image;
	}

	@Override
	public String toString() {
		return "Comment [created=" + created + ", author=" + author + ", body="
				+ body + "]";
	}

}
