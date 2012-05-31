package com.github.ryanbrainard.richobjects;

import com.github.ryanbrainard.richsobjects.RichSObject;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;
import com.github.ryanbrainard.richsobjects.service.RichSObjectsServiceImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public class ForceApiIT {

    private RichSObjectsServiceImpl service = new RichSObjectsServiceImpl();
    
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
}
