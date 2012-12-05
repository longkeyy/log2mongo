import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class LogToMongo {
	
	private final static String fieldNames = "id, facility, severity,timestamp, hostname, msg";
	private final static String[] Facility = { "kernel messages ",
			"user-level messages ", "mail system ", "system daemons ",
			"security/authorization messages ",
			"messages generated internally by syslogd ",
			"line printer subsystem ", "network news subsystem ",
			"UUCP subsystem ", "clock daemon ",
			"security/authorization messages ", "FTP daemon ",
			"NTP subsystem ", "log audit ", "log alert ", "clock daemon ",
			"local0", "local1", "local2", "local3", "local4", "local5",
			"local6", "local7" };
	private final static String[] Severity = { "Emergency", "Alert",
			"Critical", "Error", "Warning", "Notice", "Informational", "Debug" };

	private static void addDocs(String fieldNames, String[] value){
		
	}

	private static String[] docFilterSyslog(String fieldNames, String s) {

		String[] fieldName = fieldNames.split(",");
		int fieldNameCount = fieldName.length;
		Pattern p = Pattern.compile("<(.+)>\\d");
		String[] v = s.split(" ", 9);
		// String[] vv = { "id", "facility", "severity", "timestamp",
		// "hostname","msg" };
		String[] Value = new String[fieldNameCount];
		int l;
		// System.out.println(v.length);
		if (v.length < 9) {
			return Value;
		}

		Value[0] = Long.toString(System.currentTimeMillis()
				+ (new Random()).nextInt(1000));

		Value[3] = Long.toString(System.currentTimeMillis());
		Value[4] = v[2];
		Value[5] = v[8];

		Matcher matcher = p.matcher(v[0]);
		if (matcher.find()) {
			l = new Integer(matcher.group(1));
			Value[1] = Severity[l % 8];
			Value[2] = Facility[l / 8];
		}
		/*
		 * for (int i = 0; i < fieldName.length; i++) {
		 * System.out.println("key:" + fieldName[i] + "->value: " + vv[i]); }
		 */
		return Value;
	}

	public static void main(String[] args) throws MalformedURLException {

		String s = "";
		String[] doc;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			while ((s = br.readLine()) != null) {
				doc = docFilterSyslog(fieldNames, s);
				if (doc[0] != null) {
					
				}
				// es.submit(new LogInsert(s));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}