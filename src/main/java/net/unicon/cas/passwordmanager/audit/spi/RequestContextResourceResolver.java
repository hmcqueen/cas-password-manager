package net.unicon.cas.passwordmanager.audit.spi;

import com.github.inspektr.audit.spi.support.AbstractAuditResourceResolver;
import org.springframework.webflow.execution.RequestContext;

/**
 *
 * @author Harvey McQueen
 * 
 */
public class RequestContextResourceResolver extends AbstractAuditResourceResolver {

	@Override
	protected String[] createResource(Object[] args) {
		final RequestContext requestContext = (RequestContext) args[0];
		return new String[]{ "[username:" + (String) requestContext.getFlowScope().get("username") + "]" };
	}

}
