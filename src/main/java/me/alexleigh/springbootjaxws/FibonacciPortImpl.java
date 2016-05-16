package me.alexleigh.springbootjaxws;

import me.alexleigh.demo.service.FibonacciFault;
import me.alexleigh.demo.service.FibonacciPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

@WebService(endpointInterface = "me.alexleigh.demo.service.FibonacciPort")
@Component
public class FibonacciPortImpl implements FibonacciPort {

    @Autowired
    private NumberService numberService;
    
    public int fibonacci(int index) throws FibonacciFault {
        if (index < 0) {
            String message = "Index cannot be negative.";
            me.alexleigh.demo.datatype.FibonacciFault fault
                    = new me.alexleigh.demo.datatype.FibonacciFault();
            fault.setMessage(message);
            fault.setFaultInfo("Index: " + index);
            throw new FibonacciFault(message, fault);
        }

        return numberService.fibonacci(index);
    }
}
