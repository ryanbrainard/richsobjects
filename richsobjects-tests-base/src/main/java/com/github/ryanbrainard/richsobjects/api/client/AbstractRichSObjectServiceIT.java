package com.github.ryanbrainard.richsobjects.api.client;

import com.github.ryanbrainard.richsobjects.RichSObject;
import com.github.ryanbrainard.richsobjects.RichSObjectsService;
import com.github.ryanbrainard.richsobjects.RichSObjectsServiceImpl;
import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectDescription;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

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
        final RichSObject newAcct = service.unpopulated("Account").get("Name").setValue("TEST1");

        final RichSObject insertedAcct = service.insert(newAcct);
        assertEquals(insertedAcct.get("Name").getValue(), "TEST1");

        final RichSObject unUpdatedAcct = insertedAcct.get("Name").setValue("TEST2");
        final RichSObject updatedAcct = service.update(unUpdatedAcct);
        assertEquals(updatedAcct.get("Name").getValue(), "TEST2");

        service.delete(updatedAcct);
    }

    @Test
    public void testAsTypes() throws Exception {
        final RichSObject doc = service.query("SELECT AuthorId,Body,BodyLength,CreatedDate,Name,IsDeleted FROM Document LIMIT 1").next();

        assertAsType(doc.get("AuthorId"), String.class, String.class);
        assertAsType(doc.get("Name"), String.class, String.class);
        assertAsType(doc.get("CreatedDate"), String.class, Date.class);
        assertAsType(doc.get("IsDeleted"), Boolean.class, Boolean.class);
        assertAsType(doc.get("BodyLength"), Integer.class, Integer.class);
        assertAsType(doc.get("Body"), String.class, byte[].class);
    }

    @Test
    public void testAsTypesName() throws Exception {
        final String acctName = String.valueOf(System.currentTimeMillis());
        final double annualRevenue = 1000D;
        final RichSObject acct = service.insert("Account", new HashMap<String, Object>() {{
            put("name", acctName);
            put("annualRevenue", annualRevenue);
        }});
        RichSObject contact = service.insert("Contact", new HashMap<String, Object>() {{
            put("accountId", acct.get("id").asString());
            put("lastName", "blah");
        }});

        assertEquals(contact.get("accountId").asAny(), acct.get("id").asString());
        assertEquals(contact.get("accountId").asAnyWithNameRef(), acctName);
    }

    private void assertAsType(RichSObject.RichField field, Class<?> expectedRawType, Class<?> expectedConvertedType) {
        assertEquals(field.getValue().getClass(), expectedRawType);
        assertEquals(field.asAny().getClass(), expectedConvertedType);
    }
    
    @Test
    public void testAsRef() throws Exception {
        final RichSObject contact = service.query("SELECT AccountId FROM Contact WHERE AccountId != null LIMIT 1").next();
        final RichSObject account = contact.get("accountId").asRef();

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
