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
import com.comcast.cats.image.OCRCompareResult;
import com.comcast.cats.image.OCRRegionInfo;
import com.comcast.cats.provider.DACProvider;
import com.comcast.cats.provider.DNCSProvider;
import com.comcast.cats.provider.exceptions.DigitalControllerException;
import com.comcast.cats.test.SettopBlock;
import com.comcast.cats.test.annotation.Description;
import com.comcast.cats.test.annotation.Resource;
import com.comcast.cats.test.dataprovider.CatsRestSettopDataProvider;

@Description(author = "deepavs", description = "Perform Clear pin.", name = "Clear pin Response", tags = {
		"", "Clear pin Response" }, testLink = { "link to test in ALM etc" }, uid = "12_4_13_1531")
public class PerformClearPinInSettopOnDNCSBlock extends SettopBlock {
	
    private static final String BSID_8210 = "8210"; 
    private static final long EIGHT_SECONDS = 8000;
    
    @Resource( location = "Locked.xml", name = "Locked" )
    public OCRRegionInfo          ocrRegion_locked;

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
	@Factory(dataProvider = "SETTOPLIST-DP", dataProviderClass = CatsRestSettopDataProvider.class)
	public PerformClearPinInSettopOnDNCSBlock(Settop settop) {
		super(settop);
		logger.trace("Test Constructor " + settop);
	}

	@BeforeTest
	public void testSettop() {
		// Use this if needed.
		Assert.assertNotNull(settop); // executes in the beginning like a sanity
										// test
	}

	/**
	 * Fill your testing logic in the following method.
	 */
	@Override
	@Test
	public void execute() {
	    
	    /* Get dncs provider for performing DNCS operation. */
	    DNCSProvider dncsProvider =  settop.getDNCSProvider();
        
        /* Update billing system id for the DAC */
	    /*-------------------------------------------------*/
        log( "Start - Clear pin operation." );
        /*-------------------------------------------------*/

        /* Perform Clear pin. */
        try
        {
            boolean rebootResult = dncsProvider.clearPin();
            Assert.assertTrue( rebootResult );
            sleep(EIGHT_SECONDS);
        }
        catch ( DigitalControllerException dex )
        {
            log( " Exception while doing Clear_Pin on settop {}; Exception is {}", settop.getHostMacAddress(),
                    dex.getMessage() );
        }

        /*-------------------------------------------------*/
        log( " End - Clear pin operation." );
        /*-------------------------------------------------*/

	}

}
