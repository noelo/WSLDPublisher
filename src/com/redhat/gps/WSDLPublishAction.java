package com.redhat.gps;

import org.jboss.internal.soa.esb.publish.ContractProvider;
import org.jboss.internal.soa.esb.publish.ContractProviderLifecycleResource;
import org.jboss.internal.soa.esb.publish.Publish;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import javax.wsdl.Definition;
import org.jboss.soa.esb.Configurable;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.annotation.Process;
import org.jboss.soa.esb.actions.soap.WebServiceUtils;
import org.jboss.soa.esb.actions.soap.proxy.SOAPProxyWsdlContractPublisher;
import org.jboss.soa.esb.actions.soap.proxy.SOAPProxyWsdlLoader;
import org.jboss.soa.esb.configure.ConfigProperty;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.lifecycle.LifecycleResourceException;
import org.jboss.soa.esb.lifecycle.annotation.Initialize;
import org.jboss.soa.esb.listeners.ListenerTagNames;
import org.jboss.soa.esb.message.Message;

@Publish(SOAPProxyWsdlContractPublisher.class)
public class WSDLPublishAction {
	private static Logger logger = Logger.getLogger(WSDLPublishAction.class);

	@Initialize
	public void init(ConfigTree config) throws ConfigurationException {
		logger.info("init contact publisher");
		initialiseContractPublisher(config);
		SOAPProxyWsdlLoader wsdl_loader = SOAPProxyWsdlLoader.newLoader(config);
		Definition wsdl_def;
		try
		{
			wsdl_loader.load(false);
			wsdl_def = WebServiceUtils.readWSDL(wsdl_loader.getURL());
		}
		catch (Exception ioe)
		{
			throw new ConfigurationException(ioe);
		}
		finally
		{
			wsdl_loader.cleanup();
		}
		logger.info(wsdl_def);
	}

	public WSDLPublishAction() throws ConfigurationException {
		logger.info("CTOR WSDLPublishAction");
	}
	
	@ConfigProperty
	private String wsdl;

	/**
	 * @param message
	 */
	@Process
	public Message process(Message message) {
		return message;
	}

	private void initialiseContractPublisher(final ConfigTree config) throws ConfigurationException {
		final ConfigTree parent = config.getParent();
		if (parent != null) {
			logger.info("Parent is not NULL");
			final String category = parent.getAttribute(ListenerTagNames.SERVICE_CATEGORY_NAME_TAG);
			final String name = parent.getAttribute(ListenerTagNames.SERVICE_NAME_TAG);
			if ((category != null) && (name != null)) {
				final ContractProvider provider;
				try {
					provider = ContractProviderLifecycleResource.getContractProvider(category, name);
				} catch (final LifecycleResourceException lre) {
					throw new ConfigurationException("Unexpected exception querying contract provider", lre);
				}
				
				logger.info("Provider is "+provider);

				if ((provider != null) && (provider instanceof Configurable)) {
					final Configurable configurable = Configurable.class.cast(provider);
					configurable.setConfiguration(config);
				}
			}
		}else{
			logger.info("Parent is NULL");
		}
	}

}
