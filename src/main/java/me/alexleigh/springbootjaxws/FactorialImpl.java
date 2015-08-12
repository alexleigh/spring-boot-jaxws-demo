package me.alexleigh.springbootjaxws;

import me.alexleigh.demo.service.FactorialFault;
import me.alexleigh.demo.service.FactorialPort;

@javax.jws.WebService (endpointInterface="me.alexleigh.demo.service.FactorialPort")
public class FactorialImpl implements FactorialPort {
    
    public int factorial(int number) throws FactorialFault {
        if (number < 0) {
            String message = "Number cannot be negative.";
            me.alexleigh.demo.datatype.FactorialFault fault
                    = new me.alexleigh.demo.datatype.FactorialFault();
            fault.setMessage(message);
            fault.setFaultInfo("Number: " + number);
            throw new FactorialFault(message, fault);
        }

        return doFactorial(number);
    }

    private int doFactorial(int number) {
        return number <= 1 ? 1 : number * doFactorial(number - 1);
    }
}
