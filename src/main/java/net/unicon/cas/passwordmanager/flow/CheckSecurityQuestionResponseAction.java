package net.unicon.cas.passwordmanager.flow;

import com.github.inspektr.audit.annotation.Audit;
import java.util.List;

import net.unicon.cas.passwordmanager.service.PasswordManagerLockoutService;
import org.springframework.webflow.action.EventFactorySupport;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * <p>
 * Action for checking the responses to users' security questions.</p>
 */
public class CheckSecurityQuestionResponseAction implements Action {

	private static final String RESPONSE_PARAMETER_PREFIX = "response";
	private PasswordManagerLockoutService lockoutService;

	public void setLockoutService(PasswordManagerLockoutService lockoutService) {
		this.lockoutService = lockoutService;
	}

	@Audit(
					action = "SECURITY_CHALLENGE_",
					actionResolverName = "SECURITY_CHALLENGE_RESOLVER",
					resourceResolverName = "SECURITY_CHALLENGE_RESOURCE_RESOLVER")
	@Override
	public Event execute(RequestContext req) throws Exception {

		boolean rslt = true;

		SecurityChallenge challenge = (SecurityChallenge) req.getFlowScope().get(LookupSecurityQuestionAction.SECURITY_CHALLENGE_ATTRIBUTE);
		if (challenge != null) {
			List<SecurityQuestion> questions = challenge.getQuestions();
			for (int i = 0; i < questions.size(); i++) {
				String responseText = req.getRequestParameters().get(RESPONSE_PARAMETER_PREFIX + i);
				if (!questions.get(i).validateResponse(responseText)) {
					rslt = false;
					break;
				}
			}
		} else {
			rslt = false;  // Should not get here...
		}

		if (!rslt) {
			lockoutService.registerIncorrectAttempt((String) req.getFlowScope().get("username"));
		}

		return rslt ? success() : error(/* TODO:  Send error message to client */);
	}

	/**
	 * Returns a "success" result event.
	 * @return 
	 */
	protected Event success() {
		return getEventFactorySupport().success(this);
	}
	
		/**
	 * Returns an "error" result event.
	 * @return 
	 */
	protected Event error() {
		return getEventFactorySupport().error(this);
	}

	/**
	 * Returns an "error" result event caused by the provided exception.
	 * @param e the exception that caused the error event, to be configured as an event attribute
	 * @return 
	 */
	protected Event error(Exception e) {
		return getEventFactorySupport().error(this, e);
	}
	
		/**
	 * Returns the helper delegate for creating action execution result events.
	 * @return the event factory support
	 */
	public EventFactorySupport getEventFactorySupport() {
		return new EventFactorySupport();
	}

}
