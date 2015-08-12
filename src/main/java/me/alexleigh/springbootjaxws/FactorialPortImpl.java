package me.alexleigh.springbootjaxws;

import me.alexleigh.demo.service.FactorialFault;
import me.alexleigh.demo.service.FactorialPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@javax.jws.WebService (endpointInterface="me.alexleigh.demo.service.FactorialPort")
@Component
public class FactorialPortImpl implements FactorialPort {

    @Autowired
    private NumberService numberService;

    public int factorial(int number) throws FactorialFault {
        if (number < 0) {
            String message = "Number cannot be negative.";
            me.alexleigh.demo.datatype.FactorialFault fault
                    = new me.alexleigh.demo.datatype.FactorialFault();
            fault.setMessage(message);
            fault.setFaultInfo("Number: " + number);
            throw new FactorialFault(message, fault);
        }

        return numberService.factorial(number);
    }
}
