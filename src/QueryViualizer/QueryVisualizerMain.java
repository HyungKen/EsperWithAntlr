package QueryViualizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import QueryOptimizer.Antlr;

public class QueryVisualizerMain {

	public static void main(String[] args) {
		Socket socket = null;
		ServerSocket listener = null;
		String epl = "";
		
		try {
			listener = new ServerSocket(8080);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Post Method
		try {
			while (true) {
				socket = listener.accept();
				System.out.println("socket port :" + socket.getLocalPort());

				try {
					System.out.println("Http Server started at 8080 port");
					InputStream is = socket.getInputStream();
					InputStreamReader isReader = new InputStreamReader(is);
					OutputStream os = socket.getOutputStream();
					PrintWriter pw = new PrintWriter(os);
					BufferedReader br = new BufferedReader(isReader);

					// code to read and print headers
					String headerLine = null;
					String header = "";

					while ((headerLine = br.readLine()).length() != 0) {
						header += headerLine + "\n";
					}

					System.out.println(header);
					
					header = header.substring(0, 3);
					//header瑜� 蹂닿퀬 get諛⑹떇�씤吏� post諛⑹떇�씤吏� �뙋蹂� 

					
					StringBuilder payload = new StringBuilder();
					
					//header媛� post諛⑹떇�씠�씪硫�
					if (header.equals("POS")) {
						while (br.ready()) {
							payload.append((char) br.read());
						}

						System.out.println("Payload data is: "+ payload.toString());

						epl = URLDecoder.decode(payload.toString(), "UTF-8");
						epl = epl.substring(4, epl.length());

						System.out.println("result : " + epl);
						pw.println("HTTP/1.0 200 OK");
						pw.println("Content-Type: text/html");
						pw.println("");
						
						Antlr ant = new Antlr();
						
						//epl = "select symbol from StockTickEvent(price + 30 > 60)";
						ant.setInputEPL(epl);
						ant.setOptimize();
						String optimizedepl  = ant.getOptimzedEPL();
						String eventclass = ant.getEventClass();
						String[] selectList = ant.getSelectList();
						
						pw.println("<html>"
								+ "<head>"
								+ "<meta charset=\"UTF-8\">"
								+ "<title>EPL Register</title>"
								+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css\">"
								+ "</head>"
								+ "<body>"

								+ "<div class=\"panel panel-primary\">"
								+ "<div class=\"panel-heading\">"
								+ "<h3 class=\"panel-title\"> EPL Register</h3>"

								+ "</div>"
								+ "<div class=\"panel-body\">"
								+ "<form name = \"join2\" method='POST'>"
								+ "<div class=\"form-group\">"
								+ "<label for=\"name\">Input Query</label>"
								+ "<input type=\"name\" class=\"form-control\" name=\"Input Query\" value=\""
								+ epl
								+ "\" id=\"Input Query\" >"

								+ "</div>"

								+ "<div class=\"form-group\">"
								+ "<label for=\"Optimized Query\">Optimized Query</label>"
								+ "<input type=\"name\" class=\"form-control\" name=\"Optimized Query\" value=\""
								+ optimizedepl
								+ "\" id=\"Optimized Query\" >"
								+ "</div>"
								+ "<button type=\"submit\" id=\"generate\" value =\"generate\" class=\"btn btn-primary\">Register</button>"
								+ "</form>"
								+ "</div>"
								+ "</div> <!--  End Well -->"
								+ "<script>"
								+ "var gsWin = window.open('about:blank','dataviewer','width=1000,height=1000');"
								+ "var frm = document.join2;"
								+ "frm.target = \"dataviewer2\"; frm.action = \"http://localhost:8081\";"
								+ "frm.submit();</script>"
								+ "</body>"
								+ "</html>");

						pw.flush();
						
						PrintData wbs = new PrintData(eventclass,selectList);
					}
				} finally {
					socket.close();
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				listener.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
