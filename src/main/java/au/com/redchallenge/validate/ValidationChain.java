package au.com.redchallenge.validate;

/***
 * Validation chain that is chaining the basic validations with
 * Meter reading validations
 */
public abstract class ValidationChain {

    private final String chainId;
    private ValidationChain nextValidation;

    ValidationChain(String chainId) {
        super();
        this.chainId = chainId;
    }

    ValidationChain(final String chainId, final ValidationChain nextValidation) {
        this(chainId);
        this.nextValidation = nextValidation;
    }

    final void setNext(ValidationChain nextValidation) {
        this.nextValidation = nextValidation;
    }

    public final boolean start(final ValidationContext context) {
        // add the chain ID
        context.addChain(chainId);

        // when validation failed, then immediately result as fail
        if (!validate(context)) return false;

        // when success, attempt to invoke the next chain
        return (nextValidation == null) ? true : nextValidation.start(context);
    }

    abstract boolean validate(final ValidationContext context);

}
