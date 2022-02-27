package au.com.redchallenge.process;

import au.com.redchallenge.domain.MeterRead;
import au.com.redchallenge.validate.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SimpleNem12ParserImpl implements SimpleNem12Parser, ValidationContext {
    private static transient Logger LOGGER = Logger.getLogger(SimpleNem12ParserImpl.class.toString());

    //The number of meter read start record fields
    private static final int METER_READ_START_RECORD_TYPE_FIELD_NUMBER = 3;

    //The number of meter read record fields
    private static final int METER_READ_RECORD_TYPE_FIELD_NUMBER = 4;

    //This will be added by validators to the validationContext when validations failed
    private ValidationErrorCause cause;
    private Boolean valid = null;

    //CSV file rows list
    private List<String> fileRecordList;

    //Final outcome
    private List<MeterRead> meterReadsList = new ArrayList<MeterRead>();

    @Override
    public final void setValidationErrorCause(final ValidationErrorCause cause) {
        this.cause = cause;
    }

    @Override
    public final ValidationErrorCause getValidationErrorCause() {
        return this.cause;
    }

    @Override
    public final void addChain(String chainId)  {
        if ((chainId == null) || (chainId.length() == 0)) return;
    }

    @Override
    public List<String> getFileRecordList() {
        return this.fileRecordList;
    }

    @Override
    public List<MeterRead> getMeterReadsList() {
        return this.meterReadsList;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public boolean isValid() {
        return valid;
    }


    /***
     * Main enrty points to callers outside
     * @param simpleNem12File file in Simple NEM12 format
     * @return Collection<MeterRead>
     */
    @Override
    public Collection<MeterRead> parseSimpleNem12(File simpleNem12File) {
        try (Stream<String> stream = Files.lines(simpleNem12File.toPath())) {
            fileRecordList = stream.collect(Collectors.toList());

            //Starting the main validation process
            start();
            if(getValidationErrorCause() != null){
                return null;
            }else{
                return getMeterReadsList();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "SimpleNem12ParserImpl.parseSimpleNem12() -> Error reading file - IOException ->"+e.getMessage());
            return null;
        }
    }

    public final void start(){
        if (valid == null) {
            valid = VALIDATION_CHAIN.start(this);
        }
        // check validity
        LOGGER.log(Level.INFO, "Validation Context Valid flag -> "+valid);
        if (!valid) {
            LOGGER.log(Level.INFO, "Validation failed -> getValidationErrorCause -> "+getValidationErrorCause());
        }else{
            LOGGER.log(Level.INFO, "Validation succeeded. ");
        }
    }

    /***
     *Validation chain - chain of responsibility
     */
    private static final ValidationChain VALIDATION_CHAIN
            = new ValidationChainFileFormatCheck(
                    new ValidationChainMeterRecordCheck(null));

}
