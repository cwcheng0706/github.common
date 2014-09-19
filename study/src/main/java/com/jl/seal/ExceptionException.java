
/**
 * ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package com.jl.seal;

public class ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1411117995370L;
    
    private com.jl.seal.SealServiceStub.ExceptionE faultMessage;

    
        public ExceptionException() {
            super("ExceptionException");
        }

        public ExceptionException(java.lang.String s) {
           super(s);
        }

        public ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(com.jl.seal.SealServiceStub.ExceptionE msg){
       faultMessage = msg;
    }
    
    public com.jl.seal.SealServiceStub.ExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    