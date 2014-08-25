package com.comcast.cats.test;

import junit.framework.Assert;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.Settop;
import com.comcast.cats.image.ImageCompareRegionInfo;
import com.comcast.cats.test.SettopBlock;
import com.comcast.cats.test.annotation.Description;
import com.comcast.cats.test.annotation.Resource;
import com.comcast.cats.test.dataprovider.CatsRestSettopDataProvider;

@Description( author = "deepavs", description = "Testing Clear Pin on DAC using DP", name = "DAC_ClearPin", tags =
    { "Testing Clear Pin on DAC", "DAC Clear Pin Response" }, testLink =
    { "link to test in ALM etc" }, uid = "12_4_13_1531" )
public class SettopOnDNCSClearPinTest extends SettopBlock
{
   
    @Resource( location = "CH_5.xml", name = "Five" )
    public ImageCompareRegionInfo icRegion_5;

    @Resource( location = "Channel_locks.xml", name = "Channel_locks" )
    public ImageCompareRegionInfo icRegion_channelLocks;
    
    @Resource(location = "blank_region.xml", name="blank_region")
    public ImageCompareRegionInfo icBlank_region;
    
    @Resource( location = "Lock.xml" , name="Lock")
    public ImageCompareRegionInfo        icRegion_lock;
    
    
    private static final int FIVE = 5; 

    private static final long FIVE_SECONDS = 5000;
    private static final long EIGHT_SECONDS = 8000;

    
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
    public SettopOnDNCSClearPinTest( Settop settop )
    {
        super( settop );
        log( "Test SettopOnDACOpTest Constructor {} ", settop );
    }

    @BeforeTest
    public void testSettop()
    {
        // Use this if needed.
        Assert.assertNotNull( settop ); // executes in the beginning like a
                                        // sanity
    }

    /**
     * Test - Test CLEAR PIN on a DNCS settop box. First apply Lock pin on a channel, verify and then clear the pin and verify.
     * Expected Result - Lock will be cleared and user can view the channel. 
     * Verify - Verify that the channel is not locked.
     *
      * This test is written for Settop [00:19:47:25:AC:FA] , Model[Explorer 2200] and 
     *  Manufacturer [Cisco].
     */
    @SuppressWarnings( "unchecked" )
    @Override
    @Test( invocationCount = 1 )
    public void execute()
    {

        log( "Settop with mac id : [{}] and Controller [{}]", settop.getHostMacAddress(), settop.getController() );

        log( "Executing Clear Pin on settop MAC : [{}] ", settop.getHostMacAddress() );

        /* TUNE CHANNEL 5. */
        Assert.assertTrue( settop.pressKey( FIVE ) );

        getIC().waitForRegion( icRegion_5, FIVE_SECONDS );
        sleep( FIVE_SECONDS );
        
        /* Call the SetLockPinBlock to set locks pin. */
        call( SetLockPinInDNCSBlock.class );

        Assert.assertTrue( settop.pressKey( RemoteCommand.EXIT ) );
        
        /* We will be in CHANNEL 5 now or tune to 5. */
        Assert.assertTrue( settop.pressKey( RemoteCommand.FIVE ) );

        /* We can see the LOCKED state of the channel. */
        call( VerifyChannelLockedBlock.class );

        sleep( FIVE_SECONDS );

        call( PerformClearPinInSettopOnDNCSBlock.class );
        
        sleep( EIGHT_SECONDS );
        
        /* Press CHANNEL 5 to see if its still in locked state. */
        Assert.assertTrue( settop.pressKey( RemoteCommand.FIVE ) );

        sleep( 10000 );
        
        /* Verify that the CHANNEL 5 is not showing the LOCKED message. */
        call( VerifyChannelNotLockedBlock.class );

        /* EXIT if needed. */
        settop.pressKey( RemoteCommand.EXIT );
        
    }
    
}
