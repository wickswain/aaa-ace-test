package com.aaa.ace.workflow.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.beans.UserDetailsBean;
import com.aaa.ace.common.Constants;
import com.aaa.ace.services.AEMUtilityService;
import com.adobe.acs.commons.email.EmailService;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.commons.Externalizer;

/**
 * Send email to specified users or group of users with a custom email template
 * and custom user or group.
 *
 * @author bharath.kambam
 *
 */
@Component(metatype = false, immediate = false)
@Properties({ @Property(name = "service.label", value = "Send Templated Email Workflow Process"),
        @Property(name = "service.description", value = "Send Templated Email Workflow Process implementation."),
        @Property(label = "Workflow Label", name = "process.label", value = "Send Templated Email Workflow Process", description = "Send Templated Email Workflow Process description") })
@Service
public class SendTemplatedEmailProcess implements WorkflowProcess {

    /** Default log. **/
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String ARGUMENT_STATUS = "status";

    private static final String ARGUMENT_EMAIL_TEMPLATE = "emailTemplate";

    private static final String ARGUMENT_SEND_TO = "sendTo";

    private static final String EMAIL_PARAM_RECIPIENT_NAME = "recipientName";

    private static final String EMAIL_PARAM_PAYLOAD_PATH = "payloadpath";

    private static final String EMAIL_PARAM_STATUS = "status";

    private static final String EMAIL_PARAM_COMMENT = "comment";

    private static final String EMAIL_PARAM_WORK_ITEM_TITLE = "title";

    private static final String EMAIL_PARAM_INBOX_PATH = "inboxpath";

    private String payloadPath = null;

    private String reviewStatus = null;

    private String comment = null;

    private String title = null;

    private ResourceResolver resolver = null;

    @Reference
    EmailService emailService;

    @Reference
    AEMUtilityService utilityService;

    @Reference
    Externalizer externalizer;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args)
            throws WorkflowException {
        String templatePath = null;
        String sendTo = "";

        // Fetch the arguments from workflow meta data.
        String processArgs = args.get(Constants.PROCESS_ARGS, String.class);
        String[] argumnets = processArgs.split(Constants.COMMA);
        log.info("Workflow process step arguments: " + processArgs);

        // Split and separate the arguments received from workflow meta data.
        for (String argumnet : argumnets) {
            String[] argumentNameValue = argumnet.split(Constants.COLON);

            if (argumentNameValue[0].equals(ARGUMENT_SEND_TO)) {
                sendTo = argumentNameValue[1];
            } else if (argumentNameValue[0].equals(ARGUMENT_EMAIL_TEMPLATE)) {
                templatePath = argumentNameValue[1];
            } else if (argumentNameValue[0].equals(ARGUMENT_STATUS)) {
                reviewStatus = argumentNameValue[1];
            }
        }

        // Get resource resolver
        try {
            resolver = resourceResolverFactory.getResourceResolver(
                    Collections.singletonMap("user.jcr.session", (Object) workflowSession));
        } catch (LoginException e) {
            log.error("Error occured while fetching the resource resolver from workflow session.");
            e.printStackTrace();
        }

        // Get the payload path from workflow data.
        WorkflowData workflowData = workItem.getWorkflowData();
        if (workflowData.getPayloadType().equals(Constants.JCR_PATH)) {
            payloadPath = workflowData.getPayload().toString();

            if (payloadPath.startsWith(Constants.CONTENT_ROOT_PATH)) {
                payloadPath += Constants.HTML_EXTENSION;
            }

            if (resolver != null) {
                payloadPath = externalizer.authorLink(resolver, payloadPath);
            }
        }
        log.info("Workflow payload path: " + payloadPath);

        // Capturing workflow comments from previous step
        List<HistoryItem> historyList = workflowSession.getHistory(workItem.getWorkflow());
        int listSize = historyList.size();
        HistoryItem lastItem = historyList.get(listSize - 1);
        title = lastItem.getWorkItem().getNode().getTitle();
        comment = lastItem.getComment();
        log.info("Workflow previous step comments: " + comment);

        // Send email if send to group name and template path are not empty.
        if (StringUtils.isNotBlank(sendTo) && StringUtils.isNotBlank(templatePath)) {
            // Get the recipients list of users tagged with the group.
            List<UserDetailsBean> recipientsList = null;
            if (reviewStatus.contains("reject")) {
                String initiator = workItem.getWorkflow().getInitiator();
                log.info("Workflow initiator id: " + initiator);
                recipientsList = getRecipientsList(initiator, "workflowmapperservice");
            } else {
                recipientsList = getRecipientsList(sendTo, "workflowmapperservice");
            }

            if (recipientsList != null) {
                sendTemplatedEmail(recipientsList, templatePath, reviewStatus, workItem);
            }

        } else {
            log.error("Workflow process step mandatory field arguments are empty.");
        }

    }

    private void sendTemplatedEmail(List<UserDetailsBean> recipientsList, String templatePath,
            String reviewStatus, WorkItem workItem) {
        if (!recipientsList.isEmpty()) {
            Map<String, String> emailParams = new HashMap<String, String>();
            emailParams.put(EMAIL_PARAM_PAYLOAD_PATH, payloadPath);
            emailParams.put(EMAIL_PARAM_STATUS, reviewStatus);
            emailParams.put(EMAIL_PARAM_WORK_ITEM_TITLE, title);
            emailParams.put(EMAIL_PARAM_INBOX_PATH, externalizer.authorLink(resolver, "/inbox"));

            if (comment != null && comment.length() > 0) {
                log.debug("Previous Workflow Comment = " + comment);
                emailParams.put(EMAIL_PARAM_COMMENT, comment);
            } else {
                log.debug("Previous Workflow Comment = null or ''");
            }

            for (UserDetailsBean recipient : recipientsList) {
                emailParams.put(EMAIL_PARAM_RECIPIENT_NAME, recipient.getGivenName());
                List<String> emailStatus = emailService.sendEmail(templatePath, emailParams,
                        recipient.getEmail());
                if (emailStatus.isEmpty()) {
                    log.info("Email sent successfully to: " + recipient.getEmail());
                } else {
                    log.info("Email sent failed: " + recipient.getEmail());
                }
            }
        }
    }

    private List<UserDetailsBean> getRecipientsList(String groupName, String userMapperService) {
        List<UserDetailsBean> recipientList = new ArrayList<UserDetailsBean>();
        UserManager userManager = null;
        Session adminSession = null;

        log.info("Group Name: " + groupName);
        if (StringUtils.isNotBlank(groupName)) {
            try {
                adminSession = utilityService.getSystemUserMapperServiceSession(userMapperService);

                if (adminSession != null) {
                    JackrabbitSession session = (JackrabbitSession) adminSession;
                    userManager = session.getUserManager();

                    if (userManager != null) {
                        Authorizable authorizable = userManager.getAuthorizable(groupName);
                        if (authorizable != null) {
                            log.info("Authroziable: " + authorizable);
                            if (authorizable.isGroup()) {
                                Group group = (Group) userManager.getAuthorizable(groupName);
                                log.info("Group Authroziable: " + group.getID());
                                Iterator<Authorizable> iterator = group.getMembers();

                                while (iterator.hasNext()) {
                                    User user = (User) iterator.next();
                                    if (user != null) {
                                        UserDetailsBean userDetails = getUserDetails(user);
                                        recipientList.add(userDetails);
                                    }
                                }

                            } else {
                                User user = (User) userManager.getAuthorizable(groupName);
                                log.info("User Authroziable: " + user.getID());
                                if (user != null) {
                                    UserDetailsBean userDetails = getUserDetails(user);
                                    recipientList.add(userDetails);
                                }
                            }
                        }

                    } else {
                        log.error("Not able to get the user manager from admin session.");
                    }

                } else {
                    log.error("Not able to get the admin session from user mapper service.");
                }

            } catch (Exception e) {
                log.error(
                        "Error occured while fetching the admin session from user mapper service: "
                                + e.toString());
                e.printStackTrace();
            } finally {
                if (adminSession != null) {
                    adminSession.logout();
                }
            }
        } else {
            log.error("Group name is empty.");
        }

        return recipientList;
    }

    /**
     * Gets the user details from user authorizable object.
     *
     * @param user
     * @return
     * @throws RepositoryException
     * @throws ValueFormatException
     */
    private UserDetailsBean getUserDetails(User user)
            throws RepositoryException, ValueFormatException {
        UserDetailsBean userDetails = new UserDetailsBean();

        if (user != null) {
            userDetails.setUserID(user.getID());

            if (user.hasProperty(Constants.PROFILE_EMAIL)) {
                Value existingEmail = user.getProperty(Constants.PROFILE_EMAIL)[0];
                userDetails.setEmail(existingEmail.getString());
            }

            if (user.hasProperty(Constants.PROFILE_GIVEN_NAME)) {
                Value givenName = user.getProperty(Constants.PROFILE_GIVEN_NAME)[0];
                userDetails.setGivenName(givenName.getString());
            }
        }
        log.info("User details in workflow: " + userDetails.toString());

        return userDetails;
    }

}
