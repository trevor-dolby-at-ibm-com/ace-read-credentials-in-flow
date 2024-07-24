# ace-read-credentials-in-flow
Reading credentials from App Connect Enterprise flow nodes using ESQL and Java

# Application

The application uses a timer node to trigger the credentials read on server startup, and prints the contents
to the server console. While reading credentials from within a flow is not problematic, printing the contents
to the console is not a recommended practice for production. It does, however, allow for exploration of the ACE
credential reading APIs, and the output should look as follows:
```
2024-07-09 20:13:07.357     64 Java code found username |dummyUser2| password |dummyPassword2|
2024-07-09 20:13:07.359005: BIP8099I: ReadFlow_ReadCredsFromESQL: dummyUser2  -  dummyPassword2 
```

# Docs

This code is based on the Java APIs described in the [ACE docs](https://www.ibm.com/docs/en/app-connect/12.0?topic=java-accessing-credentials-from-user-code) to read the credentials for both ESQL and JavaCompute.

The credentials by default must be of type `userdefined` but this can be changed as long as the server
has been configured to allow other credentials to be read; see [Overrides](#overrides) below for details.

# Creds

Create the credentials using `mqsisetdbparms` similar to the following:
```
mqsisetdbparms -w c:\Users\684084897\ibm\ACET12\ace-read-credentials-in-flow-workspace\TEST_SERVER -n userdefined::demoAlias -u dummyUser -p dummyPassword
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
