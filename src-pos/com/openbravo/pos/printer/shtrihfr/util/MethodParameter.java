/*
 * MethodParameter.java
 *
 * Created on 2 April 2008, 17:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.util;

/**
 *
 * @author V.Kravtsov
 */

import java.security.InvalidParameterException;

public final class MethodParameter
{
    public static void checkRange(
        long parameterValue, 
        long minValue, 
        long maxValue, 
        String parameterName)
        throws InvalidParameterException
    {
        if (parameterValue < minValue) 
        {
            throw new InvalidParameterException(
                parameterName + ": invalid parameter value (" +
                String.valueOf(parameterValue) + " < " +
                String.valueOf(minValue) + ")");
        }
        if (parameterValue > maxValue) 
        {
            throw new InvalidParameterException(
                parameterName + ": invalid parameter value (" +
                String.valueOf(parameterValue) + " > " +
                String.valueOf(maxValue) + ")");
        }
    }
}
