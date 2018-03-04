import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.*;
public class PredParse {
	public static String streamerURL = "https://overrustlelogs.net/Keemstar%20chatlog/December%202017/userlogs/";
	public static void main(String[] args) throws IOException {

		// builds string from html
		StringBuilder inputBuilder = new StringBuilder();
		// where html will be stored as a string
		String pageText = null;
		// takes in html line by line to add to inputBuilder
		String line = null;
		
		// Build URL
		String theURLString = streamerURL;
		URL theLink = new URL(theURLString);

		// connect to URL and make html and inputStream called "is" send is to
		// bufferedReader
		BufferedReader bufferedReader = null;
		try {
			URLConnection conn = theLink.openConnection();
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			conn.connect();
			InputStream is = conn.getInputStream();
			bufferedReader = new BufferedReader((new InputStreamReader(is)));
		} catch (FileNotFoundException e1) {
			// e1.printStackTrace();
		}

		// convert the bufferedReader lines to a string "pageText"
		try {
			inputBuilder = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				inputBuilder.append(line);
			}
			bufferedReader.close();
			pageText = inputBuilder.toString();
		} catch (IOException e) {
			// e.printStackTrace();
		}

		// System.out.println(pageText);

		// regex time
		Pattern pUserName = Pattern.compile("</i> (.{1,50}).txt");
		Matcher mUserName = pUserName.matcher(pageText);

		String name;
		int count = 0;
		while (mUserName.find() && count < 1000) {
			count++;
			name = mUserName.group(1);
			System.out.print(name + "\t");
				
			//find text chat log for user
			System.out.println(chatlog(name));
		}
	}
	//https://overrustlelogs.net/Brownman%20chatlog/December%202017/userlogs/
	//https://overrustlelogs.net/Kittyplays chatlog/December 2017/userlogs/
	
	//returns the chat log for a specific username
	public static String chatlog(String textfile) throws IOException {
		String theURLString = streamerURL + textfile + ".txt";
		URL theLink = new URL(theURLString);

		// connect to URL and make html and inputStream called "is" send is to
		// bufferedReader
		BufferedReader bufferedReader = null;
		try {
			URLConnection conn = theLink.openConnection();
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			conn.connect();
			InputStream is = conn.getInputStream();
			bufferedReader = new BufferedReader((new InputStreamReader(is)));
		} catch (FileNotFoundException e1) {
			// e1.printStackTrace();
		}
		String pageText = null;
		// convert the bufferedReader lines to a string "pageText"
		try {
			StringBuilder inputBuilder = new StringBuilder();
			String line;
			int messages = 0;
			while ((line = bufferedReader.readLine()) != null && messages < 50) {
				inputBuilder.append(line + "\n");
				messages++;
			}
			bufferedReader.close();
			//add new line to end to help regex
			//inputBuilder.append("\n");
			pageText = inputBuilder.toString();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		String regex = "17-12-([0-9][0-9]).{1,15} "+textfile+": (.*)\\n";
		Pattern pmsg = Pattern.compile(regex);
		Matcher msg = pmsg.matcher(pageText);
		
		StringBuilder sb2 = new StringBuilder();
		String day = "";
		while(msg.find()) {	
			//if it's the first time or day didn't change, just append
			if (day.equals("") || day.equals(msg.group(1))) {
				sb2.append(msg.group(2) + ". ");
			}
			//else reprint name and append to new line
			else {
				sb2.append("\n" + textfile + "\t" + msg.group(2) + ". ");
			}
			day=msg.group(1);
			//sb2.append("--"+day+"--");	
		}
		
		
		return(sb2.toString());
	}
}
