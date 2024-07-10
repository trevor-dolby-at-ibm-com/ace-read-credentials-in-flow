package ace.demo.creds;

import java.util.Map;

import com.ibm.broker.plugin.MbCredential;
import com.ibm.broker.plugin.MbException;

public class ReadCredsForESQL
{
	public static Boolean getUsernameAndPassword(String alias, String [] username, String [] password)
	{
		char[] passwordChars = new char[0];

		MbCredential myCred;
		try {
			myCred = MbCredential.getCredential("userdefined", alias);
			if (myCred == null) {
				System.out.println("Could not find credential "+alias);
			}
			else
			{
				Map<String, char[]> credentialProperties = myCred.reloadAndRetrieveProperties();
				
				if (credentialProperties.containsKey(MbCredential.USERNAME))
					username[0] = new String(credentialProperties.get(MbCredential.USERNAME));
				if (credentialProperties.containsKey(MbCredential.PASSWORD))
				{
					passwordChars = credentialProperties.get(MbCredential.PASSWORD);
					password[0] = new String(passwordChars);
				}
			}
		} catch (MbException e) {
			e.printStackTrace();
			return new Boolean(false);
		}
		return new Boolean(true);
	}
}
