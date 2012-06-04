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

    private RichSObjectsService service;
    
    @BeforeClass
    public void setupService() {
        service = new RichSObjectsServiceImpl();
        System.out.println("Running " + this.getClass() + " with API provider: " + SfdcApiLoader.get(24.0).getClass());
    }

    @Test
    public void testListSObjectTypes() {
        List<BasicSObjectDescription> basicSObjectDescriptions = service.listSObjectTypes();
        assertCollectionContains(basicSObjectDescriptions, "Account", new ElementMatcher<BasicSObjectDescription, String>() {
            @Override
            public boolean matches(BasicSObjectDescription basicSObjectDescription, String matches) {
                return basicSObjectDescription.getName().equals(matches);
            }
        });
    }

    @Test
    public void testDescribeSObjectType() {
        SObjectDescription description = service.describeSObjectType("Account");
        assertEquals(description.getName(), "Account");
        assertNotNull(description.getFields());
    }

    @Test
    public void testGetRecentItems() {
        Iterator<RichSObject> recents = service.getRecentItems("Account");
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
        Map<String, String> rawAcct = new HashMap<String, String>(1);
        rawAcct.put("Name", "TEST1");

        String acctId = service.createSObject("Account", rawAcct);
        final RichSObject acct1 = service.getSObject("Account", acctId);
        assertEquals(acct1.get("Name").getValue(), "TEST1");

        rawAcct.put("Name", "TEST2");
        service.updateSObject("Account", acctId, rawAcct);
        final RichSObject acct2 = service.getSObject("Account", acctId);
        assertEquals(acct2.get("Name").getValue(), "TEST2");

        service.deleteSObject("Account", acctId);
    }

    @Test
    public void testAsTypes() throws Exception {
        final RichSObject acct = service.query("SELECT AuthorId,Body,BodyLength,CreatedDate,Name,IsDeleted FROM Document LIMIT 1").next();

        assertAsType(acct.get("AuthorId"), String.class, String.class);
        assertAsType(acct.get("Name"), String.class, String.class);
        assertAsType(acct.get("CreatedDate"), String.class, Date.class);
        assertAsType(acct.get("IsDeleted"), Boolean.class, Boolean.class);
        assertAsType(acct.get("BodyLength"), Integer.class, Integer.class);
        assertAsType(acct.get("Body"), String.class, byte[].class);
    }

    private void assertAsType(RichSObject.RichField field, Class<?> expectedRawType, Class<?> expectedConvertedType) {
        assertEquals(field.getValue().getClass(), expectedRawType);
        assertEquals(field.asAny().getClass(), expectedConvertedType);
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
