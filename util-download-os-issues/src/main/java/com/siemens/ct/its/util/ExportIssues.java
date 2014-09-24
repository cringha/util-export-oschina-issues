package com.siemens.ct.its.util;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.siemens.ct.its.util.HttpUtil.Proxy;
import com.siemens.ct.its.util.jira.Issue;

public class ExportIssues implements IssueStorage {

	String issueUser;
	String base = ".";

	String project;

	HttpEngine engine;

	@Override
	public String saveIssueImage(String issueId, String url, byte[] content)
			throws Exception {

		// save image and return file name
		String filename = subfile(url);

		File file = new File(base, "ISSUE-" + issueId + "-" + filename);
		saveContent(file, content);
		return file.getAbsolutePath();
	}

	public ExportIssues(String proxy, String issueUser, String project,
			String base) {

		if (proxy != null) {
			Proxy p = HttpUtil.parseProxy(proxy);
			if (p != null) {
				engine = new HttpEngine(p.host, p.port);
			}
		}

		if (this.engine == null)
			this.engine = new HttpEngine();

		this.issueUser = issueUser;
		this.project = project;
		this.base = base;
	}

	public static String subfile(String file) {
		int find = file.lastIndexOf('/');
		if (find < 0)
			return file;
		return file.substring(find + 1, file.length());

	}

	public static void saveContent(File file, byte[] content) throws Exception {
		FileOutputStream put = null;
		try {
			put = new FileOutputStream(file);
			put.write(content);

		} finally {
			if (put != null) {
				try {
					put.close();
				} catch (IOException e) {

				}
			}
		}
	}

	/**
	 * login git.oschina.com
	 * 
	 * @param user
	 * @param passwd
	 * @return
	 * @throws Exception
	 */
	public boolean login(String user, String passwd) throws Exception {
		return engine.login(user, passwd);
	}

	public static String makeUrl(String user, String project, String id) {

		String url = null;

		url = "http://git.oschina.net/" + user + "/" + project
				+ "/issues/" + id;

		return url;
	}

	public Issue getIssue(String id) throws Exception {

		String url = makeUrl(issueUser, project, id);

		String msg = engine.loadPage(url);

		Document doc = (Document) Jsoup.parse(msg);
		//
		Issue issue = OSChinaIssueUtil.toIssue(id, doc, this, engine);
		System.out.println(issue);

		return issue;
	}

	public void saveIssue(Issue issue) throws Exception {

		String msg = JSONUtil.toJSONString(issue);

		File file = new File(base, "issue-" + issue.getId() + ".issue");
		saveContent(file, msg.getBytes());

		System.out.println("Issue : " + issue.getId() + " saveto: " + file);
	}

	public static void help() {
		System.out.println("Usage:");
		System.out.println("--login account");
		System.out.println("--pass  password");
		System.out.println("--user  issue's owner");
		System.out.println("--proxy  proxy_address:port");
		System.out.println("--project  project name");
		System.out.println("--from   from issue begin, default 1");
		System.out.println("--to    to issue end ");

		System.out.println("--base  output base dir, default ./");
		System.out.println("--help  help info");

	}

	public static void main(String[] args) throws Exception {

		String login = null;
		String issueUser = null;
		String pass = null;
		String proxy = null;
		String project = null;
		String from = "1";
		String to = null;
		String base = ".";
		String log4j = "./log4j.properties";

		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].compareToIgnoreCase("--login") == 0) {
					login = args[++i];
				} else if (args[i].compareToIgnoreCase("--user") == 0) {
					issueUser = args[++i];
				} else if (args[i].compareToIgnoreCase("--pass") == 0) {
					pass = args[++i];
				} else if (args[i].compareToIgnoreCase("--proxy") == 0) {
					proxy = args[++i];
				} else if (args[i].compareToIgnoreCase("--base") == 0) {
					base = args[++i];
				} else if (args[i].compareToIgnoreCase("--project") == 0) {
					project = args[++i];
				} else if (args[i].compareToIgnoreCase("--log4j") == 0) {
					log4j = args[++i];
				} else if (args[i].compareToIgnoreCase("--from") == 0) {
					from = args[++i];
				} else if (args[i].compareToIgnoreCase("--to") == 0) {
					to = args[++i];
				} else if (args[i].compareToIgnoreCase("--help") == 0) {
					help();
					return;
				}

			}
		}

		if (StringUtil.isEmpty(login) || StringUtil.isEmpty(project)
				|| StringUtil.isEmpty(to)) {
			help();
			return;
		}

		if (StringUtil.isEmpty(issueUser))
			issueUser = login;

		File path = new File(base);
		if (!path.exists())
			path.mkdirs();

		org.apache.log4j.PropertyConfigurator.configure(log4j);

		if (StringUtil.isEmpty(pass)) {
			Console console = System.console();
			if (console != null) {
				
				char[] passwd = System.console().readPassword(
						"Input "+ login +"'s password:");
				pass = new String(passwd);
			} else {
				System.err.println("Can't get console, use --pass");
				help();
				return;
			}

		}

		ExportIssues loader = new ExportIssues(proxy, issueUser, project, base);

		loader.login(login, pass);

		int _begin = Integer.parseInt(from);
		int _to = Integer.parseInt(to);

		for (int i = _begin; i <= _to; i++) {

			try {
				Issue issue = loader.getIssue("" + i);
				loader.saveIssue(issue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
