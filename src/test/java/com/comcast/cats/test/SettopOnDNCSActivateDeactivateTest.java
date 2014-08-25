package com.comcast.cats.test;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.comcast.bossprotocol.response.QueryDHCTResponse;
import com.comcast.cats.RemoteCommand;
import com.comcast.cats.Settop;
import com.comcast.cats.image.ImageCompareRegionInfo;
import com.comcast.cats.image.OCRRegionInfo;
import com.comcast.cats.provider.DACProvider;
import com.comcast.cats.provider.DNCSProvider;
import com.comcast.cats.provider.exceptions.DigitalControllerException;
import com.comcast.cats.test.SettopBlock;
import com.comcast.cats.test.annotation.Description;
import com.comcast.cats.test.annotation.Resource;
import com.comcast.cats.test.dataprovider.CatsRestSettopDataProvider;

@Description( author = "deepavs", description = "Testing DNCS Overrite Package Authorisation using DP", name = "DNCS_Overrite Package Authorisation", tags =
    { "Testing Overrite Package Authorisation On DNCS", "DNCS Overrite Package Auth Response" }, testLink =
    { "link to test in ALM etc" }, uid = "12_4_13_1531" )
public class SettopOnDNCSActivateDeactivateTest extends SettopBlock
{
    private static final long FIFTEEN_SECONDS = 15000;
    
    private static final Integer CH_FIVE = 5;
    
    @Resource( location = "blank.xml", name = "blank" )
    public ImageCompareRegionInfo icRegion_blank;

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
    public SettopOnDNCSActivateDeactivateTest( Settop settop )
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
     * Test - Test Deactivate operation on a DNCS settop box. Also execute Activate operation.
     * Expected Result - The specific channel in the package will not be authorised for viewing. 
     * Verify - Verify that NOT AUTHORIZED message is shown in the settop.
     * 
     * This test is written for Settop [00:19:47:25:AC:FA] , Model[Explorer 2200] and 
     *  Manufacturer [Cisco]. 
     */
    @SuppressWarnings( "unchecked" )
    @Override
    @Test(invocationCount = 1)
    public void execute()
    {

        log( "Settop with mac id : [{}] and Controller [{}]", settop.getHostMacAddress(), settop.getController() );
        String packages = null;
        DNCSProvider dncsProvider = settop.getDNCSProvider();
        try
        {
            /*
             * Keep a back up of all the package handles' list, so that I can
             * activate all of them after deactivating.
             */
            QueryDHCTResponse queryResponse = dncsProvider.invokeQueryDHCT();
            packages = queryResponse.getPackageAuthorization().getCommaDelimitedPackageList();
            int count = queryResponse.getPackageAuthorization().getPackageCount();
            log( " count {}" , count );
            log( " Package handles ::" + packages );

            /* Tune to channel 5 */
            settop.tune( CH_FIVE);

            /* Do Deactivate operation. */
            call( PerformDeActivateInSettopOnDNCSBlock.class );
            sleep( 40000 );

            /*
             * Verify that the specific channel is showing NOT AUTHORIZED
             * message.
             */
            call( VerifyPackageNotAuthorisedDNCSBlock.class );

            /*
             * Adding all the authorised packages back, after my test with
             * overwrite package authorisation.
             */
            Assert.assertTrue( dncsProvider.activate( packages ) );
            
            /* Get the packages count after activation and verfiy it with initial count of packages.*/
           int countFinal = dncsProvider.invokeQueryDHCT().getPackageAuthorization().getPackageCount();
           log( "Package Count on Start : {}" , count );
           log( "Package count at end : {}" , countFinal );
           Assert.assertEquals( count, countFinal );
            

        }
        catch ( DigitalControllerException de )
        {
            log( " Exception while doing DEACTIVATE/ACTIVATE on DNCS settop {}; Exception is {}",
                    settop.getHostMacAddress(), de.getMessage() );
        }
    }
    

}
