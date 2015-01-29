public SomeModel getCouponCounter(String buyingCompany, String couponCode)
	  {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = "SELECT (SELECT COUNT(*) FROM COUPON_USE WHERE BUYING_COMPANY_ID = ? AND COUPON_CODE  = ? )AS COUNTER, C.COUPON_RULE_TYPE,C.NUMBER_OF_USE_PER_CUSTOMER,C.COUPON_IMAGE FROM COUPONS C WHERE C.COUPON_CODE  = ?";
	    //Get the number of coupons used with respect to that particular coupon and warehouse
	    SomeModel couponInfo = null;
	    try
	    {
	      conn = ConnectionManager.getDBConnection();
	   
	      pstmt = conn.prepareStatement(sql);
	      pstmt.setString(1, buyingCompany);
	      pstmt.setString(2, couponCode);
	      pstmt.setString(3, couponCode);
	      rs = pstmt.executeQuery();

	      while (rs.next())
	      {
	        couponInfo = new SomeModel();
	        couponInfo.setCouponCounter(rs.getInt("COUNTER"));
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
	    return couponInfo;
	  }
