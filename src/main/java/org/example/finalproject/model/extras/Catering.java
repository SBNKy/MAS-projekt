package org.example.finalproject.model.extras;

import java.io.Serial;

public class Catering extends AdditionalService {
    @Serial
    private static final long serialVersionUID = 8421603560029879371L;

    private CateringVariant variant;
    private boolean requiresMaintenance;

    public Catering(String serviceName, double price, CateringVariant variant, boolean requiresMaintenance) {
        super(serviceName, price);
        this.variant = variant;
        this.requiresMaintenance = requiresMaintenance;
    }

    public CateringVariant getVariant() {
        return variant;
    }

    public void setVariant(CateringVariant variant) {
        this.variant = variant;
    }

    public boolean isRequiresMaintenance() {
        return requiresMaintenance;
    }

    public void setRequiresMaintenance(boolean requiresMaintenance) {
        this.requiresMaintenance = requiresMaintenance;
    }
}
