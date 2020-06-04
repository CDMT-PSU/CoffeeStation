package cdmtpsu.coffee.data;

public interface DataObject {
    Object getValue(int fieldIndex);

    void setValue(int fieldIndex, Object value);
}
