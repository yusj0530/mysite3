<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/guestbook-ajax.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/jquery/jquery-1.9.0.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script>
// jquery PlugIn
(function($){
	$.fn.hello = function(){
		console.log( $(this).attr("id") + ": hello~");
	}
})(jQuery);
</script>

<script>
var isEnd = false;
var messageBox = function(title, message, callback){
	$( "#dialog-message" ).attr( "title", title );
	$( "#dialog-message p" ).text( message );
	$( "#dialog-message" ).dialog({
		modal: true,
		buttons: {
			"확인": function(){
				$(this).dialog("close");
			}
		},
		close: callback || function(){}
	});
}

var render = function( vo, mode ) {
	var html = 
		"<li data-no='" + vo.no + "'>" +
		"<strong>" + vo.name + "</strong>" +
		"<p>" + vo.content.replace(/\n/gi, "<br>") + "</p>" +
		"<strong></strong>" +
		"<a href='' data-no='" + vo.no + "'>삭제</a>"+ 
		"</li>";
	
	if( mode == true ) {
		$( "#list-guestbook" ).prepend( html );
	} else {
		$( "#list-guestbook" ).append( html );
		
	}
}

var fetchList = function(){
	if( isEnd == true ) {
		return;
	}
	var startNo = $( "#list-guestbook li" ).last().data("no") || 0;
	$.ajax({
		url: "/mysite3/api/guestbook/list?no=" + startNo,
		type: "get",
		dataType: "json",
		data:"",
		success: function( response ) {
			if( response.result != "success" ) {
				console.log( response.message );
				return;
			}
			
			// 끝 감지
			if( response.data.length < 5){
				isEnd = true;
				$( "#btn-next" ).prop( "disabled", true );
			}
			
			$.each( response.data, function(index, vo){
				render( vo, false );
			});
		}
	});
}

$(function(){
	// 삭제 시 비밀번호 입력 다이알로그 정의
	var deleteDialog = $("#dialog-delete-form").dialog({
		autoOpen: false,
		modal: true,
		buttons: {
			"삭제": function(){
				var password = $("#password-delete").val(); 
				var no = $("#hidden-no").val();
				console.log( "삭제:" + no + ":" + password );
				
				//ajax 통신
				$.ajax({
					url: "/mysite3/api/guestbook/delete",
					type: "post",
					dataType: "json",
					data: "no=" + no + "&password=" + password,
					success: function( response ) {
						if( response.result == "fail" ) {
							console.log( response.message );
							return;
						}
						
						if( response.data == -1 ) {
							$( ".validateTips.normal" ).hide();
							$( ".validateTips.error" ).show();
							$( "#password-delete" ).val( "" );
							return;
						}
						
						$( "#list-guestbook li[data-no=" + response.data + "]" ).remove();
						deleteDialog.dialog( "close" );
					},
					error: function( xhr, status, e){
						console.error( status + ":" + e );
					}
				});
				
//				$(this).dialog("close");
			},
			"취소": function(){
				$(this).dialog("close");
			}
		},
		close: function(){
			$("#password-delete").val( "" );
			$("#hidden-no").val( "" );
		}
	});
	
	$( window ).scroll( function(){
		var $window = $(this);
		var scrollTop = $window.scrollTop();
		var windowHeight = $window.height();
		var documentHeight = $( document ).height();
		
		//console.log( 
		//	scrollTop + ":" + 
		//	windowHeight + ":" + 
		//	documentHeight );
		// scollbar의 thumb가 바닥 전 30px 까지 도달 했을 때
		if( scrollTop + windowHeight + 30 > documentHeight ) {
			fetchList();
		}
	});
	
	$("#add-form").submit( function(event){
		event.preventDefault();
		//var queryString = $(this).serialize();
		/*
		var o = {
			"name": $("#input-name").val(),
			"password": $("#input-password").val(),
			"content": $("#tx-content").val()
		};
		*/
		if($("#input-name").val() === ''){
			//$( "#dialog-message" ).dialog();
			messageBox( 
				"메세지 등록", 
				"이름이 비어 있습니다.",
				function(){
					$("#input-name").focus();
				});
			return;
		}
		
		if($("#input-password").val() === ''){
			messageBox( 
				"메세지 등록", 
				"비밀번호가 비어 있습니다.",
				function(){
					$("#input-password").focus();
				});
			return;
		}
		
		var data = {};
		$.each($(this).serializeArray(), function(index, o){
			data[ o.name ] = o.value
		});
		console.log( data );
		
		$.ajax({
			url: "/mysite3/api/guestbook/insert",
			type: "post",
			dataType: "json",
			contentType: "application/json",
			data: JSON.stringify( data ),
			success: function( response ){
				render( response.data, true );
				//reset form
				$("#add-form")[0].reset();
			}
		});
	});
	
	$("#btn-next").click( function(){
		fetchList();
	});
	
	// live event 
	$( document ).on( "click", "#list-guestbook li a", function(event){
		event.preventDefault();
		
		var no = $(this).data( "no" );
		$( "#hidden-no" ).val( no );
		
		deleteDialog.dialog( "open" );
	});
	
	// 최초 리스트 가져오기 
	fetchList();
	
	$("#content").hello();
});
</script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="guestbook">
				<h1>방명록</h1>
				<form id="add-form" action="" method="post">
					<input type="text" id="input-name" name="name" placeholder="이름">
					<input type="password" id="input-password" name="password" placeholder="비밀번호">
					<textarea id="tx-content" name="content" placeholder="내용을 입력해 주세요."></textarea>
					<input type="submit" value="보내기" />
				</form>
				<ul id="list-guestbook">
				</ul>
				<div style="text-align:center;padding:20px">
					<button id="btn-next" style="padding:10px 20px">다음</button>
				</div>
			</div>
			<div id="dialog-delete-form" title="메세지 삭제" style="display:none">
  				<p class="validateTips normal">작성시 입력했던 비밀번호를 입력하세요.</p>
  				<p class="validateTips error" style="display:none">비밀번호가 틀립니다.</p>
  				<form>
 					<input type="password" id="password-delete" value="" class="text ui-widget-content ui-corner-all">
					<input type="hidden" id="hidden-no" value="">
					<input type="submit" tabindex="-1" style="position:absolute; top:-1000px">
  				</form>
			</div>
			<div id="dialog-message" title="" style="display:none">
  				<p></p>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="guestbook-ajax"/>
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>