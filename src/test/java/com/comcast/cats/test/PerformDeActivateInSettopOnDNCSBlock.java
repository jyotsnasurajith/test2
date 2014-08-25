package com.comcast.cats.test;

import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.Settop;
import com.comcast.cats.image.ImageCompareRegionInfo;
import com.comcast.cats.image.OCRRegionInfo;
import com.comcast.cats.provider.DACProvider;
import com.comcast.cats.provider.exceptions.DigitalControllerException;
import com.comcast.cats.test.SettopBlock;
import com.comcast.cats.test.annotation.Description;
import com.comcast.cats.test.annotation.Resource;
import com.comcast.cats.test.dataprovider.CatsRestSettopDataProvider;

@Description( author = "deepavs", description = "Overrite Package Authorisation in DNCS using DP", name = "DNCS_Overrite_Package_Authorisation", tags =
    { "Overrite Package Authorisation On DNCS Block", "DNCS Overrite Package Auth Response" }, testLink =
    { "link to test in ALM etc" }, uid = "12_4_13_1531" )
public class PerformDeActivateInSettopOnDNCSBlock extends SettopBlock
{

    private static final String channelHandle = "101";
    /**
     * Utilize the DataProvider so that N number of these Test Classes are
     * created based on the number of settops returned from the DataProvider.
     * These "can" be executed in parallel given the configuration parameters
     * for the DataProvider implementation.
     * 
     * @param settop
     *            - Settop being injected by the DataProvider obtained from the
     *            CatsFramework.
     */
    @Factory( dataProvider = "SETTOPLIST-DP", dataProviderClass = CatsRestSettopDataProvider.class )
    public PerformDeActivateInSettopOnDNCSBlock( Settop settop )
    {
        super( settop );
        log( "Test SettopOnDNCSOpTest Constructor " + settop );
    }

    @BeforeTest
    public void testSettop()
    {
        // Use this if needed.
        Assert.assertNotNull( settop ); // executes in the beginning like a
                                        // sanity
    }

    
    /**
     * Perform overwrite package authorisation.
     */
    @Override
    @Test(invocationCount = 1)
    public void execute()
    {

        log( "Settop with mac id : [{}] and Controller [{}]", settop.getHostMacAddress(),
                settop.getController() );

        /* Perform 'Deactivate' command on a Settop on DNCS */
        try
        {
            boolean result = settop.getDNCSProvider().deactivate();
            Assert.assertTrue( result );
        }
        catch ( DigitalControllerException de )
        {
            log( " Exception while doing DEACTIVATE on settop {}; Exception is {}", settop.getHostMacAddress(),
                    de.getMessage() );
        }
    }
    
    
}
