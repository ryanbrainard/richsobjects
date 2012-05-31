package com.github.ryanbrainard.richsobjects.api;

import com.github.ryanbrainard.richsobjects.RichSObject;
import com.github.ryanbrainard.richsobjects.api.client.SfdcRestApiClientLoader;
import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectDescription;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;
import com.github.ryanbrainard.richsobjects.service.RichSObjectsService;
import com.github.ryanbrainard.richsobjects.service.RichSObjectsServiceImpl;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public abstract class AbstractRichSObjectServiceTest {

    private RichSObjectsService service;
    
    @BeforeClass
    public void setupService() {
        service = new RichSObjectsServiceImpl();
        System.out.println("Running " + this.getClass() + " with API provider: " + SfdcRestApiClientLoader.get().getClass());
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
    public void testDescribeSObject() {
        SObjectDescription rso = service.describeSObjectType("Account");
        Assert.assertEquals(rso.getName(), "Account");
    }

    @Test
    public void testSObjectCRUD() {
        Map<String, String> rawAcct = new HashMap<String, String>(1);
        rawAcct.put("Name", "TEST1");

        String acctId = service.createSObject("Account", rawAcct);
        final RichSObject acct1 = service.getSObject("Account", acctId);
        Assert.assertEquals(acct1.get("Name").getValue(), "TEST1");

        rawAcct.put("Name", "TEST2");
        service.updateSObject("Account", acctId, rawAcct);
        final RichSObject acct2 = service.getSObject("Account", acctId);
        Assert.assertEquals(acct2.get("Name").getValue(), "TEST2");

        service.deleteSObject("Account", acctId);
    }

    private static interface ElementMatcher<E,M> {
        boolean matches(E element, M matches);
    }

    private <E,M> void assertCollectionContains(List<E> collection, M matches, ElementMatcher<E,M> elementMatcher) {
        for (E element : collection) {
            if (elementMatcher.matches(element, matches)) return;
        }
        Assert.fail("Element [" + matches + "] not found in collection: \n" + collection);
    }
}
