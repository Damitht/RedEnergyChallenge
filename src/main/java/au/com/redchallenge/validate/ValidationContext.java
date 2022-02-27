package au.com.redchallenge.validate;

/***
 * Validation context shared by all validators to have common context
 * 1. ValidationErrorCause
 * 2. FileRecordList - CSV rows
 * 3. MeterReadsList - End result
 * 4. Valid flag - Denotes if validation succeeded or failed
 */

import au.com.redchallenge.domain.MeterRead;

import java.util.List;

public interface ValidationContext {
    void setValidationErrorCause(final ValidationErrorCause cause);
    ValidationErrorCause getValidationErrorCause();
    void addChain(final String chainId);
    List<String> getFileRecordList();
    List<MeterRead> getMeterReadsList();
    void setValid(final boolean valid);
    boolean isValid();
}
