package com.krishagni.catissueplus.core.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.common.service.ConfigurationService;

@Configurable
public class ConfigUtil {
	private static ConfigUtil instance = null;
	
	@Autowired
	private ConfigurationService cfgSvc;
		
	public static ConfigUtil getInstance() {
		if (instance == null) {
			instance = new ConfigUtil();
		}
		
		return instance;
	}
	
	public String getAppUrl() {
		return cfgSvc.getStrSetting("common", "app_url", "");
	}
	
	public String getDataDir() {
		return cfgSvc.getDataDir();
	}
	
	public String getAdminEmailId() {
		return cfgSvc.getStrSetting("email", "admin_email_id", "");
	}
	
	public String getDateFmt() {
		return cfgSvc.getDateFormat();
	}
	
	public String getDeDateFmt() {
		return cfgSvc.getDeDateFormat();
	}
	
	public String getTimeFmt() {
		return cfgSvc.getTimeFormat();
	}
	
	public String getDateTimeFmt() {
		return getDateFmt() + " " + getTimeFmt();
	}
	
	public String getStrSetting(String module, String name, String defValue) {
		return cfgSvc.getStrSetting(module, name, defValue);
	}
	
	public Character getCharSetting(String module, String name, char defValue) {
		return cfgSvc.getCharSetting(module, name, defValue);
	}
	
	public Boolean getBoolSetting(String module, String name, Boolean defValue) {
		return cfgSvc.getBoolSetting(module, name, defValue);
	}
	
	public Integer getIntSetting(String module, String name, Integer defValue) {
		return cfgSvc.getIntSetting(module, name, defValue);
	}
}
