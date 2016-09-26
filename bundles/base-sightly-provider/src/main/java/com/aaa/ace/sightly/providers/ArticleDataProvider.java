package com.aaa.ace.sightly.providers;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * @author raja.jillella
 *
 */

/**
 * This sightly provider is used to get the Page properties of Article header.
 *
 */
public class ArticleDataProvider extends WCMUsePojo {
	private static final Logger log = LoggerFactory.getLogger(ArticleDataProvider.class);

	private static final String ARTICLE_HERO_PATH = "/jcr:content/article-hero";

	/**
	 * articlePage object
	 */
	private Page articlePage;

	/**
	 * articleHero properties
	 */
	private ValueMap articleHero;

	@Override
	public void activate() throws Exception {
		log.debug("Start of ArticleDataProvider class");

		PageManager pagemanager = getResourceResolver().adaptTo(PageManager.class);
		String path = get("artclepagePath", String.class);
		if (StringUtils.isNotBlank(path) && pagemanager != null) {
			articlePage = pagemanager.getPage(path);
			String articleHeroPath = path + ARTICLE_HERO_PATH;
			Resource articleHeroResource = getResourceResolver().getResource(articleHeroPath);

			if (articleHeroResource != null && !ResourceUtil.isNonExistingResource(articleHeroResource)) {
				articleHero = articleHeroResource.getValueMap();
			}
		}
		log.debug("End of ArticleDataProvider class");
	}

	/**
	 * @return articlePage path
	 */
	public Page getArticlePage() {
		return articlePage;
	}

	/**
	 * @return articleHero properties
	 */
	public ValueMap getArticleHero() {
		return articleHero;
	}

}
