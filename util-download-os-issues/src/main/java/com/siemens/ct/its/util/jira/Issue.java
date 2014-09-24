package com.siemens.ct.its.util.jira;

import java.util.Date;
import java.util.List;

public class Issue {
	// String priority = "Major";
	// String description;
	String status;
	String reporter;
	// String issueType = "Bug";
	Date created;
	String summary;
	String assignee;
	// String resolution;

	String milestone;
	List<String> tags;

	List<Comment> comments;

	String id;
	String contentImage;
	String localImage;

	public String getMilestone() {
		return milestone;
	}

	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getLocalImage() {
		return localImage;
	}

	public void setLocalImage(String localImage) {
		this.localImage = localImage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContentImage() {
		return contentImage;
	}

	public void setContentImage(String contentImage) {
		this.contentImage = contentImage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "Issue [id=" + id + " ,summary=" + summary + "]";
	}

}
