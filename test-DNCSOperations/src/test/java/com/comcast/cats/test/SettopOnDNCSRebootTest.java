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

@Description( author = "deepavs", description = "Testing DNCS Reboot using DP", name = "DNCS_Reboot", tags =
    { "Testing Reboot On DNCS", "DNCS Reboot Response" }, testLink =
    { "link to test in ALM etc" }, uid = "12_4_13_1531" )
public class SettopOnDNCSRebootTest extends SettopBlock
{
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
    public SettopOnDNCSRebootTest( Settop settop )
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
        /* Test if video streaming is in progress and is not a blank screen. */
        // settop.getMotionDetectorProvider().detectMotion( 5000 )

    }

    /**
     * This test is written for Settop [00:19:47:25:AC:FA] , Model[Explorer 2200] and 
     *  Manufacturer [Cisco]. 
     */
    @Override
 //   @Test(invocationCount = 1)
    public void execute()
    {

        logger.info( "Settop with mac id : [{}] and Controller [{}]", settop.getHostMacAddress(),
                settop.getController() );

        /* Test 'REBOOT' command on a Settop on DNCS */
        testReboot();
    }
    
    
    /**
     * Test - Test Reboot on a DNCS settop box. 
     * Expected Result - The settop box will be rebooted. 
     * Verify - Verify the blank screen on settop during reboot.
     */
    @SuppressWarnings( "unchecked" )
    private void testReboot()
    {

        try
        {
            boolean rebootResult = settop.getDNCSProvider().warmReset();
            Assert.assertTrue( rebootResult );
            call(VerifyBlankRegionBlock.class);
        }
        catch ( DigitalControllerException de )
        {
            logger.error( " Exception while doing REBOOT on settop {}; Exception is {}", settop.getHostMacAddress(),
                    de.getMessage() );
        }
    }


}
