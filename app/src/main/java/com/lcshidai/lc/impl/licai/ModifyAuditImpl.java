package com.lcshidai.lc.impl.licai;

import com.lcshidai.lc.model.BaseJson;

public interface ModifyAuditImpl {

	void modifyAuditSuccess(BaseJson response);

	void modifyAuditFailed(BaseJson errorResponse);
}
