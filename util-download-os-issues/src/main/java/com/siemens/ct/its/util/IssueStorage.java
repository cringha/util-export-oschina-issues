package com.siemens.ct.its.util;

public interface IssueStorage {

	/**
	 * save issue's image to disk
	 * 
	 * @param issueId
	 * @param url
	 * @param content
	 * @return file full path
	 * @throws Exception
	 */
	public String saveIssueImage(String issueId, String url, byte[] content)
			throws Exception;

}
