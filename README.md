#Rich SObjects

A higher-level, modular library for interacting with the Salesforce API in web applications.
Compared to lower-level libraries that only provide literal translation of the API
to Java, Rich SObjects sits on top of these lower-level libraries to provide services
like:

 - `RichSObject` interface that combines sobject data and metadata into easy to use, traversable, iterable objects.
 - Chainable filters for streams of data and metadata.
   Instead of dealing with lists of records, treat them as a iterable stream that can be modularly filtered.
 - Choice of lower-level API providers.
   Rich SObjects provides a reference `richsobjects-jersey-client` implementation, but any provider can be used -- REST or SOAP.
 - Transparent session providing. Define how to provide session once and forget it.
 - Modular API caching layer. Cut down on unneeded API calls using the included `richsobjects-simple-cache` or supplying your own provider.

##Adding Rich SObjects to an App

Add to your `pom.xml`

        <!-- REQUIRED: Core Libraries -->
        <dependency>
            <groupId>com.github.ryanbrainard</groupId>
            <artifactId>richsobjects-core</artifactId>
            <version>${richsobjects.version}</version>
        </dependency>

        <!-- OPTIONAL: Low-Level API Provider (if this is not used, a different implemention must be supplied) -->
        <dependency>
            <groupId>com.github.ryanbrainard</groupId>
            <artifactId>richsobjects-jersey-client</artifactId>
            <version>${richsobjects.version}</version>
        </dependency>

        <!-- OPTIONAL: Caching Layer (if this not used, API calls will not be cached) -->
        <dependency>
            <groupId>com.github.ryanbrainard</groupId>
            <artifactId>richsobjects-simple-cache</artifactId>
            <version>${richsobjects.version}</version>
        </dependency>

Implement `com.github.ryanbrainard.richsobjects.api.client.SfdcApiSessionProvider`
to tell RichSObjects how to get session info from your app. Then put the fully-qualified name of your class in a file named
`/META-INF/services/com.github.ryanbrainard.richsobjects.api.client.SfdcApiSessionProvider`

Example usage:

    // Initialize the service
    RichSObjectsService service = new RichSObjectsServiceImpl();

    // Query
    Iterator<RichSObject> records = service.query("SELECT Name, CreatedDate, AccountId FROM Contact WHERE AccountId != null");

    // Iterate over the records (fetching additional batches is handled for you)
    while (records.hasNext()) {
        RichSObject record = records.next();

        // Iterate over the fields. Add a nestable filter to narrow down your view
        Iterator<RichSObject.RichField> fields = new PopulatedFieldsOnly(record.getFields());
        while (fields.hasNext()) {
            RichSObject.RichField field = fields.next();

            // Easy access to metadata and data together. Referenced object name resolution with asAnyWithNameRef()
            System.out.println(field.getMetadata().getLabel() + ": " + field.asAnyWithNameRef());
        }

        // Direct access to fields (with case-insensitive field names)
        System.out.println(record.get("createdDate").getValue());                                          // raw value
        System.out.println(record.get("createdDate").asAny());                                             // implicitly converted to Java type
        System.out.println(record.get("createdDate").asDate());                                            // explicitly converted to Java type
        System.out.println(record.get("accountId").asRef().get("ownerId").getValue());                     // traverse relationships and data is fetched automatically
        System.out.println(record.get("accountId").asRef().get("ownerId").getMetadata().isUpdateable());   // metadata is always accessible where you need it, even across relationships
    }

##Hacking
If you'd like to contribute to this project, fork it and send me a pull request with tests.