//Call this method when the page loads and when the user enters the Promotion code
function getPromoCode(){
	var promoCode = $('#promoCode').val();
	if(promoCode!=null && promoCode!=''){
		localStorage.setItem("promoCode", promoCode);
	}
	else{
		promoCode = localStorage.getItem("promoCode");
	}
	var isCodeApplied = localStorage.getItem("isPromoCodeApplied");
	if(isCodeApplied!=null && isCodeApplied=='Y'){
		$("#applyCoupon").attr("value","Applied");
		$("#applyCoupon").attr('disabled','disabled').addClass('btns-disable');
		$("#promoMsg").html("<p class='couponApply'>Coupon Applied</p>");
	}
	if(isCodeApplied==null || isCodeApplied==""){
		isCodeApplied = $('#isPromoCodeApplied').val();
	}
	if(promoCode!=null && promoCode!=""){
	var str = $("#updateCartForm").serialize();
	$.ajax({
		   type: "GET",
		   url: "getPromoCodePage.action?promoCode="+promoCode,
		   data: str,
		   success: function(msg){
			   var result = msg.split("|");
			   if(result!=null && typeof result!=undefined && result[0]!='N'){
			   $('#isPromoCodeApplied').val(result[0]);
			   if(result[1]!='' && result[1]!=null && typeof result[1]!=undefined){
			   alert(result[1]);
			   }
			   localStorage.setItem("isPromoCodeApplied", result[0]); //clear this 
			   var reload = localStorage.getItem("reload");
			   if(reload==null || reload=='' || reload == 1){
				   localStorage.setItem("reload", 2);//to reload once
				   location.reload();
				}
			   }
			   else{
				   localStorage.removeItem("promoCode");
				   localStorage.removeItem("isCodeApplied");
				   localStorage.removeItem("isPromoCodeApplied");
				   location.reload();
				   $("#applyCoupon").attr("value","Apply");
				   $("#applyCoupon").attr('disabled',false).removeClass('btns-disable');
				   $("#promoMsg").hide();
				   return false;
			   }
		   }
		 });
	}
}
