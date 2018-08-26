package novemberdobby.teamcity.statusBasedArtifacts.agent;

import java.util.Collection;
import java.util.Map;

import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import jetbrains.buildServer.agent.AgentBuildFeature;
import jetbrains.buildServer.agent.AgentLifeCycleAdapter;
import jetbrains.buildServer.agent.AgentLifeCycleListener;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.util.EventDispatcher;

import org.jetbrains.annotations.NotNull;

import novemberdobby.teamcity.statusBasedArtifacts.common.ArtifactsConstants;

public class ArtifactPublisher extends AgentLifeCycleAdapter {
    
    ArtifactsWatcher m_watcher;
    
    public ArtifactPublisher(EventDispatcher<AgentLifeCycleListener> events, ArtifactsWatcher watcher) {
        m_watcher = watcher;
        events.addListener(this);
    }
    
    @Override
    public void beforeBuildFinish(@NotNull AgentRunningBuild build, @NotNull BuildFinishedStatus buildStatus) {
        
        Collection<AgentBuildFeature> features = build.getBuildFeaturesOfType(ArtifactsConstants.FEATURE_TYPE_ID);
        
        StringBuilder allArtifacts = new StringBuilder();
        
        for(AgentBuildFeature publisher : features) {
            Map<String, String> options = publisher.getParameters();
            
            boolean publish = false;
            String onStatus = options.get(ArtifactsConstants.SETTING_STATUS_TYPE);
            
            //check if we should publish
            switch(buildStatus) {
                case INTERRUPTED:
                case FINISHED_FAILED:
                case FINISHED_WITH_PROBLEMS:
                    publish = "failure".equals(onStatus);
                    break;
                
                case FINISHED_SUCCESS:
                    publish = "success".equals(onStatus);
                    break;
            }
            
            if(publish) {
                String artifacts = options.get(ArtifactsConstants.SETTING_ARTIFACTS);
                if(artifacts != null) {
                    allArtifacts.append(artifacts + "\n");
                }
            }
        }
        
        if(allArtifacts.length() > 0) {
            m_watcher.addNewArtifactsPath(allArtifacts.toString());
        }
    }
}