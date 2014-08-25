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
//import com.comcast.cats.provider.DACProvider;
import com.comcast.cats.provider.DACProvider;
import com.comcast.cats.provider.DNCSProvider;
import com.comcast.cats.provider.exceptions.DigitalControllerException;
import com.comcast.cats.test.SettopBlock;
import com.comcast.cats.test.annotation.Description;
import com.comcast.cats.test.annotation.Resource;
import com.comcast.cats.test.dataprovider.CatsRestSettopDataProvider;

@Description( author = "deepavs", description = "Testing Initialise On DAC using DP", name = "DAC_Initialise", tags =
    { "Testing Settop On DAC", "DAC Initialise Response" }, testLink =
    { "link to test in ALM etc" }, uid = "12_4_13_1531" )
public class PerformResetNvmOnDNCSBlock extends SettopBlock
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
    public PerformResetNvmOnDNCSBlock( Settop settop )
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
        /* Test if video streaming is in progress and is not a blank screen. */
         settop.getMotionDetectorProvider().detectMotion( 5000 );
    }

    /**
     * Perform Reset_Nvm on DNCS (tested with Settop on DNCS) box.
     */
    @Override
    @Test( invocationCount = 1 )
    public void execute()
    {

        /*-------------------------------------------------*/
        log( "Start - Reset NVM operation." );
        /*-------------------------------------------------*/

        DNCSProvider dncsProvider = settop.getDNCSProvider();
        try
        {
            dncsProvider.reset();
        }
        catch ( DigitalControllerException e )
        {
            log(" Exception while performing Rest_NVM {}",e.getMessage());
        }
        
        /*-------------------------------------------------*/
        log( "End - Reset NVM operation." );
        /*-------------------------------------------------*/
        


    }
}
