package javax.xml.bind.annotation.adapters;

public abstract class XmlAdapter<ValueType, BoundType> {
  public abstract BoundType unmarshal(ValueType paramValueType) throws Exception;
  
  public abstract ValueType marshal(BoundType paramBoundType) throws Exception;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\annotation\adapters\XmlAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */