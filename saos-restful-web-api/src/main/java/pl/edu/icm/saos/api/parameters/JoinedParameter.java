package pl.edu.icm.saos.api.parameters;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Class represent request's 'joined' parameter:
 * if /someBaseUrl?paramName=valA,valB then joinedValue = "valA,valB" and values = [valA,valB]
 * @author pavtel
 */
public class JoinedParameter {

    //****** fields *******
    private String joinedValue;
    private List<String> values;
    //****** END fields ********

    //******* constructors *********
    public JoinedParameter(String joinedValue, List<String> values) {
        this.joinedValue = joinedValue;
        this.values = values;
    }
    //******** END constructors *********

    //******** getters **********
    public String getJoinedValue() {
        return joinedValue;
    }

    public List<String> getValues() {
        return values;
    }
    //***** END getters **********

    //********** business methods **********
    public boolean isEmpty(){
        return StringUtils.isBlank(joinedValue);
    }

    public boolean isNotEmpty(){
        return !isEmpty();
    }

    public boolean containsValue(String value){
        return values.contains(value);
    }
    //************ END business methods **********

    @Override
    public String toString() {
        return Objects.toStringHelper(JoinedParameter.class)
                .add("joinedValue", joinedValue)
                .add("values", values)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(joinedValue, values);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof JoinedParameter){
            JoinedParameter other = (JoinedParameter) obj;
            return Objects.equal(joinedValue, other.joinedValue) &&
                    Objects.equal(values, other.values);
        }

        return false;
    }
}
