package ace.demo.creds;

import java.util.Map;

import com.ibm.broker.plugin.MbCredential;
import com.ibm.broker.plugin.MbException;

public class ReadCredsForESQL
{
	public static Boolean getCredentials(String credsType, String alias, String [] username, String [] password, String [] apiKey, String [] clientId, String [] clientSecret)
	{
		char[] passwordChars = new char[0];
		char[] apiKeyChars = new char[0];
		char[] clientIdChars = new char[0];
		char[] clientSecretChars = new char[0];
		
		// Set default to blank
		username[0] = new String();
		password[0] = new String();
		apiKey[0] = new String();
		clientId[0] = new String();
		clientSecret[0] = new String();
				
		MbCredential myCred;
		try {
			myCred = MbCredential.getCredential(credsType, alias);
			if (myCred == null) {
				System.out.println("Could not find credential "+credsType+"::"+alias);
			}
			else
			{
				Map<String, char[]> credentialProperties = myCred.reloadAndRetrieveProperties();
				
				if (credentialProperties.containsKey(MbCredential.USERNAME))
				{
					username[0] = new String(credentialProperties.get(MbCredential.USERNAME));
				}
				if (credentialProperties.containsKey(MbCredential.PASSWORD))
				{
					passwordChars = credentialProperties.get(MbCredential.PASSWORD);
					password[0] = new String(passwordChars);
				}
				if (credentialProperties.containsKey(MbCredential.API_KEY))
				{
					apiKeyChars = credentialProperties.get(MbCredential.API_KEY);
					apiKey[0] = new String(apiKeyChars);
				}
				if (credentialProperties.containsKey(MbCredential.CLIENT_ID))
				{
					clientIdChars = credentialProperties.get(MbCredential.CLIENT_ID);
					clientId[0] = new String(clientIdChars);
				}
				if (credentialProperties.containsKey(MbCredential.CLIENT_SECRET))
				{
					clientSecretChars = credentialProperties.get(MbCredential.CLIENT_SECRET);
					clientSecret[0] = new String(clientSecretChars);
				}

			}
		} catch (MbException e) {
			e.printStackTrace();
			return new Boolean(false);
		}
		return new Boolean(true);
	}
}
