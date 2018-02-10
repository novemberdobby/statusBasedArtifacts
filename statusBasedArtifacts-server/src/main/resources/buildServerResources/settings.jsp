<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<%@ page import="novemberdobby.teamcity.statusBasedArtifacts.common.ArtifactsConstants" %>

<c:set var="status_type" value="<%=ArtifactsConstants.SETTING_STATUS_TYPE%>"/>
<c:set var="artifacts_list" value="<%=ArtifactsConstants.SETTING_ARTIFACTS%>"/>

<tr class="noBorder">
  <th>Publish:</th>
  <td>
    <props:selectProperty name="${status_type}" onchange="BS.AutoProps.onTriggerTypeChange()">
      <props:option value="success" >Only on success</props:option>
      <props:option value="failure">Only on failure</props:option>
    </props:selectProperty>
    <span class="error" id="error_${status_type}"></span>
  </td>
</tr>

<tr class="noBorder">
  <th>Artifact paths: <bs:help file="Configuring+General+Settings" anchor="artifactPaths"/></th>
  <td>
    <props:multilineProperty name="${artifacts_list}" rows="5" cols="70" linkTitle="" expanded="true" />
    <span class="error" id="error_${artifacts_list}"></span>
  </td>
</tr>