package com.redhat.gps;

import java.io.IOException;
import java.util.Properties;

import org.jboss.internal.soa.esb.publish.ContractInfo;
import org.jboss.internal.soa.esb.publish.ContractProvider;
import org.jboss.soa.esb.Configurable;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.Service;
import org.jboss.soa.esb.actions.soap.AuthBASICWsdlContractPublisher;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class WSDLPublisher extends AuthBASICWsdlContractPublisher implements
		ContractProvider, Configurable {
	
	private static Logger logger = Logger.getLogger(WSDLPublisher.class);

	public void setConfiguration(ConfigTree config)
			throws ConfigurationException {
		// TODO Auto-generated method stub

	}

	public void setContractProperties(Properties contractProperties) {
		// TODO Auto-generated method stub

	}

	public ContractInfo provideContract(Service service) throws IOException {
		return provideContract(service, null);
	}

	public ContractInfo provideContract(Service service,
			String endpointAddressOverride) throws IOException {
		ContractInfo contract;
		logger.info("provideContract for "+service+" with override "+endpointAddressOverride);
		contract = super.getContractInfo(service, getWsdlAddress());
		return contract;
	}

}
