package interpret;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ValueDouble extends Value {
    public final double value;

    public ValueDouble(double value) {
        this.value = value;
    }

}

