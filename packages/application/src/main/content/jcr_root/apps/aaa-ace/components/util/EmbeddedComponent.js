"use strict";

/**
 * Creates a component node with name, if node does not yet exist. Used to get around bug in Touch-UI -- dialog wont open if component is embedded.
 * Bug happens because embedded components do not create their content node until after the dialog has sent a POST to the component’s content path. 
 * In the Touch UI the component looks for the dialog to open based on the content path. Since the  * content path doesn’t exist the dialog fails to open with an error.
 */
use([], function () {
    var newResourceName = this.name;
    var newResourceType = this.type;
    var resourceResolver = resource.getResourceResolver();
    var newNodePath = resource.path + "/" + newResourceName;
    var existingComponentResource = resourceResolver.getResource(newNodePath);

    if(existingComponentResource == null){
        var properties = {"jcr:primaryType":"nt:unstructured",
            "sling:resourceType":newResourceType};
        resourceResolver.create(resource, newResourceName, properties);
        resourceResolver.commit();
    }
});