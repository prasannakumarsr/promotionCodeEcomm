//Method to be used to get the promotion code stored in DB
//Struts 2 is the framework used
public String promotionCode(){
	request =ServletActionContext.getRequest();
	HttpSession session = request.getSession();
	String wareHouseCode = (String) session.getAttribute("wareHouseCode"); //Get the warehouse code of the particular user logged in
	String promoCode = request.getParameter("promoCode"); //The entered Promotion code by the user
	String cartTotal = request.getParameter("cartTotal"); //The cart total value for which the discount will be applicable
	String buyingCompanyId = (String) session.getAttribute("buyingCompanyId"); //The buying company id of the user which he/she belongs to
	String message = "";
	double total = 0.0;
	DecimalFormat df = new DecimalFormat("#.00");
	String isPromoCodeApplied = request.getParameter("isPromoCodeApplied"); //Parameter to check if the user has already applied the Promotion code or not
	ProductsModel promotion = new ProductsModel(); //Model classes
	ProductsModel couponInfo = new ProductsModel(); //Model classes
	promotion.setWareHouseCode(wareHouseCode);
	promotion.setCartTotal(Double.parseDouble((cartTotal)));
	promotion.setPromoCode(promoCode);
	if(promoCode!=null && promoCode.length() > 0){	
		session.setAttribute("promotionCode",promoCode);
		promotion = SomeDAO.getPromotionalCode(promotion); // This method is defined in other class
		couponInfo = SomeDAO.getCouponCounter(buyingCompanyId, promoCode); // This method is defined in other class
		if(couponInfo!=null && couponInfo.getCouponCounter() > promotion.getCouponCounter()){ //Check the counter if the number of coupons assigned is over or not
			message = "Coupon has expired";
		}
	}
	if(promotion!=null && couponInfo!=null && OtherDAO.validateString(promotion.getDiscountType())!="" && OtherDAO.validateString(promotion.getDiscountValue())!="" && couponInfo.getCouponCounter() < promotion.getCouponCounter()){
		if(promotion.getDiscountType().trim().equals("$")){
			total = (Double.parseDouble(cartTotal) - UsersDAO.validateNumber(promotion.getDiscountValue()));
			isPromoCodeApplied = "Y";
			session.setAttribute("discountType", promotion.getDiscountType()); //Set all the values to session to be retrieved later
			session.setAttribute("discountValue", promotion.getDiscountValue());
			session.setAttribute("discountValueToERP", promotion.getDiscountValue());
			//Update the discount value everytime the cart is updated by calling which ever method applicable to display the discounted value.
			renderContent = isPromoCodeApplied+"|"+message; //
		}
		else if(promotion.getDiscountType().trim().equals("%")){
			double calcDiscount = ((Double.parseDouble(cartTotal) * (Double.parseDouble(promotion.getDiscountValue())))/100);
			total = Double.parseDouble(cartTotal) - calcDiscount;
			isPromoCodeApplied = "Y";
			session.setAttribute("discountType", promotion.getDiscountType());
			session.setAttribute("discountValue", String.valueOf(calcDiscount));
			session.setAttribute("discountValueToERP", promotion.getDiscountValue());
			//Update the discount value everytime the cart is updated by calling which ever method applicable to display the discounted value.
			renderContent = isPromoCodeApplied+"|"+message;
		}
	} 
	else{
		session.removeAttribute("discountType");
		session.removeAttribute("discountValue");
		//Update the discount value everytime the cart is updated by calling which ever method applicable to display the discounted value.
		renderContent = "N|"+"Display some error message which can be read from the properties";
	}
	session.setAttribute("isPromoCodeApplied", isPromoCodeApplied);
	return SUCCESS;
}
