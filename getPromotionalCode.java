public SomeModel getPromotionalCode(SomeModel promo) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		SomeModel  promotion = new SomeModel();
		String couponType = ""; //Type of coupon required goes here
	    try
	    {
	      conn = ConnectionManager.getDBConnection();
	      String sql = "SELECT C.COUPON_ID,C.COUPON_RULE_ID,C.COUPON_RULE_TYPE,CRT.DISCOUNT_TYPE,CRT.DISCOUNT_VALUE,C.NUMBER_OF_USE_PER_CUSTOMER FROM COUPONS C, WAREHOUSE_COUPONS WC, COUPON_RULE_TABLE CRT WHERE C.COUPON_ID = WC.COUPON_ID AND CRT.COUPON_ID = C.COUPON_ID AND WC.WAREHOUSE_ID IN ( SELECT WAREHOUSE_ID FROM WAREHOUSE WHERE WAREHOUSE_CODE = ?  ) AND C.COUPON_CODE =? AND TO_DATE(TO_CHAR(SYSDATE,'DD-MM-YY'),'DD-MM-YY') >= TO_DATE(TO_CHAR(C.COUPON_START_DATE,'DD-MM-YY'),'DD-MM-YY') AND TO_DATE(TO_CHAR(SYSDATE,'DD-MM-YY'),'DD-MM-YY') <= TO_DATE(TO_CHAR(C.COUPON_END_DATE,'DD-MM-YY'),'DD-MM-YY') AND CRT.MIN_ORDER_TOTAL  <=? AND CRT.MAX_ORDER_TOTAL  >= ?"; //Query to get promotional code from DB goes here
	      sql = sql.replace("COUPON_RULE_TABLE", couponType); 
	      //The start date, end date, minimum and maximum cart total are validated in the SQL query only
	      pstmt = conn.prepareStatement(sql);
	      pstmt.setString(1, promo.getWareHouseCode());
	      pstmt.setString(2, promo.getPromoCode());
	      pstmt.setDouble(3, promo.getCartTotal());
	      pstmt.setDouble(4, promo.getCartTotal());
	      rs = pstmt.executeQuery();
	      while (rs.next())
	      {
         	promotion = new SomeModel();
	        promotion.setDiscountType(rs.getString("DISCOUNT_TYPE"));
	        promotion.setDiscountValue(rs.getString("DISCOUNT_VALUE"));
	        promotion.setCouponCounter(rs.getInt("NUMBER_OF_USE_PER_CUSTOMER")); //Number of coupons assigned per customer
	      }

	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    finally
	    {
	    	ConnectionManager.closeDBConnection(conn);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);
	    	ConnectionManager.closeDBResultSet(rs);
	    }

	    return promotion;
	}
