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
import com.comcast.cats.test.SettopBlock;
import com.comcast.cats.test.annotation.Description;
import com.comcast.cats.test.annotation.Resource;
import com.comcast.cats.test.dataprovider.CatsRestSettopDataProvider;
import com.comcast.cats.test.event.Response;

@Description( author = "deepavs", description = "Testing DP", name = "Digit Key Press Response", tags =
    { "Arris HDUDTA 6060", "Digit Key Press Response" }, testLink =
    { "link to test in ALM etc" }, uid = "12_4_13_1531" )
public class BaseTest extends SettopBlock
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
    public BaseTest( Settop settop )
    {
        super( settop );
        log( "Test Constructor " + settop );
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
     * Fill your testing logic in the following method.
     */

    @SuppressWarnings( "unchecked" )
    @Override
    @Test( invocationCount = 1 )
    public void execute()
    {
                
        /* ------------------------------------------------------------ */
        /* ----------------- Tests on DNCS ---------------------------- */
        /* ------------------------------------------------------------ */

        /* Test Initialise on DNCS. */
        // call( SettopOnDACInitialiseTest.class );

        /* Test REBOOT on DNCS. */
        // call(SettopOnDNCSRebootTest.class);

        /* Test Reset_Nvm on DNCS. */
        // call(SettopOnDNCSResetNvmTest.class);

        /* Test Overwrite Package Authorisation on DNCS. */
        // call( SettopOnDNCSOverritePackageAuthTest.class ); // working tested

        /* Test activate/deactivate on settopn on DNCS */
        // call(SettopOnDNCSActivateDeactivateTest.class);

        // * Test Initialise on settop on DNCS */
        // call(SettopOnDNCSInstantHitTest.class);

        /* Test clear pin on settop on DNCS. */
        // call(SettopOnDNCSClearPinTest.class);

        /* Delete settop operation not done. */
        
        
    }

}
