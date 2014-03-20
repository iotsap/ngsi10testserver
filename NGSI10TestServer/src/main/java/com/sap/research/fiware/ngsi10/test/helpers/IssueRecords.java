package com.sap.research.fiware.ngsi10.test.helpers;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

/**
 * Class for storing IssueRecords in a List (i.e., it *has* a List to delegate
 * to rather than being one).
 * */

public class IssueRecords {
	private List<IssueRecord> issues = new ArrayList<IssueRecord>();

	List<IssueRecord> getIssues() {
		return this.issues;
	}

	void addIssueRecord(IssueRecord rec) {
		this.issues.add(rec);
	}

	/**
	 * Convenience method for adding a new IssueRecord. Equivalent to
	 * <code><pre>
IssueRecord rec = new IssueRecord(httpStatus, desc, ngsiStatusCode);
addIssueRecord(rec);</pre></code>
	 * 
	 * */

	void addNewIssueRecord(Status httpStatus, String desc, NgsiStatusCodes ngsiStatusCode) {
		IssueRecord rec = new IssueRecord(httpStatus, desc, ngsiStatusCode);
		addIssueRecord(rec);
	}

	public boolean hasErrors() {
		return getFirstError() != null;
	}

	/**
	 * Returns the first issue that has an HTTP Status greater than or equal to
	 * 400 or an NGSI Status != 200 (which indicates an error).
	 */
	public IssueRecord getFirstError() {
		IssueRecord retVal = null;
		for (IssueRecord ir : getIssues()) {
			if (ir.isHttpError() || ir.isNgsiError()) {
				retVal = ir;
				break;
			} // end if
		} // end for
		return retVal;
	} // getFirstError()

}
