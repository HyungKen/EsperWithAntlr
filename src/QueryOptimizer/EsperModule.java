package QueryOptimizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.time.Instant;
import java.util.Random;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.epl.dataclass.FruitEvent;
import com.espertech.esper.epl.dataclass.TaxiEntityClass;
import com.opencsv.CSVReader;


public class EsperModule {
		static String filename = "sorted_data.csv";
	
	   public EsperModule(String eplresult,PrintWriter pw,String[] selectList,String eventclass) throws  ParseException,IOException{
		      String[] nextLine;
		      //csvReader
		      CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filename)));
		      
		      Configuration config = new Configuration();
		      config.addEventTypeAutoName("com.espertech.esper.epl.dataclass");
		     
		      //Creating a Statement
		      EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);
		      EPStatement statement = epService.getEPAdministrator().createEPL(eplresult);
		      EventListener listener = new EventListener(pw,selectList);
		      statement.addListener(listener);
		      
		      if(eventclass.equals("FruitEvent")){
			      while(true){
						Random rand = new Random();
						int rFactor = rand.nextInt(100000)/100 + 100;
						int fruitnum = rand.nextInt(3) + 1;
						Instant timestamp;
						timestamp = Instant.now();
						String fruitname;
						
						if(fruitnum == 1)
							fruitname = "apple";
						else if(fruitnum == 2)
							fruitname = "orange";
						else
							fruitname = "cheery";
						
						FruitEvent event = new FruitEvent(fruitname,rFactor, timestamp);
						epService.getEPRuntime().sendEvent(event);
						
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			      }
		      }else{
			      while ((nextLine = reader.readNext()) != null) {
				         try {
				        	 TaxiEntityClass taxiEntity = new TaxiEntityClass(nextLine);
				        	 
				            epService.getEPRuntime().sendEvent(taxiEntity);
				            
				         }catch (Exception e) {
				             e.printStackTrace();
				         }   
				      }
		      }
	}
}
