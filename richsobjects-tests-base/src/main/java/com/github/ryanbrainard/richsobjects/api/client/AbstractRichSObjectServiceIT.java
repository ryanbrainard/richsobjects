package com.github.ryanbrainard.richsobjects.api.client;

import com.github.ryanbrainard.richsobjects.RichSObject;
import com.github.ryanbrainard.richsobjects.RichSObjectsService;
import com.github.ryanbrainard.richsobjects.RichSObjectsServiceImpl;
import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectDescription;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;
import org.joda.time.DateTime;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

/**
 * @author Ryan Brainard
 */
public abstract class AbstractRichSObjectServiceIT {

    protected RichSObjectsService service;
    
    @BeforeClass
    public void setupService() {
        service = new RichSObjectsServiceImpl();
        System.out.println("Running " + this.getClass() + " with API provider: " + service.api().getClass());
    }

    @Test
    public void testListSObjectTypes() {
        List<BasicSObjectDescription> basicSObjectDescriptions = service.types();
        assertCollectionContains(basicSObjectDescriptions, "Account", new ElementMatcher<BasicSObjectDescription, String>() {
            @Override
            public boolean matches(BasicSObjectDescription basicSObjectDescription, String matches) {
                return basicSObjectDescription.getName().equals(matches);
            }
        });
    }

    @Test
    public void testDescribeSObjectType() {
        SObjectDescription description = service.describe("Account");
        assertEquals(description.getName(), "Account");
        assertNotNull(description.getFields());
    }

    @Test
    public void testGetRecentItems() {
        Iterator<RichSObject> recents = service.recentItems("Account");
        assertTrue(recents.hasNext());
    }

    @Test
    public void testQuery() {
        Iterator<RichSObject> queryResults = service.query("SELECT Id FROM Account LIMIT 2001");

        int count = 0;
        while (queryResults.hasNext()) {
            assertEquals("Account", queryResults.next().getMetadata().getName());
            count++;
        }

        assertEquals(count, 2001);
    }

    @Test
    public void testSObjectCRUD() {
        final RichSObject newAcct = service.of("Account").getField("Name").setValue("TEST1");

        final RichSObject insertedAcct = service.insert(newAcct);
        assertEquals(insertedAcct.getField("Name").getValue(), "TEST1");

        final RichSObject unUpdatedAcct = insertedAcct.getField("Name").setValue("TEST2");
        final RichSObject updatedAcct = service.update(unUpdatedAcct);
        assertEquals(updatedAcct.getField("Name").getValue(), "TEST2");

        service.delete(updatedAcct);
    }

    @Test
    public void testSetValue() throws Exception {
        assertSetting("name", "Something");
        assertSetting("My_Checkbox__c", true);
        assertSetting("My_Checkbox__c", false);
        assertSetting("AnnualRevenue", 123D);
        assertSetting("NumberOfEmployees", 456);
        assertSetting("Send_Email_At__c", new DateTime().toDateMidnight().toDate());
        assertSetting("img_src_closed_gif__c", new DateTime().toDateMidnight().toDate());
    }

    private void assertSetting(String fieldName, Object value) {
        final RichSObject base = service.of("Account").getField("name").setValue("XXX");
        final RichSObject set = base.getField(fieldName).setValue(value);
        final RichSObject saved = service.insert(set);
        assertEquals(saved.getField(fieldName).asAny(), value);
    }

    @Test
    public void testAsTypes() throws Exception {
        final RichSObject doc = service.query("SELECT AuthorId,Body,BodyLength,CreatedDate,Name,IsDeleted FROM Document LIMIT 1").next();
        assertAsType(doc.getField("AuthorId"), String.class, String.class);
        assertAsType(doc.getField("Name"), String.class, String.class);
        assertAsType(doc.getField("CreatedDate"), String.class, Date.class);
        assertAsType(doc.getField("IsDeleted"), Boolean.class, Boolean.class);
        assertAsType(doc.getField("BodyLength"), Integer.class, Integer.class);
        assertAsType(doc.getField("Body"), String.class, byte[].class);

        final RichSObject acct = service.query("SELECT LastActivityDate FROM Account WHERE LastActivityDate != null LIMIT 1").next();
        assertAsType(acct.getField("LastActivityDate"), String.class, Date.class);
    }

    @Test
    public void testAsTypesName() throws Exception {
        final String acctName = String.valueOf(System.currentTimeMillis());
        final double annualRevenue = 1000D;
        final RichSObject acct = service.insert(service.of("Account", new HashMap<String, Object>() {{
            put("name", acctName);
            put("annualRevenue", annualRevenue);
        }}));
        RichSObject contact = service.insert(service.of("Contact", new HashMap<String, Object>() {{
            put("accountId", acct.getField("id").asString());
            put("lastName", "blah");
        }}));

        assertEquals(contact.getField("accountId").asAny(), acct.getField("id").asString());
        assertEquals(contact.getField("accountId").asAnyWithNameRef(), acctName);
    }

    private void assertAsType(RichSObject.RichField field, Class<?> expectedRawType, Class<?> expectedConvertedType) {
        assertEquals(field.getValue().getClass(), expectedRawType);
        assertEquals(field.asAny().getClass(), expectedConvertedType);
    }
    
    @Test
    public void testAsRef() throws Exception {
        final RichSObject contact = service.query("SELECT AccountId FROM Contact WHERE AccountId != null LIMIT 1").next();
        final RichSObject account = contact.getField("accountId").asRef();

        assertEquals(contact.getMetadata().getName(), "Contact");
        assertEquals(account.getMetadata().getName(), "Account");
    }

    private static interface ElementMatcher<E,M> {
        boolean matches(E element, M matches);
    }

    private <E,M> void assertIteratorContains(Iterator<E> iterator, M matches, ElementMatcher<E,M> elementMatcher) {
        while(iterator.hasNext()) {
            E element = iterator.next();
            if (elementMatcher.matches(element, matches)) return;
        }
        fail("Element [" + matches + "] not found in iterator: \n" + iterator);
    }
    
    private <E,M> void assertCollectionContains(List<E> collection, M matches, ElementMatcher<E,M> elementMatcher) {
        for (E element : collection) {
            if (elementMatcher.matches(element, matches)) return;
        }
        fail("Element [" + matches + "] not found in collection: \n" + collection);
    }
}
