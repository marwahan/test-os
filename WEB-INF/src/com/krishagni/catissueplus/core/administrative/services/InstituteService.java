package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.events.InstituteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.InstituteSummary;
import com.krishagni.catissueplus.core.administrative.repository.InstituteListCriteria;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface InstituteService {

	public ResponseEvent<List<InstituteSummary>> getInstitutes(RequestEvent<InstituteListCriteria> req);

	public ResponseEvent<InstituteDetail> getInstitute(RequestEvent<InstituteQueryCriteria> req);
	
	public ResponseEvent<InstituteDetail> createInstitute(RequestEvent<InstituteDetail> req);

	public ResponseEvent<InstituteDetail> updateInstitute(RequestEvent<InstituteDetail> req);
	
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req);

	public ResponseEvent<InstituteDetail> deleteInstitute(RequestEvent<DeleteEntityOp> req);
	
}
