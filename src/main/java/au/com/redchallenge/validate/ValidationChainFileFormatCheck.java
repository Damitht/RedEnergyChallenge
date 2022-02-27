package au.com.redchallenge.validate;

import au.com.redchallenge.constants.FileConstants;
import java.util.List;

/**
 * Basic file format checks are done by this validator
 * 1. Empty file
 * 2. 100 record check
 * 3. 900 record check
 * If errors found then corresponding ValidationErrorCause will be added to Validation Context
 */
public final class ValidationChainFileFormatCheck extends ValidationChain {

    private static final String CHAIN_ID = "FILE_FORMAT_CHECKING";

    public ValidationChainFileFormatCheck(final ValidationChain nextValidation){
        super(CHAIN_ID, nextValidation);
    }

    @Override
    boolean validate(ValidationContext context) {
        List<String> readingList = context.getFileRecordList();

        if(readingList == null || readingList.size() < 1){
            context.setValidationErrorCause(ValidationErrorCause.InvalidFile);
            return false;
        }

        if(!readingList.get(0).equals(FileConstants.FileRowIndicators.START_OF_FILE)){
            context.setValidationErrorCause(ValidationErrorCause.Invalid100Record);
            return false;
        }

        if(!readingList.get(readingList.size()-1).equals(FileConstants.FileRowIndicators.END_OF_FILE)){
            context.setValidationErrorCause(ValidationErrorCause.Invalid900Record);
            return false;
        }

        return true;
    }
}
