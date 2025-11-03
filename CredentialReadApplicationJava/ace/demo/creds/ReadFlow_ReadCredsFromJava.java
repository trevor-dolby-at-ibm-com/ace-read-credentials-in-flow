package ace.demo.creds;

import java.util.Map;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbCredential;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbService;
import com.ibm.broker.plugin.MbUserException;
import com.ibm.broker.plugin.MbUtilities;

public class ReadFlow_ReadCredsFromJava extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
			// create new message as a copy of the input
			MbMessage outMessage = new MbMessage(inMessage);
			outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			// ----------------------------------------------------------
			// Add user code below
			
			String username = new String();
			char[] password = new char[0];
			char[] apiKey = new char[0];
			char[] clientId = new char[0];
			char[] clientSecret = new char[0];

			String credsType = (String)(getUserDefinedAttribute("credsType"));
			if ( ( credsType == null ) || ( credsType.isEmpty()) )
			{
				credsType = "userdefined";
			}
			else if ( !credsType.equals("userdefined") )
			{
				// The server may not be configured to allow non-userdefined creds
				String scyCredTypes = MbUtilities.getServerProperty("Credentials/userRetrievableCredentialTypes");
				if ( ( scyCredTypes == null ) || ( scyCredTypes.isEmpty() ) || ( scyCredTypes.equals("userdefined") ) )
				{
					System.out.println("\nCredentials/userRetrievableCredentialTypes not found or is the default - expect exceptions when accessing credentials other than userdefined\n");
				}
			}
			String alias = (String)(getUserDefinedAttribute("credsAlias"));
			
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
						username = new String(credentialProperties.get(MbCredential.USERNAME));
					if (credentialProperties.containsKey(MbCredential.PASSWORD))
						password = credentialProperties.get(MbCredential.PASSWORD);
					if (credentialProperties.containsKey(MbCredential.API_KEY))
						apiKey = credentialProperties.get(MbCredential.API_KEY);
					if (credentialProperties.containsKey(MbCredential.CLIENT_ID))
						clientId = credentialProperties.get(MbCredential.CLIENT_ID);
					if (credentialProperties.containsKey(MbCredential.CLIENT_SECRET))
						clientSecret = credentialProperties.get(MbCredential.CLIENT_SECRET);
					
					System.out.println("Java code found username |"+username+"| password |"+(new String(password))+
							"| apiKey |"+(new String(apiKey))+"| clientId |"+(new String(clientId))+"| clientSecret |"+(new String(clientSecret))+"|");
				}
			} catch (MbException e) {
				e.printStackTrace();
				throw new RuntimeException("Something went wrong during credential lookup", e);
			}
			
			//getUserDefinedAttribute("test");

			// End of user code
			// ----------------------------------------------------------
		} catch (MbException e) {
			// Re-throw to allow Broker handling of MbException
			throw e;
		} catch (RuntimeException e) {
			// Re-throw to allow Broker handling of RuntimeException
			throw e;
		} catch (Exception e) {
			// Consider replacing Exception with type(s) thrown by user code
			// Example handling ensures all exceptions are re-thrown to be handled in the flow
			throw new MbUserException(this, "evaluate()", "", "", e.toString(), null);
		}
		// The following should only be changed
		// if not propagating message to the 'out' terminal
		out.propagate(outAssembly);

	}

	/**
	 * onPreSetupValidation() is called during the construction of the node
	 * to allow the node configuration to be validated.  Updating the node
	 * configuration or connecting to external resources should be avoided.
	 *
	 * @throws MbException
	 */
	@Override
	public void onPreSetupValidation() throws MbException {
	}

	/**
	 * onSetup() is called during the start of the message flow allowing
	 * configuration to be read/cached, and endpoints to be registered.
	 *
	 * Calling getPolicy() within this method to retrieve a policy links this
	 * node to the policy. If the policy is subsequently redeployed the message
	 * flow will be torn down and reinitialized to it's state prior to the policy
	 * redeploy.
	 *
	 * @throws MbException
	 */
	@Override
	public void onSetup() throws MbException {
	}

	/**
	 * onStart() is called as the message flow is started. The thread pool for
	 * the message flow is running when this method is invoked.
	 *
	 * @throws MbException
	 */
	@Override
	public void onStart() throws MbException {
	}

	/**
	 * onStop() is called as the message flow is stopped. 
	 *
	 * The onStop method is called twice as a message flow is stopped. Initially
	 * with a 'wait' value of false and subsequently with a 'wait' value of true.
	 * Blocking operations should be avoided during the initial call. All thread
	 * pools and external connections should be stopped by the completion of the
	 * second call.
	 *
	 * @throws MbException
	 */
	@Override
	public void onStop(boolean wait) throws MbException {
	}

	/**
	 * onTearDown() is called to allow any cached data to be released and any
	 * endpoints to be deregistered.
	 *
	 * @throws MbException
	 */
	@Override
	public void onTearDown() throws MbException {
	}

}
