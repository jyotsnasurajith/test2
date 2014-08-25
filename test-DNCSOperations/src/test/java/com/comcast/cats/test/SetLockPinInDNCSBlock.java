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
import com.comcast.cats.image.OCRRegionInfo;
import com.comcast.cats.test.SettopBlock;
import com.comcast.cats.test.annotation.Description;
import com.comcast.cats.test.annotation.Resource;
import com.comcast.cats.test.dataprovider.CatsRestSettopDataProvider;

@Description( author = "deepavs", description = "Set Channel Lock pin", name = "Lock pin Response", tags =
    { "", "Lock pin Response" }, testLink =
    { "link to test in ALM etc" }, uid = "12_4_13_1531" )
public class SetLockPinInDNCSBlock extends SettopBlock
{

    private static final int      ONE                = 1;
    private static final int      TWO                = 2;
    private static final int      THREE              = 3;
    private static final int      FOUR               = 4;
    private static final long     TWO_SECONDS        = 2000;
    private static final long     FIVE_SECONDS       = 5000;
    private static final long     EIGHT_SECONDS      = 8000;

    private static final String   CHANNEL_LOCK_SETUP = "Ch_Lock_Setup";
    private static final String OCR_TEXT_CREATE_PIN = "CREATE LOCKS PIN";

    private final static String   refImagePath       = "src/test/resources/Lock.jpg";
    private BufferedImage         refImage           = null;

    @Resource( location = "Main_menu.xml", name = "Main_menu" )
    public OCRRegionInfo          ocrRegion_mainMenu;

    @Resource( location = "Parental_Control.xml", name = "Parental_control" )
    public OCRRegionInfo          ocrRegion_parentalCntrl;

    @Resource( location = "create_locks_pin_dncs.xml", name = "create_locks_pin_dncs" )
    public OCRRegionInfo        ocrRegion_createPin;

    @Resource( location = "confirm_pin_dncs.xml", name = "confirm_pin_dncs" )
    public ImageCompareRegionInfo icRegion_confirmPin;

    @Resource( location = "Ch_Lock_Setup.xml" )
    public ImageRegionInfo        imageRegion_lockSetup;
    
    @Resource( location = "Lock.xml", name = "Lock" )
    public ImageCompareRegionInfo icRegion_lock;

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
    public SetLockPinInDNCSBlock( Settop settop )
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
                                        // test

        /* Get the reference image. */
        File imgFile = new File( refImagePath );
        try
        {
            refImage = ImageIO.read( imgFile );
        }
        catch ( IOException e )
        {
            log(" Exception while reading image file {}",e.getMessage());
        }
    }

    /**
     * For setting pin follow the path.
     * Go to Main menu option --> Parental Control --> Create Lock pin
     * ----> Confirm Pin --> Channel Locks --> Click SELECT to apply lock for a channel.
     * 
     */
    @Override
    @Test
    public void execute()
    {

        /* Press MENU twice to get Setup/Main menu window. */
        Assert.assertTrue( settop.pressKey( TWO, RemoteCommand.MENU ) );
        sleep( TWO_SECONDS );
        /* Compare if 'Main menu' window is available. */
        getOCR().waitForOCRRegion( ocrRegion_mainMenu ) ;

        sleep( TWO_SECONDS );
        /*
         * Check if 'Parental Control' option for setting new pin, is the
         * currently selected one in the main menu, else go to Parental control
         * option(PGDN and DOWN keys) and do a SELECT.
         */
     //   boolean result = getOCR().isOCRTextOnScreenNow( ocrRegion_parentalCntrl );
        boolean result = getOCR().waitForTextInOCRRegion( ocrRegion_parentalCntrl, "Parental Control" );
        sleep( TWO_SECONDS );

        /*
         * If 'Parental Control' option is not selected, go to the same by doing
         * a PGDN and DOWN.
         */
        if ( !result )
        {
            Assert.assertTrue( settop.pressKey( RemoteCommand.PGDN ) );
            Assert.assertTrue( settop.pressKey( RemoteCommand.RIGHT ) );
        }

        /* SELECT 'Parental Control' option. */
        Assert.assertTrue( settop.pressKey( RemoteCommand.SELECT ) );

        sleep( FIVE_SECONDS );

        /* Wait for 'Create lock pin' view to appear and verify.' */
        getOCR().waitForText( ocrRegion_createPin, OCR_TEXT_CREATE_PIN ); // returns false.
        
        
        sleep( TWO_SECONDS );

        /* Create lock pin here , for e.g. pin = 1234 */
        enterLockPin();

        sleep( TWO_SECONDS );
        
        
      /*  This will ask for confirmation pin, so re-enter pin */
        getIC().waitForRegion( icRegion_confirmPin, FIVE_SECONDS );

        sleep( TWO_SECONDS );
       /*  Enter pin 1234. */
        enterLockPin();

        sleep( FIVE_SECONDS );

        /* Steps to click on 'Channel Lock' option. */
        settop.pressKey( RemoteCommand.LEFT );
        sleep( TWO_SECONDS );
        settop.pressKey( RemoteCommand.DOWN );
        sleep( TWO_SECONDS );
        settop.pressKey( RemoteCommand.DOWN );
        sleep( TWO_SECONDS );
        settop.pressKey( RemoteCommand.DOWN );
        sleep( TWO_SECONDS );
        settop.pressKey( RemoteCommand.DOWN );
        sleep( TWO_SECONDS );
        settop.pressKey( RemoteCommand.UP );

        sleep( FIVE_SECONDS );
     //   String lockText = getOCR().getCurrentText( imageRegion_lockSetup, CHANNEL_LOCK_SETUP );
        
        getOCR().waitForTextInOCRRegion( imageRegion_lockSetup, CHANNEL_LOCK_SETUP, "Channel Locks Setup" ) ;
        /* If 'Channel lock' option is reached, then click SELECT. */
        sleep( EIGHT_SECONDS );
        settop.pressKey( RemoteCommand.SELECT );
        sleep( FIVE_SECONDS );
        
        /* Check if Lock button exists in corresponding channel. If false select the channel where Locks are to be applied. */
        boolean res = getIC().waitForRegion( icRegion_lock, refImage, EIGHT_SECONDS );
        if ( !res )
        {
            /* Apply Lock on this channel. */
            settop.pressKey( RemoteCommand.SELECT );
        }

        sleep(FIVE_SECONDS);
        Assert.assertTrue( settop.pressKey( RemoteCommand.EXIT ) );
    }

    /**
     * Enter any pin for locking the settop. I have chosen pin as '1234'. So
     * send IR key presses for 1,2,3 and 4.
     */
    private void enterLockPin()
    {
        Assert.assertTrue( settop.pressKey( ONE ) );
        Assert.assertTrue( settop.pressKey( TWO ) );
        Assert.assertTrue( settop.pressKey( THREE ) );
        Assert.assertTrue( settop.pressKey( FOUR ) );
    }

}
