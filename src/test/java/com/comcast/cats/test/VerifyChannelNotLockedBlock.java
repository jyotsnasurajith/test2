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
import com.comcast.cats.test.SettopBlock;
import com.comcast.cats.test.annotation.Description;
import com.comcast.cats.test.annotation.Resource;
import com.comcast.cats.test.dataprovider.CatsRestSettopDataProvider;

@Description(author = "deepavs", description = "Checking for unlocked channel", name = "Unlocked channel", tags = {
		"", "" }, testLink = { "link to test in ALM etc" }, uid = "12_4_13_1531")
public class VerifyChannelNotLockedBlock extends SettopBlock {
	
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
	public VerifyChannelNotLockedBlock(Settop settop) {
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

	    /* Verify that the channel is not showing the LOCKED message. */
	     Assert.assertFalse( getOCR().waitForTextInOCRRegion( ocrRegion_locked, "LOCKED", 5000 ));

	}

}
