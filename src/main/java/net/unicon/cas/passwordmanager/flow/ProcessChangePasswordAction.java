package net.unicon.cas.passwordmanager.flow;

import com.github.inspektr.audit.annotation.Audit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.unicon.cas.passwordmanager.InvalidPasswordException;
import net.unicon.cas.passwordmanager.service.PasswordManagerService;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.webflow.execution.RequestContext;

/**
 * <p>Changes the user's password.</p>
 */
public class ProcessChangePasswordAction {

	private final Log logger = LogFactory.getLog(this.getClass());
	private PasswordManagerService passwordManagerService;

	@Audit(
					action = "CHANGE_PASSWORD",
					actionResolverName = "CHANGE_PASSWORD_RESOLVER",
					resourceResolverName = "CHANGE_PASSWORD_RESOURCE_RESOLVER")
	public boolean changePassword(RequestContext requestContext, String flowScopeUsername, String beanUsername, String oldPassword, String password,
			MessageContext messageContext) throws Exception {
		
		// prefer a username found in the flow scope to one found in the bean
		String username = flowScopeUsername != null ? flowScopeUsername : beanUsername;
		
		if(username == null) {
			messageContext.addMessage(new MessageBuilder().error().source("username")
					.code("cas.pm.username.empty")
					.defaultText("Please enter your NetID")
					.build());
			logger.debug("No username found changing password.");
			return false;
		}
		
		try {
			passwordManagerService.changeUserPassword(username, oldPassword, password);
		} catch(InvalidPasswordException ex) {
			messageContext.addMessage(new MessageBuilder().error().source("oldPassword")
					.code("cas.pm.oldPassword.invalid")
					.defaultText("Username/password combination incorrect")
					.build());
			logger.debug("InvalidPasswordException changing password for user.");
			return false;
		} catch(NameNotFoundException ex) {
			messageContext.addMessage(new MessageBuilder().error().source("oldPassword")
					.code("cas.pm.oldPassword.invalid")
					.defaultText("Username/password combination incorrect")
					.build());
			logger.debug("NameNotFoundException changing password for user " + username);
			return false;
		} catch(Exception ex) {
			logger.error("Unknown exception changing user's password.",ex);
			return false;
		}
		
		return true;
	}
	
	@Audit(
					action = "SET_PASSWORD",
					actionResolverName = "SET_PASSWORD_RESOLVER",
					resourceResolverName = "SET_PASSWORD_RESOURCE_RESOLVER")
	public boolean setPassword(RequestContext requestContext, String username, String password,
			MessageContext messageContext) throws Exception {
		
		try {
			passwordManagerService.setUserPassword(username, password);
		} catch(Exception ex) {
			logger.error("Unknown exception changing user's password.",ex);
			return false;
		}
		
		return true;
	}
	
	@Audit(
					action = "CHANGE_PASSWORD",
					actionResolverName = "CHANGE_PASSWORD_RESOLVER",
					resourceResolverName = "CHANGE_PASSWORD_RESOURCE_RESOLVER")
	public boolean changePassword(RequestContext requestContext, String username, String oldPassword, 
			String newPassword) throws Exception {
		
		try {
			passwordManagerService.changeUserPassword(username, oldPassword, newPassword);
		} catch(Exception ex) {
			logger.error("Exception changing user's password.");
			return false;
		}
		return true;
	}

	public void setPasswordManagerService(
			PasswordManagerService passwordManagerService) {
		this.passwordManagerService = passwordManagerService;
	}

}
