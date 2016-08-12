/**
 * 
 */
package com.aaa.ace.sightly.providers;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.JCRQueryService;
import com.adobe.cq.sightly.WCMUsePojo;

/**
 * @author yogesh.mahajan
 *
 */
public class EditorialArticleProvider extends WCMUsePojo {

	static String sqlStringSelect = "SELECT child.* FROM [cq:Page] AS parent "
			+ "INNER JOIN [nt:base] AS child ON ISCHILDNODE(child,parent) ";
	
	static String sqlStringWhere = "WHERE ISDESCENDANTNODE(parent, '<<path>>') ";
	
	static String sqlTagClause = " child.[cq:tags]='<<tag>>' ";
	
	static String sqlOrderClause = " ORDER BY ";
	
	static final String COMMA = ",";
	
	
/*	static String sqlStatement = "SELECT child.* FROM [cq:Page] AS parent "
			+ "INNER JOIN [nt:base] AS child ON ISCHILDNODE(child,parent) "
			+ "WHERE ISDESCENDANTNODE(parent, '<<path>>') "
			+ "<<tags>>"
			+ "ORDER BY child.popularity,child.[jcr:created] desc";
*/	
	//AND (child.[cq:tags]='AAA:region-tx' or child.[cq:tags]='AAA:eastways')
	//

	private final Logger log = LoggerFactory.getLogger(EditorialArticleProvider.class);
	
	List<EditorialCardBean> cards = new ArrayList<EditorialCardBean>(3);
	
	@Override
	public void activate() throws Exception {
		
		log.info("Started Activate of EditorialArticleProvider");
		
		String path =getProperties().get("searchPath", String.class); //get("path", String.class);
		log.info("Path: " + path);
		
		String[] searchTags = getProperties().get("tags", String[].class); //get("searchTags",String[].class);
		log.info("Tags: " + searchTags);
		
		boolean usePopularity = Boolean.valueOf(getProperties().get("popularityFlag",String.class)); //Boolean.valueOf(BooleanUtils.toBoolean(get("popularityFlag",String.class)));
		log.info("usePopularity: " + usePopularity);		
		
		String sqlQueryStr = getQueryString(path, searchTags, usePopularity);
		
		if (StringUtils.isEmpty(sqlQueryStr))
		{
			log.info("Could not activate EditorialArticleProvider due to empty sql generated" );
			return;
		}
		
		JCRQueryService queryService = getSlingScriptHelper().getService(JCRQueryService.class);
		
		log.info("Executing query statement {}", sqlQueryStr );
		
		List<Node> nodeList = queryService.getResults(sqlQueryStr, 3);
		
		log.info("Query service returned {} results", nodeList.size() );
		
		EditorialCardBean card;
		
		for(Node node : nodeList)
		{
			card = new EditorialCardBean();
			
			Resource resultResource = getResourceResolver().getResource(node.getPath());
			ValueMap valueMap = resultResource.getValueMap();
			
			log.info("================================================\n\n");
			
			log.info("{} jcr:title {}", node.getName() , valueMap.get("jcr:title", String.class));
			card.setTitle(valueMap.get("jcr:title", String.class));
			
			log.info("{} jcr:description {}", node.getName() , valueMap.get("jcr:description"));
			card.setDescription(valueMap.get("jcr:description", String.class));
			
			log.info("{} Image path {}", node.getName() , resultResource.getValueMap().get("fileReference"));
			card.setImagePath(valueMap.get("fileReference", String.class));
			
			card.setImageAltText(valueMap.get("altText", String.class));
			
			card.setArticlePath(resultResource.getPath());

			log.info("================================================\n\n");
			
			cards.add(card);
		}
		
		log.info("End Activate of EditorialArticleProvider");
	}
	
	private String getQueryString(String path, String[] searchTags, Boolean usePopularity) {
		
		if(StringUtils.isEmpty(path))
		{
			log.info("Path is not available returning null");
			return null;
		}
		
		StringBuffer sqlStringBuffer = new StringBuffer();
		
		//build sql statement
		//add select
		sqlStringBuffer.append(sqlStringSelect);
		
		//add where, replace path 
		sqlStringBuffer.append(StringUtils.replace(sqlStringWhere, "<<path>>", path));
		
		if(StringUtils.isNoneEmpty(searchTags))
		{
			sqlStringBuffer.append(" AND ");
			//String[] tagArray = StringUtils.split(searchTags, COMMA);
			String[] tagArray = searchTags;
			
			for(int i=0; i<tagArray.length; i++)
			{
				sqlStringBuffer.append(StringUtils.replace(sqlTagClause, "<<tag>>", tagArray[i]));
				
				if(!(i==tagArray.length-1))
				{
					sqlStringBuffer.append(" OR ");
				}
			}
		}
		
		sqlStringBuffer.append(sqlOrderClause);
		
		if (usePopularity)
		{
			sqlStringBuffer.append(" child.popularity ").append(COMMA);
		}
		
		sqlStringBuffer.append(" child.[jcr:created] desc ");
		
		return sqlStringBuffer.toString();
	}

	public List<EditorialCardBean> getCards()
	{
		return cards;
	}

}
