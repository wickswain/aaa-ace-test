/**
 * 
 */
package com.aaa.ace.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.jcr.RepositoryException;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.beans.EditorialCardBean;
import com.aaa.ace.services.EditorialArticleService;
import com.aaa.ace.services.QueryService;

/**
 * @author yogesh.mahajan
 *
 */

@Component
@Service(EditorialArticleService.class)

public class EditorialArticleServiceImpl implements EditorialArticleService {
	
	@Reference
	QueryService queryService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	static String sqlStringSelect = "SELECT child.* FROM [cq:Page] AS parent "
			+ "INNER JOIN [nt:base] AS child ON ISCHILDNODE(child,parent) ";

	static String sqlStringWhere = "WHERE ISDESCENDANTNODE(parent, '<<path>>') ";

	static String sqlTagClause = " child.[cq:tags]='<<tag>>' ";

	static String sqlOrderClause = " ORDER BY ";

	static final String COMMA = ",";
	
	static final String AND = " AND ";
	
	static final String OR = " OR ";

	/*
	 * Sample Query: static String sqlStatement =
	 * "SELECT child.* FROM [cq:Page] AS parent " +
	 * "INNER JOIN [nt:base] AS child ON ISCHILDNODE(child,parent) " +
	 * "WHERE ISDESCENDANTNODE(parent, '<<path>>') " +
	 * "AND (child.[cq:tags]='AAA:region-tx' or child.[cq:tags]='AAA:eastways')"
	 * + "ORDER BY child.popularity,child.[jcr:created] desc";
	 */

	@Override
	public List<EditorialCardBean> getEditorialArticles(String path, List<String> searchTagList, boolean usePopularity) 
	{
		List<EditorialCardBean> cards = new ArrayList<EditorialCardBean>();
		
		String sqlQueryStr = getQueryString(path, searchTagList, usePopularity);
		
		if (StringUtils.isEmpty(sqlQueryStr))
		{
			log.info("Could not fetch articles due to empty sql generated" );
			
			//Return empty list, let caller deal with it
			return cards;
		}
		
		log.info("Executing query statement {}", sqlQueryStr );
		
		List<Resource> resourceList;
		try {
			
			resourceList = queryService.getResultResources(sqlQueryStr, 3);
			
		} catch (RepositoryException e) {
			
			log.error("Could not retrieve articles " , e);
			
			//Return empty list, let caller deal with it
			return cards;
			
		}
		
		log.info("Query service returned {} results", resourceList.size() );
		
		EditorialCardBean card;
		
		for(Resource resource : resourceList)
		{
			card = new EditorialCardBean();
			
			ValueMap valueMap = resource.getValueMap();
			
			log.info("{} jcr:title {}", resource.getName() ,valueMap.get("jcr:title", String.class));
			card.setTitle(valueMap.get("jcr:title", String.class));
			
			log.info("{} jcr:description {}", resource.getName() , valueMap.get("jcr:description"));
			card.setDescription(valueMap.get("jcr:description", String.class));
			
			log.info("{} Image path {}", resource.getName() , valueMap.get("fileReference"));
			card.setImagePath(valueMap.get("fileReference", String.class));
			
			card.setImageAltText(valueMap.get("altText", String.class));
			
			card.setArticlePath(resource.getPath());
			
			cards.add(card);
		}
		
		return cards;
	}

	private String getQueryString(String path, List<String> searchTagList, Boolean usePopularity) {

		if (StringUtils.isEmpty(path)) {
			log.info("Path is not available returning null");
			return null;
		}

		StringBuffer sqlStringBuffer = new StringBuffer();

		// build sql statement
		// add select
		sqlStringBuffer.append(sqlStringSelect);

		// add where, replace path
		sqlStringBuffer.append(StringUtils.replace(sqlStringWhere, "<<path>>",
				path));

		if (searchTagList != null && !searchTagList.isEmpty()) {
			sqlStringBuffer.append(AND);
			
			ListIterator<String> tagStrIterator = searchTagList.listIterator();
			
			while(tagStrIterator.hasNext())
			{
				sqlStringBuffer.append(StringUtils.replace(sqlTagClause, "<<tag>>", tagStrIterator.next()));
				
				if (tagStrIterator.nextIndex() != searchTagList.size()) {
					sqlStringBuffer.append(OR);
				}
			}
		}

		sqlStringBuffer.append(sqlOrderClause);

		if (usePopularity) {
			sqlStringBuffer.append(" child.popularity ").append(COMMA);
		}

		sqlStringBuffer.append(" child.[jcr:created] desc ");

		return sqlStringBuffer.toString();
	}

}
