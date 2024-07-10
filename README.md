# ace-read-credentials-in-flow
Reading credentials from App Connect Enterprise flow nodes using ESQL and Java


# Application

The application uses a timer node to trigger the credentials read on server startup, and prints the contents
to the server console. This is not a recommended practice for production, but does allow for exploration of 
the code, and the output should look as follows:
```
2024-07-09 20:13:07.357     64 Java code found username |dummyUser2| password |dummyPassword2|
2024-07-09 20:13:07.359005: BIP8099I: ReadFlow_ReadCredsFromESQL: dummyUser2  -  dummyPassword2 
```

# Docs

Both ESQL and JavaCompute use the Java APIs described in the [ACE docs](https://www.ibm.com/docs/en/app-connect/12.0?topic=java-accessing-credentials-from-user-code) to read the credentials, which must be of type `userdefined`.

# Creds

Create the credentials using `mqsisetdbparms` similar to the following:
```
mqsisetdbparms -w c:\Users\684084897\ibm\ACET12\ace-read-credentials-in-flow-workspace\TEST_SERVER -n userdefined::demoAlias -u dummyUser -p dummyPassword
```
The credentials can also be provided using `mqsicredentials`, server.conf.yaml, or a credentials script.

# Overrides

The alias to be read defaults to `demoAlias` but can be changed by setting the `credsAlias` flow-level
user-defined property, either using the BAR editor or `mqsiapplybaroverride`.