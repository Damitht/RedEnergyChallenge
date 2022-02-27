package au.com.redchallenge.validate;

import au.com.redchallenge.domain.MeterRead;
import au.com.redchallenge.process.SimpleNem12ParserImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class ValidationChainMeterRecordCheckTest {

    private File testFile;
    private SimpleNem12ParserImpl simpleNem12Parser = new SimpleNem12ParserImpl();
    ClassLoader classLoader;

    @Before
    public void runBeforeEveryTest() {
        ClassLoader classLoader = getClass().getClassLoader();
    }

    @After
    public void runAfterEveryTest() {
        simpleNem12Parser = null;
        testFile = null;
    }

    @Test
    void wrongNMItFile(){
        classLoader = ClassLoader.getSystemClassLoader();
        testFile = new File(classLoader.getResource("wrongNMISimpleNem12.csv").getFile());
        simpleNem12Parser = new SimpleNem12ParserImpl();
        Collection<MeterRead> meterReads = simpleNem12Parser.parseSimpleNem12(testFile);

        //Checking for ValidationErrorCause.InvalidNMI -> InvalidNMI
        assertEquals(simpleNem12Parser.getValidationErrorCause(),ValidationErrorCause.InvalidNMI);
    }

    @Test
    void wrongDateFormatFile(){
        classLoader = ClassLoader.getSystemClassLoader();
        testFile = new File(classLoader.getResource("wrongDateFormatSimpleNem12.csv").getFile());
        simpleNem12Parser = new SimpleNem12ParserImpl();
        Collection<MeterRead> meterReads = simpleNem12Parser.parseSimpleNem12(testFile);

        //Checking for ValidationErrorCause.InvalidDate -> InvalidDate
        assertEquals(simpleNem12Parser.getValidationErrorCause(),ValidationErrorCause.InvalidDate);
    }

    @Test
    void wrongEnergyUnit(){
        classLoader = ClassLoader.getSystemClassLoader();
        testFile = new File(classLoader.getResource("wrongEnergyUnitSimpleNem12.csv").getFile());
        simpleNem12Parser = new SimpleNem12ParserImpl();
        Collection<MeterRead> meterReads = simpleNem12Parser.parseSimpleNem12(testFile);

        //Checking for ValidationErrorCause.InvalidEnergy -> InvalidEnergy
        assertEquals(simpleNem12Parser.getValidationErrorCause(),ValidationErrorCause.InvalidEnergy);
    }

    @Test
    void wrongQuality(){
        classLoader = ClassLoader.getSystemClassLoader();
        testFile = new File(classLoader.getResource("wrongQualitySimpleNem12.csv").getFile());
        simpleNem12Parser = new SimpleNem12ParserImpl();
        Collection<MeterRead> meterReads = simpleNem12Parser.parseSimpleNem12(testFile);

        //Checking for ValidationErrorCause.InvalidQuality -> InvalidQuality
        assertEquals(simpleNem12Parser.getValidationErrorCause(),ValidationErrorCause.InvalidQuality);
    }
}