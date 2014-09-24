package com.siemens.ct.its.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.siemens.ct.its.util.jira.Comment;
import com.siemens.ct.its.util.jira.Issue;

public class OSChinaIssueUtil {

	public static String contentImage(Document doc) {

		// issue-show
		Elements imgs = doc.select("#issue-show p img");

		// imgs = doc.select("div.issue-content,.markdown-body p img");
		if (imgs == null || imgs.isEmpty())
			return null;

		Element img = imgs.first();
		// System.out.println(h2);
		// System.out.println(h2.text());
		return img.attr("src");
	}

	public static String title(Document doc) {
		Elements divs = doc.select("div.issue-title");
		if (divs == null || divs.isEmpty())
			return null;

		Element h2 = divs.first().child(0);
		// System.out.println(h2);
		// System.out.println(h2.text());
		return h2.text();
	}

	public static String milestone(Document doc) {
		Elements as = doc.select("div.milestone strong a");
		if (as == null || as.isEmpty())
			return null;

		Element a = as.first();
		// System.out.println(h2);
		// System.out.println(h2.text());
		return a.text();
	}

	public static List<String> tags(Document doc) {
		Elements lis = doc.select("div.label-list-show,.pull-right ul li");
		if (lis == null || lis.isEmpty())
			return null;

		List<String> tags = null;

		int size = lis.size();
		for (int i = 0; i < size; i++) {
			Element li = lis.get(i);

			if (li.tagName() != null && li.tagName().equalsIgnoreCase("li")) {
				String val = li.child(0).text();

				if (tags == null)
					tags = new ArrayList<String>();
				tags.add(val);
			}

		}

		// System.out.println(h2);
		// System.out.println(h2.text());
		return tags;
	}

	public static String status(Document doc) {
		Elements divs = doc.select("div.issue-stat");
		if (divs == null || divs.isEmpty())
			return null;

		Element h2 = divs.first();
		// System.out.println(h2);
		// System.out.println(h2.text());
		return h2.text();
	}

	public static String author(Document doc) {
		Elements divs = doc.select("div.issue-author a span.author");
		if (divs == null || divs.isEmpty())
			return null;

		Element h2 = divs.first();
		// System.out.println(h2);
		// System.out.println(h2.text());
		return h2.attr("data-username");
	}

	public static String create(Document doc) {
		Elements divs = doc.select("div.issue-author span.timeago");
		if (divs == null || divs.isEmpty())
			return null;

		Element h2 = divs.first();

		return h2.attr("title");
	}

	public static String assignee(Document doc) {
		Elements divs = doc.select("div.assignee a span.author");
		if (divs == null || divs.isEmpty())
			return null;

		Element h2 = divs.first();

		return h2.attr("data-username");
	}

	public static String commentUser(Element c) {

		Elements es = c.select("span.author");
		if (es == null || es.isEmpty())
			return null;

		Element e = es.first();
		return e.attr("data-username");

	}

	public static String commentCreate(Element c) {

		// <span class="timeago note-last-update"
		// title="2014-03-11 21:19">6个月前</span>
		Elements es = c.select("span.timeago,.note-last-update");
		if (es == null || es.isEmpty())
			return null;

		Element e = es.first();
		return e.attr("title");
	}

	public static String commentMessage(Element c) {

		// <div class="note-content markdown-body">
		// <p><em>状态更改为 <strong>已关闭</strong></em></p>
		// </div>
		Elements es = c.select("div.note-body div p");
		if (es == null || es.isEmpty())
			return null;

		Element e = es.first();
		return e.text();
	}

	public static String commentImage(Element c) {

		// <div class="note-content markdown-body">
		// <p><em>状态更改为 <strong>已关闭</strong></em></p>
		// </div>
		Elements es = c.select("div.note-body div p img");
		if (es == null || es.isEmpty())
			return null;

		Element e = es.first();
		return e.attr("src");
	}

	public static Comment toComment(Element e) throws ParseException {
		String user = commentUser(e);
		String create = commentCreate(e);
		String msg = commentMessage(e);
		String _user = user;
		String image = commentImage(e);

		return new Comment(_user, _toDate(create, DATETIMEFORMAT_NORMAL_Z),
				msg, image);
	}

	public static List<Comment> commens(Document doc) throws ParseException {
		Elements notes = doc.select("ul.notes li.note");
		if (notes == null || notes.isEmpty())
			return null;

		List<Comment> cms = new ArrayList<Comment>();
		for (Element e : notes) {
			cms.add(toComment(e));
		}

		return cms;
	}

	public static final String DATETIMEFORMAT_NORMAL_Z = "yyyy-MM-dd HH:mm:ssZ";

	public static Date _toDate(String time, String format1)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(format1);
		Date date;
		date = format.parse(time);
		return date;
	}

	/**
	 * get image from remote and save
	 * 
	 * @param issueId
	 * @param url
	 * @param storage
	 * @param engine
	 * @return
	 * @throws Exception
	 */
	public static String loadImage(String issueId, String url,
			IssueStorage storage, HttpEngine engine) throws Exception {

		byte[] image = engine.loadImage(url);
		return storage.saveIssueImage(issueId, url, image);

	}

	/**
	 * 将 OSCHINA ISSUE 抽取字段，转换为 JAVA 对象
	 * 
	 * @param id
	 * @param doc
	 * @param engine
	 * @return
	 * @throws Exception
	 */
	public static Issue toIssue(String id, Document doc, IssueStorage storage,
			HttpEngine engine) throws Exception {

		Issue issue = new Issue();

		issue.setId(id);

		String title = title(doc);

		issue.setSummary(title);

		// 检查 内容中是否有图片， 如果有的话，下载
		String image = contentImage(doc);
		issue.setContentImage(image);
		if (!StringUtil.isEmpty(image)) {
			String local = loadImage(id, image, storage, engine);
			if (local != null)
				issue.setLocalImage(local);
		}

		String _milestone = milestone(doc);
		issue.setMilestone(_milestone);

		List<String> tags = tags(doc);
		issue.setTags(tags);

		String _status = status(doc);
		issue.setStatus(_status);

		String _user = (author(doc));
		issue.setReporter(_user);

		String _create = create(doc);
		issue.setCreated(_toDate(_create, DATETIMEFORMAT_NORMAL_Z));

		_user = (assignee(doc));
		issue.setAssignee(_user);

		List<Comment> cms = commens(doc);
		if (cms != null) {
			for (Comment cmt : cms) {
				if (!StringUtil.isEmpty(cmt.getImage())) {
					String local = loadImage(id, image, storage, engine);
					if (local != null)
						cmt.setLocal(local);
				}
			}
		}

		issue.setComments(cms);
		return issue;
	}

}
