/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package edu.cornell.mannlib.vitro.webapp.web.templatemodels.individual;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.policy.PolicyHelper;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.usepages.SeeVerbosePropertyInformation;
import edu.cornell.mannlib.vitro.webapp.beans.BaseResourceBean.RoleLevel;
import edu.cornell.mannlib.vitro.webapp.beans.Individual;
import edu.cornell.mannlib.vitro.webapp.beans.Property;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.UrlBuilder;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.UrlBuilder.Route;
import edu.cornell.mannlib.vitro.webapp.web.templatemodels.BaseTemplateModel;

/** 
 * Represents the property statement list for a single property of an individual.
 */
public abstract class PropertyTemplateModel extends BaseTemplateModel {

    private static final Log log = LogFactory.getLog(PropertyTemplateModel.class); 
    
    private String name;
    private String localName;
    protected String propertyUri;
    protected Map<String, Object> verboseDisplay = null;
    protected String subjectUri = null;
    protected VitroRequest vreq;
    protected boolean addAccess = false;
    
    PropertyTemplateModel(Property property, Individual subject, EditingPolicyHelper policyHelper, VitroRequest vreq) {
        subjectUri = subject.getURI(); 
        propertyUri = property.getURI();
        this.vreq = vreq;
        localName = property.getLocalName();        
        log.debug("Local name for property " + propertyUri + ": " + localName);
        setVerboseDisplayValues(property);
        // Do in subclass constructor. The label has not been set on the property, and the
        // means of getting the label differs between object and data properties.
        // this.name = property.getLabel();
    }
    
    protected void setVerboseDisplayValues(Property property) {  
        // No verbose display for these properties.
        // This models previous behavior. In theory the verbose display can be provided, but we may not want
        // to give anyone access to these properties, since the application is dependent on them.
        if (GroupedPropertyList.VITRO_PROPS_TO_ADD_TO_LIST.contains(property)) {
            return;
        }
        Boolean verboseDisplayValue = (Boolean) vreq.getSession().getAttribute("verbosePropertyDisplay");
        if ( ! Boolean.TRUE.equals(verboseDisplayValue))  {
            return;
        }
        
        if (!PolicyHelper.isAuthorizedForActions(vreq, new SeeVerbosePropertyInformation())) {
            return;
        }
        
        verboseDisplay = new HashMap<String, Object>();
        
        RoleLevel roleLevel = property.getHiddenFromDisplayBelowRoleLevel();
        String roleLevelLabel = roleLevel != null ? roleLevel.getLabel() : "";
        verboseDisplay.put("displayLevel", roleLevelLabel);

        roleLevel = property.getHiddenFromDisplayBelowRoleLevel();
        roleLevelLabel = roleLevel != null ? roleLevel.getLabel() : "";
        verboseDisplay.put("updateLevel", roleLevelLabel);   
        
        verboseDisplay.put("localName", property.getLocalNameWithPrefix());
        verboseDisplay.put("displayRank", getPropertyDisplayTier(property));
        
        UrlBuilder urlBuilder = new UrlBuilder(vreq.getAppBean());
        String editUrl = urlBuilder.getPortalUrl(getPropertyEditRoute(), "uri", property.getURI());
        verboseDisplay.put("propertyEditUrl", editUrl);
    }
    
    protected abstract int getPropertyDisplayTier(Property p);
    protected abstract Route getPropertyEditRoute();
    
    protected void setName(String name) {
        this.name = name;
    }
    
    // Determine whether a new statement can be added
    protected abstract void setAddAccess(EditingPolicyHelper policyHelper, Property property);
    
    
    /* Access methods for templates */
    
    public abstract String getType();
    
    public String getName() {
        return name;
    }

    public String getLocalName() {
        return localName;
    }
    
    public String getUri() {
        return propertyUri;
    }
    
    public abstract String getAddUrl();
    
    public Map<String, Object> getVerboseDisplay() {
        return verboseDisplay;
    }
 
}
