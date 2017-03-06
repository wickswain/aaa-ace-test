package com.aaa.ace.sightly.providers;

import javax.jcr.Node;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * Utility class to help a Sightly component to create a new page
 */
public class CreatePageProvider extends WCMUsePojo {

	/**
	 * Logger variable.
	 */
	private final Logger logger = LoggerFactory.getLogger(CreatePageProvider.class);

	@Override
	public void activate() throws Exception {

		Page newPage = null;
		Page checkPage = null;
		Node jcrNode = null;
		Node pageNode = null;
		String pagePath = null;
		String slash = "/";
		Session session = getResourceResolver().adaptTo(Session.class);
		PageManager pageManager = getPageManager();
		Page currentPage = getCurrentPage();
		String pageName = currentPage.getName();
		String pageTitle = currentPage.getTitle();
		String template = get("template", String.class);
		String nonPurgingPath = get("nonpurgingpath", String.class);
		String resourceType = get("resourcetype", String.class);

		logger.debug("CreatePageProvider started()");

		pagePath = nonPurgingPath + slash + pageName;
		checkPage = pageManager.getPage(pagePath);

		if (checkPage == null) {
			logger.debug("non-purging page non exists for the page path : {}", pagePath);
			newPage = pageManager.create(nonPurgingPath, pageName, template, pageTitle);

			pageNode = newPage.adaptTo(Node.class);

			if (newPage.hasContent()) {
				jcrNode = newPage.getContentResource().adaptTo(Node.class);
			} else {
				jcrNode = pageNode.addNode("jcr:content", "cq:PageContent");
			}
			jcrNode.setProperty("sling:resourceType", resourceType);

			Node parNode = jcrNode.addNode("content-par");
			parNode.setProperty("sling:resourceType", "wcm/foundation/components/parsys");

			Node textNode = parNode.addNode("text");
			textNode.setProperty("sling:resourceType", "aaa-core/components/content/rte");
			textNode.setProperty("textIsRich", "true");

			session.save();
			session.refresh(true);

		} else {
			logger.debug("non-purging page already exists :{}", pagePath);
		}
		logger.debug("CreatePageProvider end()");
	}
}
