package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;

import java.net.InetAddress;

/**
 * Convert string to InetAddress
 *
 * @author Sailing
 * @since 2008-2-28
 */
public class InetAddressConverter extends AbstractConverter<InetAddress>
{
    public InetAddressConverter() {
        super(DataTypes.TYPE_INET_ADDRESS);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected InetAddress doConvert(Object obj, InetAddress defValue) {
        if (obj instanceof InetAddress) {
            return (InetAddress)obj;
        }
        else if (obj instanceof String) {
            String addr = (String)obj;
            if ("*".equals(addr)) {
                addr = "0.0.0.0";
            }
            try {
                return InetAddress.getByName(addr);
            }
            catch (Exception e) {
                return defValue;
            }
        }
        return defValue;
    }
}
