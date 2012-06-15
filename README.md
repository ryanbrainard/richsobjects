#Rich SObjects

A higher-level, modular library for interacting with the Salesforce API in web applications.
Compared to lower-level libraries that only provide literal translation of the API
to Java, Rich SObjects sits on top of these lower-level libraries to provide services
like:

 - `RichSObject` interface that combines sobject data and metadata into easy to use, traversable, iterable objects.
 - Chainable filters for streams of data and metadata.
   Instead of dealing with lists of records, treat them as a iterable stream that can be modularly filtered.
 - Choice of lower-level API providers.
   Rich SObjects provides a reference `richsobjects-api-jersey-client` implementation, but any provider can be used -- REST or SOAP.
 - Transparent session providing. Define how to provide session once and forget it.
 - Modular API caching layer. Cut down on unneeded API calls using the included `richsobjects-cache-simple` or supplying your own provider.

##Adding Rich SObjects to an App
This is a modular library and the various modules can added to your `pom.xml` as dependencies for desired functionality:

#### Core Library
This module contains core Rich SObjects library and is required for all projects.

        <dependency>
            <groupId>com.github.ryanbrainard</groupId>
            <artifactId>richsobjects-core</artifactId>
            <version>${richsobjects.version}</version>
        </dependency>

#### Low-Level SFDC Client APIs
These modules provide low-level SFDC API support to the core library.
One of the following client APIs must be included.
Alternatively, a custom or third-party `SfdcApiClientProvider` implementaion can be included.

        <dependency>
            <groupId>com.github.ryanbrainard</groupId>
            <artifactId>richsobjects-api-jersey-client</artifactId>
            <version>${richsobjects.version}</version>
        </dependency>

#### Caching Layers
These modules provide caching support between the core library and the provided low-level SFDC client API.
Caching is optional, but highly recommended as Rich SObjects requires many API calls for fetching metadata
that can be avoided with caching. The caching layers make a best attempt to invalidate cached data when
known changes are made to the data, but are suseptable to changes made directly in Salesforce or
by third-party applications. If signifigant changes are made to metadata and caches need to be invalidated
across a distributed system, all app servers can provide a new synchonized value for the envinronment
variable `RICH_SOBJECT_CACHE_KEY_PREFIX`.

Zero or more cache modules can be included. If no cache modules are included, caching will be disabled.
If more than one cache module is included, they will be wrapped around each other in the order they are
on the classpath (i.e. the order they are declare in your `pom.xml`), so slower, more persistant caches
should be declared first. At runtime, the service will make requests to the outer layers, which will
delegate to inner caching layers (and eventually to the low-level API client) if the given layer does
not contain a cached response. If the classpath order is not predicable, an explicit
`com.github.ryanbrainard.richsobjects.api.client.SfdcApiCacheProvider`  provider file can
also be supplied in `META-INF/services` to enforce the order of layers. Note, the order of layer
invocation (oppsite order of cache delegation) is logged on first use.

##### Memcahed Caching
A caching layer that attaches to Memcache. This is the best choice for distributed systems that can
share a common Memcache cluster. If Memcached is not configured correctly, this layer will be disabled.

        <dependency>
            <groupId>com.github.ryanbrainard</groupId>
            <artifactId>richsobjects-cache-memcached</artifactId>
            <version>${richsobjects.version}</version>
        </dependency>

In addition the following environment variables must to set to configure memcached:

 - `MEMCACHE_USERNAME`
 - `MEMCACHE_PASSWORD`
 - `MEMCACHE_SERVERS`

If running an application on Heroku, Memcache can be automatically provisioned and these variables set by executing:

    heroku plugins:add memcache

##### Simple In-Memory Caching
A simple in-memory, LRU caching layer. This can be used alone or in conjunction with other caching layers.
This is not recommended for use with distributed systems.

        <dependency>
            <groupId>com.github.ryanbrainard</groupId>
            <artifactId>richsobjects-cache-simple</artifactId>
            <version>${richsobjects.version}</version>
        </dependency>

#### Session Management
Implement `com.github.ryanbrainard.richsobjects.api.client.SfdcApiSessionProvider`
to tell RichSObjects how to get session info from your app. Then put the fully-qualified name of your class in a file named
`/META-INF/services/com.github.ryanbrainard.richsobjects.api.client.SfdcApiSessionProvider`

#### Logging
This library uses SLF4J for logging, which is disabled by default.
To enable logging, include of the SLF4J binding implementation of your choice in your application.

##Example Usage

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
            field.getMetadata().getLabel() + ": " + field.asAnyWithNameRef();
        }

        // Direct access to fields (with case-insensitive field names)
        record.getField("createdDate").getValue();                                               // raw value
        record.getField("createdDate").asAny();                                                  // implicitly converted to Java type
        record.getField("createdDate").asDate();                                                 // explicitly converted to Java type
        record.getField("accountId").asRef().getField("ownerId").getValue();                     // traverse relationships and data is fetched automatically
        record.getField("accountId").asRef().getField("ownerId").getMetadata().isUpdateable();   // metadata is always accessible where you need it, even across relationships
    }

    // Setting field values
    final RichSObject fetchedRecord = service.fetch(account, id);                           // immutable and does not change below
    final RichSObject updatedRecord = fetchedRecord.getField("Name").setValue("NEW VALUE"); // returns a new immutable copy of the record
    final RichSObject savedRecord   = service.update(updatedRecord);                        // sends update to API and gets a fresh copy of the record

##Hacking
If you'd like to contribute to this project, fork it and send me a pull request with tests.