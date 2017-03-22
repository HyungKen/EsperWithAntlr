package com.espertech.esper.epl.dataclass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaxiEntityClass {
	private String medallion;
	private String hack_license;
	private Date pickup_datetime;
	private Date dropoff_datetime;
	private String trip_time_in_secs;
	private double trip_distance;
	private double pickup_longitude;
	private double pickup_latitude;
	private double dropoff_longitude;
	private double dropoff_latitude;
	private String payment_type;
	private String fare_amount;
	private String surcharge;
	private String mta_tax;
	private String tip_amount;
	private String tolls_amount;
	private double total_amount;
	private int pickup_longitude_cell;
	private int pickup_latitude_cell;
	private int dropoff_longitude_cell;
	private int dropoff_latitude_cell;
	
	private long timestamp;

	public TaxiEntityClass(String[] list) throws ParseException {
		setMedallion(list[0]);
		setHack_license(list[1]);
		setPickup_datetime(list[2]);
		setDropoff_datetime(list[3]);
		setTrip_time_in_secs(list[4]);
		setTrip_distance(list[5]);
		setPickup_longitude(list[6]);
		setPickup_latitude(list[7]);
		setDropoff_longitude(list[8]);
		setDropoff_latitude(list[9]);
		setPayment_type(list[10]);
		setFare_amount(list[11]);
		setSurcharge(list[12]);
		setMta_tax(list[13]);
		setTip_amount(list[14]);
		setTolls_amount(list[15]);
		setTotal_amount(list[16]);
		
		
		setTimestamp(dropoff_datetime.getTime());
		setPickup_longitude_cell(getPickup_longitude());
		setPickup_latitude_cell(getPickup_latitude());
		setDropoff_longitude_cell(getDropoff_longitude());
		setDropoff_latitude_cell(getDropoff_latitude());
	}
	

	  public int getEntityPosition() {
	      return 0;
	   }
	 

	   public String getMedallion() {
	      return medallion;
	   }

	   public void setMedallion(String medallion) {
	      this.medallion = medallion;
	   }

	   public String getHack_license() {
	      return hack_license;
	   }

	   public void setHack_license(String hack_license) {
	      this.hack_license = hack_license;
	   }

	   public Date getPickup_datetime() {
	      return pickup_datetime;
	   }

	   public void setPickup_datetime(String pickup_datetime) throws ParseException {
	      SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	      Date to = transFormat.parse(pickup_datetime);
	      this.pickup_datetime = to;
	   }

	   public Date getDropoff_datetime() {
	      return dropoff_datetime;
	   }

	   public void setDropoff_datetime(String dropoff_datetime) throws ParseException{
	      SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	      Date time = transFormat.parse(dropoff_datetime);
	      this.dropoff_datetime = time;
	   }

	   public String getTrip_time_in_secs() {
	      return trip_time_in_secs;
	   }

	   public void setTrip_time_in_secs(String trip_time_in_secs) {
	      this.trip_time_in_secs = trip_time_in_secs;
	   }
	   
	   
	   public double getTrip_distance() {
	      return trip_distance;
	   }

	   public void setTrip_distance(String trip_distance) {
	      double to = Double.parseDouble(trip_distance);
	      this.trip_distance = to;
	   }

	   public double getPickup_longitude() {
	      return pickup_longitude;
	   }

	   public void setPickup_longitude(String pickup_longitude) {
	      
	      this.pickup_longitude = Double.parseDouble(pickup_longitude);
	   }

	   public double getPickup_latitude() {
	      return pickup_latitude;
	   }

	   public void setPickup_latitude(String pickup_latitude) {
	      this.pickup_latitude = Double.parseDouble(pickup_latitude);
	   }

	   public Double getDropoff_longitude() {
	      return dropoff_longitude;
	   }

	   public void setDropoff_longitude(String dropoff_longitude) {
	      this.dropoff_longitude = Double.parseDouble(dropoff_longitude);
	   }

	   public Double getDropoff_latitude() {
	      return dropoff_latitude;
	   }

	   public void setDropoff_latitude(String dropoff_latitude) {
	      this.dropoff_latitude = Double.parseDouble(dropoff_latitude);
	   }

	   public String getPayment_type() {
	      return payment_type;
	   }

	   public void setPayment_type(String payment_type) {
	      this.payment_type = payment_type;
	   }

	   public String getFare_amount() {
	      return fare_amount;
	   }

	   public void setFare_amount(String fare_amount) {
	      this.fare_amount = fare_amount;
	   }

	   public String getSurcharge() {
	      return surcharge;
	   }

	   public void setSurcharge(String surcharge) {
	      this.surcharge = surcharge;
	   }

	   public String getMta_tax() {
	      return mta_tax;
	   }

	   public void setMta_tax(String mta_tax) {
	      this.mta_tax = mta_tax;
	   }

	   public String getTip_amount() {
	      return tip_amount;
	   }

	   public void setTip_amount(String tip_amount) {
	      this.tip_amount = tip_amount;
	   }

	   public String getTolls_amount() {
	      return tolls_amount;
	   }

	   public void setTolls_amount(String tolls_amount) {
	      this.tolls_amount = tolls_amount;
	   }

	   public double getTotal_amount() {
	      return total_amount;
	   }

	   public void setTotal_amount(String total_amount) {
	      double to = Double.parseDouble(total_amount);
	      this.total_amount = to;
	   }
	   
	   public void setdropoff_longitude(String dropoff_longitude) throws ParseException {
	      
	      double to = Double.parseDouble(dropoff_longitude);
	      this.dropoff_longitude = to;
	   }

	   public long getTimestamp() {
	      return timestamp;
	   }

	   public void setTimestamp(long timestamp) {
	      this.timestamp = timestamp;
	   }
	   
	   
	   public int getPickup_longitude_cell()
	   {
		   return pickup_longitude_cell;
	   }
	   
	   public void setPickup_longitude_cell(double pickup_longitude)
	   {
		   if(pickup_longitude > -74.913585)
		   {
			   double longitude_meter = calDistance(0,pickup_longitude,0,-74.913585);
			   int a = (int)(longitude_meter / 500);
			   if(a<300)
				   this.pickup_longitude_cell = a+1;
			   else
				   this.pickup_longitude_cell = -1;
			 
		   }
		   else
			   this.pickup_longitude_cell = -1;
	   }
	   
	   public int getPickup_latitude_cell()
	   {
		   return pickup_latitude_cell;
	   }
	   
	   public void setPickup_latitude_cell(double pickup_latitude)
	   {
		   if(pickup_latitude< 41.474937)
		   {
			   double latitude_meter = calDistance(pickup_latitude,0,41.474937,0);
			   int a = (int)(latitude_meter / 500);
			   
			   if(a<300)
				   this.pickup_latitude_cell = a+1;
			   else
				   this.pickup_latitude_cell = -1;
		   }
		   else
			   this.pickup_latitude_cell = -1;
	   }
	   
	   public int getDropoff_longitude_cell()
	   {
		   return dropoff_longitude_cell;
	   }
	   
	   public void setDropoff_longitude_cell(double pickup_longitude)
	   {
		   if(dropoff_longitude > -74.913585)
		   {
			   double longitude_meter = calDistance(0,dropoff_longitude,0,-74.913585);
			   int a = (int)(longitude_meter / 500);
			  
			   if(a<300)
				   this.dropoff_longitude_cell = a+1;
			   else
				   this.dropoff_longitude_cell = -1;
		   }
		   else
			   this.dropoff_longitude_cell = -1;
	   }
	   
	   public int getDropoff_latitude_cell()
	   {
		   return dropoff_latitude_cell;
	   }
	   
	   public void setDropoff_latitude_cell(double dropoff_latitude)
	   {
		   if(dropoff_latitude< 41.474937)
		   {
			   double latitude_meter = calDistance(dropoff_latitude,0,41.474937,0);
			   int a = (int)(latitude_meter / 500);
			  
			   if(a<300)
				   this.dropoff_latitude_cell = a+1;
			   else
				   this.dropoff_latitude_cell = -1;
		   }
		   else
			   this.dropoff_latitude_cell = -1;
	   }
	   
	   
	   public double calDistance(double lat1, double lon1, double lat2, double lon2){  
		    
		    double theta, dist;  
		    theta = lon1 - lon2;  
		    dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))   
		          * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));  
		    dist = Math.acos(dist);  
		    dist = rad2deg(dist);  
		      
		    dist = dist * 60 * 1.1515;   
		    dist = dist * 1.609344;    // 단위 mile 에서 km 변환.  
		    dist = dist * 1000.0;      // 단위  km 에서 m 로 변환  
		  
		    return dist;  
		}  
		  
		    // 주어진 도(degree) 값을 라디언으로 변환  
		private double deg2rad(double deg){  
		    return (double)(deg * Math.PI / (double)180d);  
		}  
		  
		    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환  
		private double rad2deg(double rad){  
		    return (double)(rad * (double)180d / Math.PI);  
		} 



}
