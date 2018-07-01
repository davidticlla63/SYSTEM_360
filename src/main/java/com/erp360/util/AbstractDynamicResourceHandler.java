package com.erp360.util;

import java.util.Map;

import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
public abstract class AbstractDynamicResourceHandler {

	private static final StreamedContent EMPTY_STREAMED_CONTENT = new DefaultStreamedContent();

	/**
	 * IMG
	 * @return StreamedContent
	 * @throws Exception
	 */
	public StreamedContent getStreamedContentImage() throws Exception {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceHandler handler = context.getApplication().getResourceHandler();
		
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		String param = params.get("param");

		if (handler.isResourceRequest(context)){
			return buildStreamedContentImage(context,Integer.parseInt(param));
		}else{
			return EMPTY_STREAMED_CONTENT;
		}
	}

	protected abstract StreamedContent buildStreamedContentImage(FacesContext context,Integer idObject) throws Exception;

}