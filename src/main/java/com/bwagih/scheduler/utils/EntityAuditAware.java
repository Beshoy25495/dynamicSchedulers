package com.bwagih.scheduler.utils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class EntityAuditAware implements AuditorAware<String> {

	Logger log = LoggerFactory.getLogger(this.getClass());
	public static ThreadLocal<EntityAuditAware> threadLocal = new ThreadLocal<EntityAuditAware>();

	@Autowired(required = false)
	private HttpServletRequest request;

	private String username;
	private String sessionId;
	private String ip;

	@Override
	public Optional<String> getCurrentAuditor() {

//		UserModel model = new UserModel();
//		try {
//			model = CommonUtils.getUserModel();
//		} catch (Exception e) {
//			model.setUsername("System");
//			model.setId(0L);
//		}
		maintainAuditInfo();
		return Optional.ofNullable("");
	}

	private void maintainAuditInfo() {
		try {
			if (request != null) {
				HttpSession session = request.getSession(false);
				if(session == null)
					setSessionId("System");
				else
					setSessionId(session.getId());
				setIp(request.getRemoteAddr());
				threadLocal.set(this);
				return;

			}
		} catch (Exception e) {
			log.error("-------------EntityAuditAware.maintainAuditInfo(Falling back to default values)" + e.getMessage());
		}

		setSessionId(System.currentTimeMillis() + "");
		setIp("System");
		threadLocal.set(this);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
