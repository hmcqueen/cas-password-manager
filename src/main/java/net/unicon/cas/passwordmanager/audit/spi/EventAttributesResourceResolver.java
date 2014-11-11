package net.unicon.cas.passwordmanager.audit.spi;

import com.github.inspektr.audit.spi.AuditResourceResolver;
import org.aspectj.lang.JoinPoint;
import org.springframework.webflow.execution.Event;

/**
 *
 * @author hgm02a
 */
public class EventAttributesResourceResolver implements AuditResourceResolver {

	@Override
	public String[] resolveFrom(JoinPoint target, Object returnValue) {
		return new String[]{ ((Event)returnValue).getAttributes().toString() };
	}

	@Override
	public String[] resolveFrom(JoinPoint target, Exception exception) {
		return new String[]{ exception.toString() };
	}

}
