package net.unicon.cas.passwordmanager.audit.spi;

import com.github.inspektr.audit.spi.support.AbstractAuditResourceResolver;

/**
 *
 * @author hgm02a
 */
public class UsernameAsFirstParameterResourceResolver extends AbstractAuditResourceResolver {

	@Override
	protected String[] createResource(Object[] args) {
		return new String[] { "[username:" + args[0] + "]" };
	}
	
}
