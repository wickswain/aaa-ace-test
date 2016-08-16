/**
 * 
 */
package com.aaa.ace.sightly.providers;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.beans.EditorialCardBean;
import com.aaa.ace.services.EditorialArticleService;
import com.adobe.cq.sightly.WCMUsePojo;

/**
 * @author yogesh.mahajan
 *
 */
public class EditorialArticleProvider extends WCMUsePojo {

	private final Logger log = LoggerFactory.getLogger(EditorialArticleProvider.class);
	
	List<EditorialCardBean> cards;
	
	@Override
	public void activate() throws Exception {
		
		log.info("Started Activate of EditorialArticleProvider");
		
		String path = getProperties().get("searchPath", String.class); 
		log.info("Path: " + path);
		
		List<String> searchTagList = Arrays.asList(getProperties().get("tags", String[].class)); 
		log.info("Tags: " + searchTagList);
		
		boolean usePopularity = Boolean.valueOf(getProperties().get("popularityFlag",String.class)); 
		log.info("usePopularity: " + usePopularity);
		
		EditorialArticleService articleService = getSlingScriptHelper().getService(EditorialArticleService.class);
		
		cards = articleService.getEditorialArticles(path, searchTagList, usePopularity);
		
		log.info("End Activate of EditorialArticleProvider");
	}
	

	public List<EditorialCardBean> getCards()
	{
		return cards;
	}

}
