# ace-read-credentials-in-flow
Reading credentials from App Connect Enterprise flow nodes using ESQL and Java

ACE servers load credentials into memory so they can be used by running applications and flows after being loaded into the server's in-memory store. The storage of the credentials on disk is in an ACE format, and managed by ACE. 

![ace-credentials-1](/files/ace-credentials-1.png)

This picture can be augmented by loading credentials from a script run by the server at startup time, and 
credentials loaded this way can also be read by the code in this repo.

# Application

The application uses a timer node to trigger the credentials read on server startup, and prints the contents
to the server console. While reading credentials from within a flow is not problematic, printing the contents
to the console is not a recommended practice for production. It does, however, allow for exploration of the ACE
credential reading APIs, and the output should look as follows:
```
2025-11-03 10:23:51.685     65 Java code found username |demoUser| password |demoPwd| apiKey |demoApiKey| clientId |demoId| clientSecret |demoSec|
2025-11-03 10:23:51.687508: BIP8099I: ReadFlow_ReadCredsFromESQL: demoAlias  -  USERNAME: demoUser PASSWORD: demoPwd APIKEY: demoApiKey CLIENTID: demoId CLIENTSECRET: demoSec
```

The flow uses a JavaCompute node to read the credentials directly (the first log line) and uses 
ESQL calling Java to print the second line:

![ReadFlow](/files/ReadFlow.png)

Not all credential elements (username, apiKey, etc) need to be present, with blanks being the default
if an element is missing.

# Docs

This code is based on the Java APIs described in the [ACE docs](https://www.ibm.com/docs/en/app-connect/13.0?topic=java-accessing-credentials-from-user-code) to read the credentials for both ESQL and JavaCompute.

The credentials by default must be of type `userdefined` but this can be changed as long as the server
has been configured to allow other credentials to be read; see [Overrides](#overrides) below for details.

# Creds

Create the credentials using `mqsisetdbparms` similar to the following:
```
mqsisetdbparms -w c:\Users\684084897\ibm\ACET12\ace-read-credentials-in-flow-workspace\TEST_SERVER -n userdefined::demoAlias -u dummyUser -p dummyPassword
```
or to set all fields:
```
mqsisetdbparms  -w /tmp/test-work-dir -n userdefined::demoAlias -u demoUser -p demoPd -k demoApiKey -c demoId -s demoSec
```

The credentials can also be provided using `mqsicredentials`, server.conf.yaml, or a credentials script.

# Overrides

The alias to be read defaults to `demoAlias` but can be changed by setting the `credsAlias` flow-level
user-defined property, either using the BAR editor or `mqsiapplybaroverride`.

Credentials of types other than `userdefined` can also be read as long as the server.conf.yaml settings
allow it; see the [ACE docs](https://www.ibm.com/docs/en/app-connect/12.0?topic=java-accessing-credentials-from-user-code)
for details, with an example server.conf.yaml setting being
```
Credentials:
  userRetrievableCredentialTypes: 'ALL'
```
to allow all credential types to be read.

To instruct the code in this repo to read a type other than `userdefined`, set the `credsType` flow-level
user-defined property, either using the BAR editor or `mqsiapplybaroverride`.
