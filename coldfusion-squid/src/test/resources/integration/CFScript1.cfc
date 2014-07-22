component {
	url.returnformat="json";
	 
	remote function getEvents() {
	 
		var q = new query();
		q.setSQL("select eid, ecatTextColor, eCatColor, etitle, eCatName, estart, eEnd, eAllDay, eDescription from mycal_Events_view");
		var dbresults = q.execute().getResult();
		 
		var results = [];
		 
		for(var i=1; i<= dbresults.recordCount; i++) {
			arrayAppend(results, {
			"id"=dbresults.eid[i],
			"title"=dbresults.eTitle[i],
			"start"=dbresults.eStart[i],
			"end"=dbresults.eEnd[i],
			"allDay"=dbresults.eAllDay[i],
			"description" = dbresults.eDescription[i],
			"color" = dbresults.eCatColor[i],
			"textColor" = dbresults.eCatTextColor[i]
			 
			});
		}
		return results;
	}
 
}