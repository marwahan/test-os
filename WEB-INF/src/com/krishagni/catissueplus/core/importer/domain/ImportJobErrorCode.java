package com.krishagni.catissueplus.core.importer.domain;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum ImportJobErrorCode implements ErrorCode {
	NOT_FOUND,
	
	ACCESS_DENIED,
			
	OUTPUT_FILE_NOT_CREATED,
	
	OBJ_SCHEMA_NOT_FOUND,
	
	RECORD_PARSE_ERROR;

	@Override
	public String code() {		
		return "IMPORT_JOB_" + this.name();
	}
}
