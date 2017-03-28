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
import com.aaa.ace.common.Constants;
import com.aaa.ace.services.EditorialArticleService;
import com.aaa.ace.services.QueryService;

/**
 * Editorial service implementation class. This class is responsible to
 * construct the query and get the results back to the provider class.
 *
 * @author yogesh.mahajan
 *
 */
@Component
@Service(EditorialArticleService.class)
public class EditorialArticleServiceImpl implements EditorialArticleService {

	@Reference
	QueryService queryService;

	/**
	 * Logger variable.
	 */
	private static final Logger logger = LoggerFactory.getLogger(EditorialArticleServiceImpl.class);

	/**
	 * SQL where clause string.
	 */
	static String sqlWhereClause = " WHERE ISDESCENDANTNODE(parent, '<<path>>') ";

	/**
	 * SQL tag clause string.
	 */
	static String sqlTagClause = " child.[cq:tags]='<<tag>>' ";

	@Override
	public List<EditorialCardBean> getEditorialArticles(String path, List<String> searchTagList,
			boolean usePopularity) {
		logger.info("Start of getEditorialArticles method with parameters Path: {}, Search Tags: {}, UsePopularity: {}",
				path, searchTagList, usePopularity);

		// List to be returned
		List<EditorialCardBean> cards = new ArrayList<EditorialCardBean>();

		// construct the query
		String sqlQueryStr = getQueryString(path, searchTagList, usePopularity);

		// Return empty list, let caller deal with it
		if (StringUtils.isEmpty(sqlQueryStr)) {
			logger.warn("Could not fetch articles due to empty sql generated, return empty list");
			return cards;
		}

		logger.debug("Executing query statement {}", sqlQueryStr);

		// Result of the query
		List<Resource> resourceList;
		try {
			resourceList = queryService.getResultResources(sqlQueryStr, 3);

			logger.debug("Query service returned {} results", resourceList.size());

			// loop over the result resource list and construct bean list
			for (Resource resource : resourceList) {
				EditorialCardBean card = new EditorialCardBean();

				logger.debug("Fetching properties for resource {} at path {} ", resource.getName(), resource.getPath());
				/*
				 * Assuming here, the query returns good articles which have
				 * title, description and image TODO: Modify query to have not
				 * null check on these properties after article template is
				 * final.
				 */
				ValueMap valueMap = resource.getValueMap();

				card.setTitle(valueMap.get("jcr:title", String.class));
				card.setDescription(valueMap.get("jcr:description", String.class));
				card.setImagePath(valueMap.get("fileReference", String.class));
				card.setImageAltText(valueMap.get("altText", String.class));
				// goto Parent resource to get the page path.
				card.setArticlePath(resource.getParent().getPath());

				cards.add(card);
			}

			logger.debug("Returning Editorial Card bean: " + cards.toString());

		} catch (RepositoryException e) {
			logger.error("Could not retrieve articles ", e);
			return cards; // Return empty list, let caller deal with it
		}

		logger.info("End of getEditorialArticles method: " + cards.toString());
		return cards;
	}

	private String getQueryString(String path, List<String> searchTagList, Boolean usePopularity) {
		// Path is null, dangerous to run query without path restriction. Let
		// caller handle null.
		if (StringUtils.isEmpty(path)) {
			logger.warn("Path is not available returning null");
			return null;
		}

		StringBuffer sqlStringBuffer = new StringBuffer();

		// build sql statement add select
		sqlStringBuffer.append(Constants.SQL_SELECT_CLAUSE);

		// add where, replace path
		sqlStringBuffer.append(StringUtils.replace(sqlWhereClause, "<<path>>", path));

		// loop over tag list and add them one by one
		if (searchTagList != null && !searchTagList.isEmpty()) {
			sqlStringBuffer.append(Constants.AND);

			ListIterator<String> tagStrIterator = searchTagList.listIterator();

			while (tagStrIterator.hasNext()) {
				sqlStringBuffer.append(StringUtils.replace(sqlTagClause, "<<tag>>", tagStrIterator.next()));

				if (tagStrIterator.nextIndex() != searchTagList.size()) {
					sqlStringBuffer.append(Constants.OR);
				}
			}
		}

		// add sorting
		sqlStringBuffer.append(Constants.SQL_ORDER_BY);

		if (usePopularity) {
			sqlStringBuffer.append(" child.pagePopularity ").append(Constants.COMMA);
		}

		sqlStringBuffer.append(" child.[jcr:created] desc ");

		return sqlStringBuffer.toString();
	}

}
