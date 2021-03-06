package com.comcast.cats.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import junit.framework.Assert;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.Settop;
import com.comcast.cats.image.ImageCompareRegionInfo;
import com.comcast.cats.image.ImageRegionInfo;
import com.comcast.cats.image.OCRCompareResult;
import com.comcast.cats.image.OCRRegionInfo;
import com.comcast.cats.provider.DACProvider;
import com.comcast.cats.provider.DNCSProvider;
import com.comcast.cats.provider.exceptions.DigitalControllerException;
import com.comcast.cats.test.SettopBlock;
import com.comcast.cats.test.annotation.Description;
import com.comcast.cats.test.annotation.Resource;
import com.comcast.cats.test.dataprovider.CatsRestSettopDataProvider;

@Description( author = "deepavs", description = "Testing Reset_Nvm On DNCS using DP", name = "DNCS_Reset_Nvm", tags =
    { "Testing Settop On DNCS", "DNCS Reset_Nvm" }, testLink =
    { "link to test in ALM etc" }, uid = "12_4_13_1531" )
public class SettopOnDNCSResetNvmTest extends SettopBlock
{
   
    @Resource( location = "CH_5.xml", name = "Five" )
    public ImageCompareRegionInfo icRegion_5;
    
    private static final int FIVE = 5; 

    private static final long TWO_SECONDS = 2000;
    private static final long FIVE_SECONDS = 5000;
    private static final long TEN_SECONDS = 10000;
    
    
    private final static String refImagePath          = "src/test/resources/blank_region.JPG";
    private BufferedImage               refImage                = null;
    private static final String ocr_text                     = "Please Wait";
    
    
   // private final static String filePathOCRGuideRegionInfo="src/test/resources/Guide2.xml";

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
    public SettopOnDNCSResetNvmTest( Settop settop )
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
     * Test - Test RESET_NVM on a DNCS on Settop box. First apply Lock pin on a channel. 
     * Then RESET_NVM operation is done.Settop will be rebooted and pin will be cleared 
     * when channel is up.
     * Expected Result - Lock will be cleared and user can view the channel. 
     * Verify - Verify that the channel is not locked.
     * 
     *  This test is written for Settop [00:19:47:25:AC:FA] , Model[Explorer 2200] and 
     *  Manufacturer [Cisco].        
     */
    @SuppressWarnings( "unchecked" )
    @Override
    @Test( invocationCount = 1 )
    public void execute()
    {

        log( "Settop with mac id : [{}] and Controller [{}]", settop.getHostMacAddress(), settop.getController() );

        /* TUNE to CHANNEL 5. */
        Assert.assertTrue( settop.pressKey( FIVE ) );
        Assert.assertTrue( getIC().waitForRegion( icRegion_5, FIVE_SECONDS ) );
        sleep( TWO_SECONDS );

        call( SetLockPinInDNCSBlock.class );

        /* We will be in CHANNEL 5 now or tune to 5. */
        Assert.assertTrue( settop.pressKey( RemoteCommand.FIVE ) );

        call( VerifyChannelLockedBlock.class );
        sleep( TWO_SECONDS );

        call( PerformResetNvmOnDNCSBlock.class );

        sleep(TEN_SECONDS);
        /* Press CHANNEL 5 to see if its still in locked state. */
        Assert.assertTrue( settop.pressKey( RemoteCommand.FIVE ) );

        /* Confirm that the CHANNEL 5 is not showing the LOCKED message. */
        call( VerifyChannelNotLockedBlock.class );

        /* EXIT if needed. */
        settop.pressKey( RemoteCommand.EXIT );

    }
    
}
