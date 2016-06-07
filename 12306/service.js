/**
 * Created by gao on 15/12/1.
 */
	
~function(req, exp){
	"use strict";

	//车票查询
	exp.surplusTicketCount = function(params){
		return {
			url: "/otn/leftTicket/queryT",
			type: "get",
			data:{
				"leftTicketDTO.train_date": params.depDate,
				"leftTicketDTO.from_station": params.dep,
				"leftTicketDTO.to_station": params.dst,
				"purpose_codes": params.passengerType
			}
		};
	};

}(require, exports);
