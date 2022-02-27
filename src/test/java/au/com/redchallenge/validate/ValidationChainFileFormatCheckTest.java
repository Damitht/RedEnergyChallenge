package au.com.redchallenge.validate;

import au.com.redchallenge.domain.MeterRead;
import au.com.redchallenge.process.SimpleNem12ParserImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class ValidationChainFileFormatCheckTest {

    private File testFile;
    private SimpleNem12ParserImpl simpleNem12Parser = new SimpleNem12ParserImpl();
    ClassLoader classLoader;

    @Before
    public void runBeforeEveryTest() {
        classLoader = getClass().getClassLoader();
    }

    @After
    public void runAfterEveryTest() {
        simpleNem12Parser = null;
        testFile = null;
    }

    @Test
    void readEmptyFile(){
        classLoader = ClassLoader.getSystemClassLoader();
        testFile = new File(classLoader.getResource("emptySimpleNem12.csv").getFile());
        simpleNem12Parser = new SimpleNem12ParserImpl();
        Collection<MeterRead> meterReads = simpleNem12Parser.parseSimpleNem12(testFile);

        //Checking for ValidationErrorCause.InvalidFile -> InvalidFile
        assertEquals(simpleNem12Parser.getValidationErrorCause(),ValidationErrorCause.InvalidFile);
    }

    @Test
    void wrongStartOfFile(){
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        testFile = new File(classLoader.getResource("wrongStartOfFileSimpleNem12.csv").getFile());
        simpleNem12Parser = new SimpleNem12ParserImpl();
        Collection<MeterRead> meterReads = simpleNem12Parser.parseSimpleNem12(testFile);

        //Checking for ValidationErrorCause.Invalid100Record -> Invalid100Record
        assertEquals(simpleNem12Parser.getValidationErrorCause(),ValidationErrorCause.Invalid100Record);
    }

    @Test
    void wrongEndOfFile(){
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        testFile = new File(classLoader.getResource("wrongEndOfFileSimpleNem12.csv").getFile());
        simpleNem12Parser = new SimpleNem12ParserImpl();
        Collection<MeterRead> meterReads = simpleNem12Parser.parseSimpleNem12(testFile);

        //Checking for ValidationErrorCause.Invalid900Record -> Invalid900Record
        assertEquals(simpleNem12Parser.getValidationErrorCause(),ValidationErrorCause.Invalid900Record);
    }
}