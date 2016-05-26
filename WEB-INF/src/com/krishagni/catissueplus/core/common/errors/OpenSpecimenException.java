
package com.krishagni.catissueplus.core.common.errors;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;

import com.krishagni.catissueplus.core.common.util.MessageUtil;

public class OpenSpecimenException extends RuntimeException {
	private static final long serialVersionUID = -1473557909717365251L;
	
	private ErrorType errorType = ErrorType.NONE;

	private List<ParameterizedError> errors = new ArrayList<ParameterizedError>();
	
	private Throwable exception;
	
	public OpenSpecimenException(ErrorType type, ErrorCode error, Object ... params) {
		this.errorType = type;
		errors.add(new ParameterizedError(error, params));
	}
	
	public OpenSpecimenException(ErrorType type) {
		this.errorType = type;
	}
	
	public OpenSpecimenException(Throwable t) {
		this.errorType = ErrorType.SYSTEM_ERROR;
		this.exception = t;

		if (t instanceof ConstraintViolationException) {
			ConstraintViolationException cve = (ConstraintViolationException)t;
			String dbMsg = cve.getConstraintName();
			if (StringUtils.isBlank(dbMsg)) {
				dbMsg = cve.getSQLException().getMessage();
			}

			errors.add(new ParameterizedError(CommonErrorCode.CONSTRAINT_VIOLATION, dbMsg));
		}
	}
	
	public ErrorType getErrorType() {
		return errorType;
	}
	
	public List<ParameterizedError> getErrors() {
		return errors;
	}
	
	public Throwable getException() {
		return exception;
	}
	
	public void addError(ErrorCode error, Object ... params) {
		this.errors.add(new ParameterizedError(error, params));
	}
	
	public boolean hasAnyErrors() {
		return !this.errors.isEmpty() || exception != null;
	}
	
	public void checkAndThrow() {
		if (hasAnyErrors()) {
			throw this;
		}
	}
	
	public boolean containsError(ErrorCode error) {
		boolean containsError = false;
		for (ParameterizedError parameterizedError : this.getErrors()) {
			if (parameterizedError.error().equals(error)) {
				containsError = true;
				break;
			}
		}
		return containsError;
	}
	
	public void rethrow(ErrorCode oldError, ErrorCode newError, Object ... params) {
		if (containsError(oldError)) {
			throw OpenSpecimenException.userError(newError, params);
		}
		throw this;
	}	
	
	public String getMessage() {
		StringBuilder errorMsg = new StringBuilder();

		if (CollectionUtils.isNotEmpty(errors)) {
			for (ParameterizedError pe : errors) {
				errorMsg.append(getMessage(pe)).append(", ");
			}
			errorMsg.delete(errorMsg.length() - 2, errorMsg.length());
		} else if (exception != null) {
			errorMsg.append(exception.getMessage());
		} else {
			errorMsg.append(MessageUtil.getInstance().getMessage("internal_error"));
		}

		return errorMsg.toString();
	}

	public static OpenSpecimenException userError(ErrorCode error, Object ... params) {		
		return new OpenSpecimenException(ErrorType.USER_ERROR, error, params);
	}
	
	public static OpenSpecimenException serverError(ErrorCode error, Object ... params) {
		return new OpenSpecimenException(ErrorType.SYSTEM_ERROR, error, params);
	}
	
	public static OpenSpecimenException serverError(Throwable e) {
		return new OpenSpecimenException(e);
	}

	private String getMessage(ParameterizedError error) {
		return MessageUtil.getInstance().getMessage(error.error().code().toLowerCase(), error.params());
	}
}
