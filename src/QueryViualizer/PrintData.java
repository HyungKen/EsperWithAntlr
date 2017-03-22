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
import java.text.ParseException;
import QueryOptimizer.Antlr;
import QueryOptimizer.EsperModule;


public class PrintData {
	private String epl = "";
	private Socket socket = null;

	public PrintData(String eventclass,String[] selectList ) {
		ServerSocket listener = null;
		try {
			listener = new ServerSocket(8081);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Post Method
		try {
			while (true) {
				socket = listener.accept();
				try {
					System.out.println("Http Server started at 8081 port");
					InputStream is = socket.getInputStream();
					InputStreamReader isReader = new InputStreamReader(is);
					OutputStream os = socket.getOutputStream();
					PrintWriter pw = new PrintWriter(os);
					BufferedReader br = new BufferedReader(isReader);

					// code to read and print headerss
					String headerLine = null;
					String header = "";
					while ((headerLine = br.readLine()).length() != 0) {
						header += headerLine + "\n";
					}

					System.out.println(header);
					header = header.substring(0, 3);

					// code to read the post payload data
					StringBuilder payload = new StringBuilder();
					if (header.equals("POS")) {
						while (br.ready()) {
							payload.append((char) br.read());
						}

						System.out.println("Payload data is: " + payload.toString());

						String[] decode = URLDecoder.decode(payload.toString(), "UTF-8").split("=");
						epl = decode[2];

						pw.println("HTTP/1.0 200 OK");
						pw.println("Content-Type: text/html");
						pw.println("");

						pw.println("<html>"
								+ "<head>"
								+ "<meta charset=\"UTF-8\">"
								+ "<title>Data Proccesing</title>"
								+ "<meta name=\"viewport\" content\"width=device-width, initial-scale=1\">"
								+ "<link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">"
								+ "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js\"></script>"
								+ "<script src=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>"			
								+ "</head>");

						pw.println("<body><div class=\"container\"> <TABLE class=\"table table-bordered\">");
						pw.print("<thead>");

						
						pw.println("<TR>");

						for (int i = 0; i < selectList.length; i++) {
							pw.println("<TH>" + selectList[i] + "</TH>");
						}
						pw.println("</TR></thead><tbody>");
						pw.flush();
						
						EsperModule re = new EsperModule(epl, pw, selectList,eventclass);
					}

					//pw.println("</tbody></TABLE></div>");

					//pw.println("</body></html>");

					pw.flush();

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

	public void exit() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};

	public String getEPL() {
		return epl;
	}
}
