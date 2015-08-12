package me.alexleigh.springbootjaxws;

import me.alexleigh.demo.service.FibonacciFault;
import me.alexleigh.demo.service.FibonacciPort;

@javax.jws.WebService (endpointInterface="me.alexleigh.demo.service.FibonacciPort")
public class FibonacciImpl implements FibonacciPort {
    
    public int fibonacci(int index) throws FibonacciFault {
        if (index < 0) {
            String message = "Index cannot be negative.";
            me.alexleigh.demo.datatype.FibonacciFault fault
                    = new me.alexleigh.demo.datatype.FibonacciFault();
            fault.setMessage(message);
            fault.setFaultInfo("Index: " + index);
            throw new FibonacciFault(message, fault);
        }

        return doFibonacci(index);
    }

    private int doFibonacci(int index) {
        if (index == 0) {
            return 0;
        }
        if (index == 1) {
            return 1;
        }
        return doFibonacci(index - 1) + doFibonacci(index - 2);
    }
}
