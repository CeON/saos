package pl.edu.icm.saos.webapp.judgment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import pl.edu.icm.saos.common.http.HttpServletRequestUtils;

/**
 * @author Łukasz Dumiszewski, Łukasz Pawełczak
 * 
 * Copied from project CRPD
 *
 */
public class PageLinkGenerator {

	private static final String PAGE_NO_PARAM_NAME = "page";


	//------------------------ LOGIC --------------------------
	
	/**
	 * Generates a base search page link. It is used in UI to create links to specific search result pages.
	 * The base link will contain the full request URL with all query parameters except the page number parameter (it will be added later in UI during creating specific search result pages).
	 * @param request
	 * @param pageNoParameterName A page number parameter name. 
	 */
	public static String generateSearchPageBaseLink(HttpServletRequest request, String pageNoParameterName, List<String> filterParameters) {
		String pageLink = HttpServletRequestUtils.constructRequestUrl(request) + "?";
		
		if (request.getQueryString()!=null) {
		    String filteredQueryString = request.getQueryString();
		    
		    filteredQueryString = filteredQueryString.replaceAll("[\\&]*"+pageNoParameterName+"\\=[0-9]*", "");
		    
		    for (String paramName : filterParameters) {
		        filteredQueryString = filteredQueryString.replaceAll("[\\&]*"+paramName+"\\=.*?($|[&;])", "");
		    }
		    
			pageLink += filteredQueryString;
		}
		return pageLink;
	}
	
	/**
	  * Invokes {@link #generateSearchPageBaseLink(HttpServletRequest, String)} with pageNoParameterName=={@link SearchConst#PAGE_NO} 
	 */
	public static String generateSearchPageBaseLink(HttpServletRequest request, List<String> filterParameters) {
		return generateSearchPageBaseLink(request, PAGE_NO_PARAM_NAME, filterParameters);
	}
}

