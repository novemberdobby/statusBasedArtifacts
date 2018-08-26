package novemberdobby.teamcity.statusBasedArtifacts.server;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.web.util.HtmlUtils;

import jetbrains.buildServer.serverSide.BuildFeature;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.web.openapi.PluginDescriptor;

import novemberdobby.teamcity.statusBasedArtifacts.common.ArtifactsConstants;

public class ArtifactsFeature extends BuildFeature {

    private String m_editUrl;
    
    public ArtifactsFeature(PluginDescriptor descriptor) {
        m_editUrl = descriptor.getPluginResourcesPath(ArtifactsConstants.FEATURE_SETTINGS_JSP);
    }
    
    @Override
    public String getDisplayName() {
        return ArtifactsConstants.FEATURE_DISPLAY_NAME;
    }

    @Override
    public String getEditParametersUrl() {
        return m_editUrl;
    }

    @Override
    public String getType() {
        return ArtifactsConstants.FEATURE_TYPE_ID;
    }
    
    @Override
    public boolean isMultipleFeaturesPerBuildTypeAllowed() {
        return true;
    }
    
    @Override
    public String describeParameters(Map<java.lang.String, java.lang.String> params) {
        
        StringBuilder sb = new StringBuilder();
        String statusType = params.get(ArtifactsConstants.SETTING_STATUS_TYPE);
        String paths = params.get(ArtifactsConstants.SETTING_ARTIFACTS);
        
        if(statusType == null || paths == null) {
            return "Error";
        }
        
        //when to publish?
        sb.append("Only on <b>");
        sb.append(statusType);
        sb.append("</b>, publish artifacts:<br><div style=\"font-family: monospace;\">");
        
        //just list all of the artifact paths
        List<String> artLines = Arrays.asList(paths.split("[\n\r]"));
        for(String artLine : artLines) {
            sb.append(HtmlUtils.htmlEscape(artLine));
            sb.append("<br>");
        }
        sb.append("</div>");
        
        return sb.toString();
    }
    
    @Override
    public Map<String, String> getDefaultParameters() {
        
        HashMap<String, String> result = new HashMap<String, String>();
        result.put(ArtifactsConstants.SETTING_STATUS_TYPE, ArtifactsConstants.SETTING_STATUS_TYPE_DEFAULT);
        return result;
    }
    
    @Override
    public PropertiesProcessor getParametersProcessor() {
        return new FeatureValidator();
    }

    static class FeatureValidator implements PropertiesProcessor {
        
        @Override
        public Collection<InvalidProperty> process(Map<String, String> input) {
            
            ArrayList<InvalidProperty> result = new ArrayList<InvalidProperty>();
            
            String type = input.get(ArtifactsConstants.SETTING_STATUS_TYPE);
            if(type == null || !(type.equals("success") || type.equals("failure"))) {
                result.add(new InvalidProperty(ArtifactsConstants.SETTING_STATUS_TYPE, "Invalid status type"));
            }
            
            String list = input.get(ArtifactsConstants.SETTING_ARTIFACTS);
            if(list == null || list.length() == 0) {
                //no point of a feature with no artifacts listed
                //(we can't really do any validation here past that)
                result.add(new InvalidProperty(ArtifactsConstants.SETTING_ARTIFACTS, "Please enter one or more artifact paths"));
            }
            
            return result;
        }
    }
}