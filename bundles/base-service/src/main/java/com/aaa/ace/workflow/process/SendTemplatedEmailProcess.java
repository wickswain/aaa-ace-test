package com.aaa.ace.workflow.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;
import javax.jcr.Value;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.beans.UserDetailsBean;
import com.aaa.ace.services.AEMUtilityService;
import com.adobe.acs.commons.email.EmailService;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
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

    private static final String PROFILE_GIVEN_NAME = "./profile/givenName";

    private static final String PROFILE_EMAIL = "./profile/email";

    /** Default log. **/
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String PROCESS_ARGS = "PROCESS_ARGS";

    private static final String RECIPIENT_NAME = "recipientName";

    private static final String PAYLOAD_PATH = "payloadpath";

    private String payloadPath = null;

    @Reference
    EmailService emailService;

    @Reference
    AEMUtilityService utilityService;

    @Reference
    Externalizer externalizer;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args)
            throws WorkflowException {
        boolean reviewStatus = false;
        String templatePath = null;
        String sendTo = "";

        // Fetch the arguments from workflow meta data.
        String processArgs = args.get(PROCESS_ARGS, String.class);
        String[] argumnets = processArgs.split(",");
        log.info("Process Args: " + processArgs);

        // Split and separate the arguments received from workflow meta data.
        for (String argumnet : argumnets) {
            String[] argumentNameValue = argumnet.split(":");
            log.info("Argument Name: " + argumentNameValue[0]);
            log.info("Argument Value: " + argumentNameValue[1]);

            if (argumentNameValue[0].equals("emailTemplate")) {
                templatePath = argumentNameValue[1];
            } else if (argumentNameValue[0].equals("sendTo")) {
                sendTo = argumentNameValue[1];
            } else if (argumentNameValue[0].equals("status")) {
                reviewStatus = Boolean.valueOf(argumentNameValue[1]);
            }
        }

        // Get the payload path from workflow data.
        WorkflowData workflowData = workItem.getWorkflowData();
        if (workflowData.getPayloadType().equals("JCR_PATH")) {
            payloadPath = workflowData.getPayload().toString();

            if (payloadPath.startsWith("/content")) {
                payloadPath += ".html";
            }
        }

        // Send email if send to group name and template path are not empty.
        if (StringUtils.isNotBlank(sendTo) && StringUtils.isNotBlank(templatePath)) {
            // Get the recipients list of users tagged with the group.
            List<UserDetailsBean> recipientsList = getRecipientsList(sendTo,
                    "workflowmapperservice");

            sendTemplatedEmail(recipientsList, templatePath, reviewStatus);
        } else {
            log.error("Workflow process step mandatory field arguments are empty.");
        }

    }

    private void sendTemplatedEmail(List<UserDetailsBean> recipientsList, String templatePath,
            boolean reviewStatus) {
        if (!recipientsList.isEmpty()) {
            Map<String, String> emailParams = new HashMap<String, String>();
            emailParams.put(PAYLOAD_PATH, payloadPath);

            for (UserDetailsBean recipient : recipientsList) {
                emailParams.put(RECIPIENT_NAME, recipient.getGivenName());
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

                    Group group = (Group) userManager.getAuthorizable(groupName);
                    log.info("Group Authroziable: " + group.getID());
                    Iterator<Authorizable> iterator = group.getMembers();

                    while (iterator.hasNext()) {
                        User user = (User) iterator.next();
                        UserDetailsBean userDetails = new UserDetailsBean();
                        userDetails.setUserID(user.getID());

                        if (user.hasProperty(PROFILE_EMAIL)) {
                            Value existingEmail = user.getProperty(PROFILE_EMAIL)[0];
                            userDetails.setEmail(existingEmail.getString());
                        }

                        if (user.hasProperty(PROFILE_GIVEN_NAME)) {
                            Value givenName = user.getProperty(PROFILE_GIVEN_NAME)[0];
                            userDetails.setEmail(givenName.getString());
                        }

                        recipientList.add(userDetails);
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

}
