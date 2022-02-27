package au.com.redchallenge.validate;

import au.com.redchallenge.constants.FileConstants;
import au.com.redchallenge.domain.EnergyUnit;
import au.com.redchallenge.domain.MeterRead;
import au.com.redchallenge.domain.MeterVolume;
import au.com.redchallenge.domain.Quality;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import static java.util.Objects.isNull;

public class ValidationChainMeterRecordCheck  extends ValidationChain{
    private static final String CHAIN_ID = "METER_REAIDNG_CHECKING";

    public ValidationChainMeterRecordCheck(final ValidationChain nextValidation){
        super(CHAIN_ID, nextValidation);
    }

    @Override
    boolean validate(ValidationContext context) {
        context.setValid(true);
        context.getFileRecordList().stream()
                .filter(line ->
                        line.startsWith(FileConstants.FileRowIndicators.START_OF_DATA) || line.startsWith(FileConstants.FileRowIndicators.METER_READINGS))
                .forEach(line -> {

                    /***
                     * Checking validity of the 200 record. Validations are
                     * 1. Validate NMI
                     * 2. validate Energy Unit
                     * If errors found then corresponding ValidationErrorCause will be added to Validation Context and Valid flag will be set to false
                     */
                    if(line.startsWith(FileConstants.FileRowIndicators.START_OF_DATA)){
                        String[] type200Iems = line.split(",");
                        if(type200Iems.length == FileConstants.recordColumnSize.type200){
                            if(validate200Record(type200Iems[1], type200Iems[2], context)){
                                context.getMeterReadsList().add(new MeterRead(type200Iems[1], EnergyUnit.valueOf(type200Iems[2])));
                            }else{
                                context.setValid(false);
                            }
                        }
                    }

                    /***
                     * Checking validity of the 300 record. Validations are
                     * 1. Validate Quality
                     * 2. validate Meter reading
                     * If errors found then corresponding ValidationErrorCause will be added to Validation Context and Valid flag will be set to false
                     */
                    if(line.startsWith(FileConstants.FileRowIndicators.METER_READINGS)){
                        String[] type300Iems = line.split(",");
                        if(type300Iems.length == FileConstants.recordColumnSize.type300){
                            if(validate300Record(type300Iems[1], type300Iems[2], type300Iems[3], context)){
                                MeterRead meterRead = context.getMeterReadsList().get(context.getMeterReadsList().size() - 1);
                                MeterVolume meterVolume = new MeterVolume(parseMeterReding(type300Iems[2]), Quality.valueOf(type300Iems[3]));
                                meterRead.appendVolume(parseToLocalDate(type300Iems[1]), meterVolume);
                            }else{
                                context.setValid(false);
                            }
                        }
                    }
                });
        return context.isValid();
    }

    /***
     * 200 Record validator
     * @param nmi
     * @param energyUnit
     * @param context
     * @return boolean
     */
    private boolean validate200Record(String nmi, String energyUnit, ValidationContext context){
        return validateNMI(nmi, context) && validateEnergyUnit(energyUnit, context);
    }

    /**
     * 300 record validator
     * @param date
     * @param reading
     * @param quality
     * @param context
     * @return boolean
     */
    private boolean validate300Record(String date, String reading, String quality, ValidationContext context){
        return validateDate(date, context) && validateQuality(quality, context) && (parseMeterReding(reading) != null);
    }

    /***
     * Validating NMI
     * @param nmi
     * @param context
     * @return boolean
     */
    private boolean validateNMI(String nmi, ValidationContext context){
        if (isNull(nmi) || nmi.equals("") || nmi.length() != 10) {
            context.setValidationErrorCause(ValidationErrorCause.InvalidNMI);
            return false;
        }
        return true;
    }

    /***
     * Validating EnergyUnit
     * @param energyUnit
     * @param context
     * @return boolean
     */
    private boolean validateEnergyUnit(String energyUnit, ValidationContext context){
        if (isNull(energyUnit) || energyUnit.equals("") || ! EnergyUnit.KWH.toString().equals(energyUnit)){
            context.setValidationErrorCause(ValidationErrorCause.InvalidEnergy);
            return false;
        }
        return true;
    }

    /***
     * Validating date
     * @param date
     * @param context
     * @return boolean
     */
    private boolean validateDate(String date, ValidationContext context){
        if(date == null || parseToLocalDate(date) == null){
            context.setValidationErrorCause(ValidationErrorCause.InvalidDate);
            return false;
        }
        return true;
    }

    /***
     * Validating quality
     * @param quality
     * @param context
     * @return boolean
     */
    private boolean validateQuality(String quality, ValidationContext context){
        if (isNull(quality) || quality.equals("") || !FileConstants.getQualityMap().containsKey(quality)){
            context.setValidationErrorCause(ValidationErrorCause.InvalidQuality);
            return false;
        }else{
            return true;
        }
    }

    private BigDecimal parseMeterReding(String reading){
        if(reading != null && !reading.equals("")) {
            try {
                return BigDecimal.valueOf(Double.parseDouble(reading));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private LocalDate parseToLocalDate(String date){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            formatter = formatter.withLocale(Locale.ENGLISH);
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
