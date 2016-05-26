package com.krishagni.catissueplus.core.common.repository.impl;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Session;

import com.krishagni.catissueplus.core.common.domain.KeySequence;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.repository.UniqueIdGenerator;

public class UniqueIdGeneratorImpl extends AbstractDao<KeySequence> implements UniqueIdGenerator {

	@SuppressWarnings("unchecked")
	@Override
	public Long getUniqueId(String type, String id) {
		Session session = null;
		
		try {
			session = sessionFactory.getCurrentSession();
			List<KeySequence> seqs = session
					.getNamedQuery(GET_BY_TYPE_AND_TYPE_ID)
					.setLockMode("ks", LockMode.PESSIMISTIC_WRITE)
					.setString("type", type)
					.setString("typeId", id)
					.list();
			
			KeySequence seq = null;
			if (seqs.isEmpty()) {
				seq = new KeySequence();
				seq.setType(type);
				seq.setTypeId(id);			
			} else {
				seq = seqs.iterator().next();
			}
			
			Long uniqueId = seq.increment();
			
			session.saveOrUpdate(seq);
			return uniqueId;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static final String FQN = KeySequence.class.getName();
	
	private String GET_BY_TYPE_AND_TYPE_ID = FQN + ".getByTypeAndTypeId";
}