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
        
        for(AgentBuildFeature publisher : features) {
            Map<String, String> options = publisher.getParameters();
            
            boolean publish = false;
            String onStatus = options.get(ArtifactsConstants.SETTING_STATUS_TYPE);
            
            //check if we should publish
            switch(buildStatus) {
                case FINISHED_FAILED:
                case FINISHED_WITH_PROBLEMS:
                    publish = onStatus != null && onStatus.equals("failure");
                    break;
                
                case FINISHED_SUCCESS:
                    publish = onStatus != null && onStatus.equals("success");
                    break;
            }
            
            if(publish) {
                String artifacts = options.get(ArtifactsConstants.SETTING_ARTIFACTS);
                m_watcher.addNewArtifactsPath(artifacts);
            }
        }
    }
}