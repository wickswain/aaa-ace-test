<%@include file="/libs/granite/ui/global.jsp"%><%
%><%@page session="false"
          import="java.util.HashMap,
                  java.util.Iterator,
                  org.apache.commons.collections.Transformer,
                  org.apache.commons.collections.iterators.TransformIterator,
                  org.apache.sling.api.resource.Resource,
                  org.apache.sling.api.resource.ResourceMetadata,
                  org.apache.sling.api.resource.ResourceResolver,
                  org.apache.sling.api.resource.ResourceUtil,
                  org.apache.sling.api.resource.ValueMap,
                  org.apache.sling.api.wrappers.ValueMapDecorator,
                  com.adobe.granite.ui.components.ds.DataSource,
                  com.adobe.granite.ui.components.ds.EmptyDataSource,
                  com.adobe.granite.ui.components.ds.SimpleDataSource,
                  com.adobe.granite.ui.components.ds.ValueMapResource"%><%

final String resourcePath = resource.getValueMap().get("path", "");
final Resource columnResource = resourceResolver.getResource(resourcePath);
final Iterator<Resource> resources = columnResource.listChildren();
DataSource ds;

if (ResourceUtil.isNonExistingResource(columnResource)) {
    ds = EmptyDataSource.instance();
}

final ResourceResolver resolver = resourceResolver;
ds = new SimpleDataSource(new TransformIterator(resources, new Transformer() {
    public Object transform(Object o) {
        Resource res = (Resource) o;
        ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());
        vm.put("value", res.getValueMap().get("value"));
        vm.put("text", res.getValueMap().get("text"));

        return new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm);
    }
}));

request.setAttribute(DataSource.class.getName(), ds);
%>
