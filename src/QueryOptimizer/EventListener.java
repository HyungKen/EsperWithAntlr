package QueryOptimizer;

import java.io.PrintWriter;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.PropertyAccessException;
import com.espertech.esper.client.UpdateListener;

public class EventListener implements UpdateListener {

	PrintWriter pw = null;
	private String[] selectList;
	
	public EventListener(PrintWriter pw,String[] selectList) {
		this.pw = pw;
		this.selectList = new String[selectList.length];
		for(int i=0;i<selectList.length;i++){
			this.selectList[i] = selectList[i];
		}
	}

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		System.out.println("±æÀÌ :" + newEvents.length);
		try {
			for (int i = 0; i < newEvents.length; i++) {
				pw.println("<TR ALIGN=LEFT>");
				for(int j=0;j<selectList.length;j++){
					pw.println("<TD>");
					pw.print((newEvents[i].get(selectList[j]).toString()));
					pw.print("</TD>");
					pw.flush();
				}
				Thread t = new Thread();
				t.sleep(300);
				pw.print("</TR>");

			}
			pw.flush();

	
		} catch (PropertyAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 	
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (oldEvents != null)
			System.out.println(oldEvents.length);

	}
}
