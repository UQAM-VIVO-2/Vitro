/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.utils.configuration;

import edu.cornell.mannlib.vitro.webapp.modelaccess.ContextModelAccess;

/**
 * When the ConfigurationBeanLoader creates an instance of this class, it will
 * call this method, supplying the RDF models from the context.
 */
public interface ContextModelsUser {
	void setContextModels(ContextModelAccess models);
}
