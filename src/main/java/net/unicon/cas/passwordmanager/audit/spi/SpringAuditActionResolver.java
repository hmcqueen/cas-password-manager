package net.unicon.cas.passwordmanager.audit.spi;

import com.github.inspektr.audit.annotation.Audit;
import com.github.inspektr.audit.spi.AuditActionResolver;
import org.aspectj.lang.JoinPoint;
import org.springframework.webflow.execution.Event;

/**
 *
 * @author Harvey McQueen <hmcqueen@gmail.com>
 *
 * <p>Appends the Spring Webflow event id to the audit.action()</p>
 * 
 */
public class SpringAuditActionResolver implements AuditActionResolver {

	private final String exceptionSuffix;

	/**
	 * Constructs the resolver with default value for failure suffix.
	 */
	public SpringAuditActionResolver() {
		this("EXCEPTION");
	}

	/**
	 * Constructs the
	 * {@link net.unicon.cas.passwordmanager.audit.spi.SpringAuditActionResolver}
	 * with an exception suffix. CANNOT be NULL.
	 *
	 * @param exceptionSuffix the suffix to use in the event of a failure.
	 */
	public SpringAuditActionResolver(final String exceptionSuffix) {
		if (exceptionSuffix == null) {
			throw new IllegalArgumentException("exceptionSuffix cannot be null.");
		}

		this.exceptionSuffix = exceptionSuffix;
	}

	protected final String getExceptionSuffix() {
		return this.exceptionSuffix;
	}

	@Override
	public String resolveFrom(JoinPoint auditableTarget, Object retval, Audit audit) {
		Event event = (Event) retval;
		return audit.action() + event.getId().toUpperCase();
	}

	@Override
	public String resolveFrom(JoinPoint auditableTarget, Exception exception, Audit audit) {
		return audit.action() + getExceptionSuffix();
	}

}
