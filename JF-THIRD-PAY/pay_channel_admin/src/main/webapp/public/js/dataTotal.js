$(function() {
	var index = {
		init: function() {
			$.ajax({
				url: " /wdAppsSpreadPosition/appList.do",
				data: {
					appType: "",
					companyId: $('.comId').val()
				},
				type: "get",
				dataType: 'json',
				success: function(res) {
						if(res.resCode === '000') {
							console.log(res);
						}
					}
					//						error: function(er) {
					//							BackErr(er);
					//						}
			})
			$.ajax({
				url: " /wdClickStartup/sdkDataStatistics.do",
				data: {
					appId: "",
					createTimeBegin: "",
					createTimeEnd: "",
					fromOs: "",
					companyId: $('.comId').val()
				},
				type: "get",
				dataType: 'json',
				success: function(res) {
						if(res.resCode === '000') {
							console.log(res);
						}
					}
					//						error: function(er) {
					//							BackErr(er);
					//						}
			})
			$.ajax({
				url: " /wdClickStartup/dataStatistics.do",
				data: {
					appId: "",
					createTimeBegin: "",
					createTimeEnd: "",
					fromOs: "",
					companyId: $('.comId').val()
				},
				type: "get",
				dataType: 'json',
				success: function(res) {
						if(res.resCode === '000') {
							console.log(res);
						}
					}
					//						error: function(er) {
					//							BackErr(er);
					//						}
			})

			index.queryData();
		},
		queryData: function() {
			index.reder(res);
		},
		reder: function() {
			index.bindEvent();
		},
		bindEvent: function() {
			$(".searchData").on('click', function() {
				$.ajax({
					url: "   /wdAppsSpreadPosition/appList.do",
					data: {
						appType: $('.appIdSelect').val(),

						companyId: $('.comId').val()
					},
					type: "get",
					dataType: 'json',
					success: function(res) {
							if(res.resCode === '000') {
								console.log(res);
							}
						}
						//								error: function(er) {
						//									BackErr(er);
						//								}
				})
				$.ajax({
					url: " /wdClickStartup/sdkDataStatistics.do",
					data: {
						appId: $('.appIdSelect').val(),
						fromOs: $('.isShowSelect').val(),
						createTimeBegin: $('.startTime').val(),
						createTimeEnd: $('.endTime').val(),
						companyId: $('.comId').val()
					},
					type: "get",
					dataType: 'json',
					success: function(res) {
							if(res.resCode === '000') {
								console.log(res);
							}
						}
						//								error: function(er) {
						//									BackErr(er);
						//								}
				})
				$.ajax({
					url: " /wdClickStartup/dataStatistics.do",
					data: {
						appId: $('.appIdSelect').val(),
						fromOs: $('.isShowSelect').val(),
						createTimeBegin: $('.startTime').val(),
						createTimeEnd: $('.endTime').val(),
						companyId: $('.comId').val()
					},
					type: "get",
					dataType: 'json',
					success: function(res) {
							if(res.resCode === '000') {
								console.log(res);
							}
						}
						//								error: function(er) {
						//									BackErr(er);
						//								}
				})
			})
		}
		
	}
	index.init();
})