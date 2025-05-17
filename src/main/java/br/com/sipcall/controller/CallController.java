package br.com.sipcall.controller;

import br.com.sipcall.service.CallService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class CallController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallController.class);

    @Autowired
    private CallService service;

    @RequestMapping(value = "/invite/{numA}/{numB}", method = RequestMethod.GET)
    ResponseEntity<?> checkInvite(@PathVariable String numA, @PathVariable String numB) {
        LOGGER.info("checkInvite called for numA=" + numA + " and numB=" + numB);
        if (service.validateInvite(numA, numB)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/200ok/{numA}/{numB}", method = RequestMethod.GET)
    ResponseEntity<?> check200Ok(@PathVariable String numA, @PathVariable String numB) {
        LOGGER.info("check200Ok called for numA=" + numA + " and numB=" + numB);
        if (service.validate200Ok(numA, numB)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
