package com.lcshidai.lc.impl.account;


/**
 * 
 * @author lfq
 * 
 */
public interface AccountCheckImpl<T> {
	void checkSuccess(T t);

	void checkFail();
}
